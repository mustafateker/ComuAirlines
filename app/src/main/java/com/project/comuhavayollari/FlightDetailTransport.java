package com.project.comuhavayollari;

import java.io.Serializable;

public class FlightDetailTransport implements Serializable {

    private String fromCity;
    private String toCity;
    private String flightDate;
    private String flightNumber;
    private String flightTime;
    private String id;
    private String ticketPrice;
    private String seatNumber;
    private String memberType;
    private boolean ticketType;
    private String ticketNumber;
    private String purschaedDate;

    public FlightDetailTransport(String fromCity, String toCity , String flightDate , String flightNumber , String flightTime,
                                 String id , String ticketPrice , String seatNumber , String memberType , boolean ticketType , String ticketNumber , String purschaedDate){
        this.fromCity = fromCity;
        this.toCity = toCity;
        this.flightDate = flightDate;
        this.flightNumber = flightNumber;
        this.flightTime = flightTime;
        this.id = id;
        this.ticketPrice = ticketPrice;
        this.seatNumber = seatNumber;
        this.memberType = memberType;
        this.ticketType = ticketType;
        this.ticketNumber = ticketNumber;
        this.purschaedDate = purschaedDate;
    }


    public FlightDetailTransport() {
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

    public String getFlightDate() {
        return flightDate;
    }

    public void setFlightDate(String flightDate) {
        this.flightDate = flightDate;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getFlightTime() {
        return flightTime;
    }

    public void setFlightTime(String flightTime) {
        this.flightTime = flightTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(String ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }


    public Boolean getTicketType() {
        return ticketType;
    }

    public void setTicketType(Boolean ticketType) {
        this.ticketType = ticketType;
    }


    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public String getPurschaedDate() {
        return purschaedDate;
    }

    public void setPurschaedDate(String purschaedDate) {
        this.purschaedDate = purschaedDate;
    }
}
