package com.benevolo.entity;

public class Event {

    private String id;
    private String eventName;
    private Address address;

    public Event() {
    }

    public Event(String eventId) {
        this.id = eventId;
    }

    public Event(String eventId, String eventName, Address address) {
        this.id = eventId;
        this.eventName = eventName;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

}