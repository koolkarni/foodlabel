package com.example.foodlabel.service;


import com.example.foodlabel.dto.FoodDto;
import com.example.foodlabel.dto.LabelDto;
import com.example.foodlabel.mapper.FoodLabelMapper;
import com.example.foodlabel.model.Food;
import com.example.foodlabel.model.Label;
import com.example.foodlabel.repository.FoodRepository;
import com.example.foodlabel.repository.LabelRepository;
import graphql.GraphQLException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FoodLabelServiceTest {

    @Mock
    private FoodRepository foodRepository;

    @Mock
    private LabelRepository labelRepository;

    @InjectMocks
    private FoodLabelService foodLabelService;
    @Mock
    private FoodLabelMapper mapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpdateFood_Success() {
        Long foodId = 1L;
        String name = "Updated Food";
        String desc = "Updated Description";
        List<String> labelNames = Arrays.asList("Label1", "Label2");

        Food food = new Food();
        food.setId(foodId);
        food.setLabels(new HashSet<>());

        List<Label> labels = Arrays.asList(
                new Label(1L, "Label1", null, new HashSet<>()),
                new Label(2L, "Label2", null, new HashSet<>())
        );

        when(foodRepository.findById(foodId)).thenReturn(Optional.of(food));
        when(labelRepository.findByNameIn(labelNames)).thenReturn(labels);
        when(foodRepository.save(any(Food.class))).thenReturn(food);
        when(mapper.toFoodDto(any(Food.class))).thenReturn(
                new FoodDto(foodId, name, desc, Set.of())
        );
        FoodDto result = foodLabelService.updateFood(foodId, name, desc, labelNames);

        assertEquals(name, result.getName());
        assertEquals(desc, result.getDescription());
        verify(foodRepository).save(food);
    }

    @Test
    void testUpdateFood_LabelMissing_ThrowsException() {
        Long foodId = 1L;
        List<String> labelNames = Arrays.asList("Label1", "Label2");

        Food food = new Food();
        food.setId(foodId);

        when(foodRepository.findById(foodId)).thenReturn(Optional.of(food));
        when(labelRepository.findByNameIn(labelNames)).thenReturn(List.of(new Label(1L, "Label1", null, new HashSet<>())));

        assertThrows(GraphQLException.class, () ->
                foodLabelService.updateFood(foodId, "Food", "Desc", labelNames)
        );
    }

    @Test
    void testUpdateFood_FoodNotFound_ThrowsException() {
        when(foodRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(GraphQLException.class, () ->
                foodLabelService.updateFood(99L, "Food", "Desc", List.of("Label"))
        );
    }

    @Test
    void testDeleteLabel_Success() {
        when(labelRepository.existsById(1L)).thenReturn(true);
        doNothing().when(labelRepository).deleteById(1L);

        assertDoesNotThrow(() -> foodLabelService.deleteLabel(1L));
        verify(labelRepository).deleteById(1L);
    }

    @Test
    void testDeleteLabel_NotFound_ThrowsException() {
        when(labelRepository.existsById(99L)).thenReturn(false);

        GraphQLException ex = assertThrows(GraphQLException.class, () -> foodLabelService.deleteLabel(99L));
        assertEquals("Label not found", ex.getMessage());
    }

    @Test
    void testDeleteFood_Success() {
        when(foodRepository.existsById(1L)).thenReturn(true);
        doNothing().when(foodRepository).deleteById(1L);

        assertDoesNotThrow(() -> foodLabelService.deleteFood(1L));
        verify(foodRepository).deleteById(1L);
    }

    @Test
    void testDeleteFood_NotFound_ThrowsException() {
        when(foodRepository.existsById(42L)).thenReturn(false);

        GraphQLException ex = assertThrows(GraphQLException.class, () -> foodLabelService.deleteFood(42L));
        assertEquals("Food not found", ex.getMessage());
    }
}
