package com.etitc.aeropuerto2.web.servlet;

import com.etitc.aeropuerto2.models.Personal;
import com.etitc.aeropuerto2.service.PersonalService;
import com.etitc.aeropuerto2.service.ReglaNegocioException;
import com.etitc.aeropuerto2.web.Parametros;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/app/personal")
public class PersonalServlet extends HttpServlet {

    @EJB
    private PersonalService personalService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("personal", personalService.listar());
        req.getRequestDispatcher("/WEB-INF/views/personal.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Personal personal = new Personal();
        personal.setNombre(Parametros.texto(req, "nombre"));
        personal.setCedula(Parametros.texto(req, "cedula"));
        personal.setPuesto(Parametros.texto(req, "puesto"));
        personal.setTelefono(Parametros.texto(req, "telefono"));
        personal.setEmail(Parametros.texto(req, "email"));
        personal.setUsuario(Parametros.texto(req, "usuario"));
        personal.setContrasena(Parametros.texto(req, "contrasena"));
        try {
            personalService.crear(personal);
            resp.sendRedirect(req.getContextPath() + "/app/personal?ok=1");
        } catch (ReglaNegocioException e) {
            req.setAttribute("error", e.getMessage());
            doGet(req, resp);
        }
    }
}
