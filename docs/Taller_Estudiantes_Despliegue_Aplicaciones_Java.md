# Taller: ¿Cómo se pone en marcha una aplicación empresarial Java?

**Programación III — Semana 13**
**Duración:** aproximadamente 45 minutos
**Modalidad:** trabajo en parejas, seguido de una mini exposición de 2 minutos por pareja

---

## Objetivo

Al terminar el taller, el estudiante podrá explicar qué hace cada pieza de una aplicación Jakarta EE (servidor, base de datos, DataSource, WAR) y por qué el despliegue es una etapa distinta del desarrollo.

---

## Preparación — Clonar el repositorio (antes de empezar)

Cada pareja debe clonar el proyecto del taller. Necesitan Git instalado (`git --version`).

```
git clone https://github.com/MatisHead99/aeropuerto2_wifly.git
cd aeropuerto2_wifly
```

Si la carpeta ya existe, no la clonen de nuevo: entren a ella y ejecuten `git pull` para actualizarla.

> El repositorio se usa para las partes 2 a 4. No es necesario ejecutar el proyecto para completar el taller, pero quienes tengan WildFly y MySQL instalados pueden seguir los pasos de despliegue.

---

## Parte 1 — Calentamiento (5 min)

Respondan en parejas, sin buscar en internet:

1. ¿Qué diferencia hay entre **escribir código** y **que el código funcione para un usuario**?
2. Si tu aplicación de escritorio funciona en tu computador, ¿qué cambiaría para que la use una oficina entera al mismo tiempo?

---

## Parte 2 — Exploración del proyecto (15 min)

Abran la carpeta `Proyecto_Aeropuerto_2` y contesten:

1. Lee el `README.md`. ¿Qué capas aparecen en el diagrama de arquitectura (Navegador → Servlets/JSP → EJB → JPA → MySQL)? ¿Qué responsabilidad tiene cada una?
2. Abre `pom.xml`. ¿Por qué la dependencia `jakartaee-api` tiene `<scope>provided</scope>`? ¿Quién proporciona esas clases al ejecutar la aplicación?
3. Abre `persistence.xml`. ¿Qué significa `hibernate.hbm2ddl.auto=validate`? ¿Qué pasaría si cambiaran a `create`?
4. Abre `scripts/configurar-wildfly.cli`. ¿Qué es un **DataSource**? ¿Por qué la aplicación no tiene la URL de la base de datos escrita en el código Java?
5. ¿Por qué el `README` dice que el proyecto **no** está verificado en el despliegue real?

---

## Parte 3 — Preguntas de análisis (15 min)

Discutan y anoten una respuesta breve para cada una:

1. **Servidor de aplicaciones vs. servidor web.** ¿Qué puede hacer WildFly que un servidor web simple (como un Apache con archivos estáticos) no puede?
2. **WAR.** ¿Qué contiene el archivo `proyecto-aeropuerto-2.war`? ¿Por qué se copia a la carpeta `deployments` en lugar de ejecutarse directamente?
3. **Transacciones.** Lean la regla de "vender tiquete": bloquea el vuelo con `PESSIMISTIC_WRITE`. ¿Qué problema evita? Imaginen dos agentes vendiendo el último asiento al mismo tiempo.
4. **Seguridad.** El README indica que las contraseñas están en texto plano. ¿Qué riesgo implica? ¿Qué es un **hash con sal** y por qué ayuda?
5. **Credenciales.** El script usa el usuario `root` de MySQL. ¿Por qué es mala práctica en producción? ¿Qué permisos debería tener un usuario dedicado?
6. **Datos inconsistentes.** El README señala que `vuelos.pasajeros` no coincide con los tiquetes vendidos. ¿Cómo podría una aplicación evitar esta inconsistencia desde el diseño?

---

## Parte 4 — Reto de diseño (5 min)

En parejas, propongan en pocas frases cómo agregarían un módulo nuevo llamado **Equipaje**, siguiendo los pasos de la sección "Cómo agregar un módulo nuevo" del README. Mencionen:

- Qué clase de entidad necesitan.
- Qué archivo deben modificar además de la clase nueva.
- Qué ocurre si la tabla no existe en `schema.sql`.

---

## Parte 5 — Mini exposición (5 min)

Cada pareja prepara **2 minutos** con esta estructura:

1. **Una pregunta** que les haya parecido difícil.
2. **Una respuesta** que hayan construido entre los dos.
3. **Un riesgo** que identifiquen en el despliegue o en el código.

---

## Criterios de evaluación

| Criterio | Peso |
|---|---|
| Explica la función de al menos tres capas de la arquitectura | 25 % |
| Justifica con ejemplos del proyecto (no solo teoría) | 25 % |
| Identifica al menos un riesgo de seguridad o de datos | 20 % |
| Participación equilibrada en la pareja | 15 % |
| Claridad en la mini exposición | 15 % |

---

## Respuestas de referencia 

- **Capas:** los servlets y JSP reciben la petición; los EJB aplican las reglas de negocio con transacciones; JPA traduce objetos a SQL; MySQL almacena los datos.
- **`provided`:** WildFly ya incluye la API de Jakarta EE, así que el WAR no la empaqueta; evita duplicados y conflictos.
- **`validate`:** Hibernate solo comprueba que el modelo coincide con el esquema; con `create` borraría y recrearía las tablas, perdiendo datos.
- **DataSource:** el servidor administra el pool de conexiones y las credenciales; el código solo pide el recurso por su nombre JNDI (`java:/jdbc/AeropuertoDS`), lo que permite cambiar de base sin recompilar.
- **Bloqueo pesimista:** serializa las ventas sobre el mismo vuelo para que la capacidad no se supere cuando hay concurrencia.
- **Hash con sal:** guarda una huella irreversible de la contraseña, con un valor aleatorio por usuario, de modo que una filtración de la base no revela las contraseñas.
- **Usuario `root`:** tiene permisos sobre todo el servidor MySQL; un usuario con privilegios mínimos limita el daño si la credencial se filtra.
