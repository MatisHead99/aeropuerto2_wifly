package com.etitc.aeropuerto2.service;

import com.etitc.aeropuerto2.models.Asignacion;
import com.etitc.aeropuerto2.models.Personal;
import com.etitc.aeropuerto2.models.Vuelo;
import jakarta.ejb.Stateless;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Asignación de tripulación a vuelos (nueva necesidad).
 * Regla principal: una persona no puede estar en dos vuelos que se solapen en horario.
 */
@Stateless
public class AsignacionService extends AbstractService<Asignacion> {

    public AsignacionService() {
        super(Asignacion.class);
    }

    public Asignacion asignar(Integer vueloId, Integer personalId, String rol) throws ReglaNegocioException {
        if (rol == null || rol.isBlank()) {
            throw new ReglaNegocioException("Debe indicar el rol del tripulante.");
        }

        Vuelo vuelo = em.find(Vuelo.class, vueloId);
        if (vuelo == null || !"Activo".equals(vuelo.getEstado())) {
            throw new ReglaNegocioException("El vuelo no existe o no está activo.");
        }
        Personal personal = em.find(Personal.class, personalId);
        if (personal == null || !personal.estaActivo()) {
            throw new ReglaNegocioException("El personal no existe o no está activo.");
        }
        if (yaAsignadoAlVuelo(vueloId, personalId)) {
            throw new ReglaNegocioException(personal.getNombre() + " ya está asignado a este vuelo.");
        }
        if (tieneSolapamiento(personalId, vuelo)) {
            throw new ReglaNegocioException(personal.getNombre()
                    + " ya tiene otro vuelo en ese horario.");
        }

        Asignacion asignacion = new Asignacion();
        asignacion.setVuelo(vuelo);
        asignacion.setPersonal(personal);
        asignacion.setRol(rol.trim());
        asignacion.setFechaAsignacion(LocalDateTime.now());
        return guardar(asignacion);
    }

    public List<Asignacion> listarPorVuelo(Integer vueloId) {
        return em.createQuery(
                "SELECT a FROM Asignacion a WHERE a.vuelo.id = :vueloId ORDER BY a.rol", Asignacion.class)
                .setParameter("vueloId", vueloId)
                .getResultList();
    }

    private boolean yaAsignadoAlVuelo(Integer vueloId, Integer personalId) {
        Long total = em.createQuery(
                "SELECT COUNT(a) FROM Asignacion a WHERE a.vuelo.id = :vueloId AND a.personal.id = :personalId",
                Long.class)
                .setParameter("vueloId", vueloId)
                .setParameter("personalId", personalId)
                .getSingleResult();
        return total > 0;
    }

    private boolean tieneSolapamiento(Integer personalId, Vuelo vuelo) {
        Long total = em.createQuery(
                "SELECT COUNT(a) FROM Asignacion a " +
                "WHERE a.personal.id = :personalId " +
                "AND a.vuelo.estado = :estado " +
                "AND a.vuelo.horaSalida < :llegada " +
                "AND a.vuelo.horaLlegada > :salida", Long.class)
                .setParameter("personalId", personalId)
                .setParameter("estado", "Activo")
                .setParameter("llegada", vuelo.getHoraLlegada())
                .setParameter("salida", vuelo.getHoraSalida())
                .getSingleResult();
        return total > 0;
    }
}
