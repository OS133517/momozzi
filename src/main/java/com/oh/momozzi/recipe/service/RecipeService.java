package com.oh.momozzi.recipe.service;

import com.oh.momozzi.common.paging.SelectCriteria;
import com.oh.momozzi.recipe.dao.RecipeMapper;
import com.oh.momozzi.recipe.dto.RecipeDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class RecipeService {

    @Value("${image.image-dir}")
    private String IMAGE_DIR;
    @Value("${image.image-url}")
    private String IMAGE_URL;

    private final RecipeMapper recipeMapper;

    public RecipeService(RecipeMapper recipeMapper) {
        this.recipeMapper = recipeMapper;
    }

    public int selectRecipeTotal() {

        log.info("[RecipeService] selectRecipeTotal Start ===================================");
        int result = recipeMapper.selectRecipeTotal();

        log.info("[RecipeService] selectRecipeTotal End ===================================");
        return result;
    }

    public List<RecipeDto> selectRecipeListWithPaging(SelectCriteria selectCriteria) {

        log.info("[RecipeService] selectRecipeListWithPaging Start ===================================");
        List<RecipeDto> recipeList = recipeMapper.selectRecipeListWithPaging(selectCriteria);

        for(int i = 0 ; i < recipeList.size() ; i++) {
            recipeList.get(i).setImgUrl(IMAGE_URL + recipeList.get(i).getImgUrl());
        }
        log.info("[RecipeService] selectRecipeListWithPaging End ===================================");
        return recipeList;
    }

    public RecipeDto selectRecipe(String recipeNo) {

        log.info("[RecipeService] selectRecipe Start ===================================");
        RecipeDto recipeDto = recipeMapper.selectRecipe(recipeNo);
        recipeDto.setImgUrl(IMAGE_URL + recipeDto.getImgUrl());

        log.info("[RecipeService] selectRecipe End ===================================");
        return recipeDto;
    }
}
