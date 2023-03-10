package com.oh.momozzi.recipe.controller;

import com.oh.momozzi.common.ResponseDto;
import com.oh.momozzi.common.paging.Pagenation;
import com.oh.momozzi.common.paging.ResponseDtoWithPaging;
import com.oh.momozzi.common.paging.SelectCriteria;
import com.oh.momozzi.recipe.dto.RecipeDto;
import com.oh.momozzi.recipe.service.RecipeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;

@Api(tags = {"뭐먹지 스웨거 연동 테스트"})
@Slf4j
@RestController
@RequestMapping("/api/v1")
public class RecipeController {
    // http://localhost:8090/swagger-ui/index.html

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @ApiOperation(value = "페이징 처리한 레시피 목록 조회")
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

    @ApiOperation(value = "페이징 처리한 관리자 추천 레시피 목록 조회")
    @GetMapping("/recipes/recommend")
    public ResponseEntity<ResponseDto> selectRecipeListWithPagingRecommended(@RequestParam(name="offset", defaultValue="1") String offset) {

        log.info("[RecipeController] selectRecipeListWithPagingRecommended : " + offset);
        int totalCount = recipeService.selectRecipeTotalRecommend();
        int limit = 10;
        int buttonAmount = 5;
        SelectCriteria selectCriteria = Pagenation.getSelectCriteria(Integer.parseInt(offset), totalCount, limit, buttonAmount);

        log.info("[RecipeController] selectCriteria : " + selectCriteria);

        ResponseDtoWithPaging responseDtoWithPaging = new ResponseDtoWithPaging();
        responseDtoWithPaging.setPageInfo(selectCriteria);
        responseDtoWithPaging.setData(recipeService.selectRecipeListWithPagingRecommended(selectCriteria));

        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "조회 성공", responseDtoWithPaging));
    }

    @ApiOperation(value = "레시피 상세 조회")
    @GetMapping("/recipes/{recipeNo}")
    public ResponseEntity<ResponseDto> selectRecipeDetail(@PathVariable String recipeNo) {

        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "레시피 상세정보 조회 성공",  recipeService.selectRecipe(recipeNo)));
    }

    @ApiOperation(value = "레시피 등록")
    @PostMapping("/recipes")
    public ResponseEntity<ResponseDto> insertRecipe(@RequestPart(required = false) MultipartFile recipeImage, String recipeName
            , String categoryNo, String ingredients, String recipeBody, String memberCode) {

        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setMemberCode(memberCode);
        recipeDto.setRecipeName(recipeName);
        recipeDto.setCategoryNo(categoryNo);
        recipeDto.setIngredients(ingredients);
        recipeDto.setRecipeBody(recipeBody);
        recipeDto.setRecipeImage(recipeImage);

        log.info("[RecipeController] PostMapping RecipeDto : " + recipeDto);
        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "레시피 입력 성공", recipeService.insertRecipe(recipeDto)));
    }

    @ApiOperation(value = "레시피 관리자 추천")
    @PutMapping("/recipes-recommend/{recipeNo}")
    public ResponseEntity<ResponseDto> updateRecipeForRecommend(@PathVariable String recipeNo) {

        log.info("[RecipeController] PutMapping recipeNo : " + recipeNo);
        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "레시피 관리자 추천 성공", recipeService.updateRecipeForRecommend(recipeNo)));
    }

    @ApiOperation(value = "레시피 사용자 추천")
    @PutMapping("/recipes-thumbs-up/{recipeNo}")
    public ResponseEntity<ResponseDto> updateRecipeForThumbsUp(@PathVariable String recipeNo) {

        log.info("[RecipeController] PutMapping recipeNo : " + recipeNo);
        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "레시피 멤버 추천 성공", recipeService.updateRecipeForThumbsUp(recipeNo)));
    }

    @ApiOperation(value = "레시피 수정")
    @PutMapping("/recipes")
    public ResponseEntity<ResponseDto> updateRecipe(@RequestPart(required = false) MultipartFile recipeImage, String recipeName, String categoryNo, String ingredients, String recipeBody, String memberCode, String recipeNo) {

        RecipeDto recipeDto = new RecipeDto();

        recipeDto.setRecipeNo(recipeNo);
        recipeDto.setRecipeImage(recipeImage);
        recipeDto.setMemberCode(memberCode);
        recipeDto.setRecipeName(recipeName);
        recipeDto.setCategoryNo(categoryNo);
        recipeDto.setIngredients(ingredients);
        recipeDto.setRecipeBody(recipeBody);
        log.info("[RecipeController]PutMapping recipeDto : " + recipeDto);

        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "레시피 업데이트 성공",  recipeService.updateRecipe(recipeDto)));
    }

    @ApiOperation(value = "레시피 삭제")
    @DeleteMapping("/recipes/{recipeNo}")
    public ResponseEntity<ResponseDto> deleteRecipe(@PathVariable String recipeNo) {

        log.info("[RecipeController]DeleteMapping recipeNo : " + recipeNo);

        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "레시피 삭제 성공",  recipeService.deleteRecipe(recipeNo)));
    }

    @ApiOperation(value = "카테고리 별 레시피 조회")
    @GetMapping("/recipes/categories/{categoryNo}")
    public ResponseEntity<ResponseDto> selectRecipeByCategoryWithPaging(@PathVariable String categoryNo, @RequestParam(name="offset", defaultValue="1") String offset) {

        log.info("[RecipeController] selectRecipeByCategoryWithPaging : " + offset);
        int totalCount = recipeService.selectRecipeTotalByCategory(categoryNo);
        int limit = 10;
        int buttonAmount = 5;
        SelectCriteria selectCriteria = Pagenation.getSelectCriteria(Integer.parseInt(offset), totalCount, limit, buttonAmount);

        selectCriteria.setCategoryNo(categoryNo);
        log.info("[RecipeController] selectCriteria : " + selectCriteria);

        ResponseDtoWithPaging responseDtoWithPaging = new ResponseDtoWithPaging();
        responseDtoWithPaging.setPageInfo(selectCriteria);
        responseDtoWithPaging.setData(recipeService.selectRecipeByCategoryWithPaging(selectCriteria));

        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "카테고리별 레시피 조회 성공", responseDtoWithPaging));
    }

//    @ApiOperation(value = "레시피 랜덤레시피 조회")
//    @GetMapping("/recipes/random")
//    public ResponseEntity<ResponseDto> selectRecipesRandom() {
//
//        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "레시피 랜덤 조회 성공",  recipeService.selectRecipesRandom()));
//    }

    @ApiOperation(value = "레시피 탑3, 랜덤 레시피 조회")
    @GetMapping("/recipes/top3-and-randoms")
    public ResponseEntity<ResponseDto> selectRecipeTopAndRandoms() {

        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "조회 성공", recipeService.selectRecipeTopAndRandoms()));
    }

    @ApiOperation(value = "마이 레시피 등록")
    @PostMapping("/recipes/my-recipe")
    public ResponseEntity<ResponseDto> insertMyRecipe(@RequestBody JSONObject object) {

        HashMap<String, String> map = new HashMap<>();
        map.put("memberCode", (String) object.get("memberCode"));
        map.put("recipeNo", (String) object.get("recipeNo"));
        log.info("params = {}", map);

//        log.info("[RecipeController] PostMapping map : " + map);
        return ResponseEntity.ok().body(new ResponseDto(HttpStatus.OK, "마이레시피 입력 성공", recipeService.insertMyRecipe(map)));
    }


}
