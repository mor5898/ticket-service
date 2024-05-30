package com.benevolo.utils.query_builder.section.order_by;

public class OrderBySection {

    private final String attribute;
    private final OrderType type;

    private OrderBySection(String attribute, OrderType type) {
        this.attribute = attribute;
        this.type = type;
    }

    public static OrderBySection of(String attribute, OrderType type) {
        return new OrderBySection(attribute, type);
    }

    public String toQuery() {
        return " ORDER BY " + attribute + " " + type.toString();
    }

}
