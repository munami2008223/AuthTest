package ru.netology.service.data;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import com.github.javafaker.Faker;

import java.util.Locale;

import static io.restassured.RestAssured.given;

import io.restassured.specification.RequestSpecification;
import lombok.Value;


public class DataGenerator {
    private static final Faker FAKER = new Faker(new Locale("en"));
    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    private DataGenerator() {
    }

    static void sendRequest(DataGenerator.RegistrationDto user) {
        given()
                .spec(requestSpec)
                .body(user)
                .when().log().all()
                .post("/api/system/users")
                .then().log().all()
                .statusCode(200);

    }

    public static String getRandomLogin() {
        return FAKER.name().username();
    }

    public static String getRandomPassword() {
        return FAKER.internet().password();
    }

    public static class Registration {
        private Registration() {
        }

        public static RegistrationDto getUser(String status) {
            return new RegistrationDto(getRandomLogin(), getRandomPassword(), status);
        }

        public static RegistrationDto getRegisteredUser(String status) {
            var user = getUser(status);
            sendRequest(user);
            return user;
        }

    }

    @Value
    public static class RegistrationDto {
        String login;
        String password;
        String status;
    }


}


