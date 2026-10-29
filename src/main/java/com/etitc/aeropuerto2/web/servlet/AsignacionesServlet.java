package com.etitc.aeropuerto2.web.servlet;

import com.etitc.aeropuerto2.service.AsignacionService;
import com.etitc.aeropuerto2.service.PersonalService;
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

@WebServlet("/app/asignaciones")
public class AsignacionesServlet extends HttpServlet {

    @EJB
    private AsignacionService asignacionService;

    @EJB
    private VueloService vueloService;

    @EJB
    private PersonalService personalService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("vuelosActivos", vueloService.listarActivos());
        req.setAttribute("personalActivo", personalService.listarActivos());

        Integer vueloId = Parametros.entero(req, "vueloId");
        if (vueloId != null) {
            req.setAttribute("vueloSeleccionado", vueloService.buscarPorId(vueloId));
            req.setAttribute("asignaciones", asignacionService.listarPorVuelo(vueloId));
        }
        req.getRequestDispatcher("/WEB-INF/views/asignaciones.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer vueloId = Parametros.entero(req, "vueloId");
        try {
            String accion = req.getParameter("accion");
            if ("asignar".equals(accion)) {
                asignacionService.asignar(vueloId, Parametros.entero(req, "personalId"), Parametros.texto(req, "rol"));
            } else if ("quitar".equals(accion)) {
                asignacionService.eliminar(Parametros.entero(req, "asignacionId"));
            } else {
                throw new ReglaNegocioException("Acción no reconocida.");
            }
            resp.sendRedirect(req.getContextPath() + "/app/asignaciones?vueloId=" + vueloId + "&ok=1");
        } catch (ReglaNegocioException e) {
            req.setAttribute("error", e.getMessage());
            doGet(req, resp);
        }
    }
}
