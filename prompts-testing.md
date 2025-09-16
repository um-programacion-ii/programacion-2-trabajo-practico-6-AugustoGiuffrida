# Prompts-test

## Prompt 1: entender como testear la comunicacion entre microservicios

### Prompt utilizado:
En el contexto de la aplicacion en la que estamos trabajando Como podria realizar test que permitan probar la comunicacion entre microservicios

### Respuesta:   

Para probar la **comunicación entre microservicios** en un proyecto Spring Boot con FeignClient, puedes utilizar **WireMock**:

---

### 1️⃣ Agregar la dependencia de WireMock

En tu `pom.xml` agrega:

```xml
<dependency>
    <groupId>com.github.tomakehurst</groupId>
    <artifactId>wiremock-jre8</artifactId>
    <version>2.35.0</version>
    <scope>test</scope>
</dependency>
```

Esto te permitirá usar WireMock dentro de tus tests.

---

### 2️⃣ Configurar WireMock en los tests

Puedes arrancar un servidor WireMock local dentro de un test de integración. Por ejemplo:

```java
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.*;

@SpringBootTest
public class ProductoBusinessServiceTest {

    private static WireMockServer wireMockServer;

    @BeforeAll
    static void setup() {
        wireMockServer = new WireMockServer(8089); // Puerto para el stub
        wireMockServer.start();
        WireMock.configureFor("localhost", 8089);
    }

    @AfterAll
    static void teardown() {
        wireMockServer.stop();
    }
}
```

---

### 3️⃣ Crear stubs con `stubFor`

El método `stubFor` te permite definir **qué respuesta debe devolver WireMock** cuando recibe una petición específica. Por ejemplo, para simular un GET a `/data/productos/1`:

```java
WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/data/productos/1"))
        .willReturn(WireMock.aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody("{\"id\":1,\"nombre\":\"Producto A\",\"precio\":10}")));
```

* `get(urlEqualTo("/data/productos/1"))`: Indica que se espera una petición GET a esa URL.
* `willReturn(aResponse()...)`: Define la respuesta que devolverá el stub.

    * `withStatus(200)`: Código HTTP de la respuesta.
    * `withHeader(...)`: Headers que deseas enviar.
    * `withBody(...)`: Cuerpo de la respuesta en formato JSON.

Para un POST:

```java
WireMock.stubFor(WireMock.post(WireMock.urlEqualTo("/data/productos"))
        .withRequestBody(WireMock.matchingJsonPath("$.nombre"))
        .willReturn(WireMock.aResponse()
            .withStatus(201)
            .withHeader("Content-Type", "application/json")
            .withBody("{\"id\":2,\"nombre\":\"Producto B\"}")));
```

* `withRequestBody(matchingJsonPath(...))`: Permite validar que el request body tenga cierta estructura o campo.

---

### 4️⃣ Testear la comunicación usando FeignClient

Dentro del test, tu servicio Spring Boot hará la llamada a la URL que WireMock está simulando:

```java
@Autowired
private ProductoBusinessService productoBusinessService;

@Test
void testGetProductoById() {
    ProductoDTO producto = productoBusinessService.findProductById(1L);

    Assertions.assertEquals("Producto A", producto.getNombre());
    Assertions.assertEquals(10, producto.getPrecio().intValue());
}
```

WireMock responderá según el stub definido, sin necesidad de que el servicio real esté levantado.

---

### 5️⃣ Simular errores o fallos de comunicación

Puedes probar cómo tu microservicio maneja errores del servicio externo:

```java
WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/data/productos/999"))
        .willReturn(WireMock.aResponse()
            .withStatus(404)
            .withHeader("Content-Type", "application/json")
            .withBody("{\"error\":\"Producto no encontrado\"}")));
```

Esto te permite verificar que tu `BusinessService` lance la excepción correcta (`ProductoNoEncontrado`) o devuelva la respuesta esperada.

---

### ✅ Resumen

* WireMock permite simular **servicios externos** para pruebas.
* `stubFor` define **qué respuesta debe devolver WireMock** según el request recibido.
* Puedes simular **respuestas exitosas, errores, latencia, headers personalizados**, etc.
* Con esto, puedes probar **toda la comunicación entre microservicios** sin depender de servicios reales.

---
