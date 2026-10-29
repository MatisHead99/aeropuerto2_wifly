<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="titulo" value="Selección" scope="request"/>
<c:set var="activo" value="seleccion" scope="request"/>
<%@ include file="layout/cabecera.jspf" %>

<div class="content-header">
    <h2>Mi selección de vuelos</h2>
</div>

<div class="section">
    <p class="text-muted">
        Esta lista vive en un bean <strong>@Stateful</strong>: se conserva entre peticiones de tu sesión
        y no se guarda en la base de datos.
    </p>

    <c:choose>
        <c:when test="${empty seleccionados}">
            <p class="text-muted">No has seleccionado vuelos.</p>
        </c:when>
        <c:otherwise>
            <table class="table">
                <thead>
                    <tr>
                        <th>Código</th>
                        <th>Ruta</th>
                        <th>Salida</th>
                        <th>Cupo disponible</th>
                        <th>Acción</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="v" items="${seleccionados}">
                        <tr>
                            <td><strong><c:out value="${v.codigo}"/></strong></td>
                            <td><c:out value="${v.origen}"/> → <c:out value="${v.destino}"/></td>
                            <td>${fn:replace(v.horaSalida, 'T', ' ')}</td>
                            <td>${v.capacidad - v.pasajeros}</td>
                            <td>
                                <form method="post" action="${ctx}/app/seleccion" style="display:inline">
                                    <input type="hidden" name="accion" value="quitar">
                                    <input type="hidden" name="vueloId" value="${v.id}">
                                    <button type="submit" class="btn btn-small btn-danger">Quitar</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>

    <form method="post" action="${ctx}/app/seleccion" style="display:inline">
        <input type="hidden" name="accion" value="cerrar">
        <button type="submit" class="btn btn-secondary">Cerrar selección</button>
    </form>
</div>

<%@ include file="layout/pie.jspf" %>
