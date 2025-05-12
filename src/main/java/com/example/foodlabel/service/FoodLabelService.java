package com.example.foodlabel.service;

import com.example.foodlabel.dto.FoodDto;
import com.example.foodlabel.mapper.FoodLabelMapper;
import com.example.foodlabel.model.Food;
import com.example.foodlabel.model.Label;
import com.example.foodlabel.repository.FoodRepository;
import com.example.foodlabel.repository.LabelRepository;
import graphql.GraphQLException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FoodLabelService {

    private final FoodRepository foodRepository;
    private final LabelRepository labelRepository;
    private final FoodLabelMapper mapper;

    @Transactional
    public FoodDto createFood(String name, String description, List<String> labelNames) {
        Set<Label> labels = Set.of();

        if (labelNames != null && !labelNames.isEmpty()) {
            List<Label> foundLabels = labelRepository.findByNameIn(labelNames);

            if (foundLabels.size() != labelNames.size()) {
                throw new GraphQLException("One or more labels do not exist. Please add them first.");
            }
            labels = Set.copyOf(foundLabels);
        }

        Food food = Food.builder()
                .name(name)
                .description(description)
                .labels(labels)
                .build();

        return mapper.toFoodDto(foodRepository.save(food));
    }

    @Transactional(readOnly = true)
    public List<FoodDto> getFoodsByLabels(List<String> labels, int page, int size) {
        List<Food> foods = foodRepository.findFoodsByAllLabelNames(labels, PageRequest.of(page, size)).getContent();
        return mapper.toFoodDtoList(foods);
    }

    @Transactional
    public Label updateLabel(Long id, String name) {
        Label label = labelRepository.findById(id).orElseThrow(() -> new GraphQLException("Label not found"));
        label.setName(name);
        return labelRepository.save(label);
    }

    @Transactional
    public Label updateLabelByName(String currentName, String newName) {
        Label label = labelRepository.findByName(currentName);
        if (label == null) {
            throw new EntityNotFoundException("Label with name " + currentName + " not found");
        }
        label.setName(newName);
        return labelRepository.save(label);  // or just return label; (@Transactional will auto-save)
    }

    @Transactional
    public FoodDto updateFood(Long id, String name, String description, List<String> labelNames) {
        Food food = foodRepository.findById(id).orElseThrow(() -> new GraphQLException("Food not found"));

        if (labelNames != null && !labelNames.isEmpty()) {
            List<Label> labels = labelRepository.findByNameIn(labelNames);
            if (labels.size() != labelNames.size()) {
                throw new GraphQLException("One or more labels do not exist. Please add them first.");
            }
            food.setLabels(new HashSet<>(labels));
        }

        food.setName(name);
        food.setDescription(description);

        return mapper.toFoodDto(foodRepository.save(food));
    }

    @Transactional
    public void deleteLabel(Long id) {
        if (!labelRepository.existsById(id)) {
            throw new GraphQLException("Label not found");
        }
        labelRepository.deleteById(id);
    }

    @Transactional
    public void deleteFood(Long id) {
        if (!foodRepository.existsById(id)) {
            throw new GraphQLException("Food not found");
        }
        foodRepository.deleteById(id);
    }


}
