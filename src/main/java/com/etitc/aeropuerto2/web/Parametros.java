package com.etitc.aeropuerto2.web;

import jakarta.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

/** Lectura y validación de parámetros de formulario. Devuelve null si el valor no es válido. */
public final class Parametros {

    private Parametros() {
    }

    public static String texto(HttpServletRequest req, String nombre) {
        String valor = req.getParameter(nombre);
        return valor == null || valor.isBlank() ? null : valor.trim();
    }

    public static Integer entero(HttpServletRequest req, String nombre) {
        try {
            return Integer.valueOf(req.getParameter(nombre));
        } catch (NumberFormatException | NullPointerException e) {
            return null;
        }
    }

    public static BigDecimal decimal(HttpServletRequest req, String nombre) {
        try {
            return new BigDecimal(req.getParameter(nombre));
        } catch (NumberFormatException | NullPointerException e) {
            return null;
        }
    }

    /** Acepta el formato de <input type="datetime-local">: 2026-03-15T14:30 */
    public static LocalDateTime fechaHora(HttpServletRequest req, String nombre) {
        try {
            return LocalDateTime.parse(req.getParameter(nombre));
        } catch (DateTimeParseException | NullPointerException e) {
            return null;
        }
    }
}
