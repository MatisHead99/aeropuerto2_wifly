<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="titulo" value="Tiquetes" scope="request"/>
<c:set var="activo" value="tiquetes" scope="request"/>
<%@ include file="layout/cabecera.jspf" %>

<div class="content-header">
    <h2>Venta de tiquetes</h2>
</div>

<div class="section">
    <h3>Vender tiquete</h3>
    <form method="post" action="${ctx}/app/tiquetes" class="form-grid">
        <div class="form-group">
            <label for="vueloId">Vuelo</label>
            <select id="vueloId" name="vueloId" required>
                <c:forEach var="v" items="${vuelosActivos}">
                    <option value="${v.id}">
                        <c:out value="${v.codigo}"/> (<c:out value="${v.origen}"/> → <c:out value="${v.destino}"/>) — ${v.pasajeros}/${v.capacidad}
                    </option>
                </c:forEach>
            </select>
        </div>
        <div class="form-group">
            <label for="pasajero">Pasajero</label>
            <input type="text" id="pasajero" name="pasajero" maxlength="150" required>
        </div>
        <div class="form-group">
            <label for="asiento">Asiento (opcional)</label>
            <input type="text" id="asiento" name="asiento" maxlength="10">
        </div>
        <div class="form-group">
            <label for="precio">Precio</label>
            <input type="number" id="precio" name="precio" step="0.01" min="0.01" required>
        </div>
        <div>
            <button type="submit" class="btn btn-success">Vender</button>
        </div>
    </form>
</div>

<div class="section">
    <h3>Tiquetes emitidos</h3>
    <c:choose>
        <c:when test="${empty tiquetes}">
            <p class="text-muted">Aún no hay tiquetes vendidos</p>
        </c:when>
        <c:otherwise>
            <table class="table">
                <thead>
                    <tr>
                        <th>Número</th>
                        <th>Vuelo</th>
                        <th>Pasajero</th>
                        <th>Asiento</th>
                        <th>Precio</th>
                        <th>Estado</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="t" items="${tiquetes}">
                        <tr>
                            <td><c:out value="${t.numero}"/></td>
                            <td><c:out value="${t.vuelo.codigo}"/></td>
                            <td><c:out value="${t.pasajero}"/></td>
                            <td><c:out value="${t.asiento}"/></td>
                            <td><fmt:formatNumber value="${t.precio}" type="currency" currencySymbol="$" maxFractionDigits="2"/></td>
                            <td><span class="badge badge-success"><c:out value="${t.estado}"/></span></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>
</div>

<%@ include file="layout/pie.jspf" %>
