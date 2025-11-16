# Gestión de Sprints - Aplicación de Escritorio en Java

## Descripción

Aplicación de escritorio desarrollada en **Java** utilizando **NetBeans** y **Swing** para la gestión de tareas y sprints. Permite a los usuarios organizar sus pendientes, clasificarlos por categorías, prioridades y estados, y agruparlos en **sprints** usando la metodología agil de **SCRUM**. El sistema cuenta con **roles de usuario** que diferencian las funciones de administradores y usuarios normales, asegurando un uso seguro y eficiente de la información.

---

## Funcionalidades

* Gestión de usuarios con roles:

  * **Administrador:** Puede crear, editar y eliminar usuarios, tareas y sprints.
  * **Usuario normal:** Puede ver y actualizar sus tareas asignadas.
* Creación, edición y eliminación de **tareas**:

  * Asignación de prioridad, categoría y estado.
  * Fecha de vencimiento y seguimiento del progreso.
* Gestión de **sprints**:

  * Crear sprints con nombre, fecha de inicio y fecha de fin.
  * Asignar tareas a un sprint específico.
  * Visualizar tareas asociadas a cada sprint.
* Filtrado y búsqueda de tareas y sprints.
* Interfaz gráfica amigable con **Swing**.

---

## Instalación

### 1. Descargar y preparar los archivos

1. Descarga el archivo `.zip` del proyecto:
   [Descargar proyecto](https://drive.google.com/file/d/1YDnQ0iD4OFttuX-Shk9BzUlmKts_qc_k/view?usp=sharing) el mismo repositorio.
2. Descomprime el archivo en una carpeta de tu preferencia.
   Contiene:

   * El proyecto Java.
   * Archivo `.jar` para la conexión con la base de datos.

### 2. Abrir el proyecto en NetBeans

1. Abre **NetBeans**.
2. Selecciona `Archivo → Abrir proyecto`.
3. Busca la carpeta descomprimida y selecciona el proyecto.

### 3. Configurar la librería de conexión (JAR)

1. Haz clic derecho sobre el nombre del proyecto → `Propiedades`.
2. Ve a la sección `Librerías`.
3. En `Classpath`, haz clic en `+ Añadir → Añadir JAR/Carpeta`.
4. Selecciona el archivo `.jar` incluido en el proyecto.
5. Guarda los cambios.

### 4. Crear la base de datos en PostgreSQL

1. Abre **PgAdmin4**.
2. Crea una nueva base de datos con el nombre: `todo`.
3. Copia y ejecuta el siguiente script SQL para crear las tablas y registros iniciales:

```sql
-- Tablas principales
CREATE TABLE prioridad (id SERIAL PRIMARY KEY, nombre VARCHAR(50) NOT NULL);
CREATE TABLE categoria (id SERIAL PRIMARY KEY, nombre VARCHAR(50) NOT NULL);
CREATE TABLE estado (id SERIAL PRIMARY KEY, nombre VARCHAR(50) NOT NULL);
CREATE TABLE usuario (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    rol INT NOT NULL -- 1 = ADMIN, 2 = NORMAL
);
CREATE TABLE tarea (
    id SERIAL PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    descripcion TEXT,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_vencimiento TIMESTAMP,
    idcategoria INT REFERENCES categoria(id),
    idestado INT REFERENCES estado(id),
    idprioridad INT REFERENCES prioridad(id)
);
CREATE TABLE usuario_tarea (
    id SERIAL PRIMARY KEY,
    idusuario INT REFERENCES usuario(id),
    idtarea INT REFERENCES tarea(id)
);
-- Tablas de sprint
CREATE TABLE sprint (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL,
    descripcion TEXT
);
CREATE TABLE sprint_tarea (
    id SERIAL PRIMARY KEY,
    idsprint INT REFERENCES sprint(id),
    idtarea INT REFERENCES tarea(id)
);

-- Datos iniciales
INSERT INTO prioridad (nombre) VALUES ('Alta'), ('Media'), ('Baja');
INSERT INTO categoria (nombre) VALUES ('Trabajo'), ('Personal'), ('Estudio');
INSERT INTO estado (nombre) VALUES ('Pendiente'), ('En Progreso'), ('Completada');
INSERT INTO usuario (nombre, email, password, rol)
VALUES ('Admin', 'admin@correo.com', 'admin123', 1),
       ('Usuario Normal', 'user@correo.com', 'user123', 2);
INSERT INTO tarea (titulo, descripcion, fecha_vencimiento, idcategoria, idestado, idprioridad)
VALUES ('Tarea de prueba 1', 'Descripción de prueba', CURRENT_DATE + INTERVAL '7 days', 1, 1, 1),
       ('Tarea de prueba 2', 'Otra descripción', CURRENT_DATE + INTERVAL '3 days', 2, 2, 2);
INSERT INTO usuario_tarea (idusuario, idtarea) VALUES (2, 1), (2, 2);
```

### 5. Configurar la conexión a la base de datos

1. En NetBeans, abre `DBConnection.java`.
2. Modifica las credenciales de PostgreSQL según tu configuración local.

### 6. Ejecutar el proyecto

1. Busca el archivo `LoginFrame.java`.
2. Haz clic derecho → `Correr archivo (Run File)`.

---

## Tecnologías utilizadas

* **Java SE 8+**
* **Swing** (interfaz gráfica)
* **NetBeans IDE**
* **PostgreSQL**
* **JDBC** (para la conexión con la base de datos)

