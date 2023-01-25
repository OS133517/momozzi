package com.oh.momozzi.recipe.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;

@Data
@RequiredArgsConstructor
public class RecipeDto {

    private String recipeNo;
    private String recipeName;
    private String memberCode;
    private String memberName;
    private String categoryNo;
    private String categoryName;
    private String ingredients;
    private String recipeBody;
    private String viewNum;
    private String thumbsUpNum;
    private String recommendStatus;
    private MultipartFile recipeImage;
    private String recipeImageUrl;
    private Date regDate;
}
