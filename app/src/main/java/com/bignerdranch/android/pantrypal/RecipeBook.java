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
            Recipe recipe = new Recipe();
            recipe.setTitle("Recipe #" + i);
            mGeneratedRecipes.add(recipe);
        }
    }

    public List<Recipe> getGeneratedRecipes() {
        return mGeneratedRecipes;
    }

    public Recipe getSavedRecipe(UUID id) {
        for (Recipe recipe : mFavoriteRecipes) {
            if (recipe.getId().equals(id)) {
                return recipe;
            }
        }
        return null;
    }

    public Recipe getGeneratedRecipe(UUID id) {
        for (Recipe recipe : mGeneratedRecipes) {
            if (recipe.getId().equals(id)) {
                return recipe;
            }
        }
        return null;
    }

    public List<Recipe> getFavoriteRecipes() {
        List<Recipe> savedRecipes = new ArrayList<>();
        for (Recipe recipe : mGeneratedRecipes) {
            if (recipe.isFavorite()) {
                savedRecipes.add(recipe);
            }
        }
        return savedRecipes;
    }

    public List<Recipe> getAllRecipes() {
        List<Recipe> allRecipes = new ArrayList<>(mGeneratedRecipes);
        allRecipes.addAll(mFavoriteRecipes);
        return allRecipes;
    }

    public void addFavoriteRecipe(Recipe recipe) {
        if (!mFavoriteRecipes.contains(recipe)) {
            recipe.setFavorite(true);
            mFavoriteRecipes.add(recipe);
        }
    }

    public void removeFavoriteRecipe(Recipe recipe) {
        recipe.setFavorite(false);
        mFavoriteRecipes.remove(recipe);
    }
}
