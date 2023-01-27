package com.oh.momozzi.recipe.service;

import com.oh.momozzi.MomozziApplication;
import com.oh.momozzi.common.paging.Pagenation;
import com.oh.momozzi.common.paging.SelectCriteria;
import com.oh.momozzi.recipe.dto.RecipeDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ContextConfiguration(classes = MomozziApplication.class)
class RecipeServiceTest {

    @Value("${image.image-dir}")
    private String IMAGE_DIR;
    @Value("${image.image-url}")
    private String IMAGE_URL;

    @Autowired
    private RecipeService recipeService;

    @Test
    void 레시피_서비스_의존성_주입_테스트() {

        assertNotNull(recipeService);
    }

    @Test
    void 레시피_총개수_조회_서비스_테스트() {

        // given
        // when
        int total = recipeService.selectRecipeTotal();
        // then
        assertTrue(total > 0);
    }

    @Test
    void 레시피_조회_페이징_서비스_테스트() {

        // given
        String offset = "1";
        int totalCount = 10;
        int limit = 10;
        int buttonAmount = 5;
        SelectCriteria selectCriteria = Pagenation.getSelectCriteria(Integer.parseInt(offset), totalCount, limit, buttonAmount);
        // when
        List<RecipeDto> list = recipeService.selectRecipeListWithPaging(selectCriteria);
        // then
        assertNotNull(list);
    }

    @Test
    void 레시피_상세조회_서비스_테스트() {

        // given
        String recipeNo = "1";
        // when
        RecipeDto recipeDto = recipeService.selectRecipe(recipeNo);
        // then
        assertNotNull(recipeDto);
    }

    @Test
    void 레시피_추가_서비스_테스트() {


    }

    @Test
    void updateRecipeForRecommend() {
    }

    @Test
    void updateRecipe() {
    }

    @Test
    void deleteRecipe() {
    }

    @Test
    void selectRecipeTotalByCategory() {
    }

    @Test
    void selectRecipeByCategoryWithPaging() {
    }
}