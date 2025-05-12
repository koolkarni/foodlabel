package com.example.foodlabel.repository;

import com.example.foodlabel.model.Food;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FoodRepositoryCustom {
    Page<Food> findFoodsByAllLabelNames(List<String> labelNames, Pageable pageable);
}
