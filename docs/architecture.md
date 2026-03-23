# Arquitectura del Sistema

## Patrón Arquitectónico

El proyecto sigue una **Arquitectura en Capas (Layered Architecture)** monolítica, organizada por capa técnica (*Package-by-Layer*).

```mermaid
graph TD
    Cliente["🌐 Cliente (Postman / Frontend)"]
    
    subgraph "Spring Boot Application"
        C["controlador\n@RestController"]
        S["servicio\nInterfaz (*I) + Impl"]
        M["mapper\n@Component"]
        R["repositorio\nJpaRepository"]
        EX["excepciones\n@RestControllerAdvice"]
    end
    
    DB[("🗄️ MySQL")]

    Cliente -->|HTTP Request| C
    C -->|Delega| S
    S -->|Transforma| M
    S -->|Consulta/Persiste| R
    R -->|JDBC| DB
    C -.->|Errores| EX
    S -.->|Lanza TableroExcepcion| EX
    EX -->|ErrorDtoSalida| Cliente
```

## Responsabilidad por Capa

| Paquete | Responsabilidad | Regla Clave |
|---------|----------------|-------------|
| `controlador` | Expone endpoints REST, delega al servicio | Cero lógica de negocio |
| `servicio/interfaces` | Define contratos (sufijo `*I`) | Toda operación pasa por la interfaz |
| `servicio/impl` | Implementa la lógica de negocio | Inyecta repositorios y mappers |
| `repositorio` | Acceso a datos vía Spring Data JPA | Interfaces que extienden `JpaRepository` |
| `mapper` | Transforma Entity ↔ DTO | Clases `@Component`, mapeo manual |
| `entidades/entidades` | Modelos JPA mapeados a tablas MySQL | Sufijo `Entity`, usan Lombok |
| `entidades/dtos/entrada` | Payloads de request (`@Valid`) | Sufijo `DtoEntrada` |
| `entidades/dtos/salida` | Payloads de response | Sufijo `DtoSalida` |
| `entidades/entidades/enums` | Constantes de estado | Persistidos como `EnumType.STRING` |
| `excepciones/excepcion` | Excepción personalizada del proyecto | `TableroExcepcion extends RuntimeException` |
| `excepciones/handler` | Interceptor global de errores | `@RestControllerAdvice` |
| `configuracion` | Configuraciones de Spring (futuro) | Reservado |
| `seguridad` | Spring Security (futuro) | Reservado |

## Diagrama de Relaciones entre Entidades

```mermaid
erDiagram
    PerfilEntity ||--o{ ProyectoEntity : "tiene proyectos"
    ProyectoEntity ||--o{ TareaEntity : "tiene tareas"
    TareaEntity ||--o{ EntregablesEntity : "tiene entregables (max 4)"
    TareaEntity ||--o{ AprovacionEntity : "tiene aprobaciones"

    PerfilEntity {
        UUID id PK
        String correo
        String contrasena
        String nombre
        String nickName
    }

    ProyectoEntity {
        UUID id PK
        String nombreProyecto
        String nombreCliente
        String correoCliente
        String acceso
        EstadosProyecto estado
    }

    TareaEntity {
        UUID id PK
        String titulo
        String descripcion
        int posicion
        EstadosTarea estado
        String clienteAccion
    }

    EntregablesEntity {
        UUID id PK
        String Recurso
        TipoEntregable tipoEntregable
        String nombreArchivo
        String ruta
    }

    AprovacionEntity {
        UUID id PK
        EstadoAprovado estadoAprovacion
        String comentario
        LocalDate fecha
    }
```

## Flujo de una Petición Típica

1. El **Cliente** envía una petición HTTP al **Controlador**.
2. El Controlador valida el DTO de entrada (`@Valid`) y delega al **Servicio** (vía interfaz).
3. El Servicio ejecuta la lógica de negocio, usa el **Mapper** para transformar DTOs ↔ Entities.
4. El Servicio persiste/consulta datos a través del **Repositorio**.
5. Si ocurre un error de negocio, el Servicio lanza `TableroExcepcion`.
6. El `ManejadorGlobalExcepciones` intercepta la excepción y retorna un `ErrorDtoSalida` estandarizado.
7. Si todo es exitoso, el Controlador retorna un `ResponseEntity<T>` con el código HTTP apropiado.
