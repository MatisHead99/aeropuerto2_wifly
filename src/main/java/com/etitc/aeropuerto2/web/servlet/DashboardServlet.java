package com.etitc.aeropuerto2.web.servlet;

import com.etitc.aeropuerto2.models.Vuelo;
import com.etitc.aeropuerto2.service.TiqueteService;
import com.etitc.aeropuerto2.service.VueloService;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/app/dashboard")
public class DashboardServlet extends HttpServlet {

    @EJB
    private VueloService vueloService;

    @EJB
    private TiqueteService tiqueteService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Vuelo> activos = vueloService.listarActivos();

        int pasajeros = 0;
        long vuelosLlenos = 0;
        for (Vuelo v : activos) {
            pasajeros += v.getPasajeros();
            if (!v.tieneCupo()) {
                vuelosLlenos++;
            }
        }

        req.setAttribute("vuelos", activos);
        req.setAttribute("totalVuelos", activos.size());
        req.setAttribute("totalPasajeros", pasajeros);
        req.setAttribute("ingresos", tiqueteService.ingresosTotales());
        req.setAttribute("vuelosLlenos", vuelosLlenos);
        req.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(req, resp);
    }
}
