package com.benevolo.entity;

public class TicketType {

    private String id;
    private int price;
    private int taxRate;
    private String name;

    public TicketType() {
    }

    public TicketType(String id, int price, int taxRate, String name) {
        this.id = id;
        this.price = price;
        this.taxRate = taxRate;
        this.name = name;
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
}
