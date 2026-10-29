package com.etitc.aeropuerto2.service;

import jakarta.ejb.ApplicationException;

/**
 * Error esperado de negocio (por ejemplo, vuelo lleno o asiento ocupado).
 * Al ser ApplicationException con rollback, el contenedor revierte la transacción
 * y entrega la excepción tal cual a la capa web, sin envolverla en EJBException.
 */
@ApplicationException(rollback = true)
public class ReglaNegocioException extends Exception {

    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
