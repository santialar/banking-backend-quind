# banking-backend-quind

Esta es una aplicación bancaria desarrollada con **Spring Boot** y **arquitectura hexagonal** que permite la gestión de clientes, productos financieros y transacciones.

##  Arquitectura

El proyecto sigue los principios de **Arquitectura Hexagonal (Ports & Adapters)**, lo que proporciona:

- **Separación clara de responsabilidades**
- **Independencia de frameworks y tecnologías**
- **Facilidad para testing**
- **Mantenibilidad y escalabilidad**

### Estructura del Proyecto

```
src/main/java/com/quind/financialapp/
├── domain/                          # Lógica de negocio
│   ├── model/                       # Entidades de dominio
│   ├── port/
│   │   ├── in/                      # Puertos de entrada (casos de uso)
│   │   └── out/                     # Puertos de salida (repositorios)
│   └── exception/                   # Excepciones del dominio
├── application/                     # Servicios de aplicación
│   └── service/                     # Implementación de casos de uso
└── infrastructure/                  # Adaptadores
    └── adapter/
    |    ├── in/                     # Adaptadores de entrada
    |    │   └── web/                # Controladores REST
    |    └── out/                    # Adaptadores de salida
    |       └── persistence/         # Persistencia con JPA
    └── config/                      # Swagger/OpenAPI
         └── OpenApiConfig           # configura la documentación OpenAPI/Swagger de tu aplicación      
```

##  Características

### Funcionalidades Principales

#### Gestión de Clientes
-  Crear cliente (validación de mayoría de edad)
-  Consultar cliente por ID o número de identificación
-  Actualizar información del cliente
-  Eliminar cliente (solo si no tiene productos vinculados)
-  Validaciones de negocio (email y longitud de nombres)

#### Gestión de Productos Financieros
-  Crear cuentas de ahorro y corrientes
-  Generación automática de números de cuenta únicos
-  Cuentas de ahorro inician con "53", corrientes con "33"
-  Cambio de estado (Activa, Inactiva, Cancelada)
-  Validación de saldo cero para cancelación

#### Gestión de Transacciones
-  Consignaciones
-  Retiros (con validación de saldo para cuentas de ahorro)
-  Transferencias entre cuentas
-  Actualización automática de saldos
-  Histórial completo de transacciones

### Validaciones de Negocio
-  Clientes menores de edad no pueden ser registrados
-  Clientes con productos no pueden ser eliminados
-  Cuentas de ahorro no pueden tener saldo negativo
-  Solo cuentas con saldo cero pueden ser canceladas
-  Validación de formato de email
-  Nombres y apellidos mínimo 2 caracteres

##  Tecnologías Utilizadas

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **PostgreSQL**
- **MapStruct** (para mapeo de objetos)
- **OpenAPI/Swagger** (documentación)
- **JUnit 5** (testing)
- **Docker & Docker Compose**

##  Prerrequisitos

- Java 17 o superior
- Maven 3.6+
- Docker y Docker Compose (opcional)
- PostgreSQL 12+ (si no usa Docker)

##  Instalación y Ejecución

### Opción 1: Con Docker 

1. **Clonar el repositorio**
```bash
git clone <https://github.com/santialar/banking-backend-quind.git>
cd banking/Backend-Quind
```

2. **Levantar la base de datos**
```bash
docker-compose up -d postgres
```

3. **ingresas a la interfaz de pgadmin**
   ```bash
    [docker-compose up -d postgres](http://localhost:5050/browser/)
   ```
4. **Credenciales**
   una vez estes en la interfaz colocas las credenciales
    ```bash
      PGADMIN_DEFAULT_EMAIL: admin@admin.com
      PGADMIN_DEFAULT_PASSWORD: admin
   ```

5. **Ejecutar la aplicación**
```bash
mvn spring-boot:run
```

### Opción 2: Sin Docker

1. **Configurar PostgreSQL**
    - Crear base de datos: `financial_db`
    - Usuario: `tus credenciales`
    - Contraseña: `tus credenciales`


3. **Ejecutar la aplicación**
```bash
mvn spring-boot:run
```

### Opción 3: Entorno completo con Docker

```bash
docker-compose up -d
mvn spring-boot:run
```

## Documentación de la API

Una vez iniciada la aplicación, la documentación de la API estará disponible en:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api-docs

### Endpoints Principales

#### Clientes
- `POST /api/clientes` - Crear cliente
- `GET /api/clientes` - Listar todos los clientes
- `GET /api/clientes/{id}` - Obtener cliente por ID
- `GET /api/clientes/identificacion/{numero}` - Obtener cliente por identificación
- `PUT /api/clientes/{id}` - Actualizar cliente
- `DELETE /api/clientes/{id}` - Eliminar cliente

#### Productos
- `POST /api/productos` - Crear producto
- `GET /api/productos` - Listar todos los productos
- `GET /api/productos/{id}` - Obtener producto por ID
- `GET /api/productos/cuenta/{numeroCuenta}` - Obtener producto por número de cuenta
- `GET /api/productos/cliente/{clienteId}` - Obtener productos de un cliente
- `PATCH /api/productos/{id}/estado` - Cambiar estado del producto
- `DELETE /api/productos/{id}` - Eliminar producto

#### Transacciones
- `POST /api/transacciones/consignacion` - Realizar consignación
- `POST /api/transacciones/retiro` - Realizar retiro
- `POST /api/transacciones/transferencia` - Realizar transferencia
- `GET /api/transacciones` - Listar todas las transacciones
- `GET /api/transacciones/{id}` - Obtener transacción por ID
- `GET /api/transacciones/cuenta/{numeroCuenta}` - Obtener transacciones de una cuenta

## Testing

### Ejecutar Tests
```bash
mvn test
```

### Generar Reporte de Cobertura
```bash
mvn clean test jacoco:report
```

El reporte estará disponible en: `target/site/jacoco/index.html`

##  Base de Datos

### Modelo de Datos

La aplicación utiliza las siguientes tablas principales:

- **clientes**: Información de los clientes
- **productos**: Productos financieros (cuentas)
- **transacciones**: Historial de transacciones


##  Ejemplos de Uso

### Crear un Cliente
```json
POST /api/clientes
{
  "tipoIdentificacion": "CEDULA_CIUDADANIA",
  "numeroIdentificacion": "12345678",
  "nombres": "Juan Carlos",
  "apellido": "Pérez",
  "correoElectronico": "juan.perez@email.com",
  "fechaNacimiento": "1990-05-15"
}
```

### Crear un Producto
```json
POST /api/productos
{
  "tipoCuenta": "CUENTA_AHORROS",
  "clienteId": 1,
  "exentaGMF": false
}
```

### Realizar una Consignación

En el request se solicita cuentaOrigen y cuentaDestino, si quieres hacer una cosignacion por primera vez en los dos campos colocas
el mismo numero de cuenta, lo mismo para cuando se requiere hacer un retiro
```json
POST /api/transacciones/consignacion
{
  "numeroCuentaOrigen": "5312345678",
  "monto": 100000.00,
  "descripcion": "Consignación de prueba"
}
```



---
**Desarrollado por Santiago Alarcon Vargas**
