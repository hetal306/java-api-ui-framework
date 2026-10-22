package com.portfolio.api.data;

import com.portfolio.api.model.Booking;
import com.portfolio.api.model.BookingDates;

import java.util.UUID;

public final class BookingFactory {
    private BookingFactory() {

    }

    public static Booking withPrice(int price) {
        String suffix = UUID.randomUUID().toString().substring(0, 3);
        return new Booking(
                "Test" + suffix,
                "User",
                price,
                true,
                new BookingDates("2026-11-01", "2026-11-05"),
                "Breakfast");
    }

    public static Booking defaultPriceBooking() {
        return withPrice(150);
    }
}
