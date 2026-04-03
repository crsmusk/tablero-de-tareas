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
    *   Springdoc OpenAPI (Documentación iterativa e interactiva de API)

## 2. Convenciones de Código

*   **Idioma de Nomenclatura:** Las clases, paquetes, propiedades y métodos están **en español** (ej. `ProyectoEntity`, `guardarTarea`). Se reserva el inglés únicamente para palabras clave del lenguaje y annotations.
*   **Tipos de Identificadores:** El tipo de dato estándar para las claves primarias (IDs) es **UUID** auto-generado (`GenerationType.UUID`), nunca numéricos primitivos.
*   **Uso de Lombok:** Uso intensivo de Lombok. Es obligatorio emplear anotaciones como `@Data`, `@NoArgsConstructor`, `@AllArgsConstructor` y el patrón `@Builder` para reducir código repetitivo.
*   **Respuestas REST:** Los Controladores (`*Controlador`) DEBEN retornar siempre objetos de tipo `ResponseEntity<T>`, usando códigos HTTP explícitos (`201 CREATED`, `200 OK`, `204 NO CONTENT`).

## 3. Patrones de Diseño y Arquitectura

El proyecto sigue una **Arquitectura en Capas (Layered Architecture)** monolítica estructurada por capa técnica (*Package-by-Layer*):
*   **`controlador`**: Exposición de los endpoints REST. Cero lógica de negocio; recibe peticiones y delega al servicio.
*   **`servicio`**: Capa lógica. Programada orientada a interfaces (sufijo `*I`, ej. `TareaI`) implementadas por clases concretas.
    *   **Manejo de Archivos:** Toda lógica relacionada con la manipulación y gestión física de archivos (imágenes, PDFs, entregables) DEBE estar absolutamente desacoplada. Se DEBE centralizar en un servicio dedicado (por ejemplo, `AlmacenamientoService`).
*   **`repositorio`**: Interfaces de Spring Data JPA.
*   **`mapper`**: Clases con anotación `@Component` dedicadas a la transformación manual de Entidades a DTOs y viceversa.
*   **`entidades`**:
    *   Subpaquete `entidades`: Patrón Active Record/Modelos mapeados a DB. Llevan siempre el sufijo `Entity`.
    *   Subpaquete `dtos`: Estricta subdivisión en `entrada` (Payloads `@Valid` para Request) y `salida` (Response). Llevan el sufijo `DtoEntrada` o `DtoSalida`.
*   **`excepciones`**: Contiene la lógica para atrapar centralizadamente los errores de la aplicación, siguiendo la plantilla `TableroExcepcion` e interceptadas mediante el `ManejadorGlobalExcepciones` (`@RestControllerAdvice`) en la carpeta `handler`. Estas responden consistentemente con la estructura `ErrorDtoSalida`.

## 4. Documentación de API

La estandarización de contratos REST es obligatoria y se delega a Swagger/OpenAPI.

*   **Anotaciones Base:** Todo controlador nuevo DEBE construirse obligatoriamente con configuraciones aplicadas de `springdoc-openapi`. Es exigido el uso de:
    *   `@Tag` a nivel de clase para agrupar y describir sus propósitos.
    *   `@Operation` en cada endpoint.
    *   `@ApiResponses` incluyendo cada posible status code emitido esperado.
*   **Endpoints de Contenido Multimedia:** Si existen endpoints que retornan archivos (imágenes, PDFs u otros binarios):
    *   DEBEN declarar explícitamente el tipo de contenido resultante a través del MediaType en el RequestMapping/Operation (ej. `produces = MediaType.APPLICATION_PDF_VALUE`).
    *   DEBEN documentarse explícitamente y de manera transparente los flujos de respuesta en Swagger (@Operation).
    *   Si aplica a configuraciones globales del proyecto, reflejar estas lógicas en el constructor de metadatos dentro de `SwaggerConfig`.

## 5. Comandos Frecuentes

Basado en la estructura habitual de Maven wrapper o Maven local dentro del directorio `tablero/tablero`:

*   **Instalar y compilar:** `mvn clean install`
*   **Arrancar en servidor local:** `mvn spring-boot:run`
*   **Lanzar tests:** `mvn test` o `.\mvnw.cmd test`

## 6. Prácticas Prohibidas (Anti-patrones locales)

*   **❌ Exposición directa de Entidades:** Prohibido recibir o retornar un `*Entity` en los Controladores. La transferencia de datos hacia afuera DEBE realizarse estrictamente mediante sus repetitivos `*DtoEntrada` / `*DtoSalida` procesados a través de la capa `mapper`.
*   **❌ Lógica de Archivos en Controladores:** NO DEBE habitar ninguna lógica para lectura, escritura, ni manipulación de archivos localizadas en los Controladores.
*   **❌ Recursividad de JSON/Lombok en Relaciones:** En relaciones JPA bidireccionales (`@OneToMany` y `@ManyToOne`), NO DEBE omitirse las anotaciones `@ToString.Exclude` y `@EqualsAndHashCode.Exclude` de Lombok en la entidad principal para impedir dependencias cíclicas (`StackOverflowError`).
*   **❌ Lógica de Negocio en Controladores:** NO DEBEN inyectarse los repositorios (`*Repositorio`) directamente en un Controlador, o realizar bucles de negocio allí. Se DEBE inyectar exclusivamente el respectivo `*Servicio`.
*   **❌ Control de Errores con Try-Catch Locales en Controladores:** NO DEBEN utilizarse bloques `try-catch` dentro de los `*Controlador` a fin de retornar respuestas localizadas `ResponseEntity` customizadas ("Hardcodeadas"). Cualquier condición fallida DEBE simplemente lanzar un objeto instanciado `TableroExcepcion` que atrapará el `@RestControllerAdvice` global.

## 7. Estrategia de Testing y Calidad

Este proyecto considera el testing como la primera barrera de defensa contra la deuda técnica generada por IA. Generar código no es validarlo; todo cambio DEBE estar respaldado por pruebas automáticas.

*   **Frameworks Estándar:** Usar JUnit 5 y Mockito para pruebas unitarias y de integración.
*   **Prioridades de Testeo:**
    *   **Lo Crítico:** Autenticación, autorización y lógica de negocio principal en la capa de `servicio`.
    *   **Integración:** Puntos de conexión con la base de datos (MySQL) y APIs externas.
    *   **Casos de Borde (Edge Cases):** Validar entradas nulas, UUIDs inválidos y excepciones personalizadas.
*   **Reglas para el Agente:**
    *   Toda nueva funcionalidad DEBE incluir su respectivo test unitario.
    *   Es inaceptable eliminar o editar tests existentes sin una justificación técnica aprobada por el humano (HITL).
    *   Los nombres de los tests DEBEN seguir la convención del proyecto en español (ej. `debeRetornarErrorCuandoElProyectoNoExiste`).
*   **Validación:** Antes de proponer un cambio como finalizado, el agente DEBE ejecutar `mvn test` y verificar que el 100% de la suite de pruebas pase con éxito.

## 8. Flujo de Git y Gestión de Ramas

Este proyecto sigue una política estricta de "main protegida". El agente DEBE actuar como un colaborador que nunca compromete la estabilidad de la rama de producción.

*   **Prohibición de Push Directo:** NO DEBE realizarse cambios o `git push` directamente a la rama `main`.
*   **Creación de Ramas:** Toda nueva funcionalidad, corrección o refactorización DEBE realizarse en una rama secundaria independiente (feature branch).
*   **Commits Atómicos:** 
    *   Se DEBEN realizar commits después de cada cambio lógico o tarea atómica completada.
    *   Cada commit DEBE dejar el código en un estado estable con capacidades de reversión.
*   **Estándar de Mensajes:** Es obligatorio usar *Conventional Commits* (ej. `feat:`, `fix:`, `docs:`, `test:`) para permitir la automatización de integraciones continuas como *Release Please*. **Los mensajes de commit DEBEN redactarse íntegramente en español** (ej. `feat: agregar controlador de aprobación`).
*   **Uso de Pull Requests (PR):**
    *   La integración en `main` solo se permite mediante un Pull Request formal.
*   **Estrategia Multi-Agente:** Si se lanzan varios agentes en paralelo, es RECOMENDADO utilizar **Git Worktrees** para aislar el trabajo en carpetas físicas distintas vinculadas a ramas diferentes, evitando colisiones de archivos.

## 9. Documentación del Proyecto

La documentación técnica del proyecto está modularizada en la carpeta `docs/` en la raíz del workspace. Todo agente o desarrollador DEBE consultarla y **mantenerla actualizada** permanentemente con sus modificaciones.

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
    *   NO DEBE eliminarse documentación existente sin justificación aprobada por el humano.
