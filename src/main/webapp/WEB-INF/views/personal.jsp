<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="titulo" value="Personal" scope="request"/>
<c:set var="activo" value="personal" scope="request"/>
<%@ include file="layout/cabecera.jspf" %>

<div class="content-header">
    <h2>Gestión de personal</h2>
</div>

<div class="section">
    <h3>Nuevo personal</h3>
    <form method="post" action="${ctx}/app/personal" class="form-grid">
        <div class="form-group">
            <label for="nombre">Nombre</label>
            <input type="text" id="nombre" name="nombre" maxlength="150" required>
        </div>
        <div class="form-group">
            <label for="cedula">Cédula</label>
            <input type="text" id="cedula" name="cedula" maxlength="20" required>
        </div>
        <div class="form-group">
            <label for="puesto">Puesto</label>
            <input type="text" id="puesto" name="puesto" maxlength="100" required>
        </div>
        <div class="form-group">
            <label for="telefono">Teléfono</label>
            <input type="text" id="telefono" name="telefono" maxlength="20">
        </div>
        <div class="form-group">
            <label for="email">Email</label>
            <input type="email" id="email" name="email" maxlength="100">
        </div>
        <div class="form-group">
            <label for="usuario">Usuario</label>
            <input type="text" id="usuario" name="usuario" maxlength="50" required>
        </div>
        <div class="form-group">
            <label for="contrasena">Contraseña</label>
            <input type="password" id="contrasena" name="contrasena" maxlength="100" required>
        </div>
        <div>
            <button type="submit" class="btn btn-success">Registrar</button>
        </div>
    </form>
</div>

<div class="section">
    <h3>Personal registrado</h3>
    <table class="table">
        <thead>
            <tr>
                <th>Nombre</th>
                <th>Cédula</th>
                <th>Puesto</th>
                <th>Usuario</th>
                <th>Estado</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="p" items="${personal}">
                <tr>
                    <td><c:out value="${p.nombre}"/></td>
                    <td><c:out value="${p.cedula}"/></td>
                    <td><c:out value="${p.puesto}"/></td>
                    <td><c:out value="${p.usuario}"/></td>
                    <td><span class="badge badge-success"><c:out value="${p.estado}"/></span></td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>

<%@ include file="layout/pie.jspf" %>
