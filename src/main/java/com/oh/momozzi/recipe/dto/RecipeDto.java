package com.oh.momozzi.recipe.dto;

import lombok.Data;

import java.sql.Date;

@Data
public class RecipeDto {

    private String recipeNo;
    private String recipeName;
    private String memberName;
    private String categoryCode;
    private String ingredients;
    private String recipeBody;
    private String viewNum;
    private String thumbsUpNum;
    private String recommendStatus;
    private String imgUrl;
    private Date regDate;
}
