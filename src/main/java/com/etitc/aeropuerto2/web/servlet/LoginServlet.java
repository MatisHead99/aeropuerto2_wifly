package com.etitc.aeropuerto2.web.servlet;

import com.etitc.aeropuerto2.models.Personal;
import com.etitc.aeropuerto2.service.PersonalService;
import com.etitc.aeropuerto2.web.Parametros;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final String COOKIE_ULTIMO_USUARIO = "ultimoUsuario";

    @EJB
    private PersonalService personalService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("ultimoUsuario", leerUltimoUsuario(req));
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String usuario = Parametros.texto(req, "usuario");
        String contrasena = Parametros.texto(req, "contrasena");

        if (usuario == null || contrasena == null) {
            req.setAttribute("error", "Usuario y contraseña son obligatorios.");
            doGet(req, resp);
            return;
        }

        Personal personal = personalService.autenticar(usuario, contrasena);
        if (personal == null) {
            req.setAttribute("error", "Credenciales no válidas o usuario inactivo.");
            req.setAttribute("ultimoUsuario", usuario);
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
            return;
        }

        // Evita fijación de sesión: el identificador cambia al autenticarse
        req.getSession(true);
        req.changeSessionId();
        HttpSession sesion = req.getSession();
        sesion.setAttribute("usuarioLogueado", personal);
        sesion.setAttribute("nombreUsuario", personal.getNombre());
        sesion.setAttribute("puesto", personal.getPuesto());

        // Cookie con solo el nombre de usuario, nunca la contraseña (semana 11)
        Cookie cookie = new Cookie(COOKIE_ULTIMO_USUARIO, usuario);
        cookie.setMaxAge(60 * 60 * 24 * 30);
        cookie.setPath(req.getContextPath().isEmpty() ? "/" : req.getContextPath());
        cookie.setHttpOnly(true);
        resp.addCookie(cookie);

        resp.sendRedirect(req.getContextPath() + "/app/dashboard");
    }

    private String leerUltimoUsuario(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (cookies == null) {
            return "";
        }
        for (Cookie c : cookies) {
            if (COOKIE_ULTIMO_USUARIO.equals(c.getName())) {
                return c.getValue();
            }
        }
        return "";
    }
}
