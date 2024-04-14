package study.stepup.productaccountingservice.controllers;

import static io.restassured.RestAssured.given;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.text.MatchesPattern;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.ext.ScriptUtils;
import org.testcontainers.jdbc.JdbcDatabaseDelegate;
import org.testcontainers.junit.jupiter.Container;
import study.stepup.productaccountingservice.models.Arrangement;
import study.stepup.productaccountingservice.models.ProductRequest;

import java.sql.Timestamp;
import java.util.regex.Pattern;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerTest {
    @LocalServerPort
    private Integer port;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:alpine3.18")
//            .withFileSystemBind("testData.sql", "/docker-entrypoint-initdb.d", BindMode.READ_ONLY)
//            .withCopyFileToContainer(MountableFile.forClasspathResource( "schema.sql"), "/docker-entrypoint-initdb.d/")
//            .withCopyFileToContainer(MountableFile.forClasspathResource( "testData.sql"), "/docker-entrypoint-initdb.d/")
            ;


    @BeforeAll
    static void beforeAll() {
        postgres.start();
        ScriptUtils.runInitScript(new JdbcDatabaseDelegate(postgres, ""), "db/schema.sql");
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }


    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        ScriptUtils.runInitScript(new JdbcDatabaseDelegate(postgres, ""), "db/testData.sql");
        System.out.println("WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW " + RestAssured.baseURI);
    }

    @AfterEach
    void  setDown(){
    }


    @Test
    @DisplayName("дублирование номера договора вернет ошибку 400")
    void isDoubleContractNumber_return400() {
        ProductRequest productRequest = getNewProductRequest();
        productRequest.setContractNumber("ContractNumber");
        given()
                .contentType(ContentType.JSON)
                .body(productRequest)
                .post("corporate-settlement-instance/create")
                .then()
                .statusCode(400).contentType(ContentType.JSON)
                .body(new MatchesPattern(Pattern.compile(".+" + productRequest.getContractNumber() + " уже существует.+"))).assertThat();
    }

    @Test
    @DisplayName("Отсутствующий код продукта вернет ошибку 404")
    void isNotFoundProductCode_return404() {
        ProductRequest request = getNewProductRequest();
        request.setProductCode("11.111.111");

        given()
                .contentType(ContentType.JSON)
                .when()
                .body(request)
                .post("corporate-settlement-instance/create")
                .then()
                .statusCode(404).contentType(ContentType.JSON)
                .body(new MatchesPattern(Pattern.compile(".+" + request.getProductCode() + " не найден.+"))).assertThat();
    }

    @Test
    @DisplayName("Некорректный branchCode вернет ошибку 404")
    void isNotFoundAccount_return404() {
        ProductRequest request = getNewProductRequest();
        request.setBranchCode("11");
        given()
                .contentType(ContentType.JSON)
                .when()
                .body(request)
                .post("corporate-settlement-instance/create")
                .then()
                .statusCode(404).contentType(ContentType.JSON)
                .body(new MatchesPattern(Pattern.compile(".+Счет не найден.+"))).assertThat();
    }

    @Test
    @DisplayName("Заведение ЭП вернет успех 200")
    void IsCreateProduct_return200() {
        ProductRequest request = getNewProductRequest();
        request.setContractNumber("2");
        given()
                .contentType(ContentType.JSON)
                .when()
                .body(request)
                .post("corporate-settlement-instance/create")
                .then()
                .statusCode(200).contentType(ContentType.JSON)
                .body(new MatchesPattern(Pattern.compile(".+instanceId.+"))).assertThat();
    }

    @Test
    @DisplayName("Некорректный productType вернет ошибку 404")
    void isIncorrectProductType_return400() {
        ProductRequest request = getNewProductRequest();
        request.setProductType("100");
        given()
                .contentType(ContentType.JSON)
                .when()
                .body(request)
                .post("corporate-settlement-instance/create")
                .then()
                .statusCode(400)
                .body(new MatchesPattern(Pattern.compile(".+допустимые productType: НСО, СМО, ЕЖО, ДБДС, договор.+"))).assertThat();
    }

    @Test
    @DisplayName("Некорректный rateType вернет ошибку 404")
    void isIncorrectRateType_return400() {
        ProductRequest request = getNewProductRequest();
        request.setRateType("100");
        given()
                .contentType(ContentType.JSON)
                .when()
                .body(request)
                .post("corporate-settlement-instance/create")
                .then()
                .statusCode(400)
                .body(new MatchesPattern(Pattern.compile(".+допустимые значения rateType: дифференцированная, прогрессивная.+"))).assertThat();
    }


    @Test
    @DisplayName("Заведение ДС вернет успех 200")
    void testValidCreateAgreement() {
        ProductRequest request = getNewProductRequest();
        request.setInstanceId(10L);
        Arrangement arrangement = getNewArrangement();
        request.setInstanceArrangement(new Arrangement[]{arrangement});

        given()
                .contentType(ContentType.JSON)
                .when()
                .body(request)
                .post("corporate-settlement-instance/create")
                .then()
                .contentType(ContentType.JSON)
                .body(new MatchesPattern(Pattern.compile("^.+instanceId.+$")))
                .statusCode(200)
                .assertThat();
    }

    @Test
    @DisplayName("Некорректные arrangementType и number вернут ошибку 400")
    void testBadRequestIncorrectArrangementTypeAndNumber() {
        ProductRequest request = getNewProductRequest();
        request.setInstanceId(10L);
        Arrangement arrangement = getNewArrangement();
        arrangement.setArrangementType("НСО3");
        arrangement.setNumber("");
        request.setInstanceArrangement(new Arrangement[]{arrangement});

        given()
                .contentType(ContentType.JSON)
                .when()
                .body(request)
                .post("corporate-settlement-instance/create")
                .then()
                .statusCode(400)
                .contentType(ContentType.JSON)
                .body(new MatchesPattern(Pattern.compile(".+допустимые arrangementType: НСО,ЕЖО,СМО,ДБДС.+" +
                        "|.+Обязательный параметр number не заполнен.+"))).assertThat();
    }

    @Test
    @DisplayName("Некорректная openingDate вернет ошибку 400")
    void isIncorrectOpeningDate_return400() {
        ProductRequest request = getNewProductRequest();
        request.setInstanceId(10L);
        Arrangement arrangement = getNewArrangement();
        arrangement.setOpeningDate(null);
        request.setInstanceArrangement(new Arrangement[]{arrangement});
        given()
                .contentType(ContentType.JSON)
                .when()
                .body(request)
                .post("corporate-settlement-instance/create")
                .then()
                .statusCode(400)
                .contentType(ContentType.JSON)
                .body(new MatchesPattern(Pattern.compile(".+Обязательный параметр openingDate не заполнен.+"))).assertThat();
    }


    private ProductRequest getNewProductRequest() {
        return new ProductRequest(
                null,
                "НСО",
                "03.012.002",
                "03.012.002_47533_ComSoLd",
                "15",
                "1",
                Timestamp.valueOf("2024-04-10 00:00:00"),
                1L,
                null,
                null,
                null,
                null,
                "дифференцированная",
                null,
                null,
                123,
                "0022",
                "800",
                "00",
                null,
                null,
                null);
    }

    private Arrangement getNewArrangement(){
        Arrangement arrangement = new Arrangement();
        arrangement.setArrangementType("НСО");
        arrangement.setNumber("123");
        arrangement.setOpeningDate(Timestamp.valueOf("2024-04-10 00:00:00"));
        return arrangement;
    }

}
