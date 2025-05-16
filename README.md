# Inditex Technical Test

Este proyecto corresponde a una prueba técnica para Inditex, en la que se implementa una API sencilla para consultar
precios según ciertos criterios como `productId`, `brandId` y `applicationDate`.

## Documentación y Accesos

* **Swagger UI:** [Swagger](http://localhost:8080/swagger-ui/index.html)
* **H2 Database Console:** [H2 Console](http://localhost:8080/h2-console/login.jsp)

    * **JDBC URL:** `jdbc:h2:mem:localdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE`

## Aspectos Técnicos a Destacar

### Decisiones de Diseño

* El atributo `brand_id` actualmente está implementado directamente como un ID numérico. Existe la posibilidad de
  ampliarlo mediante una tabla `brand` independiente o usando un enum con múltiples atributos, gestionado mediante un
  `AttributeConverter` para mejorar la sincronización con la base de datos.

* En el caso de `currency`, se decidió mantener como Enum directamente en la entidad `PriceEntity`. Se consideró
  mapearlo posteriormente al dominio como String, pero se optó por evitar complejidad adicional.

* El uso de identificadores (IDs) como tipo `Integer` podría ser reemplazado por `Long` para futuras ampliaciones,
  aunque no impacta significativamente esta prueba.

### Consultas SQL

Actualmente se utiliza una consulta SQL nativa debido a las limitaciones de JPA para ejecutar consultas que devuelvan
una única entidad priorizada:

```java

@Query(
        value = "SELECT * " +
                "FROM PRICES p " +
                "WHERE p.PRODUCT_ID = :productId " +
                "  AND p.BRAND_ID = :brandId " +
                "  AND :applicationDate BETWEEN p.START_DATE AND p.END_DATE " +
                "ORDER BY p.PRIORITY DESC " +
                "LIMIT 1",
        nativeQuery = true
)
Optional<PriceEntity> findTopByCriteria(LocalDateTime applicationDate, Long productId, Integer brandId);
```

### Tests

Los tests implementados cubren los casos más relevantes. Debido a limitaciones de tiempo, algunos casos de error
adicionales podrían haber sido incluidos para asegurar un manejo exhaustivo de excepciones.

### Dependencias y Configuración

* La gestión de dependencias actualmente es extensa debido al uso intencional de diversas herramientas y librerías,
  incluyendo:

    * Generador de APIs con OpenAPI y Mustache.
    * MapStruct para facilitar la conversión entre entidades y DTOs.

* Se podría optimizar la configuración mediante la centralización de versiones en el apartado `<properties>` del archivo
  `pom.xml`.

* El proyecto podría mejorarse mediante herramientas como SonarQube en Docker para análisis de calidad del código,
  vulnerabilidades y cobertura de pruebas.

## Observaciones Adicionales

* La mezcla de comentarios en español e inglés es producto de la naturaleza rápida de esta prueba. En condiciones
  ideales, todos los comentarios deberían seguir un único idioma consistente.

* El historial de commits está simplificado, dado el contexto individual de trabajo. En condiciones profesionales, sería
  importante mantener una gestión adecuada de commits, branches y merges utilizando Git.

Este proyecto refleja la aplicación de múltiples tecnologías y buenas prácticas aprendidas, adaptado al contexto
limitado de tiempo disponible para esta prueba técnica.
