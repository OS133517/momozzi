package com.oh.momozzi.recipe.dao;

import com.oh.momozzi.MomozziApplication;
import com.oh.momozzi.common.paging.Pagenation;
import com.oh.momozzi.common.paging.SelectCriteria;
import com.oh.momozzi.recipe.dto.RecipeDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ContextConfiguration(classes = MomozziApplication.class)
class RecipeMapperTest {

    @Autowired
    private RecipeMapper recipeMapper;

    @Test
    void 매퍼_인터페이스_의존성_주입_테스트() {

        assertNotNull(recipeMapper);
    }

    @Test
    void 레시피_총개수_조회_매퍼_테스트() {

        // given
        // when
        int total = recipeMapper.selectRecipeTotal();
        // then
        assertTrue(total > 0);
    }

    @Test
    void 레시피_조회_페이징_매퍼_테스트() {

        // given
        String offset = "1";
        int totalCount = 10;
        int limit = 10;
        int buttonAmount = 5;
        SelectCriteria selectCriteria = Pagenation.getSelectCriteria(Integer.parseInt(offset), totalCount, limit, buttonAmount);
        // when
        List<RecipeDto> list = recipeMapper.selectRecipeListWithPaging(selectCriteria);
        // then
        assertNotNull(list);
    }

    @Test
    void 레시피_상세조회_매퍼_테스트() {
        
        // given
        String recipeNo = "1";
        // when
        RecipeDto recipeDto = recipeMapper.selectRecipe(recipeNo);
        // then
        assertNotNull(recipeDto);
    }

    @Test
    void 레시피_추가_매퍼_테스트() {

        // given
        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setRecipeName("낙곱새");
        recipeDto.setMemberCode("2");
        recipeDto.setCategoryNo("1");
        recipeDto.setIngredients("낙지, 곱창, 새우");
        recipeDto.setRecipeBody("맛나");
        recipeDto.setRecipeImageUrl("test.png");
        // when
        int result = recipeMapper.insertRecipe(recipeDto);
        // then
        assertTrue(result > 0);
    }

    @Test
    void 관리자용_추천용_수정_매퍼_테스트() {

        // given
        String recipeNo = "3";
        // when
        int result = recipeMapper.updateRecipeForRecommend(recipeNo);
        // then
        assertTrue(result > 0);
    }

    @Test
    void 레시피_수정_매퍼_테스트() {

        // given
        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setRecipeNo("1");
        recipeDto.setRecipeName("낙곱새");
        recipeDto.setMemberCode("2");
        recipeDto.setCategoryNo("1");
        recipeDto.setIngredients("낙지, 곱창, 새우");
        recipeDto.setRecipeBody("수정테스트222");
        recipeDto.setRecipeImageUrl("test2222.png");
        // when
        int result = recipeMapper.updateRecipe(recipeDto);
        // then
        assertTrue(result > 0);
    }

    @Test
    void 레시피_삭제_매퍼_테스트() {

        // given
        String recipeNo = "30";
        // when
        int result = recipeMapper.deleteRecipe(recipeNo);
        // then
        assertTrue(result > 0);
    }

    @Test
    void 카테고리별_레시피_총개수_조회_매퍼_테스트() {

        // given
        String categoryNo = "1";
        // when
        int total = recipeMapper.selectRecipeTotalByCategory(categoryNo);
        // then
        assertTrue(total > 0);
    }

    @Test
    void 카테고리별_레시피_조회_매퍼_테스트() {

        // given
        String offset = "1";
        int totalCount = 10;
        int limit = 10;
        int buttonAmount = 5;
        SelectCriteria selectCriteria = Pagenation.getSelectCriteria(Integer.parseInt(offset), totalCount, limit, buttonAmount);
        selectCriteria.setCategoryNo("1");
        // when
        List<RecipeDto> list = recipeMapper.selectRecipeByCategoryWithPaging(selectCriteria);
        // then
        assertNotNull(list);
    }
}