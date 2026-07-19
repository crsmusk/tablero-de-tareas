# 🗂️ Tablero de Tareas y Gestión de Proyectos (Backend)

Un robusto backend empresarial diseñado para gestionar perfiles, proyectos, tareas y entregables, con un fuerte enfoque en el aislamiento de datos y la seguridad a través de roles y tokens JWT.

---

## 1. Descripción del Proyecto

**¿Qué hace el sistema?**  
La aplicación "Tablero" es un backend (API REST) que sirve para administrar de manera estructurada proyectos y sus recursos derivados. Permite la creación de usuarios (perfiles), cada uno gestionando sus propios proyectos. A su vez, los proyectos se subdividen en tareas y permiten adjuntar entregables.

**¿Qué problema resuelve?**  
Provee una solución segura y escalable para organizar flujos de trabajo. Al implementar un asilamiento total de datos según el usuario autenticado, evita filtraciones, garantizando que los usuarios interactúen únicamente con su información autorizada.

---

## 2. Tecnologías Usadas

- **Java 21**
- **Spring Boot 3.5.10**
- **Spring Security & JWT** (Autenticación sin estado)
- **Spring Data JPA** e Hibernate
- **MySQL 8+**
- **Swagger / OpenAPI 3**
- **MapStruct & Lombok**
- **Maven 3.9+**

---

## 3. Arquitectura del Sistema

- **Controllers:** Exponen endpoints REST e interceptan HTTP.
- **Services:** Donde reside la lógica de negocio, validación e integridad.
- **Repositories:** Exclusivos para transacciones de datos con JPA.
- **Patrón DTO:** Los controladores esconden las Entidades del sistema a los clientes.
- **Mappers:** MapStruct transforma Entidad ↔ DTO.

---

## 4. Seguridad

- **JWT como pilar central:** Autenticación integral mediante "Bearer" Tokens.
- **Control de roles (`@PreAuthorize`):** Protecciones como `@PreAuthorize("hasAnyRole('ADMIN', 'USER')")`.
- **Extracción de `userId` In-Memory:** El sistema NO recibe el `userId` desde el frontend. El ID del usuario ha sido incrustado dinámicamente como un _Claim_ del JWT y se extrae vía `SecurityContextHolder`.

---

## 5. Flujo de Uso

1. **Registro:** El cliente interactúa con la ruta de creación de Perfil `/perfil/crear`.
2. **Login:** El cliente envía credenciales vía `/auth/login` y genera el JWT.
3. **Uso de Rutas Protegidas:** Toda petición posterior transita con `Authorization: Bearer <token>`.
4. El filtro inyecta la identidad para proveer aislamiento en BD.

---

## 6. Endpoints Principales (Ejemplos)

- `POST /auth/login` (Obtención de JWT)
- `POST /perfil/crear`
- `GET /proyecto` (Recupera mis proyectos filtrados automáticamente al extraer mi UUID del token)
- `GET /tarea/resumen?idProyecto={uuid}`
- `POST /entregable` (Subida de documento en Multipart context)

---

## 7. Requisitos y Cómo Ejecutar

1. Clonar el repositorio y acceder a la carpeta `tablero/tablero`.
2. Crear la base de datos `tablero_db` en MySQL.
3. Configurar tu `.env` (credenciales).
4. Ejecutar: `./mvnw spring-boot:run`

La API estará disponible en `http://localhost:8080`.

---

## 8. Pruebas con Postman

- Usa una variable de entorno `BASE_URL` (`http://localhost:8080`).
- Tras ejecutar `/auth/login`, toma el String devuelto e inyéctalo en Postman seleccionando tipo **Bearer Token** bajo la pestaña *Authorization*.
- Alternativamente, puedes documentar y probar cada endpoint directamente desde Swagger UI en `/swagger-ui.html`.

---

## 9. Buenas Prácticas y Estado del Proyecto

- ✅ **Seguridad JWT:** Eliminación total de IDORs y endpoints que dependen de parámetros `userId`.
- ✅ **SRP e Inversión de Control:** Lógica extraída limpiamente a interfaces en la capa de `Services`.
- 🚀 **Estado Actual:** Completamente Funcional y Estabilizado (Listo para Pruebas).

---

## 📚 Documentación Adicional

La plataforma ha sido documentada detalladamente. Revisa el directorio `docs/` en la raíz para profundizar:

| Documento | Descripción |
|-----------|-------------|
| [Arquitectura](docs/architecture.md) | Capas del sistema, paquetes y flujo de datos |
| [Base de Datos](docs/database-schema.md) | Modelo ER, entidades, enums y relaciones |
| [Referencia de API](docs/api-reference.md) | Todos los endpoints REST documentados |
| [Guía de Instalación](docs/setup-guide.md) | Prerrequisitos, configuración y troubleshooting |
| [Manejo de Errores](docs/error-handling.md) | Excepciones personalizadas y respuestas de error |
| [Guía de Contribución](docs/contributing.md) | Convenciones de código, Git flow y testing |

---

## 📂 Estructura del Proyecto

```
tablero/
├── agent.md                  ← Fuente de verdad para agentes IA
├── README.md                 ← Este archivo actualizado
├── docs/                     ← Documentación modular
└── tablero/                  ← Código fuente Spring Boot
    └── src/main/java/com/example/tablero/
```
