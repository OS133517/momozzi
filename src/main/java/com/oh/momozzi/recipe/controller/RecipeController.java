package com.oh.momozzi.recipe.controller;

import com.oh.momozzi.common.ResponseDto;
import com.oh.momozzi.common.paging.Pagenation;
import com.oh.momozzi.common.paging.ResponseDtoWithPaging;
import com.oh.momozzi.common.paging.SelectCriteria;
import com.oh.momozzi.recipe.dto.RecipeDto;
import com.oh.momozzi.recipe.service.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/recipes")
    public ResponseEntity<ResponseDto> selectRecipeListWithPaging(@RequestParam(name="offset", defaultValue="1") String offset) {

        log.info("[RecipeController] selectRecipeListWithPaging : " + offset);
        int totalCount = recipeService.selectRecipeTotal();
        int limit = 10;
        int buttonAmount = 5;
        SelectCriteria selectCriteria = Pagenation.getSelectCriteria(Integer.parseInt(offset), totalCount, limit, buttonAmount);

        log.info("[RecipeController] selectCriteria : " + selectCriteria);

        ResponseDtoWithPaging responseDtoWithPaging = new ResponseDtoWithPaging();
        responseDtoWithPaging.setPageInfo(selectCriteria);
        responseDtoWithPaging.setData(recipeService.selectRecipeListWithPaging(selectCriteria));

        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "조회 성공", responseDtoWithPaging));
    }

    @GetMapping("/recipes/{recipeNo}")
    public ResponseEntity<ResponseDto> selectRecipeDetail(@PathVariable String recipeNo) {

        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "레시피 상세정보 조회 성공",  recipeService.selectRecipe(recipeNo)));
    }

    @PostMapping(value = "/recipes")
    public ResponseEntity<ResponseDto> insertRecipe(@RequestPart(required = false) MultipartFile recipeImage, @RequestPart RecipeDto recipeDto) {

        recipeDto.setRecipeImage(recipeImage);
        log.info("[RecipeController] PostMapping RecipeDto : " + recipeDto);
        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "레시피 입력 성공", recipeService.insertRecipe(recipeDto)));
    }

    @PutMapping("/recipes-recommend/{recipeNo}")
    public ResponseEntity<ResponseDto> updateRecipeForRecommend(@PathVariable String recipeNo) {

        log.info("[RecipeController] PutMapping recipeNo : " + recipeNo);
        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "레시피 관리자 추천 성공", recipeService.updateRecipeForRecommend(recipeNo)));
    }

    @PutMapping("/recipes")
    public ResponseEntity<ResponseDto> updateRecipe(@RequestPart(required = false) MultipartFile recipeImage, @RequestPart RecipeDto recipeDto) {

        recipeDto.setRecipeImage(recipeImage);
        log.info("[RecipeController]PutMapping recipeDto : " + recipeDto);

        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "레시피 업데이트 성공",  recipeService.updateRecipe(recipeDto)));
    }

    @DeleteMapping("/recipes/{recipeNo}")
    public ResponseEntity<ResponseDto> deleteRecipe(@PathVariable String recipeNo) {

        log.info("[RecipeController]DeleteMapping recipeNo : " + recipeNo);

        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "레시피 삭제 성공",  recipeService.deleteRecipe(recipeNo)));
    }
}
