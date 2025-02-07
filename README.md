### Escuela Colombiana de Ingeniería

### Arquitectura Empresarial - AREP

#  TALLER MICROFRAMEWORKS

El taller se centrará en la creación y evolución de un servidor web básico en Java, sin el uso de frameworks populares como Spark o Spring. Inicialmente, el servidor manejará múltiples solicitudes de manera secuencial, permitiendo la lectura de archivos locales para servir contenido estático, como páginas HTML, archivos JavaScript, CSS e imágenes.

A medida que avance el taller, se introducirá la implementación de servicios REST en el backend, permitiendo la comunicación asíncrona con aplicaciones web. Además, el servidor se mejorará progresivamente hacia un framework funcional que facilitará el desarrollo de aplicaciones web. Esto incluirá la definición de servicios REST mediante funciones lambda, la gestión de parámetros en consultas, y la configuración eficiente de la ubicación de archivos estáticos, ofreciendo a los desarrolladores herramientas clave para proyectos modernos. 

## Empezando

Estas instrucciones te permitirán obtener una copia del proyecto y ejecutarlo en tu máquina local para propósitos de desarrollo y pruebas.

### Prerequisitos

- Java 21 preferiblemente.
- Maven 3.x
- Acceso a una terminal.

### Instalando

Pasos para configurar el entorno de desarrollo:

1. Clona el repositorio del proyecto:

   ```bash
   git clone https://github.com/MiltonGutierrez/AREP-TALLER-2.git
   cd AREP-TALLER-2
   ```

2. Compila el proyecto usando Maven:

   ```bash
   mvn clean compile
   ```

3. Ejecuta el servidor:

   ```bash
   java -cp target/classes edu.escuelaing.arep.taller1.HttpServer
   ```

4. Accede al servidor desde tu navegador en [http://localhost:8080](http://localhost:8080).

## Arquitectura

El siguiente diagrama de componentes describe la estructura básica de la aplicación, basada en el patrón **MVC (Modelo-Vista-Controlador)**:

![Componentes Arep](https://github.com/user-attachments/assets/8befcfa4-e3dc-4cbf-b2a9-eaa1679a6098)

### Componentes Principales:
1. **Browser - HttpServer - Controller**:
   - **Puerto 8080**: Punto de entrada para las solicitudes HTTP.
   - **HttpServer**: Servidor web básico en Java que maneja solicitudes y respuestas HTTP.
   - **Controller**: Procesa las peticiones del endpoint de /app, valida datos y coordina la interacción entre el servidor y los servicios.

2. **NoteApplication (Lógica de Negocio - Modelo - Http )**:
   - **Services**: Implementan la lógica para operaciones CRUD de notas (crear, leer).
   - **Model**: Define la estructura de datos.
   - **Http**: Es un componente que contiene una implementación propia para representar las clases HttpRequest y HttpResponse.

### Flujo de la Aplicación:
1. El navegador envía solicitudes al **HttpServer** (puerto 8080) este procesa la peticion de archivos HTML, CSS, JS e imagenes..
2. El **Controller** recibe las solicitudes del endpoint /app, valida los parámetros y delega la lógica a los `Services` creando al final la respuesta.
3. Los **Services** interactúan con el **Model** para acceder a la estructura de datos de modo que pueda responder a la petición..
4. El **Controller** genera respuestas HTTP (éxito o error) que el **HttpServer** envía al navegador.

### Diagrama de Clases y Explicación
Se presentara el diagrama de clases que describe los métodos y las dependencias entre las clases existentes para cada componente del backend.

![Clases AREP](https://github.com/user-attachments/assets/74b2d044-e7b5-4926-9197-474321bc71ba)

#### Clases Principales:
1. **Clase** `HttpServer`:
   - **Responsabilidad**: Núcleo del servidor web. Escucha en el puerto definido (`PORT`), gestiona conexiones entrantes y delega solicitudes.
   - **Atributos Clave**:
     - `PORT`: Puerto de escucha (ej: *8080*).
     - `WEB_ROOT`: Ruta de archivos estáticos (HTML, CSS, JS).
     - `noteController`: Controlador para peticiones con el endpoint /app/**.
   - **Métodos Destacados**:
     - `runServer()`: Inicia el servidor y acepta conexiones.
     - `handleRequests()`: Dirige solicitudes a métodos específicos (GET/POST) .
     - `handleGetRequests()`: Retorna archivos estáticos que se encuentran en el webroot del servidor (ej: *notes.html*).
     - `handleAppGetRequests()`: Maneja las solicitudes *GET* realizadas al endpoint /app/** de manera que utiliza la función lamda implementada en el controlador para poder obtener el recurso.
     - `handleAppPostRequests()`:Maneja las solicitudes *POST* realizadas al endpoint /app/** de manera que utiliza la función lamda implementada en el controlador para poder realizar la petición.

2. **Controladores**:
   - **Interfaz `NoteController`**:
     - Define métodos como:
         - `get()` para guardar los servicios *GET* mendiante la implementación de la función lambda (BiFunction<T,F,R>).
         - `post()` para  guardar los servicios *POST* mediante el uso de funciones lambda (BiFunction<T,F,R>).
         - `getServices()` retorna la función del servicio *GET* según la ruta dada.
         - `postServices()` retorna la función del servicio *POST* según la ruta dada.

   - **Clase `NoteControllerImpl`**:
     - Implementa la interfaz y utiliza *NoteServices* para acceder a la lógica de negocio.
     - Define el metodo *setRoutes()* que permite al programador asignar las rutas o servicios del controlador.
     - Implementación:
  ```java

   private void setRoutes() {
        post("/note", (req, res) -> {
            String title = req.getQueryParams().get("title");
            String group = req.getQueryParams().get("group");
            String content = req.getQueryParams().get("content");
            System.out.println(title + " " + group + " " + content);
            try {
                noteServices.addNote(title, group, content);
                return "{ \"title\": " + "\"" + title + "\", " + "\"group\": " + "\"" + group + "\", "
                        + "\"content\": " + "\"" + content + "\" " + "}";
            } catch (Exception e) {
                return "{ \"error\": " + "\"" + e.getMessage() + "\"}";
            }
        });

        get("/note", (req, res) -> {
            return "[" + noteServices.getNotes().stream()
                .map(note -> String.format(
                    "{\"title\":\"%s\", \"group\":\"%s\", \"content\":\"%s\", \"date\":\"%s\"}",
                    note.getTitle(),
                    note.getGroup().name(),
                    note.getContent(),
                    note.getDate().toString()))
                .collect(Collectors.joining(","))
                +"]";
        });

        get("/pi", (req, resp) -> {
            return String.valueOf(Math.PI);
        });
    }
   ```

     - **Dependencia**: `NoteServices` (inyección de servicios).

3. **Servicios**:
   - **Interfaz `NoteServices`**:
     - Define operaciones como `addNote()` y `getNotes()`.
   - **Clase `NoteServicesImpl`**:
     - Implementa la interfaz y gestiona una lista de notas (`notes`).
     - **Atributo**: `notes` (almacenamiento temporal en memoria).

4. **Modelo**:
   - **Clase `Note`**:
     - Representa una nota con atributos: `title`, `group`, `content`, `date`.
     - **Nota**: `date` sugiere el uso de `LocalDate` para manejar fechas.
    
5. **Http**
   - **Clase `HttpRequest`**
      - Implementa la función *getQueryParams()* que permite obtener la lista de los querys en la petición.

#### Flujo de una Solicitud:
1. **Cliente → `HttpServer`**:  
   El navegador envía una solicitud (ej: `POST /app/notes` con parámetros).
2. **`HttpServer` → `NoteController`**:  
   El servidor detecta rutas bajo `/app` y delega al controlador.
3. **`NoteController` → `NoteServices`**:  
   El controlador valida los datos y usa el servicio para agregar/retornar notas.
4. **Respuesta HTTP**:  
   - Éxito: `200 OK` con JSON de notas.  
   - Error: `400 Bad Request` con mensaje descriptivo (ej: parámetros inválidos).


### Validaciones y Pruebas:
- Se implementaron pruebas automatizadas con **JUnit** para validar solicitudes `GET`/`POST`, incluyendo manejo de errores (ej: parámetros inválidos devuelven código `400` y JSON con detalles).
- El cliente incluye validaciones frontend para evitar enviar datos incompletos.

## Correr las pruebas 

Para ejecutar las pruebas automatizadas del sistema:

```bash
mvn test
```

### Pruebas 

Estas pruebas validan el correcto funcionamiento de las funcionalidades principales del servidor. Especificamente las peticiones GET y POST hacia el recurso /app/note (para la realización de estas se utilizo JUnit Jupiter. Una muestra de estas pruebas es: 
```java
    @Test
    void testGetNotesResponseShouldReturnEmptyArray() {
        String responseByController = noteController.getServices("/app/note").apply(null, null);
        String responseThatShouldReturn = "[" + "]";
        assertEquals(responseByController, responseThatShouldReturn);
    }
```
# Descripción de las Pruebas

## 1. testGetNotesResponseShouldReturnEmptyArray
**Propósito:**  
Verificar que el método `getNotes()` retorne un Array vacio..

**Qué prueba:**
- Cuerpo de la respuesta como un arreglo JSON vacío (`[]`).

## 2. testGetNotesResponseShouldReturnArrayWithCreatedNotes
**Propósito:**  
Validar que `getNotes()` retorne un JSON con todas las notas creadas, incluyendo sus metadatos.

**Qué prueba:**
- Formato correcto de los campos: `title`, `group`, `content`, `date`.
- Coherencia entre las notas añadidas y las mostradas.

## 3. testPostNoteResponseShouldHandleErrors
**Propósito:**  
Garantizar que el controlador maneje errores en solicitudes POST con parámetros inválidos.

**Qué prueba:**
- Respuestas 400 Bad Request para casos como:
  - Parámetros vacíos (`title=`, `group=`, `content=`).
  - Grupos no permitidos (ej: `group=hi`).
  - Mensajes de error claros en formato JSON (ej: `{"error": "Some parameters are empty"}`).

## 4. testPostNoteResponseShouldReturnNote
**Propósito:**  
Asegurar que una solicitud POST válida retorne la nota creada en formato JSON.

**Qué prueba:**
- Coincidencia exacta entre los campos enviados (`title`, `group`, `content`) y los devueltos.

## 5. shouldThrowNotesServicesExceptionSomeParametersAreEmpty
**Propósito:**  
Validar que el servicio rechace parámetros vacíos lanzando `NoteServicesException`.

**Qué prueba:**
- Escenarios como:
  - Todos los campos vacíos.
  - Campos parcialmente vacíos (`title=`, `group=work`, `content=`).

## 6. shouldThrowNotesServicesExceptionInvalidGroup
**Propósito:**  
Comprobar que el servicio solo permita grupos predefinidos (`personal` o `work`).

**Qué prueba:**
- Lanzamiento de excepciones para grupos no válidos (ej: `invalid`, `personal1`).

## 7. shouldAddNotes
**Propósito:**  
Confirmar que el servicio añade notas correctamente cuando los parámetros son válidos.

**Qué prueba:**
- Incremento del tamaño de la lista de notas después de agregar elementos.
- Ausencia de excepciones en casos válidos.


# Tecnologías Usadas en Pruebas
- **JUnit Jupiter 5:** Para pruebas unitarias y parametrizadas.
- **Maven:** Gestión de dependencias y ejecución de pruebas.

- **Resultado de las pruebas**
![image](https://github.com/user-attachments/assets/2f42a15e-784c-4cf5-8959-78cc1516cfc8)

### Muestra de la ejecución

1. Acceso al aplicativo:
![image](https://github.com/user-attachments/assets/7942c3df-bb6f-479a-9a4c-a3da2f44ade8)
2. Ejemplo creación de la nota
![image](https://github.com/user-attachments/assets/a2da0022-34cf-48a0-a57e-5965e6db0b0b)
3. Ejemplo petición de notas creadas
![image](https://github.com/user-attachments/assets/63429968-5db6-44f2-9b22-b9d873899ce3)
4. Ejemplo añadir nota con datos incompletos (se realiza la validación desde el cliente por lo que no se ejecuta la petición)
![image](https://github.com/user-attachments/assets/35d2ef47-8986-4233-807f-ed11c2dd91f0)
5. Ejemplo petición al recurso /app/pi
![image](https://github.com/user-attachments/assets/da8ef5df-8767-4064-bf42-ee1e5f871c60)

## Construido con.

- [Maven](https://maven.apache.org/) - Dependency Management

## Autores

- **Milton Andres Gutierrez Lopez** - *Initial work* - [MiltonGutierrez](https://github.com/MiltonGutierrez)

## Licencia

Este proyecto está licenciado bajo la Licencia GNU - mira el archivo [LICENSE.md](LICENSE.md) para más detalles.

