<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ingreso - Sistema de Aeropuerto 2</title>
    <link rel="stylesheet" href="${ctx}/css/styles.css">
</head>
<body>
    <main class="login-box">
        <h1>✈️ Ingreso al sistema</h1>

        <c:if test="${param.error == '2'}">
            <div class="alert alert-warning">Debe iniciar sesión para continuar.</div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="alert alert-error"><c:out value="${error}"/></div>
        </c:if>

        <form method="post" action="${ctx}/login">
            <div class="form-group">
                <label for="usuario">Usuario</label>
                <input type="text" id="usuario" name="usuario" required autofocus
                       value="${fn:escapeXml(ultimoUsuario)}">
            </div>
            <div class="form-group">
                <label for="contrasena">Contraseña</label>
                <input type="password" id="contrasena" name="contrasena" required>
            </div>
            <button type="submit" class="btn btn-primary btn-block">Ingresar</button>
        </form>
    </main>
</body>
</html>
