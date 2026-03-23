# 🗂️ Tablero de Tareas

**Sistema de gestión de proyectos y tareas** construido con Java 21 y Spring Boot 3.5.10.

Permite crear perfiles de usuario, gestionar proyectos con sus tareas asociadas, adjuntar entregables (archivos o enlaces) y registrar aprobaciones por parte de clientes.

---

## ⚡ Quick Start

### Prerrequisitos
- Java 21+
- Maven 3.9+ (o usar el wrapper `mvnw` incluido)
- MySQL 8+

### 1. Clonar y configurar
```bash
git clone <url-del-repositorio>
cd tablero/tablero
cp .env.templates .env
# Editar .env con tus credenciales de MySQL
```

### 2. Crear la base de datos
```sql
CREATE DATABASE tablero_db;
```

### 3. Arrancar
```bash
./mvnw spring-boot:run
```
La API estará disponible en `http://localhost:8080`.

---

## 📚 Documentación

| Documento | Descripción |
|-----------|-------------|
| [Arquitectura](docs/architecture.md) | Capas del sistema, paquetes y flujo de datos |
| [Base de Datos](docs/database-schema.md) | Modelo ER, entidades, enums y relaciones |
| [Referencia de API](docs/api-reference.md) | Todos los endpoints REST documentados |
| [Guía de Instalación](docs/setup-guide.md) | Prerrequisitos, configuración y troubleshooting |
| [Manejo de Errores](docs/error-handling.md) | Excepciones personalizadas y respuestas de error |
| [Guía de Contribución](docs/contributing.md) | Convenciones de código, Git flow y testing |

---

## 🛠️ Stack Tecnológico

| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| Java | 21 | Lenguaje principal |
| Spring Boot | 3.5.10 | Framework backend |
| Spring Data JPA | - | Persistencia ORM |
| MySQL | 8+ | Base de datos relacional |
| Lombok | - | Reducción de boilerplate |
| Maven | 3.9+ | Gestión de dependencias y build |

---

## 📂 Estructura del Proyecto

```
tablero/
├── agent.md                  ← Fuente de verdad para agentes IA
├── README.md                 ← Este archivo
├── docs/                     ← Documentación modular
└── tablero/                  ← Código fuente Spring Boot
    ├── pom.xml
    ├── .env / .env.templates
    └── src/main/java/com/example/tablero/
        ├── controlador/      ← REST Controllers
        ├── servicio/         ← Lógica de negocio
        ├── repositorio/      ← Spring Data Repositories
        ├── mapper/           ← Entity ↔ DTO transformations
        ├── entidades/        ← Entities, DTOs y Enums
        └── excepciones/      ← Global Exception Handler
```
