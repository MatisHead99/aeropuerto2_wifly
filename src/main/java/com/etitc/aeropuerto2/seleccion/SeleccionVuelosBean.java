package com.etitc.aeropuerto2.seleccion;

import com.etitc.aeropuerto2.models.Vuelo;
import jakarta.ejb.Remove;
import jakarta.ejb.Stateful;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Stateful: guarda la lista de vuelos que un usuario está revisando.
 * Cada cliente obtiene su propia instancia (ver SeleccionServlet), por eso el estado
 * se conserva entre peticiones sin tocar la base de datos.
 */
@Stateful
public class SeleccionVuelosBean {

    @PersistenceContext(unitName = "aeropuertoPU")
    private EntityManager em;

    private final List<Integer> vueloIds = new ArrayList<>();

    public void agregar(Integer vueloId) {
        if (vueloId != null && !vueloIds.contains(vueloId)) {
            vueloIds.add(vueloId);
        }
    }

    public void quitar(Integer vueloId) {
        vueloIds.remove(vueloId);
    }

    public List<Vuelo> obtenerSeleccion() {
        List<Vuelo> vuelos = new ArrayList<>();
        for (Integer id : vueloIds) {
            Vuelo vuelo = em.find(Vuelo.class, id);
            if (vuelo != null) {
                vuelos.add(vuelo);
            }
        }
        return vuelos;
    }

    /** Cierra la sesión del bean: el contenedor destruye la instancia al terminar el método. */
    @Remove
    public void finalizar() {
        vueloIds.clear();
    }
}
