package com.ptithcm.shared.base;

import com.ptithcm.shared.dto.FindOptions;
import java.util.Map;
import org.hibernate.query.Query;

public class HqlQueryBuilder<T> {

    private final Class<T> entityClass;

    public HqlQueryBuilder(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public StringBuilder buildHql(String selectClause, Map<String, Object> filter, FindOptions options) {
        StringBuilder hql = new StringBuilder(selectClause).append(" FROM ").append(entityClass.getName())
                .append(" e ");

        // 1. Xử lý WHERE (Filter)
        if (filter != null && !filter.isEmpty()) {
            hql.append(" WHERE ");
            int i = 0;
            for (String key : filter.keySet()) {
                if (i > 0) {
                    hql.append(" AND ");
                }
                hql.append("e.").append(key).append(" = :").append(key);
                i++;
            }
        }

        // 2. Xử lý ORDER BY (Options)
        if (options != null && options.getOrder() != null && !options.getOrder().isEmpty()) {
            hql.append(" ORDER BY ");
            int i = 0;
            for (Map.Entry<String, String> entry : options.getOrder().entrySet()) {
                if (i > 0) {
                    hql.append(", ");
                }
                hql.append("e.").append(entry.getKey()).append(" ").append(entry.getValue());
                i++;
            }
        }
        return hql;
    }

    public void bindParameters(Query<?> query, Map<String, Object> filter) {
        if (filter != null) {
            for (Map.Entry<String, Object> entry : filter.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
        }
    }
}
