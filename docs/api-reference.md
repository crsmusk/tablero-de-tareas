# Referencia de API

Base URL: `http://localhost:8080`

Todas las respuestas de error siguen la estructura [`ErrorDtoSalida`](error-handling.md).

---

## PerfilControlador

**Base Path:** `/perfil`

| Método | Ruta | Descripción | Body (Entrada) | Respuesta | Código HTTP |
|--------|------|-------------|-----------------|-----------|-------------|
| `POST` | `/perfil` | Crear un nuevo perfil | `PerfilDtoEntrada` | Vacío | `201 Created` |
| `GET` | `/perfil/{id}` | Buscar perfil por UUID | - | `PerfilDtoSalida` | `200 OK` |
| `PUT` | `/perfil/{id}` | Actualizar perfil existente | `PerfilDtoEntrada` | Vacío | `200 OK` |
| `DELETE` | `/perfil/{id}` | Eliminar perfil | - | Vacío | `204 No Content` |

### PerfilDtoEntrada (JSON)
```json
{
  "correo": "usuario@email.com",
  "contraseña": "password123",
  "nombre": "Juan Pérez",
  "nickName": "juanp"
}
```

---

## ProyectoControlador

**Base Path:** `/proyectos`

| Método | Ruta | Descripción | Body (Entrada) | Respuesta | Código HTTP |
|--------|------|-------------|-----------------|-----------|-------------|
| `POST` | `/proyectos` | Crear un nuevo proyecto | `ProyectoDtoEntrada` | `String` (ID) | `200 OK` |
| `GET` | `/proyectos` | Listar todos los proyectos (resumido) | - | `List<ProyectoResumidoDtoSalida>` | `200 OK` |
| `GET` | `/proyectos/{id}` | Buscar proyecto por UUID (detallado) | - | `ProyectoDtoSalida` | `200 OK` |
| `PUT` | `/proyectos/{id}` | Actualizar proyecto existente | `ProyectoDtoEntrada` | Vacío | `204 No Content` |
| `DELETE` | `/proyectos/{id}` | Eliminar proyecto | - | Vacío | `204 No Content` |

### ProyectoDtoEntrada (JSON)
```json
{
  "nombreProyecto": "Rediseño Web",
  "nombreCliente": "Empresa ABC",
  "correoCliente": "cliente@empresa.com",
  "acceso": "https://figma.com/file/xxx",
  "perfilId": "uuid-del-perfil"
}
```

---

## TareaControlador

**Base Path:** `/tarea`

| Método | Ruta | Descripción | Body (Entrada) | Respuesta | Código HTTP |
|--------|------|-------------|-----------------|-----------|-------------|
| `POST` | `/tarea` | Crear una nueva tarea | `TareaDtoEntrada` | Vacío | `201 Created` |
| `GET` | `/tarea/{id}` | Buscar tarea por UUID | - | `TareaDtoSalida` | `200 OK` |
| `GET` | `/tarea` | Listar todas las tareas | - | `List<TareaDtoSalida>` | `200 OK` |
| `GET` | `/tarea/buscar?titulo=` | Buscar tareas por título | `@RequestParam titulo` | `List<TareaDtoSalida>` | `200 OK` |
| `PUT` | `/tarea/{id}` | Actualizar tarea existente | `TareaDtoEntrada` | Vacío | `200 OK` |
| `DELETE` | `/tarea/{id}` | Eliminar tarea | - | Vacío | `204 No Content` |
| `PATCH` | `/tarea/orden?idAnterior=&idActual=` | Intercambiar orden de dos tareas | `@RequestParam idAnterior, idActual` | Vacío | `200 OK` |

### TareaDtoEntrada (JSON)
```json
{
  "titulo": "Diseñar mockups",
  "descripcion": "Crear wireframes en Figma",
  "estado": "PENDIENTE",
  "proyectoId": "uuid-del-proyecto"
}
```

---

## EntregableControlador

**Base Path:** `/entregable`

> ⚠️ **Nota:** Este controlador usa `@ModelAttribute` (multipart/form-data) en lugar de `@RequestBody` (JSON) para soportar la subida de archivos.

| Método | Ruta | Descripción | Body (Entrada) | Respuesta | Código HTTP |
|--------|------|-------------|-----------------|-----------|-------------|
| `POST` | `/entregable` | Crear un nuevo entregable | `EntregableDtoEntrada` (form-data) | `EntregablesEntity` | `201 Created` |
| `GET` | `/entregable` | Listar todos los entregables | - | `List<EntregableDtoSalida>` | `200 OK` |
| `PUT` | `/entregable/{id}` | Actualizar entregable | `EntregableDtoEntrada` (form-data) | `EntregablesEntity` | `200 OK` |
| `DELETE` | `/entregable/{id}` | Eliminar entregable | - | Vacío | `204 No Content` |

### EntregableDtoEntrada (form-data)
```
archivo: [archivo binario]
tareaId: uuid-de-la-tarea
tipoEntregable: ARCHIVO | ENLACE
recurso: "URL o nombre del recurso"
```

---

## Estructura Común de Error

Todas las respuestas de error devuelven el formato `ErrorDtoSalida`:

```json
{
  "timestamp": "2026-03-22T22:00:00",
  "estado": 404,
  "mensaje": "El proyecto no fue encontrado",
  "detalles": "uri=/proyectos/uuid-invalido"
}
```

Para más detalles, ver [Manejo de Errores](error-handling.md).
