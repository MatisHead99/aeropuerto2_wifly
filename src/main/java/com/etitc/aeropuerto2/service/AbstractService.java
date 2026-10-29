package com.etitc.aeropuerto2.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

/**
 * Operaciones CRUD comunes para todos los servicios.
 * Para un módulo nuevo basta con extender esta clase y anotar la subclase con @Stateless.
 */
public abstract class AbstractService<T> {

    @PersistenceContext(unitName = "aeropuertoPU")
    protected EntityManager em;

    private final Class<T> tipo;

    protected AbstractService(Class<T> tipo) {
        this.tipo = tipo;
    }

    public T buscarPorId(Integer id) {
        return em.find(tipo, id);
    }

    public List<T> listar() {
        return em.createQuery("SELECT e FROM " + tipo.getSimpleName() + " e", tipo).getResultList();
    }

    public T guardar(T entidad) {
        em.persist(entidad);
        return entidad;
    }

    public T actualizar(T entidad) {
        return em.merge(entidad);
    }

    public void eliminar(Integer id) {
        T entidad = em.find(tipo, id);
        if (entidad != null) {
            em.remove(entidad);
        }
    }
}
