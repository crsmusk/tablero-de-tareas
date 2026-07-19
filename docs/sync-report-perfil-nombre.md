# Informe de Sincronización de Documentación: Refactor de `PerfilEntity`

**Autor:** Arquitecto de Software Senior  
**Fecha:** 2026-07-18  
**Componente Afectado:** Documentación Integral (Modelo, Especificaciones, Arquitectura, API, Repositorio)

---

## 1. Discovery del Cambio
**Contexto base:** Tras la decisión arquitectónica y técnica de simplificar la entidad `PerfilEntity`, el campo `nombre` fue removido íntegramente del código base backend (entidad, DTOs de entrada y salida, record de creación y lógica de servicio).
- **Backend:** `nombre` ha dejado de existir y ya no compila en el código.
- **APIs:** Los endpoints `/api/perfil` ya no admiten ni retornan `nombre`. 
- **Frontend / Clientes externos:** Obligados a utilizar `nickName` como principal identificador visual de usuario.

## 2. Análisis de Alcance
El impacto clasificado se distribuye en:
*   **Modelo de Datos:** El esquema ERD y la base de datos ya no deben referenciar `nombre` (`VARCHAR`) en la tabla `Perfiles`.
*   **Contratos de API:** La interfaz compartida con integradores y frontends ha cambiado. Exigir `nombre` generaría confusiones (400 Bad Request o simplemente pérdida de datos).
*   **Lógica de Negocio (Specs):** Los criterios de producto que indicaban la captura del "nombre real" del usuario al registrarse han quedado invalidados.
*   **Experiencia de Usuario:** El `nickName` (apodo) ha sido promovido al único indicador para presentar a la persona.

---

## 3. Actualización de Artefactos (Core)

Se ejecutó la actualización y modificación directa en los siguientes archivos (ver diffs de Git para revisión exacta):

### 📄 `product-spec.md` (Requisitos)
- **Modificación:** Se retiró la especificación que exigía capturar el "nombre" al "Crear un perfil nuevo".
- **Estado Actualizado:** `✅ Crear un perfil nuevo con correo, contraseña y apodo (nick).`

### 🌐 `docs/api-reference.md` (Contratos REST)
- **Modificación:** Se limpió el JSON de muestra del `PerfilDtoEntrada`.
- **Estado Actualizado:** Se eliminó la línea `"nombre": "Juan Pérez"`. El cuerpo HTTP ahora refleja exactamente la obligatoriedad exclusiva del `nickName`.

### 🏗️ `docs/architecture.md` (Diagramas UML)
- **Modificación:** Se purgó el campo `String nombre` dentro del bloque Mermaid que detalla los campos de `PerfilEntity`.
- **Estado Actualizado:** El diagrama Mermaid ahora solo renderiza las propiedades `id`, `correo`, `contrasena` y `nickName`.

### 🗄️ `docs/database-schema.md` (Esquema BD)
- **Modificación:** Se borraron las líneas `VARCHAR nombre` del bloque Mermaid del diagrama Entidad-Relación y de la tabla de detalles físicos.
- **Estado Actualizado:** La tabla relacional en el Markdown describe fielmente las columnas reales físicas en base de datos post-migración.

---

## 4. Eliminación de Inconsistencias
Al utilizar un sistema automatizado de escaneo (`grep`), se garantizó que no quedaron descripciones "huérfanas" referenciando un campo inexistente. Se corrigió la falsa expectativa para cualquier IA o nuevo desarrollador que lea los manuales asumiendo que `nombre` sigue siendo parte del core del dominio.

---

## 5. Registro del Cambio (Changelog)
Se ingresó directamente al archivo principal `CHANGELOG.md` la trazabilidad técnica, indicando que este fue un *breaking change* tanto de funcionalidad (Features & Refactoring) como de documentación integral (Docs).

**Extracto del Changelog actualizado:**
> **[Unreleased]**
> * **perfil:** eliminación completa del campo `nombre` en `PerfilEntity`, DTOs, mappers y endpoints...
> * **docs:** sincronización integral de la especificación técnica, arquitectura, API reference...

---

## 6. Sección Extra (Obligatoria)

### ✅ Checklist de Validación de Documentación
- [x] **`product-spec.md`**: ¿Refleja las capacidades reales actuales del sistema? Sí.
- [x] **`agent.md`**: No fue modificado porque las reglas genéricas del agente no estaban atadas explícitamente al campo `nombre` (la validación no encontró dependencia directa en este caso).
- [x] **`api-reference.md`**: ¿Los JSONs coinciden exactamente con los DTOs Java? Sí.
- [x] **`architecture.md` / `database-schema.md`**: ¿Existen modelos obsoletos? No, purgados.
- [x] **`CHANGELOG.md`**: ¿Está justificado y anotado? Sí.

### ⚠️ Riesgos (si la doc no se actualizaba)
1. **Confusión del Front-end:** El equipo de UI habría construido formularios capturando un campo que el Backend iba a ignorar.
2. **Errores de IA (Agentes de código):** Herramientas como GitHub Copilot, Cursor o este mismo agente habrían intentado sugerir `perfil.getNombre()` al leer el `architecture.md`, causando fallos de compilación continuos (alucinación inducida por doc obsoleta).
3. **Migraciones rotas:** Un DBA guiándose por el `database-schema.md` obsoleto podría asumir que la base de datos está corrupta por la falta de una columna clave.

### 🔮 Recomendaciones Futuras
1. **Docs as Code:** Acoplar la generación del `api-reference.md` directamente al framework `springdoc-openapi`. De esta forma, si borramos un campo de un DTO, el archivo Swagger se regenera automáticamente sin intervención manual.
2. **Pipelines de CI/CD para Docs:** Integrar herramientas (ej: `mermaid-cli`) para detectar si un diagrama describe un modelo de persistencia que difiere del código real.
