<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="titulo" value="Vuelos" scope="request"/>
<c:set var="activo" value="vuelos" scope="request"/>
<%@ include file="layout/cabecera.jspf" %>

<div class="content-header">
    <h2>Gestión de vuelos</h2>
</div>

<div class="section">
    <h3>Nuevo vuelo</h3>
    <form method="post" action="${ctx}/app/vuelos" class="form-grid">
        <input type="hidden" name="accion" value="crear">
        <div class="form-group">
            <label for="codigo">Código</label>
            <input type="text" id="codigo" name="codigo" maxlength="10" required>
        </div>
        <div class="form-group">
            <label for="aerolinea">Aerolínea</label>
            <input type="text" id="aerolinea" name="aerolinea" maxlength="100" required>
        </div>
        <div class="form-group">
            <label for="origen">Origen</label>
            <input type="text" id="origen" name="origen" maxlength="100" required>
        </div>
        <div class="form-group">
            <label for="destino">Destino</label>
            <input type="text" id="destino" name="destino" maxlength="100" required>
        </div>
        <div class="form-group">
            <label for="horaSalida">Salida</label>
            <input type="datetime-local" id="horaSalida" name="horaSalida" required>
        </div>
        <div class="form-group">
            <label for="horaLlegada">Llegada</label>
            <input type="datetime-local" id="horaLlegada" name="horaLlegada" required>
        </div>
        <div class="form-group">
            <label for="capacidad">Capacidad</label>
            <input type="number" id="capacidad" name="capacidad" min="1" required>
        </div>
        <div class="form-group">
            <label for="puertaSalida">Puerta</label>
            <input type="text" id="puertaSalida" name="puertaSalida" maxlength="10">
        </div>
        <div>
            <button type="submit" class="btn btn-success">Crear vuelo</button>
        </div>
    </form>
</div>

<div class="section">
    <h3>Vuelos registrados</h3>
    <c:choose>
        <c:when test="${empty vuelos}">
            <p class="text-muted">No hay vuelos registrados</p>
        </c:when>
        <c:otherwise>
            <table class="table">
                <thead>
                    <tr>
                        <th>Código</th>
                        <th>Ruta</th>
                        <th>Salida</th>
                        <th>Llegada</th>
                        <th>Ocupación</th>
                        <th>Estado</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="v" items="${vuelos}">
                        <tr>
                            <td><strong><c:out value="${v.codigo}"/></strong></td>
                            <td><c:out value="${v.origen}"/> → <c:out value="${v.destino}"/></td>
                            <td>${fn:replace(v.horaSalida, 'T', ' ')}</td>
                            <td>${fn:replace(v.horaLlegada, 'T', ' ')}</td>
                            <td>${v.pasajeros} / ${v.capacidad}</td>
                            <td><span class="badge badge-success"><c:out value="${v.estado}"/></span></td>
                            <td>
                                <form method="post" action="${ctx}/app/vuelos" style="display:inline"
                                      onsubmit="return confirm('¿Eliminar el vuelo?')">
                                    <input type="hidden" name="accion" value="eliminar">
                                    <input type="hidden" name="id" value="${v.id}">
                                    <button type="submit" class="btn btn-small btn-danger">Eliminar</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>
</div>

<%@ include file="layout/pie.jspf" %>
