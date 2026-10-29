package com.etitc.aeropuerto2.web.servlet;

import com.etitc.aeropuerto2.models.Vuelo;
import com.etitc.aeropuerto2.service.ReglaNegocioException;
import com.etitc.aeropuerto2.service.VueloService;
import com.etitc.aeropuerto2.web.Parametros;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/app/vuelos")
public class VuelosServlet extends HttpServlet {

    @EJB
    private VueloService vueloService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("vuelos", vueloService.listar());
        req.getRequestDispatcher("/WEB-INF/views/vuelos.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String accion = req.getParameter("accion");
        try {
            if ("crear".equals(accion)) {
                crear(req);
            } else if ("eliminar".equals(accion)) {
                Integer id = Parametros.entero(req, "id");
                if (id == null) {
                    throw new ReglaNegocioException("Vuelo no válido.");
                }
                vueloService.eliminar(id);
            } else {
                throw new ReglaNegocioException("Acción no reconocida.");
            }
            resp.sendRedirect(req.getContextPath() + "/app/vuelos?ok=1");
        } catch (ReglaNegocioException e) {
            req.setAttribute("error", e.getMessage());
            doGet(req, resp);
        }
    }

    private void crear(HttpServletRequest req) throws ReglaNegocioException {
        Vuelo vuelo = new Vuelo();
        vuelo.setCodigo(Parametros.texto(req, "codigo"));
        vuelo.setOrigen(Parametros.texto(req, "origen"));
        vuelo.setDestino(Parametros.texto(req, "destino"));
        vuelo.setAerolinea(Parametros.texto(req, "aerolinea"));
        vuelo.setPuertaSalida(Parametros.texto(req, "puertaSalida"));
        vuelo.setHoraSalida(Parametros.fechaHora(req, "horaSalida"));
        vuelo.setHoraLlegada(Parametros.fechaHora(req, "horaLlegada"));
        vuelo.setCapacidad(Parametros.entero(req, "capacidad"));
        vueloService.crear(vuelo);
    }
}
