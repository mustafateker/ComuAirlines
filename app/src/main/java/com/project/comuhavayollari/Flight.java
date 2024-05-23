package com.project.comuhavayollari;

public class Flight {
    private String id;
    private String fromCity;
    private String toCity;
    private String flightNumber;
    private String flightDate;
    private String flightTime;
    private String ticketPrice;

    public Flight() {
        // Default constructor required for calls to DataSnapshot.getValue(Flight.class)
    }

    public Flight(String id, String fromCity, String toCity, String flightNumber, String flightDate, String flightTime, String ticketPrice) {
        this.id = id;
        this.fromCity = fromCity;
        this.toCity = toCity;
        this.flightNumber = flightNumber;
        this.flightDate = flightDate;
        this.flightTime = flightTime;
        this.ticketPrice = ticketPrice;
    }

    // Getter ve Setter metodları
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFromCity() {
        return fromCity;
    }

    public void setFromCity(String fromCity) {
        this.fromCity = fromCity;
    }

    public String getToCity() {
        return toCity;
    }

    public void setToCity(String toCity) {
        this.toCity = toCity;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getFlightDate() {
        return flightDate;
    }

    public void setFlightDate(String flightDate) {
        this.flightDate = flightDate;
    }

    public String getFlightTime() {
        return flightTime;
    }

    public void setFlightTime(String flightTime) {
        this.flightTime = flightTime;
    }

    public String getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(String ticketPrice) {
        this.ticketPrice = ticketPrice;
    }
}
