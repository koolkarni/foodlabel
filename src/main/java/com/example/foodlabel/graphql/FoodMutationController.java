package com.example.foodlabel.graphql;

import com.example.foodlabel.dto.FoodDto;
import com.example.foodlabel.dto.LabelDto;
import com.example.foodlabel.model.Label;
import com.example.foodlabel.service.FoodLabelService;
import com.example.foodlabel.repository.LabelRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class FoodMutationController {

    private final FoodLabelService foodLabelService;
    private final LabelRepository labelRepository;

    @MutationMapping
    public LabelDto createLabel(@Argument LabelInput input) {
        Label label = Label.builder()
                .name(input.getName())
                .build();
        return new LabelDto(labelRepository.save(label).getId(), label.getName(), 0);
    }

    @MutationMapping
    public FoodDto createFood(@Argument FoodInput input) {
        return foodLabelService.createFood(
                input.getName(),
                input.getDescription(),
                input.getLabelNames()
        );
    }

    @MutationMapping
    public LabelDto updateLabel(@Argument Long id, @Argument String name) {
        Label updated = foodLabelService.updateLabel(id, name);
        return new LabelDto(updated.getId(), updated.getName(), updated.getFoods().size());
    }
    @MutationMapping
    public LabelDto updateLabelByName(@Argument String currentName, @Argument String newName) {
        Label updated = foodLabelService.updateLabelByName(currentName, newName);
        return new LabelDto(updated.getId(), updated.getName(), updated.getFoods().size());
    }
    @MutationMapping
    public FoodDto updateFood(@Argument Long id, @Argument FoodInput input) {
        return foodLabelService.updateFood(id, input.getName(), input.getDescription(), input.getLabelNames());
    }

    @MutationMapping
    public Boolean deleteLabel(@Argument Long id) {
        foodLabelService.deleteLabel(id);
        return true;
    }

    @MutationMapping
    public Boolean deleteFood(@Argument Long id) {
        foodLabelService.deleteFood(id);
        return true;
    }

    @Data
    public static class LabelInput {
        private String name;
    }

    @Data
    public static class FoodInput {
        private String name;
        private String description;
        private List<String> labelNames;
    }
}