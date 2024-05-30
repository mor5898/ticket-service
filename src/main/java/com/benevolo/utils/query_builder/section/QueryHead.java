package com.benevolo.utils.query_builder.section;

import java.util.HashMap;
import java.util.Map;

public class QueryHead {

    private Map<String, String> mainEntity;
    private Map<String, String> additionalEntities;

    public QueryHead(Map<String, String> mainEntity) {
        setMainEntity(mainEntity);
        this.additionalEntities = new HashMap<>();
    }

    public QueryHead(Map<String, String> mainEntity, Map<String, String> additionalEntities) {
        setMainEntity(mainEntity);
        this.additionalEntities = additionalEntities;
    }

    public void setAdditionalEntities(Map<String, String> additionalEntities) {
        this.additionalEntities = additionalEntities;
    }

    public Map<String, String> getMainEntity() {
        return mainEntity;
    }

    public Map<String, String> getAdditionalEntities() {
        return additionalEntities;
    }

    public void setMainEntity(Map<String, String> mainEntity) {
        if(mainEntity.entrySet().size() == 1) {
            this.mainEntity = mainEntity;
            return;
        }
        throw new RuntimeException("There can only be exactly one main entity.");
    }

    public String toQuery() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT ");
        Map.Entry<String, String> main = mainEntity.entrySet().iterator().next();
        stringBuilder.append(main.getKey()).append(" AS ").append(main.getValue());

        return stringBuilder.toString();
    }

}
