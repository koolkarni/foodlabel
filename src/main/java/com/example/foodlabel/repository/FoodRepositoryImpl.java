package com.example.foodlabel.repository;

import com.example.foodlabel.model.Food;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FoodRepositoryImpl implements FoodRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Food> findFoodsByAllLabelNames(List<String> labelNames, Pageable pageable) {
        String baseQuery = """
                SELECT f FROM Food f
                JOIN f.labels l
                WHERE l.name IN :labelNames
                GROUP BY f
                HAVING COUNT(DISTINCT l.name) = :size
                """;

        String countQuery = """
                SELECT COUNT(*) FROM (
                    SELECT f.id FROM Food f
                    JOIN f.labels l
                    WHERE l.name IN :labelNames
                    GROUP BY f
                    HAVING COUNT(DISTINCT l.name) = :size
                )
                """;

        TypedQuery<Food> query = entityManager.createQuery(baseQuery, Food.class);
        query.setParameter("labelNames", labelNames);
        query.setParameter("size", (long) labelNames.size());

        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<Food> result = query.getResultList();

        // Optional: count approximation (for performance you can skip exact count)
        long total = result.size() < pageable.getPageSize() ? pageable.getOffset() + result.size() : pageable.getOffset() + pageable.getPageSize();

        return new PageImpl<>(result, pageable, total);
    }
}
