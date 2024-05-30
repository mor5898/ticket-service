package com.benevolo;

import com.benevolo.utils.query_builder.QueryBuilder;
import com.benevolo.utils.query_builder.section.QuerySection;
import com.benevolo.utils.query_builder.util.Compartor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class QueryBuilderTest {

    @Test
    void testQueryBuilder() {
        QueryBuilder<Object> queryBuilder = QueryBuilder.build();
        queryBuilder.head("SELECT b FROM Booking AS b");
        queryBuilder.add(QuerySection.of("b.totalPrice", Compartor.EQUAlS, 2000));
        queryBuilder.add(QuerySection.of("b.bookedAt", Compartor.EQUAlS, null));

        Assertions.assertEquals("SELECT b FROM Booking AS b WHERE b.totalPrice = :" + queryBuilder.getParams().entrySet().iterator().next().getKey(), queryBuilder.buildQuery());
    }

}
