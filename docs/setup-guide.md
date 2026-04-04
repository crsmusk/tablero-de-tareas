# Guía de Instalación y Configuración

## Prerrequisitos

| Herramienta | Versión Mínima | Verificar con |
|-------------|---------------|---------------|
| Java JDK | 21 | `java -version` |
| Maven | 3.9+ | `mvn -version` (o usar `mvnw` incluido) |
| MySQL | 8.0+ | `mysql --version` |
| Git | 2.x | `git --version` |

## Paso 1: Clonar el Repositorio

```bash
git clone <url-del-repositorio>
cd tablero
```

## Paso 2: Crear la Base de Datos

Conectarse a MySQL y ejecutar:

```sql
CREATE DATABASE tablero_db;
```

## Paso 3: Configurar Variables de Entorno

```bash
cd tablero
cp .env.templates .env
```

Editar el archivo `.env` con tus credenciales reales:

```properties
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/tablero_db?useSSL=false&serverTimezone=UTC
SPRING_DATASOURCE_USERNAME=tu_usuario
SPRING_DATASOURCE_PASSWORD=tu_contraseña

# Configuración JWT
JWT_USER_SECRET=tu_secreto_para_usuarios_en_base64
JWT_USER_EXPIRATION=3600000 # 1 hora
JWT_CLIENT_SECRET=tu_secreto_para_clientes_en_base64
JWT_CLIENT_EXPIRATION=86400000 # 24 horas
```

> **Importante:** El archivo `.env` está en `.gitignore` y **nunca** debe subirse al repositorio.

## Paso 4: Compilar el Proyecto

```bash
# Con Maven wrapper (recomendado)
.\mvnw.cmd clean install     # Windows
./mvnw clean install          # Linux/Mac

# O con Maven global
mvn clean install
```

## Paso 5: Arrancar la Aplicación

```bash
.\mvnw.cmd spring-boot:run   # Windows
./mvnw spring-boot:run        # Linux/Mac
```

La API estará disponible en: `http://localhost:8080`

## Paso 6: Verificar que Funciona

```bash
# Probar el endpoint de salud (Spring Actuator)
curl http://localhost:8080/actuator/health
```

Respuesta esperada:
```json
{
  "status": "UP"
}
```

## Comandos Frecuentes

| Comando | Descripción |
|---------|-------------|
| `mvn clean install` | Compilar y generar artefacto |
| `mvn spring-boot:run` | Arrancar servidor de desarrollo |
| `mvn test` | Ejecutar suite de pruebas |
| `mvn clean compile` | Solo compilar sin tests |

## Estructura de Archivos de Configuración

| Archivo | Propósito |
|---------|-----------|
| `pom.xml` | Dependencias Maven y plugins |
| `.env` | Variables de entorno locales (secretos) |
| `.env.templates` | Plantilla de referencia para `.env` |
| `application.properties` | Configuración de Spring Boot |

## Troubleshooting

### Error: "Access denied for user"
- Verificar que las credenciales en `.env` son correctas.
- Verificar que el usuario de MySQL tiene permisos sobre la base de datos `tablero_db`.

### Error: "Communications link failure"
- Verificar que MySQL está corriendo: `mysqladmin status`.
- Verificar que el puerto 3306 está abierto y la URL en `.env` es correcta.

### Error: "Unknown database tablero_db"
- Crear la base de datos manualmente: `CREATE DATABASE tablero_db;`.

### Error: "mvn no se reconoce"
- Usar el wrapper incluido: `.\mvnw.cmd` (Windows) o `./mvnw` (Linux/Mac).
