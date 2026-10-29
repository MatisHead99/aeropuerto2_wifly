package com.etitc.aeropuerto2.service;

import com.etitc.aeropuerto2.models.Personal;
import jakarta.ejb.Stateless;

import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class PersonalService extends AbstractService<Personal> {

    public PersonalService() {
        super(Personal.class);
    }

    /** Devuelve el personal activo con esas credenciales, o null si no coincide. */
    public Personal autenticar(String usuario, String contrasena) {
        List<Personal> resultados = em.createQuery(
                "SELECT p FROM Personal p WHERE p.usuario = :usuario AND p.contrasena = :contrasena AND p.estado = :estado",
                Personal.class)
                .setParameter("usuario", usuario)
                .setParameter("contrasena", contrasena)
                .setParameter("estado", "Activo")
                .getResultList();
        return resultados.isEmpty() ? null : resultados.get(0);
    }

    public List<Personal> listarActivos() {
        return em.createQuery(
                "SELECT p FROM Personal p WHERE p.estado = :estado ORDER BY p.nombre", Personal.class)
                .setParameter("estado", "Activo")
                .getResultList();
    }

    public Personal crear(Personal personal) throws ReglaNegocioException {
        if (personal.getNombre() == null || personal.getNombre().isBlank()
                || personal.getUsuario() == null || personal.getUsuario().isBlank()
                || personal.getContrasena() == null || personal.getContrasena().isBlank()) {
            throw new ReglaNegocioException("Nombre, usuario y contraseña son obligatorios.");
        }
        Long existentes = em.createQuery(
                "SELECT COUNT(p) FROM Personal p WHERE p.usuario = :usuario OR p.cedula = :cedula", Long.class)
                .setParameter("usuario", personal.getUsuario())
                .setParameter("cedula", personal.getCedula())
                .getSingleResult();
        if (existentes > 0) {
            throw new ReglaNegocioException("Ya existe personal con ese usuario o cédula.");
        }

        personal.setEstado("Activo");
        personal.setFechaIngreso(LocalDateTime.now());
        return guardar(personal);
    }
}
