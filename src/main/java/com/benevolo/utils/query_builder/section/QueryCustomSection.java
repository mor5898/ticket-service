package com.benevolo.utils.query_builder.section;

import java.util.HashMap;
import java.util.Map;

public class QueryCustomSection implements QueryElement {

    private final String query;
    private final Map<String, Object> params;

    private QueryCustomSection(String query, Map<String, Object> params) {
        this.query = query;
        this.params = params;
    }

    public static QueryCustomSection of(String query) {
        return new QueryCustomSection(query, new HashMap<>());
    }

    public static QueryCustomSection of(String query, Map<String, Object> params) {
        return new QueryCustomSection(query, params);
    }

    @Override
    public String toQuery() {
        return "(" + query + ")";
    }

    public Map<String, Object> getParams() {
        return params;
    }
}
