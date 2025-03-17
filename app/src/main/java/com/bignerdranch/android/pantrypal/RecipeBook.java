package com.bignerdranch.android.pantrypal;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RecipeBook {
    private static RecipeBook sRecipeBook;
    private List<Recipe> mGeneratedRecipes;
    private List<Recipe> mFavoriteRecipes;

    public static RecipeBook get(Context context) {
        if (sRecipeBook == null) {
            sRecipeBook = new RecipeBook(context);
        }

        return sRecipeBook;
    }
    private RecipeBook(Context context) {
        mGeneratedRecipes = new ArrayList<>();
        mFavoriteRecipes = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Recipe recipe = new Recipe().withFavorite(true);
            recipe.setTitle("Recipe #" + i);
            mFavoriteRecipes.add(recipe);
        }
    }

    public List<Recipe> getGeneratedRecipes() {
        return mGeneratedRecipes;
    }

    public Recipe getRecipe(UUID id) {
        for (Recipe recipe : mFavoriteRecipes) {
            if (recipe.getId().equals(id)) {
                return recipe;
            }
        }
        return null;
    }

    public List<Recipe> getFavoriteRecipes() {
       return mFavoriteRecipes;
    }

    public void addFavoriteRecipe(Recipe newRecipe){
        mFavoriteRecipes.add(newRecipe);
    }
}
