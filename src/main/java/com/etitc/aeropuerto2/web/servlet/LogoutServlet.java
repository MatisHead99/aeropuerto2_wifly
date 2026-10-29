package com.etitc.aeropuerto2.web.servlet;

import com.etitc.aeropuerto2.seleccion.SeleccionVuelosBean;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/app/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession sesion = req.getSession(false);
        if (sesion != null) {
            // Cierra el stateful de selección de este usuario antes de invalidar la sesión
            SeleccionVuelosBean seleccion = (SeleccionVuelosBean) sesion.getAttribute("seleccion");
            if (seleccion != null) {
                seleccion.finalizar();
            }
            sesion.invalidate();
        }
        resp.sendRedirect(req.getContextPath() + "/login");
    }
}
