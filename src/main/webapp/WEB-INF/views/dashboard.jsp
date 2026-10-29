<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="titulo" value="Dashboard" scope="request"/>
<c:set var="activo" value="dashboard" scope="request"/>
<%@ include file="layout/cabecera.jspf" %>

<div class="dashboard-header">
    <h2>Dashboard</h2>
    <p>Resumen general del sistema</p>
</div>

<div class="stats-grid">
    <div class="stat-card">
        <div class="stat-icon">✈️</div>
        <div class="stat-info">
            <h3>Vuelos activos</h3>
            <p class="stat-number">${totalVuelos}</p>
        </div>
    </div>
    <div class="stat-card">
        <div class="stat-icon">👥</div>
        <div class="stat-info">
            <h3>Pasajeros totales</h3>
            <p class="stat-number">${totalPasajeros}</p>
        </div>
    </div>
    <div class="stat-card">
        <div class="stat-icon">💰</div>
        <div class="stat-info">
            <h3>Ingresos por tiquetes</h3>
            <p class="stat-number"><fmt:formatNumber value="${ingresos}" type="currency" currencySymbol="$" maxFractionDigits="2"/></p>
        </div>
    </div>
    <div class="stat-card">
        <div class="stat-icon">🔔</div>
        <div class="stat-info">
            <h3>Vuelos llenos</h3>
            <p class="stat-number">${vuelosLlenos}</p>
        </div>
    </div>
</div>

<div class="section">
    <h3>Próximos vuelos</h3>
    <c:choose>
        <c:when test="${empty vuelos}">
            <p class="text-muted">No hay vuelos activos en este momento</p>
        </c:when>
        <c:otherwise>
            <table class="table">
                <thead>
                    <tr>
                        <th>Código</th>
                        <th>Ruta</th>
                        <th>Salida</th>
                        <th>Aerolínea</th>
                        <th>Ocupación</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="v" items="${vuelos}">
                        <tr>
                            <td><strong><c:out value="${v.codigo}"/></strong></td>
                            <td><c:out value="${v.origen}"/> → <c:out value="${v.destino}"/></td>
                            <td>${fn:replace(v.horaSalida, 'T', ' ')}</td>
                            <td><c:out value="${v.aerolinea}"/></td>
                            <td>${v.pasajeros} / ${v.capacidad}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>
</div>

<%@ include file="layout/pie.jspf" %>
