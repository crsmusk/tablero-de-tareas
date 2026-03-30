# Agent Standard - Proyecto Tablero

Este documento establece las reglas fundamentales y la "Fuente de Verdad" a seguir por cualquier agente de IA que trabaje en este código, previniendo la amnesia de contexto y garantizando uniformidad.

## 1. Stack Tecnológico

*   **Lenguaje:** Java 21
*   **Framework Principal:** Spring Boot 3.5.10
*   **Gestor de Dependencias:** Maven
*   **Base de Datos:** MySQL (driver `mysql-connector-j`)
*   **Dependencias Clave:**
    *   Spring Web (REST API)
    *   Spring Data JPA (Persistencia)
    *   Spring Boot Validation
    *   Spring Session JDBC
    *   Lombok (Reducción de boilerplate)

## 2. Convenciones de Código

*   **Idioma de Nomenclatura:** Las clases, paquetes, propiedades y métodos están **en español** (ej. `ProyectoEntity`, `guardarTarea`). Se reserva el inglés únicamente para palabras clave del lenguaje y annotations.
*   **Tipos de Identificadores:** El tipo de dato estándar para las claves primarias (IDs) es **UUID** auto-generado (`GenerationType.UUID`), nunca numéricos primitivos.
*   **Uso de Lombok:** Uso intensivo de Lombok. Es obligatorio emplear anotaciones como `@Data`, `@NoArgsConstructor`, `@AllArgsConstructor` y el patrón `@Builder` para reducir código repetitivo.
*   **Respuestas REST:** Los Controladores (`*Controlador`) deben retornar siempre objetos de tipo `ResponseEntity<T>`, usando códigos HTTP explícitos (`201 CREATED`, `200 OK`, `204 NO CONTENT`).

## 3. Patrones de Diseño y Arquitectura

El proyecto sigue una **Arquitectura en Capas (Layered Architecture)** monolítica estructurada por capa técnica (*Package-by-Layer*):
*   **`controlador`**: Exposición de los endpoints REST. Cero lógica de negocio; recibe peticiones y delega al servicio.
*   **`servicio`**: Capa lógica. Programada orientada a interfaces (sufijo `*I`, ej. `TareaI`) implementadas por clases concretas.
*   **`repositorio`**: Interfaces de Spring Data JPA.
*   **`mapper`**: Clases con anotación `@Component` dedicadas a la transformación manual de Entidades a DTOs y viceversa.
*   **`entidades`**:
    *   Subpaquete `entidades`: Patrón Active Record/Modelos mapeados a DB. Llevan siempre el sufijo `Entity`.
    *   Subpaquete `dtos`: Estricta subdivisión en `entrada` (Payloads `@Valid` para Request) y `salida` (Response). Llevan el sufijo `DtoEntrada` o `DtoSalida`.
*   **`excepciones`**: Contiene la lógica para atrapar centralizadamente los errores de la aplicación, siguiendo la plantilla `TableroExcepcion` e interceptadas mediante el `ManejadorGlobalExcepciones` (`@RestControllerAdvice`) en la carpeta `handler`. Estas responden consistentemente con la estructura `ErrorDtoSalida`.

## 4. Comandos Frecuentes

Basado en la estructura habitual de Maven wrapper o Maven local dentro del directorio `tablero/tablero`:

*   **Instalar y compilar:** `mvn clean install`
*   **Arrancar en servidor local:** `mvn spring-boot:run`
*   **Lanzar tests:** `mvn test` o `.\mvnw.cmd test`

## 5. Prácticas Prohibidas (Anti-patrones locales)

*   **❌ Exposición directa de Entidades:** Prohibido recibir o retornar un `*Entity` en los Controladores. La transferencia de datos hacia afuera debe realizarse estrictamente mediante sus repetitivos `*DtoEntrada` / `*DtoSalida` procesados a través de la capa `mapper`.
*   **❌ Recursividad de JSON/Lombok en Relaciones:** En relaciones JPA bidireccionales (`@OneToMany` y `@ManyToOne`), está totalmente prohibido omitir las anotaciones `@ToString.Exclude` y `@EqualsAndHashCode.Exclude` de Lombok en la entidad principal. Ignorar esto causará recurrencia infinita y excepciones `StackOverflowError`.
*   **❌ Lógica de Negocio en Controladores:** Está prohibido inyectar Repositorios (`*Repositorio`) directamente en el Controlador o realizar validaciones/bucles de negocio pesados ahí. Se debe inyectar exclusivamente la interfaz del `*Servicio` y delegar en ella.
*   **❌ Control de Errores con Try-Catch Locales en Controladores:** Está prohibido utilizar bloques `try-catch` dentro de los `*Controlador` para retornar respuestas `ResponseEntity` de error customizadas ("Hardcodeadas"). Cualquier validación o condición fallida debe instanciar y lanzar un `TableroExcepcion` de forma normal. El `@RestControllerAdvice` atrapará globalmente el error y responderá formatado en el DTO configurado.

## 6. Estrategia de Testing y Calidad

Este proyecto considera el testing como la primera barrera de defensa contra la deuda técnica generada por IA. Generar código no es validarlo; todo cambio debe estar respaldado por pruebas automáticas.

*   **Frameworks Estándar:** Usar JUnit 5 y Mockito para pruebas unitarias y de integración.
*   **Prioridades de Testeo:**
    *   **Lo Crítico:** Autenticación, autorización y lógica de negocio principal en la capa de `servicio`.
    *   **Integración:** Puntos de conexión con la base de datos (MySQL) y APIs externas.
    *   **Casos de Borde (Edge Cases):** Validar entradas nulas, UUIDs inválidos y excepciones personalizadas.
*   **Reglas para el Agente:**
    *   Toda nueva funcionalidad debe incluir su respectivo test unitario.
    *   Es inaceptable eliminar o editar tests existentes sin una justificación técnica aprobada por el humano (HITL).
    *   Los nombres de los tests deben seguir la convención del proyecto en español (ej. `debeRetornarErrorCuandoElProyectoNoExiste`).
*   **Validación:** Antes de proponer un cambio como finalizado, el agente debe ejecutar `mvn test` y verificar que el 100% de la suite de pruebas pase con éxito.

## 7. Flujo de Git y Gestión de Ramas

Este proyecto sigue una política estricta de "main protegida". El agente debe actuar como un colaborador que nunca compromete la estabilidad de la rama de producción.

*   **Prohibición de Push Directo:** Está estrictamente prohibido realizar cambios o `git push` directamente a la rama `main`.
*   **Creación de Ramas:** Toda nueva funcionalidad, corrección o refactorización debe realizarse en una rama secundaria independiente (feature branch).
*   **Commits Atómicos:** 
    *   Se deben realizar commits después de cada cambio lógico o tarea atómica completada.
    *   Cada commit debe dejar el código en un estado funcional y servir como un "punto de guardado" para posibles reversiones.
*   **Estándar de Mensajes:** Es obligatorio usar *Conventional Commits* (ej. `feat:`, `fix:`, `docs:`, `test:`) para permitir la automatización de versiones y notas de lanzamiento mediante *Release Please*. **Los mensajes de commit deben redactarse íntegramente en español** (ej. `feat: agregar controlador de aprobación`).
*   **Uso de Pull Requests (PR):**
    *   La integración en `main` solo se permite mediante un Pull Request formal.
    *   El agente debe esperar a que los checks automáticos (Lint, Tests, Seguridad) pasen satisfactoriamente antes de solicitar el merge.
*   **Estrategia Multi-Agente:** Si se lanzan varios agentes en paralelo, se deben utilizar **Git Worktrees** para aislar el trabajo en carpetas físicas distintas vinculadas a ramas diferentes, evitando colisiones de archivos.

## 8. Documentación del Proyecto

La documentación técnica del proyecto está modularizada en la carpeta `docs/` en la raíz del workspace. Todo agente o desarrollador debe consultarla y **mantenerla actualizada** cuando se modifique código que afecte lo documentado.

*   **Índice de Documentos:**
    *   `docs/architecture.md` — Diagrama de capas, paquetes y relaciones entre entidades.
    *   `docs/database-schema.md` — Modelo ER, campos por entidad y enumeraciones.
    *   `docs/api-reference.md` — Endpoints REST, DTOs y códigos HTTP.
    *   `docs/setup-guide.md` — Instalación, configuración de `.env` y troubleshooting.
    *   `docs/error-handling.md` — `TableroExcepcion`, `ManejadorGlobalExcepciones` y `ErrorDtoSalida`.
    *   `docs/contributing.md` — Convenciones de código, flujo Git y reglas de testing.
*   **Reglas para el Agente:**
    *   Si se agrega un nuevo endpoint, actualizar `docs/api-reference.md`.
    *   Si se agrega o modifica una entidad/enum, actualizar `docs/database-schema.md`.
    *   Si se modifica la arquitectura de paquetes, actualizar `docs/architecture.md`.
    *   Nunca eliminar documentación existente sin justificación aprobada por el humano (HITL).
