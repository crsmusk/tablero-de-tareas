# Product Specification — Tablero

**Versión:** 1.0 | **Estado:** En desarrollo activo | **Fecha:** 2026-03-22

---

## 1. Visión General

**Tablero** es un sistema de gestión de proyectos y tareas diseñado para equipos de trabajo que necesitan organizar su flujo de trabajo, adjuntar entregables a cada tarea y obtener aprobación del cliente sobre los resultados.

**Audiencia:**
- **Gestor de proyecto:** Crea proyectos, asigna tareas y adjunta recursos de trabajo.
- **Cliente:** Revisa los entregables adjuntos y registra su aprobación o solicita cambios.

**Problema que resuelve:** Centralizar en un solo lugar el seguimiento de proyectos, el estado de cada tarea y los materiales de entrega, con un flujo de revisión formal por parte del cliente.

---

## 2. Estado Actual — Funcionalidades Operativas

> Todo lo descrito aquí funciona en producción. No eliminar ni degradar ninguna de estas capacidades.

### 2.1 Gestión de Perfiles (Usuarios)

*Un perfil representa a un gestor de proyectos dentro del sistema.*

- ✅ Crear un perfil nuevo con correo, contraseña, nombre y apodo (nick).
- ✅ Consultar un perfil por su identificador único.
- ✅ Actualizar todos los datos de un perfil existente.
- ✅ Eliminar un perfil del sistema.

### 2.2 Gestión de Proyectos

*Un proyecto agrupa un conjunto de tareas y está asociado a un cliente externo.*

- ✅ Crear un proyecto asociado a un perfil existente.
- ✅ Listar todos los proyectos en formato resumido (nombre, cliente).
- ✅ Consultar el detalle de un proyecto, incluyendo **contadores en tiempo real** de sus tareas:
  - Tareas completadas
  - Tareas pendientes
  - Tareas en progreso
  - Tareas en revisión
- ✅ Actualizar los datos de un proyecto (nombre, cliente, correo del cliente).
- ✅ Eliminar un proyecto (elimina también sus tareas en cascada).

**Estado inicial de un proyecto recién creado:** `ACTIVO`

**Estados posibles de un proyecto:**

| Estado | Significado |
|--------|-------------|
| `ACTIVO` | Proyecto en curso |
| `PAUSADO` | Temporalmente detenido |
| `COMPLETADO` | Proyecto finalizado |
| `ARCHIVADO` | Histórico, sin trabajo activo |

### 2.3 Gestión de Tareas

*Una tarea es la unidad de trabajo dentro de un proyecto.*

- ✅ Crear una tarea asociada a un proyecto existente (con o sin entregables iniciales).
- ✅ Consultar una tarea por su identificador único.
- ✅ Listar todas las tareas del sistema.
- ✅ Buscar tareas por título (búsqueda parcial, sin distinción de mayúsculas).
- ✅ Actualizar una tarea de forma **parcial**: solo se modifican los campos que se envíen; los campos no enviados conservan su valor.
- ✅ Eliminar una tarea (elimina también sus entregables en cascada).
- ✅ **Intercambiar la posición** de dos tareas entre sí (swap atómico de su número de orden).

**Estado inicial de una tarea recién creada:** `PENDIENTE`

**Ciclo de vida de una tarea:**

```
PENDIENTE → EN_PROGRESO → EN_REVISION → COMPLETADO
```

*(El sistema no fuerza el orden; el estado puede cambiarse libremente entre los valores válidos.)*

| Estado | Significado |
|--------|-------------|
| `PENDIENTE` | Tarea creada, sin iniciar |
| `EN_PROGRESO` | En desarrollo activo |
| `EN_REVISION` | Esperando revisión del cliente |
| `COMPLETADO` | Tarea finalizada y aprobada |

### 2.4 Gestión de Entregables (Recursos adjuntos)

*Un entregable es un material de trabajo adjunto a una tarea, ya sea un archivo o un enlace.*

- ✅ Adjuntar un entregable a una tarea.
- ✅ Listar todos los entregables del sistema.
- ✅ Actualizar un entregable (puede cambiar de tipo, reemplazando el recurso anterior).
- ✅ Eliminar un entregable.

**Tipos de entregable:**

| Tipo | Comportamiento |
|------|---------------|
| `ARCHIVO` | Se sube un archivo al servidor. Se guarda en el directorio `uploads/`. |
| `ENLACE` | Se guarda una URL externa. No hay archivo físico. |

> **Comportamiento de limpieza:** Al eliminar un entregable de tipo `ARCHIVO`, el archivo físico se borra automáticamente del servidor. Si se actualiza un entregable cambiando de `ARCHIVO` a `ENLACE`, el archivo físico anterior también se borra.

### 2.5 Manejo de Errores

- ✅ Todos los errores devuelven un formato estándar con: fecha/hora del error, código HTTP, mensaje descriptivo y ruta que lo originó.
- ✅ Los errores de validación de campos (envíos inválidos) devuelven la lista concatenada de todos los campos que fallaron.
- ✅ Los errores inesperados del servidor devuelven código `500` con el mensaje del error.

---

## 3. Reglas de Negocio Invariables

> Estas restricciones son **no negociables**. Ninguna futura iteración, agente de IA o desarrollador puede eliminarlas sin aprobación explícita del responsable del producto.

| # | Regla | Consecuencia de Violarla |
|---|-------|--------------------------|
| R1 | Un **Proyecto** solo puede crearse si existe un **Perfil** dueño válido. | El sistema rechaza la creación con error. |
| R2 | Una **Tarea** solo puede crearse si existe un **Proyecto** válido al que pertenezca. | El sistema rechaza la creación con error. |
| R3 | Una tarea puede tener **máximo 4 entregables**. | El sistema rechaza el quinto entregable con error. |
| R4 | Al eliminar un entregable de tipo `ARCHIVO`, el **archivo físico debe borrarse** del servidor. | Genera acumulación de archivos huérfanos. |
| R5 | El campo `posicion` de una tarea es un número entero que representa su **orden visual** (estilo Kanban). | Pérdida del ordenamiento del tablero. |
| R6 | El **estado** de una tarea solo acepta los 4 valores definidos. Un valor inválido debe ser ignorado o rechazado. | Inconsistencia en el estado del tablero. |

---

## 4. Objetivos de UX y Estándares de Respuesta

### 4.1 Respuestas Exitosas
- Las operaciones de **creación** responden con código `201` y cuerpo vacío (sin mensaje de texto libre).
- Las operaciones de **consulta y actualización** responden con código `200` y los datos solicitados.
- Las operaciones de **eliminación** responden con código `204` y cuerpo vacío.
- Los listados de colecciones vacías devuelven **`[]`** (array vacío), nunca `null`.

### 4.2 Respuestas de Error
Formato estándar invariable para toda respuesta de error:

```json
{
  "timestamp": "2026-03-22T22:00:00",
  "estado": 404,
  "mensaje": "Descripción legible del problema",
  "detalles": "uri=/ruta/que/genero/el/error"
}
```

### 4.3 Experiencia para el Desarrollador / API Consumer
- Los errores de validación deben enumerar **todos** los campos inválidos en un solo mensaje, no solo el primero.
- Los mensajes de error son en **español**, siguiendo la convención del proyecto.
- No se exponen trazas de stack (`stacktrace`) internas al cliente.

---

## 5. Funcionalidades Planificadas (No Implementadas)

> Estos paquetes existen en el código pero están vacíos. No confundir con funcionalidades activas.

- **Autenticación y Seguridad:** El paquete `seguridad` está reservado para la futura implementación de Spring Security (login, roles, tokens).
- **Configuración Avanzada:** El paquete `configuracion` está reservado para configuraciones transversales (CORS, beans globales).
- **Aprobaciones:** La entidad `Aprovacion` existe en la base de datos pero **no tiene endpoints activos** aún. Es infraestructura preparada para el flujo de revisión del cliente.
