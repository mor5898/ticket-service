package com.benevolo.utils.query_builder;

import com.benevolo.utils.query_builder.section.QueryCustomSection;
import com.benevolo.utils.query_builder.section.QueryElement;
import com.benevolo.utils.query_builder.section.QuerySection;
import com.benevolo.utils.query_builder.section.order_by.OrderBySection;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import java.util.*;

public class QueryBuilder<E> {

    private String head;
    private final List<QueryElement> querySections;
    private final Map<String, Object> params;
    private OrderBySection orderBy;

    public static <E> QueryBuilder<E> build() {
        return new QueryBuilder<E>();
    }

    public QueryBuilder<E> head(String head) {
        this.head = head;
        return this;
    }

    public QueryBuilder<E> orderBy(OrderBySection orderBy) {
        this.orderBy = orderBy;
        return this;
    }

    private QueryBuilder() {
        querySections = new LinkedList<>();
        params = new HashMap<>();
    }

    public QueryBuilder<E> add(QueryCustomSection queryCustomSection) {
        querySections.add(queryCustomSection);
        for(Map.Entry<String, Object> entry : queryCustomSection.getParams().entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if(!params.containsKey(key)) {
                params.put(key, value);
            } else {
                throw new RuntimeException("Parameter with name " + key + " already in use.");
            }
        }
        return this;
    }

    public QueryBuilder<E> add(QuerySection querySection) {
        Object queryValue = querySection.getQueryValue();
        if(queryValue != null) {
            querySections.add(querySection);
            params.put(querySection.getQueryParam(), queryValue);
        }
        return this;
    }

    public PanacheQuery<E> find(PanacheRepositoryBase<E, ?> repository) {
        return repository.find(buildQuery(), params);
    }

    public Long count(PanacheRepositoryBase<E, ?> repository) {
        return repository.count(buildQuery(), params);
    }

    public String buildQuery() {
        Iterator<QueryElement> iterator = querySections.iterator();
        StringBuilder query = new StringBuilder();
        if(head != null) {
            query.append(head);
            if(iterator.hasNext()) {
                query.append(" WHERE ");
            }
        }
        while(iterator.hasNext()) {
            query.append(iterator.next().toQuery());
            if(iterator.hasNext()) {
               query.append(" AND ");
            }
        }
        if(orderBy != null) {
            query.append(orderBy.toQuery());
        }
        return query.toString();
    }

    public Map<String, Object> getParams() {
        return params;
    }
}
