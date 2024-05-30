package com.benevolo.utils.query_builder.section;


import com.benevolo.utils.query_builder.util.Compartor;

import static com.benevolo.utils.query_builder.util.Generator.generate;

public class QuerySection implements QueryElement {

    private String queryAttribute;
    private String queryParam;
    private Compartor comparator;
    private Object queryValue;

    public static QuerySection of(String queryAttribute, Compartor comparator, Object queryValue) {
        return new QuerySection(queryAttribute, comparator, queryValue);
    }

    private QuerySection(String queryAttribute, Compartor comparator, Object queryValue) {
        this.queryAttribute = queryAttribute;
        this.queryParam = generate();
        this.comparator = comparator;
        this.queryValue = queryValue;
    }

    @Override
    public String toQuery() {
        return queryAttribute + " " + convertComparator(comparator) + " :" + queryParam;
    }

    private String convertComparator(Compartor comparator) {
        switch(comparator) {
            case EQUAlS -> {
                return "=";
            }
            case UNEQUAL -> {
                return "!=";
            }
            case GREATER_THAN -> {
                return ">";
            }
            case LESS_THAN -> {
                return "<";
            }
            case GREATER_THAN_OR_EQUALS -> {
                return ">=";
            }
            case LESS_THAN_OR_EQUALS -> {
                return "<=";
            }
        }
        throw new RuntimeException("Invalid comparator " + comparator.toString() + " not supported.");
    }

    public String getQueryAttribute() {
        return queryAttribute;
    }

    public void setQueryAttribute(String queryAttribute) {
        this.queryAttribute = queryAttribute;
    }

    public String getQueryParam() {
        return queryParam;
    }

    public void setQueryParam(String queryParam) {
        this.queryParam = queryParam;
    }

    public Compartor getComparator() {
        return comparator;
    }

    public void setComparator(Compartor comparator) {
        this.comparator = comparator;
    }

    public Object getQueryValue() {
        return queryValue;
    }

    public void setQueryValue(Object queryValue) {
        this.queryValue = queryValue;
    }
}
