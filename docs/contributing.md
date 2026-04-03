# Guía de Contribución

## Convenciones de Código

### Idioma
- **Todo el código** (clases, métodos, variables, paquetes) se escribe **en español**.
- Se reserva el inglés únicamente para keywords de Java, annotations de Spring, y nombres técnicos establecidos (ej. `Entity`, `Repository`, `DTO`).

### Nomenclatura de Clases

| Tipo | Sufijo | Ejemplo |
|------|--------|---------|
| Entidad JPA | `*Entity` | `TareaEntity` |
| DTO de entrada | `*DtoEntrada` | `TareaDtoEntrada` |
| DTO de salida | `*DtoSalida` | `TareaDtoSalida` |
| Controlador REST | `*Controlador` | `TareaControlador` |
| Interfaz de servicio | `*I` | `TareaI` |
| Implementación de servicio | `*ServiceImpl` | `TareaServiceImpl` |
| Repositorio | `*Repositorio` | `TareaRepositorio` |
| Mapper | `*Mapper` | `TareaMapper` |

### Identificadores
- Todas las claves primarias son **UUID** auto-generados (`GenerationType.UUID`).
- Nunca usar IDs numéricos (`Long`, `Integer`).

### Lombok
- Uso obligatorio de `@Data`, `@NoArgsConstructor`, `@AllArgsConstructor`.
- Usar `@Builder` en DTOs de salida.
- En relaciones bidireccionales: siempre añadir `@ToString.Exclude` y `@EqualsAndHashCode.Exclude`.

---

## Flujo de Git

### Rama Principal Protegida
- La rama `main` está **protegida**. Está prohibido hacer `git push` directo a `main`.
- Todo cambio debe realizarse en una **feature branch** independiente.

### Convención de Ramas

```
feat/nombre-de-la-funcionalidad
fix/descripcion-del-bug
docs/seccion-actualizada
test/nombre-del-test
refactor/componente-refactorizado
```

### Conventional Commits

Es obligatorio usar el estándar de *Conventional Commits*:

| Prefijo | Uso |
|---------|-----|
| `feat:` | Nueva funcionalidad |
| `fix:` | Corrección de bug |
| `docs:` | Cambios en documentación |
| `test:` | Agregar o modificar tests |
| `refactor:` | Reestructuración sin cambio funcional |
| `chore:` | Tareas de mantenimiento |

**Ejemplos:**
```
feat: agregar endpoint de búsqueda por título en TareaControlador
fix: corregir recursión infinita en ProyectoEntity.toString()
docs: actualizar referencia de API con nuevo endpoint PATCH
test: agregar pruebas unitarias para TareaServiceImpl
```

### Commits Atómicos
- Cada commit debe ser un **cambio lógico completo**.
- El código debe compilar y los tests deben pasar después de cada commit.
- Cada commit funciona como un "punto de guardado" para posibles reversiones.

### Pull Requests
- La integración a `main` **solo** se permite mediante un Pull Request formal.
- El PR debe describir qué cambió y por qué.
- Esperar a que los checks automáticos (Lint, Tests) pasen antes de solicitar merge.

### Estrategia Multi-Agente
Si se trabaja con varios agentes de IA en paralelo, usar **Git Worktrees** para aislar el trabajo en carpetas físicas distintas vinculadas a ramas diferentes.

---

## Testing

### Frameworks
- **JUnit 5** para pruebas unitarias.
- **Mockito** para mocks y stubs.

### Nombres de Tests
Seguir la convención en español con formato descriptivo:

```java
@Test
void debeGuardarTareaCorrectamente() { ... }

@Test
void debeRetornarErrorCuandoElProyectoNoExiste() { ... }

@Test
void debeLanzarExcepcionCuandoElTituloEsVacio() { ... }
```

### Prioridades
1. **Crítico:** Lógica de negocio en `servicio/impl`.
2. **Integración:** Conexiones con MySQL y APIs externas.
3. **Edge Cases:** Entradas nulas, UUIDs inválidos, excepciones personalizadas.

### Reglas
- Toda nueva funcionalidad debe incluir su test unitario correspondiente.
- **Prohibido** eliminar o editar tests existentes sin justificación técnica aprobada por el humano (HITL).
- Antes de marcar cualquier cambio como finalizado: ejecutar `mvn test` y verificar que el 100% pase.

---

## Manejo de Errores
- **Nunca** usar `try-catch` en Controladores para manejar errores de negocio.
- **Siempre** lanzar `TableroExcepcion` desde la capa de Servicio.
- El `ManejadorGlobalExcepciones` se encarga de formatear la respuesta.
- Ver [documentación completa de errores](error-handling.md).

---

## Arquitectura
- No inyectar `*Repositorio` directamente en `*Controlador`.
- No exponer `*Entity` en respuestas REST; siempre usar `*DtoSalida` vía `*Mapper`.
- **Documentación de API:** Es mandatorio que todo controlador incluya anotaciones Swagger (`springdoc-openapi`). Usa obligatoriamente `@Tag` en clases, y `@Operation` y `@ApiResponses` en cada endpoint. Especifica de forma explícita el `MediaType` (`consumes`/`produces`) cuando interactúes con archivos multipart.
- Ver [documentación de arquitectura](architecture.md) para el flujo completo.
