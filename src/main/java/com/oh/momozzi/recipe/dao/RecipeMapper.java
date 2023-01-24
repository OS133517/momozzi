package com.oh.momozzi.recipe.dao;

import com.oh.momozzi.common.paging.SelectCriteria;
import com.oh.momozzi.recipe.dto.RecipeDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RecipeMapper {

    int selectRecipeTotal();

    List<RecipeDto> selectRecipeListWithPaging(SelectCriteria selectCriteria);

    RecipeDto selectRecipe(String recipeNo);
}
