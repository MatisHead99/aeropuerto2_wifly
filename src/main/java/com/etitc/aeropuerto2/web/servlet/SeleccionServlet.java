package com.etitc.aeropuerto2.web.servlet;

import com.etitc.aeropuerto2.seleccion.SeleccionVuelosBean;
import com.etitc.aeropuerto2.web.Parametros;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;

/**
 * Cada sesión HTTP recibe su propia instancia stateful. Un @EJB inyectado en el servlet
 * sería compartido por todos los usuarios, por eso el bean se busca por JNDI la primera vez
 * y se guarda en la sesión.
 */
@WebServlet("/app/seleccion")
public class SeleccionServlet extends HttpServlet {

    private static final String ATRIBUTO_SESION = "seleccion";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SeleccionVuelosBean seleccion = obtenerSeleccion(req);
        req.setAttribute("seleccionados", seleccion.obtenerSeleccion());
        req.getRequestDispatcher("/WEB-INF/views/seleccion.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SeleccionVuelosBean seleccion = obtenerSeleccion(req);
        String accion = req.getParameter("accion");

        if ("agregar".equals(accion)) {
            seleccion.agregar(Parametros.entero(req, "vueloId"));
        } else if ("quitar".equals(accion)) {
            seleccion.quitar(Parametros.entero(req, "vueloId"));
        } else if ("cerrar".equals(accion)) {
            seleccion.finalizar();
            req.getSession().removeAttribute(ATRIBUTO_SESION);
        }
        resp.sendRedirect(req.getContextPath() + "/app/seleccion");
    }

    private SeleccionVuelosBean obtenerSeleccion(HttpServletRequest req) throws ServletException {
        HttpSession sesion = req.getSession();
        SeleccionVuelosBean seleccion = (SeleccionVuelosBean) sesion.getAttribute(ATRIBUTO_SESION);
        if (seleccion == null) {
            try {
                // Cada lookup de un stateful crea una instancia nueva para este cliente
                seleccion = (SeleccionVuelosBean) new InitialContext().lookup(
                        "java:module/SeleccionVuelosBean");
            } catch (NamingException e) {
                throw new ServletException("No se pudo crear la selección de vuelos", e);
            }
            sesion.setAttribute(ATRIBUTO_SESION, seleccion);
        }
        return seleccion;
    }
}
