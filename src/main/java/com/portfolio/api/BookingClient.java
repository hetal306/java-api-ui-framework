package com.portfolio.api;

import com.portfolio.api.model.Booking;
import com.portfolio.config.ConfigReader;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class BookingClient {
    private final RequestSpecification spec;

    public BookingClient() {
        spec = new RequestSpecBuilder()
                .setBaseUri(ConfigReader.get("api.base.url"))
                .setContentType(ContentType.JSON)
                .addHeader("Accept", "application/json")
                .setConfig(RestAssuredConfig.config().httpClient(HttpClientConfig.httpClientConfig()
                        .setParam("http.connection.timeout", 10000)
                        .setParam("http.socket.timeout", 15000)))
                .addFilter(new AllureRestAssured())
                .build();
    }

    public Response createToken(String username, String password) {
        return given().spec(spec)
                .body(Map.of("username", username, "password", password))
                .post("/auth");
    }

    public String getValidToken() {
        return createToken(ConfigReader.get("api.username"), ConfigReader.get("api.password"))
                .jsonPath().getString("token");
    }

    public Response getBookingIds() {
        return given().spec(spec).get("/booking");
    }

    public Response getBooking(int id) {
        return given().spec(spec).get("/booking/{id}", id);
    }

    public Response createBooking(Booking booking) {
        return given().spec(spec).body(booking).post("/booking");
    }

    public Response updateBooking(int id, Booking booking, String token) {
        return given().spec(spec)
                .cookie("token", token)
                .body(booking)
                .put("/booking/{id}", id);
    }

    public Response updateWithoutToken(int id, Booking booking) {
        return given().spec(spec).body(booking).put("/booking/{id}", id);
    }

    public Response deleteBooking(int id, String token) {
        return given().spec(spec).cookie("token", token).delete("/booking/{id}", id);
    }
}
