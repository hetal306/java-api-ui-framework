package com.portfolio.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Booking(
        String firstname,
        String lastname,
        int totalprice,
        boolean depositpaid,
        @JsonProperty("bookingdates") BookingDates bookingdates,
        String additionalneeds) {
}
