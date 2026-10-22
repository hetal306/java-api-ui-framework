package com.portfolio.api;

import com.portfolio.api.data.BookingFactory;
import com.portfolio.api.model.Booking;
import com.portfolio.api.model.CreatedBooking;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Feature("Booking flow")
public class BookingTest {

    private final List<Integer> createdIds = new CopyOnWriteArrayList<>();
    private BookingClient client;
    private String token;

    @BeforeClass
    public void setUp() {
        client = new BookingClient();
        token = client.getValidToken();
        Assert.assertNotNull(token, "could not get auth token");
    }

    @AfterClass(alwaysRun = true)
    public void cleanUp() {
        for (int id : createdIds) {
            client.deleteBooking(id, token);
        }
    }

    private int createBooking(Booking booking) {
        Response response = client.createBooking(booking);
        Assert.assertEquals(response.statusCode(), 200, "created failed: " + response.asString());
        int id = response.as(CreatedBooking.class).bookingid();
        createdIds.add(id);
        return id;
    }

    @DataProvider(name = "prices")
    public Object[][] prices() {
        return new Object[][] {{1}, {150}, {9999}};
    }

    @Test(dataProvider = "prices")
    public void fetchCreatedBooking(int price) {
        Booking booking = BookingFactory.withPrice(price);
        int id = createBooking(booking);

        Response fetched = client.getBooking(id);
        Assert.assertEquals(fetched.statusCode(), 200);
        Assert.assertEquals(fetched.as(Booking.class), booking);
    }

    @Test
    public void updateChangesTheBooking() {
        int id = createBooking(BookingFactory.defaultPriceBooking());
        Booking updated = BookingFactory.withPrice(777);

        Response response = client.updateBooking(id, updated, token);

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(client.getBooking(id).as(Booking.class), updated);
    }

    @Test
    public void deletedBookingIsGone() {
        int id = createBooking(BookingFactory.defaultPriceBooking());

        // Restful-Booker answers a successful delete with 201, not 200 or 204
        Assert.assertEquals(client.deleteBooking(id, token).statusCode(), 201);
        Assert.assertEquals(client.getBooking(id).statusCode(), 404);
    }

    @Test
    public void unknownBookingReturns404() {
        Assert.assertEquals(client.getBooking(999999999).statusCode(), 404);
    }

    @Test
    public void updateWithoutTokenIsForbidden() {
        int id = createBooking(BookingFactory.defaultPriceBooking());

        Response response = client.updateWithoutToken(id, BookingFactory.withPrice(5));

        Assert.assertEquals(response.statusCode(), 403);
    }

    @Test
    public void wrongCredentialsAreRejected() {
        Response response = client.createToken("admin", "wrong_password");

        Assert.assertEquals(response.jsonPath().getString("reason"), "Bad credentials");
        Assert.assertNull(response.jsonPath().getString("token"));
    }

    @Test
    public void bookingListIsNotEmpty() {
        Response response = client.getBookingIds();

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertFalse(response.jsonPath().getList("bookingid").isEmpty());
    }
}
