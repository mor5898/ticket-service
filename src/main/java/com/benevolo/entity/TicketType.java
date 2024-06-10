package com.benevolo.entity;

import java.time.LocalDateTime;

public class TicketType {

    private String id;
    private int price;
    private int taxRate;
    private String name;
    private LocalDateTime validFrom;
    private LocalDateTime validTo;
    private Boolean entryStarted;
    private Event event;

    public TicketType() {
    }

    public TicketType(String id, int price, int taxRate, String name, LocalDateTime validFrom, LocalDateTime validTo, Boolean entryStarted, Event event) {
        this.id = id;
        this.price = price;
        this.taxRate = taxRate;
        this.name = name;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.entryStarted = entryStarted;
        this.event = event;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(int taxRate) {
        this.taxRate = taxRate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValidFrom(LocalDateTime validFrom) {
        this.validFrom = validFrom;
    }

    public void setValidTo(LocalDateTime validTo) {
        this.validTo = validTo;
    }

    public LocalDateTime getValidFrom() {
        return validFrom;
    }

    public LocalDateTime getValidTo() {
        return validTo;
    }

    public Boolean getEntryStarted() {
        return entryStarted;
    }

    public void setEntryStarted(Boolean entryStarted) {
        this.entryStarted = entryStarted;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
