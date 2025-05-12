package com.example.foodlabel.mapper;

import com.example.foodlabel.dto.FoodDto;
import com.example.foodlabel.dto.LabelDto;
import com.example.foodlabel.model.Food;
import com.example.foodlabel.model.Label;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FoodLabelMapper {

    FoodDto toFoodDto(Food food);

    LabelDto toLabelDto(Label label);

    List<FoodDto> toFoodDtoList(List<Food> foods);

    List<LabelDto> toLabelDtoList(List<Label> labels);
}
