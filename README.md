# ForoHub API (Spring Boot 3 + JWT)

API REST para gestionar **tópicos** (foro) con autenticación **JWT**.

## ✅ Funcionalidades
- CRUD de tópicos:
    - Crear (201 + Location)
    - Listar (paginado y ordenable)
    - Detalle por ID
    - Actualizar (parcial)
    - Eliminar (204)
- Validaciones (`@NotBlank` en creación)
- Regla de negocio: no duplicar por **título + mensaje**
- Seguridad con **JWT** (solo `/auth` abierto)

---

## 🧱 Stack
- **Java 17**, **Spring Boot 3**
- Spring **Web**, **Data JPA**, **Validation**, **Security (JWT)**
- **MySQL 8**, **Maven**
- (Opcional) **Flyway** para migraciones

---

## 📦 Estructura (resumen)
src/main/java/com/alura/foro
├─ controller
│ └─ TopicoController.java
├─ dto
│ ├─ DatosRegistroTopico.java
│ ├─ DatosActualizarTopico.java
│ └─ DatosListadoTopico.java
├─ infra/security
│ ├─ SecurityConfiguration.java
│ └─ SecurityFilter.java
├─ model
│ └─ Topico.java
└─ repository
└─ TopicoRepository.java

yaml
Copiar
Editar

---

## ⚙️ Configuración
Archivo: `src/main/resources/application.properties`
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/forohub?createDatabaseIfNotExist=true
spring.datasource.username=TU_USUARIO
spring.datasource.password=TU_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
Sugerencia: maneja credenciales/JWT secret con variables de entorno o application-test.properties.

🗃️ Base de datos
Entidad Topico (tabla topico):

id (PK, autoincrement)

titulo (String)

mensaje (String)

fechaCreacion (LocalDateTime, default now())

status (String, default NO_RESPONDIDO)

autor (String)

curso (String)

(Con Flyway opcional puedes versionar el esquema.)

▶️ Cómo ejecutar
bash
Copiar
Editar
mvn spring-boot:run
# o
mvn clean package && java -jar target/*.jar
🔐 Autenticación (JWT)
Login

bash
Copiar
Editar
POST /auth
Content-Type: application/json

{ "login": "jason", "clave": "123456" }
200 OK

json
Copiar
Editar
{ "jwt": "eyJhbGciOi..." }
Usar el token

makefile
Copiar
Editar
Authorization: Bearer eyJhbGciOi...
Rutas protegidas: CRUD de /topicos (en SecurityConfiguration se permite /auth/** y Swagger si lo usas).

📚 Endpoints (Tópicos)
Crear
pgsql
Copiar
Editar
POST /topicos
Authorization: Bearer <token>
Content-Type: application/json

{
  "titulo": "Duda con Spring Security",
  "mensaje": "¿Cómo configuro JWT?",
  "autor": "Jason",
  "curso": "Spring Boot"
}
201 Created

Header Location: /topicos/{id}

Body:

json
Copiar
Editar
{
  "id": 1,
  "titulo": "...",
  "mensaje": "...",
  "fechaCreacion": "2025-08-15T12:34:56",
  "status": "NO_RESPONDIDO",
  "autor": "...",
  "curso": "..."
}
Listar (paginado)
sql
Copiar
Editar
GET /topicos?page=0&size=10&sort=fechaCreacion,desc
Authorization: Bearer <token>
200 OK → Page<DatosListadoTopico> (propiedades content, totalElements, totalPages, etc.).

Detalle
bash
Copiar
Editar
GET /topicos/{id}
Authorization: Bearer <token>
200 OK → DatosListadoTopico

Actualizar (parcial)
bash
Copiar
Editar
PUT /topicos/{id}
Authorization: Bearer <token>
Content-Type: application/json

{ "titulo": "Editado", "mensaje": "Editado" }
200 OK → DatosListadoTopico

Eliminar
bash
Copiar
Editar
DELETE /topicos/{id}
Authorization: Bearer <token>
204 No Content

✅ Validaciones y reglas
Crear: titulo, mensaje, autor, curso → @NotBlank.

Regla: no se permite duplicado por titulo + mensaje (TopicoRepository.existsByTituloAndMensaje).

Actualizar: permite parcial (DatosActualizarTopico con campos opcionales).

❗ Errores comunes
400 Bad Request → validaciones o duplicado.

401/403 → sin token o token inválido.

404 Not Found → recurso inexistente (ej: GET/PUT/DELETE /topicos/{id} luego de eliminar).

🧪 Pruebas rápidas (curl)
bash
Copiar
Editar
# login
TOKEN=$(curl -s -X POST http://localhost:8080/auth \
  -H "Content-Type: application/json" \
  -d '{"login":"jason","clave":"123456"}' | jq -r .jwt)

# crear
curl -i -X POST http://localhost:8080/topicos \
  -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" \
  -d '{"titulo":"Duda","mensaje":"JWT?","autor":"Jason","curso":"Spring Boot"}'
📄 Licencia
Uso educativo.ue