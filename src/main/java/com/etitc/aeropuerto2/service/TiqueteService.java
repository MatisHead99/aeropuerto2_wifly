package com.etitc.aeropuerto2.service;

import com.etitc.aeropuerto2.models.Tiquete;
import com.etitc.aeropuerto2.models.Vuelo;
import jakarta.ejb.Stateless;
import jakarta.persistence.LockModeType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Stateless
public class TiqueteService extends AbstractService<Tiquete> {

    public TiqueteService() {
        super(Tiquete.class);
    }

    /**
     * Vende un tiquete. Todo ocurre en una sola transacción: si algo falla,
     * no queda un tiquete sin actualizar el contador de pasajeros del vuelo.
     */
    public Tiquete vender(Integer vueloId, String pasajero, String asiento, BigDecimal precio)
            throws ReglaNegocioException {

        if (pasajero == null || pasajero.isBlank()) {
            throw new ReglaNegocioException("El nombre del pasajero es obligatorio.");
        }
        if (precio == null || precio.signum() <= 0) {
            throw new ReglaNegocioException("El precio debe ser mayor que cero.");
        }

        // Bloqueo pesimista: evita que dos ventas simultáneas superen la capacidad del vuelo
        Vuelo vuelo = em.find(Vuelo.class, vueloId, LockModeType.PESSIMISTIC_WRITE);
        if (vuelo == null || !"Activo".equals(vuelo.getEstado())) {
            throw new ReglaNegocioException("El vuelo no existe o no está activo.");
        }
        if (!vuelo.tieneCupo()) {
            throw new ReglaNegocioException("El vuelo " + vuelo.getCodigo() + " está lleno.");
        }
        if (asiento != null && !asiento.isBlank() && asientoOcupado(vueloId, asiento)) {
            throw new ReglaNegocioException("El asiento " + asiento + " ya está ocupado.");
        }

        Tiquete tiquete = new Tiquete();
        tiquete.setNumero("TK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        tiquete.setVuelo(vuelo);
        tiquete.setPasajero(pasajero.trim());
        tiquete.setAsiento(asiento == null || asiento.isBlank() ? null : asiento.trim().toUpperCase());
        tiquete.setPrecio(precio);
        tiquete.setEstado("Activo");
        tiquete.setFechaEmision(LocalDateTime.now());

        vuelo.setPasajeros(vuelo.getPasajeros() + 1);
        return guardar(tiquete);
    }

    public List<Tiquete> listarPorVuelo(Integer vueloId) {
        return em.createQuery(
                "SELECT t FROM Tiquete t WHERE t.vuelo.id = :vueloId ORDER BY t.asiento", Tiquete.class)
                .setParameter("vueloId", vueloId)
                .getResultList();
    }

    public BigDecimal ingresosTotales() {
        return em.createQuery(
                "SELECT COALESCE(SUM(t.precio), 0) FROM Tiquete t WHERE t.estado = :estado", BigDecimal.class)
                .setParameter("estado", "Activo")
                .getSingleResult();
    }

    private boolean asientoOcupado(Integer vueloId, String asiento) {
        Long total = em.createQuery(
                "SELECT COUNT(t) FROM Tiquete t WHERE t.vuelo.id = :vueloId AND t.asiento = :asiento AND t.estado = :estado",
                Long.class)
                .setParameter("vueloId", vueloId)
                .setParameter("asiento", asiento.trim().toUpperCase())
                .setParameter("estado", "Activo")
                .getSingleResult();
        return total > 0;
    }
}
