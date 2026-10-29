package com.etitc.aeropuerto2.web.servlet;

import com.etitc.aeropuerto2.service.ReglaNegocioException;
import com.etitc.aeropuerto2.service.TiqueteService;
import com.etitc.aeropuerto2.service.VueloService;
import com.etitc.aeropuerto2.web.Parametros;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/app/tiquetes")
public class TiquetesServlet extends HttpServlet {

    @EJB
    private TiqueteService tiqueteService;

    @EJB
    private VueloService vueloService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("tiquetes", tiqueteService.listar());
        req.setAttribute("vuelosActivos", vueloService.listarActivos());
        req.getRequestDispatcher("/WEB-INF/views/tiquetes.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            tiqueteService.vender(
                    Parametros.entero(req, "vueloId"),
                    Parametros.texto(req, "pasajero"),
                    Parametros.texto(req, "asiento"),
                    Parametros.decimal(req, "precio"));
            resp.sendRedirect(req.getContextPath() + "/app/tiquetes?ok=1");
        } catch (ReglaNegocioException e) {
            req.setAttribute("error", e.getMessage());
            doGet(req, resp);
        }
    }
}
