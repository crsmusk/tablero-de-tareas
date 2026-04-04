# Arquitectura del Sistema

## Patrón Arquitectónico

El proyecto sigue una **Arquitectura en Capas (Layered Architecture)** monolítica, organizada por capa técnica (*Package-by-Layer*).

```mermaid
graph TD
    Cliente["🌐 Cliente (Postman / Frontend)"]
    
    subgraph "Spring Boot Application"
        S1["seguridad\nFilters + JWT Utiles"]
        C["controlador\n@RestController"]
        S2["servicio\nInterfaz (*I) + Impl"]
        M["mapper\n@Component"]
        R["repositorio\nJpaRepository"]
        EX["excepciones\n@RestControllerAdvice"]
    end
    
    DB[("🗄️ MySQL")]

    Cliente -->|Header Authorization| S1
    S1 -->|Security Context| C
    C -->|Delega| S2
    S2 -->|Transforma| M
    S2 -->|Consulta/Persiste| R
    R -->|JDBC| DB
    C -.->|Errores| EX
    S2 -.->|Lanza TableroExcepcion| EX
    S1 -.->|EntryPoint / AccessDenied| EX
    EX -->|ErrorDtoSalida| Cliente
```

## Responsabilidad por Capa

| Paquete | Responsabilidad | Regla Clave |
|---------|----------------|-------------|
| `seguridad` | Autenticación JWT, filtros y configuración | Totalmente Stateless |
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
| `configuracion` | Configuraciones técnicas adicionales | Bean definitions |

## Diagrama de Relaciones entre Entidades

```mermaid
erDiagram
    PerfilEntity ||--o{ ProyectoEntity : "tiene proyectos"
    PerfilEntity }o--o{ RolEntity : "tiene roles"
    ProyectoEntity ||--o{ TareaEntity : "tiene tareas"
    TareaEntity ||--o{ EntregablesEntity : "tiene entregables"
    TareaEntity ||--o{ AprovacionEntity : "tiene aprobaciones"

    PerfilEntity {
        UUID id PK
        String correo
        String contrasena
        String nombre
        String nickName
    }

    RolEntity {
        UUID id PK
        RolNombre rolNombre
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

## Flujo de una Petición Típica (Autenticada)

1. El **Cliente** envía una petición HTTP con el header `Authorization: Bearer <token>`.
2. El interceptor de **Seguridad** valida el JWT y establece el contexto de seguridad.
3. Si es válido, la petición llega al **Controlador**.
4. El Controlador valida el DTO de entrada (`@Valid`) y delega al **Servicio**.
5. El Servicio ejecuta la lógica de negocio y persiste/consulta al **Repositorio**.
6. Si ocurre un error de seguridad (token inválido o roles insuficientes), los manejadores de seguridad retornan un `ErrorDtoSalida`.
7. Si ocurre un error de negocio, el `ManejadorGlobalExcepciones` retorna un `ErrorDtoSalida`.
8. Si todo es exitoso, se retorna un `ResponseEntity<T>` con el código HTTP apropiado.
