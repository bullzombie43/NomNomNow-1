package com.bignerdranch.android.pantrypal;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

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
        Log.d("RecipeBook", "Generated Recipes Size: " + mGeneratedRecipes.size());
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
        List<Recipe> validFavorites = new ArrayList<>();
        for (Recipe recipe : mFavoriteRecipes) {
            if (recipe != null && recipe.isFavorite()) {
                validFavorites.add(recipe);
            }
        }
        return validFavorites;
    }

    public List<Recipe> getAllRecipes() {
        List<Recipe> allRecipes = new ArrayList<>(mGeneratedRecipes);
        allRecipes.addAll(mFavoriteRecipes);
        return allRecipes;
    }

    public void addFavoriteRecipe(Recipe recipe) {
        if (recipe != null && !mFavoriteRecipes.contains(recipe)) {
            recipe.setFavorite(true);
            mFavoriteRecipes.add(recipe);
        } else {
            Log.e("RecipeBook", "Attempted to add a null recipe!");
        }
    }

    public void removeFavoriteRecipe(Recipe recipe) {
        recipe.setFavorite(false);
        mFavoriteRecipes.remove(recipe);
    }

    public CompletableFuture<Void> generateRecipes(List<String> ingredients, ExecutorService executor) {
        Log.d("RecipeBook", "Generate Recipes Called");
        CompletableFuture<Void> future = new CompletableFuture<>();

        executor.execute(() -> {
            try {
                List<Recipe> newRecipes = ChatGPTRecipeFetcher.fetchRecipes(3, ingredients);
                mGeneratedRecipes.addAll(newRecipes); // Add instead of replacing
                Log.d("RecipeBook", newRecipes.size() + " new recipes generated");
                future.complete(null); // Signal success
            } catch (IOException | JSONException e) {
                future.completeExceptionally(e); // Signal failure
            }
        });

        return future;
    }
}
