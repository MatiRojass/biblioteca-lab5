# Biblioteca Almafuerte

Aplicación web para gestionar una biblioteca: catálogo de libros y revistas, alta de socios, préstamos con carrito de reserva, devoluciones con cálculo de multas y un formulario de consultas del público.

Está construida sobre **Jakarta EE 10** (Jakarta Faces + CDI + JPA + JAX-RS) y se empaqueta como `.war` para desplegar en **GlassFish 7**.

---

## Índice

1. [Tecnologías](#tecnologías)
2. [Requisitos](#requisitos)
3. [Puesta en marcha](#puesta-en-marcha)
4. [Estructura del proyecto](#estructura-del-proyecto)
5. [Arquitectura](#arquitectura)
6. [Modelo de datos](#modelo-de-datos)
7. [Capa de acceso a datos (DAO)](#capa-de-acceso-a-datos-dao)
8. [Controladores (Managed Beans)](#controladores-managed-beans)
9. [Vistas](#vistas)
10. [Reglas de negocio de préstamos](#reglas-de-negocio-de-préstamos)
11. [Roles y autenticación](#roles-y-autenticación)
12. [Limitaciones conocidas](#limitaciones-conocidas)

---

## Tecnologías

| Capa | Tecnología |
|---|---|
| Lenguaje | Java 11 (source/target) |
| Plataforma | Jakarta EE 10 (`jakarta.jakartaee-api` 10.0.0, `provided`) |
| Vistas | Jakarta Faces (Facelets `.xhtml`), Tailwind CSS vía CDN, CSS y JS propios |
| Inyección / scopes | CDI (`bean-discovery-mode="all"`) |
| Persistencia | JPA 3.0, unidad `BibliotecaPU`, transacciones JTA |
| REST | JAX-RS (`/resources/*`) |
| Build | Maven, empaquetado `war` |
| Servidor | GlassFish 7 (configurado en NetBeans como `gfv700ee10`) |

## Requisitos

- JDK 11 o superior (GlassFish 7 requiere JDK 11+; se recomienda 17).
- Maven 3.x.
- GlassFish 7.
- Una base de datos relacional con las tablas descritas en [Modelo de datos](#modelo-de-datos) y su driver JDBC instalado en GlassFish.

## Puesta en marcha

### 1. Crear el data source en GlassFish

La unidad de persistencia usa el recurso JNDI **`bibliounse`** ([persistence.xml](src/main/resources/META-INF/persistence.xml)). Hay que crearlo antes de desplegar:

1. Copiar el driver JDBC de la base de datos a `glassfish/domains/domain1/lib/`.
2. En la consola de administración (`http://localhost:4848`):
   - **Resources → JDBC → JDBC Connection Pools → New**: crear un pool apuntando a la base.
   - **Resources → JDBC → JDBC Resources → New**: nombre JNDI `bibliounse`, asociado al pool anterior.

O por línea de comandos:

```bash
asadmin create-jdbc-connection-pool --datasourceclassname <clase-DataSource-del-driver> \
    --restype javax.sql.DataSource \
    --property user=<usuario>:password=<clave>:databaseName=<base>:serverName=localhost:portNumber=<puerto> \
    biblioPool
asadmin create-jdbc-resource --connectionpoolid biblioPool bibliounse
```

> La aplicación **no genera el esquema automáticamente**: `persistence.xml` no define `jakarta.persistence.schema-generation`. Las tablas deben existir previamente.

### 2. Cargar datos mínimos

Para poder usar la aplicación se necesita al menos:

- Un registro en `administradores` (para entrar al panel de administración).
- Registros en `categorias` y `autores` (se eligen desde combos al cargar libros; no hay ABM para ellos en la interfaz).

### 3. Compilar y desplegar

```bash
mvn clean package
asadmin deploy target/biblioteca-1.0.war
```

Desde NetBeans alcanza con **Run** sobre el proyecto (ya está asociado a GlassFish 7).

La aplicación queda en `http://localhost:8080/biblioteca-1.0/` (o el context root que asigne el IDE). La página de inicio es `index.xhtml`.

## Estructura del proyecto

```
biblioteca/
├── pom.xml
├── nb-configuration.xml              # Configuración de NetBeans (servidor GlassFish 7)
└── src/main/
    ├── java/com/biblioteca/
    │   ├── config/                   # Activación de JAX-RS
    │   ├── controlador/              # Managed beans de Faces (lógica de pantalla)
    │   ├── modelo/                   # Entidades JPA
    │   ├── repositorio/              # DAOs con EntityManager
    │   └── rest/                     # Recursos REST
    ├── resources/META-INF/
    │   └── persistence.xml           # Unidad de persistencia BibliotecaPU
    └── webapp/
        ├── *.xhtml                   # Vistas Facelets
        ├── resources/css/style.css
        ├── resources/js/codigo.js    # Carrusel y "leer más" del inicio
        ├── resources/images/         # Logo, portadas, fotos
        └── WEB-INF/
            ├── web.xml               # FacesServlet, sesión de 30 min, welcome file
            ├── beans.xml             # CDI
            └── glassfish-web.xml
```

## Arquitectura

Arquitectura en tres capas, siguiendo MVC de Jakarta Faces:

```
 Vista (.xhtml)  ──EL #{...}──►  Controlador (@Named bean)  ──@Inject──►  DAO (@ApplicationScoped)  ──JPA──►  BD
```

- **Vistas**: todas usan la plantilla [layout2.xhtml](src/main/webapp/layout2.xhtml), que define el encabezado, la navegación (dependiente del rol logueado), el formulario de consultas y dos zonas: `contenido-principal` y `aside`.
- **Controladores** (`com.biblioteca.controlador`): beans CDI con nombre, `@ViewScoped` para pantallas de ABM y `@SessionScoped` para login y carrito.
- **DAOs** (`com.biblioteca.repositorio`): beans `@ApplicationScoped` que reciben un `EntityManager` con `@PersistenceContext(unitName = "BibliotecaPU")`. Los métodos de escritura están anotados con `@Transactional`.
- **Modelo** (`com.biblioteca.modelo`): entidades JPA con IDs autoincrementales (`GenerationType.IDENTITY`).

## Modelo de datos

| Entidad | Tabla | Campos |
|---|---|---|
| `Administrador` | `administradores` | `id`, `username`, `password` |
| `Autor` | `autores` | `id`, `nombres`, `apellido` |
| `Categoria` | `categorias` | `id`, `nombre` |
| `Consulta` | `consultas` | `id`, `nombreCompleto`, `email`, `consulta` |
| `Libro` | `libros` | `id`, `titulo`, `"year"`, `edicion`, `editorial`, `descripcion`, `disponibles`, `categorias_id`, `autores_id` |
| `Revista` | `revistas` | `id`, `nombre`, `volumen`, `anio`, `editorial`, `categorias_id` |
| `Socio` | `socios` | `id`, `apellido`, `nombres`, `dni`, `direccion`, `telefono`, `password` |
| `Prestamo` | `prestamos` | `id`, `fecha`, `fecha_devuelve`, `multa`, `socios_id` |
| — | `librosprestamos` | `prestamos_id`, `libros_id` (tabla intermedia) |

Relaciones:

```
socios 1 ──── N prestamos N ──── M libros        (vía librosprestamos)
libros.categorias_id  → categorias.id   (columna Integer, sin relación JPA)
libros.autores_id     → autores.id      (columna Integer, sin relación JPA)
revistas.categorias_id → categorias.id  (columna Integer, sin relación JPA)
```

Notas:

- La columna `year` de `libros` se mapea entre comillas (`@Column(name = "\"year\"")`) porque es palabra reservada en algunos motores.
- `Prestamo.fecha_devuelve` es `NULL` mientras el préstamo está activo.
- `Prestamo.multa` se inicializa en `0`.

## Capa de acceso a datos (DAO)

| DAO | Operaciones |
|---|---|
| `AdministradorDAO` | `validarLogin(username, password)` → `Administrador` o `null` |
| `AutorDAO` | `listarTodos()` (orden apellido, nombres), `buscar`, `guardar`, `actualizar`, `eliminar` |
| `CategoriaDAO` | `listarTodas()` (orden nombre), `buscar`, `guardar`, `actualizar`, `eliminar` |
| `ConsultaDAO` | `guardar`, `eliminar`, `listarTodas()` (más recientes primero) |
| `LibroDAO` | `guardar`, `actualizar`, `eliminar`, `buscar`, `listarTodos()` (orden título), `buscarPorTitulo(texto)` (LIKE sin distinguir mayúsculas) |
| `RevistaDAO` | `guardar`, `actualizar`, `eliminar`, `buscar`, `listarTodos()` (orden nombre) |
| `SocioDAO` | `guardar`, `actualizar`, `eliminar`, `buscar`, `validarLogin(dni, password)`, `listarTodos()` |
| `PrestamoDAO` | `guardar`, `actualizar`, `buscarPorId`, `listarPrestamosActivos()`, `tienePrestamosVencidos(socioId)`, `cantidadLibrosSinDevolver(socioId)` |

## Controladores (Managed Beans)

| Bean (nombre EL) | Scope | Responsabilidad |
|---|---|---|
| `indexBean` | View | Lista los libros del inicio y filtra por título (`buscar()`); búsqueda vacía muestra todos. |
| `libroBean` | View | ABM de libros. Carga combos de categorías y autores. `guardar()` hace insert o update según la bandera `editado`. |
| `revistaBean` | View | ABM de revistas, con combo de categorías. |
| `socioBean` | View | ABM de socios. |
| `consultaBean` | View | Envío de consultas desde el formulario público y listado/borrado para el administrador. |
| `loginBean` | Session | Login/logout del administrador. `isLogueado()` controla qué opciones muestra el menú. |
| `loginSocioBean` | Session | Login/logout del socio por DNI y contraseña. |
| `prestamoBean` | Session | Carrito de reserva, confirmación de préstamo, búsqueda de ticket y registro de devolución con multa. |

## Vistas

| Página | Propósito | Quién la ve en el menú |
|---|---|---|
| `index.xhtml` | Catálogo de libros con buscador y botón para agregar al carrito | Todos |
| `socios.xhtml` | ABM de socios (menú "Clientes") | Todos |
| `login.xhtml` | Login de administrador | Sin admin logueado |
| `login-socio.xhtml` | Login de socio (DNI + contraseña) | Se llega al intentar reservar sin sesión |
| `carrito.xhtml` | Carrito de reserva y confirmación del préstamo | Socio logueado |
| `libros.xhtml` | ABM de libros | Administrador |
| `ver-consultas.xhtml` | Listado y eliminación de consultas | Administrador |
| `devoluciones.xhtml` | Búsqueda de préstamo por número y registro de devolución | Administrador |
| `revistas.xhtml` | ABM de revistas | No está enlazada en el menú; se accede por URL |
| `descripcion-libros.xhtml` | Página de detalle (actualmente es un placeholder vacío) | Enlaces desde listados |
| `layout2.xhtml` | Plantilla común: header, navegación, formulario de consultas | — |

## Reglas de negocio de préstamos

Implementadas en [PrestamoBean.java](src/main/java/com/biblioteca/controlador/PrestamoBean.java) y [PrestamoDAO.java](src/main/java/com/biblioteca/repositorio/PrestamoDAO.java).

**Al agregar un libro al carrito** (`agregarAlCarrito`):

1. Si no hay socio logueado, redirige a `login-socio.xhtml`.
2. Si el socio tiene algún préstamo sin devolver con más de **7 días**, se rechaza.
3. Límite de **3 libros** entre los que ya tiene prestados y los que están en el carrito.
4. El libro debe tener `disponibles > 0`.
5. No se agrega dos veces el mismo libro.

**Al confirmar el préstamo** (`confirmarPrestamo`):

- Se crea un `Prestamo` con la fecha de hoy y los libros del carrito.
- Se descuenta 1 unidad de `disponibles` a cada libro.
- Se vacía el carrito. El socio tiene **7 días corridos** para devolver.

**Devolución** (`buscarTicket` / `registrarDevolucion`, hecha por el administrador):

- Se busca el préstamo por su número; solo se aceptan préstamos activos.
- Multa = **$500 × días de atraso × cantidad de libros** del préstamo. El vencimiento es `fecha + 7 días`.
- Se registra `fecha_devuelve`, la multa y se repone el stock de cada libro.

## Roles y autenticación

| Rol | Credenciales | Tabla | Qué habilita |
|---|---|---|---|
| Administrador | usuario + contraseña | `administradores` | Libros ABM, Consultas, Devoluciones |
| Socio | DNI + contraseña | `socios` | Carrito y préstamos |
| Visitante | — | — | Catálogo, búsqueda, formulario de consultas |

La sesión expira a los 30 minutos (`web.xml`). El logout invalida la sesión HTTP completa.


## Limitaciones conocidas

Puntos a tener en cuenta antes de usar el sistema fuera de un entorno de práctica:

- **Contraseñas en texto plano**: administradores y socios se guardan y comparan sin hash.
- **Páginas sin protección de acceso**: el rol solo oculta enlaces del menú. `libros.xhtml`, `devoluciones.xhtml`, `ver-consultas.xhtml`, `revistas.xhtml` y `socios.xhtml` pueden abrirse escribiendo la URL sin haber iniciado sesión. Haría falta un filtro o chequeo en los beans.
- **Relaciones por ID**: `Libro` y `Revista` guardan `categorias_id` / `autores_id` como enteros, sin `@ManyToOne`, así que JPA no valida la integridad (depende de las FK de la base).
- **Sin ABM de autores ni categorías** en la interfaz, aunque los DAOs lo soportan.
- **Stock sin control de concurrencia**: dos socios pueden reservar la última unidad al mismo tiempo; la validación de stock se hace al agregar al carrito, no al confirmar.
- **Carrito y comparación de libros**: `Libro` no redefine `equals`/`hashCode`, por lo que la verificación de duplicados en el carrito compara por referencia.
- `descripcion-libros.xhtml` es un placeholder sin contenido.
- `PROJECT_STAGE` está en `Development` en `web.xml`; conviene pasarlo a `Production` al desplegar.
