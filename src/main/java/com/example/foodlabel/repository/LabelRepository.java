package com.example.foodlabel.repository;

import com.example.foodlabel.model.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabelRepository extends JpaRepository<Label, Long> {
    @Query("SELECT l.id, l.name, COUNT(f) FROM Label l LEFT JOIN l.foods f GROUP BY l.id, l.name")
    List<Object[]> findAllLabelsWithFoodCount();

    Label findByName(String name);

    List<Label> findByNameIn(List<String> names);
}
