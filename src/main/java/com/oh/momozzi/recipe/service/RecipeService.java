package com.oh.momozzi.recipe.service;

import com.oh.momozzi.common.paging.SelectCriteria;
import com.oh.momozzi.recipe.dao.RecipeMapper;
import com.oh.momozzi.recipe.dto.RecipeDto;
import com.oh.momozzi.util.FileUploadUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

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
            recipeList.get(i).setRecipeImageUrl(IMAGE_URL + recipeList.get(i).getRecipeImageUrl());
        }
        log.info("[RecipeService] selectRecipeListWithPaging End ===================================");
        return recipeList;
    }

    public List<RecipeDto> selectRecipeTopThree() {

        log.info("[RecipeService] selectRecipeTopThree Start ===================================");
        List<RecipeDto> recipeDtoList = recipeMapper.selectRecipeTopThree();

        for(int i = 0 ; i < recipeDtoList.size() ; i++) {
            recipeDtoList.get(i).setRecipeImageUrl(IMAGE_URL + recipeDtoList.get(i).getRecipeImageUrl());
        }

        log.info("[RecipeService] selectRecipeTopThree End ===================================");
        return recipeDtoList;
    }

    public RecipeDto selectRecipe(String recipeNo) {

        log.info("[RecipeService] selectRecipe Start ===================================");
        RecipeDto recipeDto = recipeMapper.selectRecipe(recipeNo);
        recipeDto.setRecipeImageUrl(IMAGE_URL + recipeDto.getRecipeImageUrl());

        log.info("[RecipeService] selectRecipe End ===================================");
        return recipeDto;
    }

    @Transactional
    public String insertRecipe(RecipeDto recipeDto) {

        log.info("[RecipeService] insertRecipe Start ===================================");
        log.info("[RecipeService] RecipeDto : " + recipeDto);
        String imageName = UUID.randomUUID().toString().replace("-", "");
        String replaceFileName = null;
        int result = 0;
        log.info("[RecipeService] IMAGE_DIR : " + IMAGE_DIR);
        log.info("[RecipeService] imageName : " + imageName);

        try {
            replaceFileName = FileUploadUtils.saveFile(IMAGE_DIR, imageName, recipeDto.getRecipeImage());
            log.info("[RecipeService] replaceFileName : " + replaceFileName);

            recipeDto.setRecipeImageUrl(replaceFileName);

            log.info("[RecipeService] insert Image Name : " + replaceFileName);

            result = recipeMapper.insertRecipe(recipeDto);

        } catch (IOException e) {
            log.info("[RecipeService] IOException IMAGE_DIR : " + IMAGE_DIR);

            log.info("[RecipeService] IOException deleteFile : " + replaceFileName);

            FileUploadUtils.deleteFile(IMAGE_DIR, replaceFileName);
            throw new RuntimeException(e);
        }
        log.info("[RecipeService] result > 0 성공: "+ result);
        return (result > 0) ? "레시피 입력 성공" : "레시피 입력 실패";
    }

    @Transactional
    public String updateRecipeForRecommend(String recipeNo) {

        log.info("[RecipeService] updateRecipeForRecommend Start ===================================");
        log.info("[RecipeService] recipeNo : " + recipeNo);

        int result = 0;

        try {
            result = recipeMapper.updateRecipeForRecommend(recipeNo);
        } catch (Exception e) {
            e.printStackTrace();
        }

        log.info("[RecipeService] result > 0 성공: "+ result);
        return (result > 0) ? "레시피 관리자 추천 성공" : "레시피 관리자 추천 실패";
    }

    @Transactional
    public String updateRecipe(RecipeDto recipeDto) {

        log.info("[RecipeService] updateRecipe Start ===================================");
        log.info("[RecipeService] recipeDto : " + recipeDto);
        int result = 0;
        String replaceFileName = null;

        try {
            String oriImage = recipeMapper.selectRecipe(String.valueOf(recipeDto.getRecipeNo())).getRecipeImageUrl();
            log.info("[updateRecipe] oriImage : " + oriImage);

            if(recipeDto.getRecipeImage() != null){
                // 이미지 변경 진행
                String imageName = UUID.randomUUID().toString().replace("-", "");
                replaceFileName = FileUploadUtils.saveFile(IMAGE_DIR, imageName, recipeDto.getRecipeImage());

                log.info("[updateRecipe] IMAGE_DIR!!"+ IMAGE_DIR);
                log.info("[updateRecipe] imageName!!"+ imageName);

                log.info("[updateRecipe] InsertFileName : " + replaceFileName);
                recipeDto.setRecipeImageUrl(replaceFileName);

                log.info("[updateRecipe] deleteImage : " + oriImage);
                boolean isDelete = FileUploadUtils.deleteFile(IMAGE_DIR, oriImage);
                log.info("[update] isDelete : " + isDelete);
            } else {
                // 이미지 변경 없을 시
                recipeDto.setRecipeImageUrl(oriImage);
            }

            result = recipeMapper.updateRecipe(recipeDto);

        } catch (IOException e) {
            log.info("[updateRecipe] Exception!!");
            FileUploadUtils.deleteFile(IMAGE_DIR, replaceFileName);
            throw new RuntimeException(e);
        }
        log.info("[RecipeService] updateRecipe End ===================================");
        log.info("[RecipeService] result > 0 성공: "+ result);

        return (result > 0) ? "레시피 업데이트 성공" : "레시피 업데이트 실패";
    }

    @Transactional
    public String deleteRecipe(String recipeNo) {

        log.info("[RecipeService] deleteRecipe Start ===================================");
        log.info("[RecipeService] recipeNo : " + recipeNo);

        int result = 0;

        result = recipeMapper.deleteRecipe(recipeNo);

        log.info("[RecipeService] deleteRecipe End ===================================");
        log.info("[RecipeService] result > 0 성공: "+ result);

        return (result > 0) ? "레시피 삭제 성공" : "레시피 삭제 실패";
    }

    public int selectRecipeTotalByCategory(String categoryNo) {

        log.info("[RecipeService] selectRecipeTotalByCategory Start ===================================");
        int result = recipeMapper.selectRecipeTotalByCategory(categoryNo);

        log.info("[RecipeService] selectRecipeTotalByCategory End ===================================");
        return result;
    }

    public List<RecipeDto> selectRecipeByCategoryWithPaging(SelectCriteria selectCriteria) {

        log.info("[RecipeService] selectRecipeByCategoryWithPaging Start ===================================");
        List<RecipeDto> recipeList = recipeMapper.selectRecipeByCategoryWithPaging(selectCriteria);

        for(int i = 0 ; i < recipeList.size() ; i++) {
            recipeList.get(i).setRecipeImageUrl(IMAGE_URL + recipeList.get(i).getRecipeImageUrl());
        }
        log.info("[RecipeService] selectRecipeByCategoryWithPaging End ===================================");
        return recipeList;
    }
}
