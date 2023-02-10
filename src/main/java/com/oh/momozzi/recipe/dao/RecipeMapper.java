package com.oh.momozzi.recipe.dao;

import com.oh.momozzi.common.paging.SelectCriteria;
import com.oh.momozzi.recipe.dto.RecipeDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RecipeMapper {

    int selectRecipeTotal();

    int selectRecipeTotalRecommend();

    List<RecipeDto> selectRecipeListWithPaging(SelectCriteria selectCriteria);

    List<RecipeDto> selectRecipeListWithPagingRecommended(SelectCriteria selectCriteria);

    RecipeDto selectRecipe(String recipeNo);

    int updateViewNum(String recipeNo);

    List<RecipeDto> selectRecipeTopThree();

    int insertRecipe(RecipeDto recipe);

    int updateRecipeForRecommend(String recipeNo);

    int updateRecipeForThumbsUp(String recipeNo);

    int updateRecipe(RecipeDto recipeDto);

    int deleteRecipe(String recipeNo);

    int selectRecipeTotalByCategory(String categoryNo);

    List<RecipeDto> selectRecipeByCategoryWithPaging(SelectCriteria selectCriteria);
}
