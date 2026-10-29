# Sistema de Aeropuerto 2 (Jakarta EE 10 / WildFly)

Segunda versión del proyecto de aeropuerto, reescrita con **EJB, JPA/Hibernate y WildFly** (Programación III, Semana 13).
Incluye la nueva necesidad de **asignar tripulación a vuelos**, evitando solapamientos de horario.

## Arquitectura

```
Navegador → Servlets + JSP (WEB-INF/views) → EJB (@Stateless / @Stateful) → JPA (EntityManager) → MySQL
                ↑ AuthFilter (/app/*)                                   ↑ DataSource JNDI java:/jdbc/AeropuertoDS
```

| Paquete | Contenido |
|---|---|
| `models` | Entidades JPA: `Vuelo`, `Tiquete`, `Personal`, `Asignacion` |
| `service` | `AbstractService<T>` (CRUD genérico) y servicios `@Stateless` |
| `seleccion` | `SeleccionVuelosBean`, bean `@Stateful` por sesión |
| `web` | `Parametros`, `AuthFilter` y servlets de cada módulo |
| `WEB-INF/views` | JSP con JSTL (`c:out` escapa el texto de los usuarios) |

### Reglas de negocio (en los servicios, no en la vista)

- **Vender tiquete** (`TiqueteService.vender`): bloquea el vuelo con `PESSIMISTIC_WRITE` para que dos ventas simultáneas no superen la capacidad, verifica asiento libre y actualiza `pasajeros` en la misma transacción.
- **Asignar tripulación** (`AsignacionService.asignar`): rechaza personal inactivo, duplicados en el mismo vuelo y solapamiento de horarios con otro vuelo activo.
- **Errores esperados**: `ReglaNegocioException` tiene `@ApplicationException(rollback = true)`, así la transacción se revierte y la capa web muestra el mensaje sin una `EJBException` genérica.

### Conceptos de la semana 11 y 13 aplicados

| Concepto | Dónde |
|---|---|
| Cookie con el último usuario (sin contraseña) | `LoginServlet` |
| Sesión y protección de rutas | `LoginServlet`, `AuthFilter`, `LogoutServlet` |
| Cambio de ID de sesión al autenticarse | `LoginServlet` (`changeSessionId`) |
| `@Stateless` sin estado en campos | Todos los servicios |
| `@Stateful` por cliente | `SeleccionVuelosBean`, creado con `java:module/` y guardado en `HttpSession` |
| `persistence.xml` con unidad `aeropuertoPU` | `src/main/resources/META-INF/persistence.xml` |
| `hbm2ddl.auto=validate` contra `sql/schema.sql` | `persistence.xml` |

## Requisitos

- JDK 17 o superior (compilado con `release 17`)
- Maven 3.8+
- WildFly 30 o superior (Jakarta EE 10)
- MySQL 8 o MariaDB con la base `aeropuerto`

## Puesta en marcha

1. **Base de datos.** Ejecuta `sql/schema.sql`. Crea las tablas y datos de prueba.
   Usuario de prueba: `admin` / `admin123`.

2. **WildFly.** Descarga `mysql-connector-j-8.0.33.jar`, arranca WildFly y ejecuta el script
   (edita antes la ruta del JAR y la contraseña):

   ```
   %WILDFLY_HOME%\bin\jboss-cli.bat --connect --file=scripts\configurar-wildfly.cli
   ```

   Esto crea el driver y el DataSource `java:/jdbc/AeropuertoDS`, que usa `persistence.xml`.

3. **Compilar y desplegar.**

   ```
   mvn clean package
   copy target\proyecto-aeropuerto-2.war %WILDFLY_HOME%\standalone\deployments\
   ```

4. **Probar.** Abre `http://localhost:8080/proyecto-aeropuerto-2/` y entra con `admin` / `admin123`.

## Cómo agregar un módulo nuevo

Para un componente nuevo (por ejemplo, `Equipaje`), sigue estos pasos:

1. Crea la entidad en `models/` con `@Entity`, `@Table` y `@Column(name=...)` para cada columna snake_case.
2. Agrega la clase a `persistence.xml` (`<class>`).
3. Crea el servicio: `@Stateless` que extienda `AbstractService<Equipaje>` y llame a `super(Equipaje.class)`.
4. Crea el servlet en `web/servlet/` con `@WebServlet("/app/equipaje")`. Queda protegido automáticamente por `AuthFilter`.
5. Crea la JSP en `WEB-INF/views/` y el enlace en `layout/cabecera.jspf`.

Si la tabla aún no existe, agrégala a `sql/schema.sql` antes de desplegar, porque `validate` falla si no coincide.

## Estado de verificación

- **Verificado:** `mvn package` compila todas las clases y genera el WAR.
- **No verificado en este entorno:** el despliegue real en WildFly, la validación de Hibernate contra la base y el renderizado de las JSP. WildFly no está instalado en esta máquina.

## Pendientes conocidos

- **Contraseñas en texto plano** (`personal.contrasena`). Para producción deben guardarse con hash y sal.
- **Datos de prueba inconsistentes.** En `schema.sql`, `vuelos.pasajeros` no coincide con el número de tiquetes (por ejemplo, `AA101` tiene 142 pasajeros y 3 tiquetes). El contador ahora solo se actualiza al vender, así que estos datos no se corrigen solos.
- **Sin rol de administrador.** Todo el personal activo accede a todos los módulos.
