package com.example.foodlabel.graphql;

import com.example.foodlabel.dto.FoodDto;
import com.example.foodlabel.dto.LabelDto;
import com.example.foodlabel.mapper.FoodLabelMapper;
import com.example.foodlabel.repository.LabelRepository;
import com.example.foodlabel.service.FoodLabelService;
import graphql.GraphQLException;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class FoodQueryController {

    private final FoodLabelService foodLabelService;
    private final LabelRepository labelRepository;
    private final FoodLabelMapper mapper;

    @QueryMapping
    public List<LabelDto> allLabelsWithCount() {
        try {
            return labelRepository.findAllLabelsWithFoodCount()
                    .stream()
                    .map(row -> new LabelDto(
                            ((Long) row[0]),      // id
                            (String) row[1],      // name
                            ((Long) row[2]).intValue()  // count
                    ))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new GraphQLException("Failed to fetch label counts.");
        }
    }

    @QueryMapping
    public List<FoodDto> foodsByLabels(@Argument List<String> labels,
                                       @Argument int page,
                                       @Argument int size) {
        try {
            if (labels != null && labels.stream().anyMatch(name -> name.length() > 20)) {
                throw new GraphQLException("Label names must not exceed 20 characters.");
            }
            return foodLabelService.getFoodsByLabels(labels, page, size);
        } catch (IllegalArgumentException | ConstraintViolationException e) {
            throw new GraphQLException("Invalid label filter or pagination input.");
        } catch (Exception e) {
            throw new GraphQLException("Unexpected error while fetching foods.");
        }
    }
}
