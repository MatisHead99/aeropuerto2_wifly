package com.etitc.aeropuerto2.service;

import com.etitc.aeropuerto2.models.Vuelo;
import jakarta.ejb.Stateless;

import java.util.List;

@Stateless
public class VueloService extends AbstractService<Vuelo> {

    public VueloService() {
        super(Vuelo.class);
    }

    public List<Vuelo> listarActivos() {
        return em.createQuery(
                "SELECT v FROM Vuelo v WHERE v.estado = :estado ORDER BY v.horaSalida", Vuelo.class)
                .setParameter("estado", "Activo")
                .getResultList();
    }

    public Vuelo buscarPorCodigo(String codigo) {
        List<Vuelo> resultados = em.createQuery(
                "SELECT v FROM Vuelo v WHERE v.codigo = :codigo", Vuelo.class)
                .setParameter("codigo", codigo)
                .getResultList();
        return resultados.isEmpty() ? null : resultados.get(0);
    }

    public Vuelo crear(Vuelo vuelo) throws ReglaNegocioException {
        if (vuelo.getCodigo() == null || vuelo.getCodigo().isBlank()) {
            throw new ReglaNegocioException("El código del vuelo es obligatorio.");
        }
        if (vuelo.getHoraSalida() == null || vuelo.getHoraLlegada() == null
                || !vuelo.getHoraLlegada().isAfter(vuelo.getHoraSalida())) {
            throw new ReglaNegocioException("La hora de llegada debe ser posterior a la de salida.");
        }
        if (vuelo.getCapacidad() == null || vuelo.getCapacidad() <= 0) {
            throw new ReglaNegocioException("La capacidad debe ser mayor que cero.");
        }
        if (buscarPorCodigo(vuelo.getCodigo()) != null) {
            throw new ReglaNegocioException("Ya existe un vuelo con el código " + vuelo.getCodigo() + ".");
        }

        vuelo.setPasajeros(0);
        vuelo.setEstado("Activo");
        return guardar(vuelo);
    }
}
