# Bitácora de puesta en marcha — Proyecto_Aeropuerto_2

Registro de los pasos seguidos para levantar el proyecto en una máquina Windows 11 con WildFly, MySQL y Maven.

**Regla de esta sesión:** no se ejecutan comandos en nombre del usuario. Solo se explica cómo hacerlo, y el usuario ejecuta cada paso.

---

## Estado del proyecto (revisión inicial)

- **Stack:** Jakarta EE 10, WildFly 30+, EJB (`@Stateless` / `@Stateful`), JPA/Hibernate, MySQL 8 o MariaDB, JSP con JSTL.
- **Compilación:** `mvn package` compila todas las clases y genera el WAR (verificado en el README).
- **No verificado:** despliegue en WildFly, validación de Hibernate contra la base y render de las JSP.
- **Pendientes conocidos (según README):**
  1. Contraseñas en texto plano en `personal.contrasena`.
  2. Datos de prueba inconsistentes en `sql/schema.sql` (`vuelos.pasajeros` no coincide con los tiquetes; p. ej., `AA101` tiene 142 pasajeros y 3 tiquetes).
  3. Sin rol de administrador; todo el personal activo accede a todos los módulos.

### Archivos clave revisados

| Archivo | Función |
|---|---|
| `README.md` | Arquitectura, requisitos y pasos de puesta en marcha |
| `sql/schema.sql` | Crea tablas y datos de prueba (usuario `admin` / `admin123`) |
| `scripts/configurar-wildfly.cli` | Crea el driver MySQL y el DataSource `java:/jdbc/AeropuertoDS` |
| `src/main/resources/META-INF/persistence.xml` | Unidad `aeropuertoPU`, JTA, `hbm2ddl.auto=validate` |
| `pom.xml` | Java 17, dependencia `jakartaee-api` 10.0.0 (provided), WAR `proyecto-aeropuerto-2` |

---

## Paso a paso (para el usuario)

### Paso 1 — Requisitos previos

1. **JDK 17 o superior.** Verificar con `java -version`. Si no está, instalar Eclipse Temurin 17 y definir `JAVA_HOME`.
2. **Maven 3.8 o superior.** Verificar con `mvn -v`.
3. **MySQL 8 o MariaDB** en ejecución, puerto 3306.
4. **Driver `mysql-connector-j-8.0.33.jar`** descargado.

### Paso 2 — Instalar WildFly

1. Descargar **WildFly 30** (o 31) desde https://www.wildfly.org/downloads/ (archivo `.zip`).
2. Crear una carpeta sin espacios ni tildes, p. ej. `C:\wildfly`, y extraer ahí el `.zip`.
3. Definir la variable de entorno de usuario `WILDFLY_HOME` apuntando a la carpeta de WildFly (p. ej. `C:\wildfly\wildfly-30.0.0.Final`).
4. Cerrar y volver a abrir la terminal.
5. Arrancar el servidor con `%WILDFLY_HOME%\bin\standalone.bat` y verificar que `http://localhost:8080/` muestra la página de bienvenida.
6. (Opcional) Crear usuario de administración con `%WILDFLY_HOME%\bin\add-user.bat`, opción `a` (Management User).

**Estado:** pendiente de confirmación del usuario (WildFly aún no instalado en la máquina).

### Paso 3 — Crear la base de datos *(pendiente)*

1. Abrir MySQL Workbench o la consola de MySQL.
2. Ejecutar `sql/schema.sql`. Crea las tablas y los datos de prueba.

### Paso 4 — Configurar el DataSource en WildFly *(pendiente)*

1. Editar `scripts\configurar-wildfly.cli`:
   - Cambiar la ruta del JAR en `--resources=` por la ruta real, con barras `/`.
   - Cambiar `--password=CAMBIAR_ESTA_CLAVE` por la contraseña real de MySQL. No subirla al repositorio.
2. Con WildFly en marcha, ejecutar:
   ```
   %WILDFLY_HOME%\bin\jboss-cli.bat --connect --file=scripts\configurar-wildfly.cli
   ```
3. Si aparece "already exists", el DataSource ya estaba creado.

### Paso 5 — Compilar y desplegar *(pendiente)*

```
mvn clean package
copy target\proyecto-aeropuerto-2.war %WILDFLY_HOME%\standalone\deployments\
```

Verificar en el log de WildFly: `Deployed "proyecto-aeropuerto-2.war"`.

### Paso 6 — Probar *(pendiente)*

1. Abrir `http://localhost:8080/proyecto-aeropuerto-2/`.
2. Entrar con `admin` / `admin123`.
3. Probar vuelos, tiquetes, personal y asignaciones.

### Solución de problemas

| Síntoma | Causa probable | Revisar |
|---|---|---|
| Error de `validate` de Hibernate | Entidades Java no coinciden con `schema.sql` | Log del servidor |
| Error de conexión al DataSource | Contraseña incorrecta, MySQL apagado o base `aeropuerto` inexistente | `configurar-wildfly.cli`, servicio MySQL |
| Log completo | — | `%WILDFLY_HOME%\standalone\log\server.log` |

### Recomendación de seguridad

El script usa el usuario `root` de MySQL. Para pruebas locales es aceptable, pero conviene crear un usuario dedicado con permisos solo sobre la base `aeropuerto`.

---

## Historial

| Fecha | Acción |
|---|---|
| 2026-10-29 | Revisión del README, `persistence.xml`, `pom.xml` y `configurar-wildfly.cli`. Explicados los pasos 1 a 2 para instalar WildFly. |
