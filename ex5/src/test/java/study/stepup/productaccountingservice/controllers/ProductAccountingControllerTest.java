package study.stepup.productaccountingservice.controllers;

import static io.restassured.RestAssured.given;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.core.IsEqual;
import org.hamcrest.text.MatchesPattern;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.ext.ScriptUtils;
import org.testcontainers.jdbc.JdbcDatabaseDelegate;
import study.stepup.productaccountingservice.models.*;
import java.util.regex.Pattern;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductAccountingControllerTest {
    @LocalServerPort
    private Integer port;
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:alpine3.18");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }


    @BeforeAll
    static void beforeAll() {
        postgres.start();
        ScriptUtils.runInitScript(new JdbcDatabaseDelegate(postgres, ""), "db/schema.sql");
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        ScriptUtils.runInitScript(new JdbcDatabaseDelegate(postgres, ""), "db/testData.sql");
    }


    @Test
    @DisplayName("Передаем в запросе несуществующий идентификатор tpp_product")
    void testNotFoundWithTppProduct() {
        ProductAccountingRequest request = getNewProductAccountingRequest();
        request.setInstanceId(9999L);

        given()
                .contentType(ContentType.JSON)
                .when()
                .body(request)
                .post("corporate-settlement-account/create")
                .then()
                .statusCode(404)
                .contentType(ContentType.JSON)
                .body(new IsEqual<>("{\"description\":\"Экземпляр продукта с параметром instanceId 9999 не найден\"}")).assertThat();
    }

    @Test
    @DisplayName("Передаем в запросе несуществующий registryTypeCode")
    void testNotFoundWithTppProductRegister() {
        ProductAccountingRequest request = getNewProductAccountingRequest();
        request.setRegistryTypeCode("zzz");
        given()
                .contentType(ContentType.JSON)
                .when()
                .body(request)
                .post("corporate-settlement-account/create")
                .then()
                .statusCode(404)
                .contentType(ContentType.JSON)
                .body(new MatchesPattern(Pattern.compile(".+Код Продукта zzz не найден в каталоге продуктов.+"))).assertThat();
    }

    @Test
    @DisplayName("Проверяем успешное заведение tppProductRegister")
    void testValidCreateTppProductRegister() {
        ProductAccountingRequest request = getNewProductAccountingRequest();
        given()
                .contentType(ContentType.JSON)
                .when()
                .body(request)
                .post("corporate-settlement-account/create")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body(new MatchesPattern(Pattern.compile(".+\"accountId\":\"\\d+\".+"))).assertThat();
    }

    @Test
    @DisplayName("Тестируем валидацию поля accountType")
    void testBadRequestWithIncorrectAccountType() {
        ProductAccountingRequest request = getNewProductAccountingRequest();
        request.setAccountType("xxx");
        given()
                .contentType(ContentType.JSON)
                .when()
                .body(request)
                .post("corporate-settlement-account/create")
                .then()
                .statusCode(400)
                .contentType(ContentType.JSON)
                .body(new MatchesPattern(Pattern.compile(".+accountType: Клиентский или Внутрибанковский.+"))).assertThat();
    }

    @Test
    @DisplayName("Тестируем валидацию currencyCode")
    void testBadRequestIncorrectCurrencyCode() {
        ProductAccountingRequest request = getNewProductAccountingRequest();
        request.setCurrencyCode("xxxx");
        given()
                .contentType(ContentType.JSON)
                .when()
                .body(request)
                .post("corporate-settlement-account/create")
                .then()
                .statusCode(400)
                .contentType(ContentType.JSON)
                .body(new MatchesPattern(Pattern.compile(".+currencyCode - трехзначный код.+"))).assertThat();
    }

    @Test
    @DisplayName("Тестируем валидацию priorityCode")
    void testBadRequestEmptyPriorityCode() {
        ProductAccountingRequest request = getNewProductAccountingRequest();
        request.setPriorityCode(null);
        given()
                .contentType(ContentType.JSON)
                .when()
                .body(request)
                .post("corporate-settlement-account/create")
                .then()
                .statusCode(400)
                .contentType(ContentType.JSON)
                .body(new MatchesPattern(Pattern.compile(".+Имя обязательного атрибута priorityCode не заполнено.+"))).assertThat();
    }

    private ProductAccountingRequest getNewProductAccountingRequest() {
        return new ProductAccountingRequest(10L,
                "02.001.005_45343_CoDowFF",
                "Клиентский",
                "500",
                "0021",
                "00",
                "13",
                null,
                null,
                null,
                null);

    }

}
