package edu.victot.todo_list_sb.controller;

import edu.victot.todo_list_sb.TodoListSbApplication;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = TodoListSbApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TaskControllerTest {
//tentando entender pq alguns testes passam quando rodo ele sozinho, mas não quando rodo bloco
    @LocalServerPort
    private int port;

    @BeforeAll
    public void beforeAll(TestInfo testInfo) {
        RestAssured.port = port;
//        if(!testInfo.getDisplayName().equals("Teste de lista vazia"))
//            return;

        String body =
                """
                {
                  "title": "Title",
                  "description": "this is an short description of the task",
                  "dueDate": "2024-01-06",
                  "dueTime": "12:12:50",
                  "status": "1"
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/tasks")
                .then()
                .statusCode(201);
    }

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

//    @Test
//    @DisplayName("Teste de lista vazia")
//    public void retorna_statusCode200_listIsEmpty(){
//        Response response = given()
//                .contentType(ContentType.JSON)
//                .when()
//                .get("/tasks");
//        String responseBody = response.then()
//                .statusCode(200)
//                .extract().body().asString();
//
//        assertEquals("List is empty", responseBody);
//    }

    @Test
    @DisplayName("Criando uma task status code: 201")
    public void deve_criar_uma_task_pelo_POST_e_retornar_stausCode201(){

        String body =
                """
                {
                  "title": "Title",
                  "description": "this is an short description of the task",
                  "dueDate": "2025-01-06",
                  "dueTime": "12:12:50",
                  "status": "1"
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/tasks")
                .then()
                .statusCode(201);
    }

    @Test
    @DisplayName("Falha ao criar status code: 409 -> You are busy this time")
    public void nao_deve_criar_um_task_e_retornar_statusCode409_falando_que_esta_ocupado(){
        String body =
                """
                {
                  "title": "Title",
                  "description": "this is an short description of the task",
                  "dueDate": "2024-01-06",
                  "dueTime": "12:12:50",
                  "status": "1"
                }
                """;

        Response response = given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/tasks");

        String responseBody = response.then()
                 .statusCode(409)
                .extract()
                .body().asString();

        assertEquals("You are busy this time", responseBody);

    }

    @Test
    @DisplayName("Lista com as tasks, status code 200")
    public void retorna_uma_lista_com_as_tasks_statusCode200(){
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/tasks")
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("Retorna task pelo id status code: 200")
    public void retorna_uma_task_pelo_id_1_statusCode200(){
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/tasks/1")
                .then().statusCode(200)
                .body("id", equalTo(1))
                .body("title", equalTo("Title"));
    }

    @Test
    @DisplayName("Id não encontrado status code: 200 mas não encontra a task Task not found")
    public void nao_retorna_uma_task_pelo_id_100_statusCode200_body_tasknotfound(){
        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/tasks/100");

        String responseBody = response.then().statusCode(200)
                .extract().body().asString();

        assertEquals("Task not found", responseBody);
    }

    @Test
    @DisplayName("Lista de tasks pelo dueDate status code:200")
    public void retorna_list_of_task_pela_dueDate_statusCode200(){

    given()
            .contentType(ContentType.JSON)
            .when()
            .get("/tasks/get-by-duedate/2024-01-06")
            .then().statusCode(200)
            .body("title[0]", equalTo("Title"))
            .body("description[0]", equalTo("this is an short description of the task"))
            .body("dueDate[0]", equalTo("2024-01-06"));
    }

    @Test
    @DisplayName("Não encontra task na data mencionada, status code: 200 -> List is empty")
    public void nao_retorna_task_pela_dueDate_ListIsEmpty_statusCode200(){

        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/tasks/get-by-duedate/2025-01-06");

        String responseBody = response.then().statusCode(200)
                .extract().body().asString();

        assertEquals("List is empty", responseBody);
    }

    @Test
    @DisplayName("Update, status code: 201")
    public void faz_o_update_status_code201(){

        String body =
                """
                {
                  "title": "Title novo",
                  "description": "this is an new short description of the task",
                  "dueDate": "2025-01-06",
                  "dueTime": "12:30:50",
                  "status": "2"
                }
                """;
        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .put("/tasks/1")
                .then()
                .statusCode(201)
                .body("id", equalTo(1))
                .body("title", equalTo("Title novo"))
                .body("description", equalTo("this is an new short description of the task"))
                .body("dueDate", equalTo("2025-01-06"))
                .body("dueTime", equalTo("12:30:50"))
                .body("status", equalTo("CONCLUIDA"));
    }

    @Test
    @DisplayName("Update de id nao encontrado, status code: 200 -> Task not found")
    public void nao_faz_o_update_status_code200_taskNotFound() {

        String body =
                """
                        {
                          "title": "Title novo",
                          "description": "this is an new short description of the task",
                          "dueDate": "2025-01-06",
                          "dueTime": "12:30:50",
                          "status": "2"
                        }
                        """;
        String responseBody = given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .put("/tasks/100")
                .then()
                .statusCode(200).extract().body().asString();

        assertEquals("Id not found", responseBody);
    }

    @Test
    @DisplayName("Delete bem sucedido -> status code: 204")
    public void retorna_statusCode204_deletando_a_tarefa(){
        given()
                .when()
                .delete("/tasks/1")
                .then().statusCode(204);
    }

    @Test
    @DisplayName("Delete, id não encontrado -> status code: 200 -> Id not found")
    public void retorna_statusCode200_taskNotFound_apos_tentar_deletar_um_id_nao_existente(){
        String responseBody = given()
                .when()
                .delete("/tasks/100")
                .then().statusCode(200)
                .extract().body().asString();

        assertEquals("Id not found", responseBody);
    }


}
