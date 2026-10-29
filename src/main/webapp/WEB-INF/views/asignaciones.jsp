<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="titulo" value="Asignaciones" scope="request"/>
<c:set var="activo" value="asignaciones" scope="request"/>
<%@ include file="layout/cabecera.jspf" %>

<div class="content-header">
    <h2>Asignación de tripulación</h2>
</div>

<div class="section">
    <h3>Elegir vuelo</h3>
    <form method="get" action="${ctx}/app/asignaciones" class="form-grid">
        <div class="form-group">
            <label for="vueloId">Vuelo</label>
            <select id="vueloId" name="vueloId" required>
                <c:forEach var="v" items="${vuelosActivos}">
                    <option value="${v.id}" ${vueloSeleccionado.id == v.id ? 'selected' : ''}>
                        <c:out value="${v.codigo}"/> — <c:out value="${v.origen}"/> → <c:out value="${v.destino}"/>
                        (${fn:replace(v.horaSalida, 'T', ' ')})
                    </option>
                </c:forEach>
            </select>
        </div>
        <div>
            <button type="submit" class="btn btn-primary">Ver tripulación</button>
        </div>
    </form>
</div>

<c:if test="${not empty vueloSeleccionado}">
    <div class="section">
        <h3>Asignar a <c:out value="${vueloSeleccionado.codigo}"/></h3>
        <form method="post" action="${ctx}/app/asignaciones" class="form-grid">
            <input type="hidden" name="accion" value="asignar">
            <input type="hidden" name="vueloId" value="${vueloSeleccionado.id}">
            <div class="form-group">
                <label for="personalId">Personal</label>
                <select id="personalId" name="personalId" required>
                    <c:forEach var="p" items="${personalActivo}">
                        <option value="${p.id}"><c:out value="${p.nombre}"/> — <c:out value="${p.puesto}"/></option>
                    </c:forEach>
                </select>
            </div>
            <div class="form-group">
                <label for="rol">Rol en el vuelo</label>
                <input type="text" id="rol" name="rol" maxlength="100" placeholder="Capitán, Copiloto, Agente de embarque…" required>
            </div>
            <div>
                <button type="submit" class="btn btn-success">Asignar</button>
            </div>
        </form>
    </div>

    <div class="section">
        <h3>Tripulación asignada</h3>
        <c:choose>
            <c:when test="${empty asignaciones}">
                <p class="text-muted">Este vuelo aún no tiene tripulación</p>
            </c:when>
            <c:otherwise>
                <table class="table">
                    <thead>
                        <tr>
                            <th>Rol</th>
                            <th>Nombre</th>
                            <th>Puesto</th>
                            <th>Acción</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="a" items="${asignaciones}">
                            <tr>
                                <td><c:out value="${a.rol}"/></td>
                                <td><c:out value="${a.personal.nombre}"/></td>
                                <td><c:out value="${a.personal.puesto}"/></td>
                                <td>
                                    <form method="post" action="${ctx}/app/asignaciones" style="display:inline">
                                        <input type="hidden" name="accion" value="quitar">
                                        <input type="hidden" name="vueloId" value="${vueloSeleccionado.id}">
                                        <input type="hidden" name="asignacionId" value="${a.id}">
                                        <button type="submit" class="btn btn-small btn-danger">Quitar</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:otherwise>
        </c:choose>
    </div>
</c:if>

<%@ include file="layout/pie.jspf" %>
