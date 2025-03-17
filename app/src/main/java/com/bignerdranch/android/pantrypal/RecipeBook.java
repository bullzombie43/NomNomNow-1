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
import java.util.concurrent.Executors;

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
        Log.d("generated recipes Size", "" + mGeneratedRecipes.size());
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
        for (Recipe recipe : mGeneratedRecipes) {
            if (recipe.isFavorite()) {
                mFavoriteRecipes.add(recipe);
            }
        }
        return mFavoriteRecipes;
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

    public CompletableFuture<Void> generateRecipes(List<String> ingredients, ExecutorService executor) {
        Log.d("Generate Recipes Called", "Called");
        CompletableFuture<Void> future = new CompletableFuture<>();

        executor.execute(() -> {
            mGeneratedRecipes = new ArrayList<>();
            try {
                mGeneratedRecipes = ChatGPTRecipeFetcher.fetchRecipes(3, ingredients);
                Log.d("Generated Recipes", mGeneratedRecipes.size() + " recipes generated");
                future.complete(null); // Signal success
            } catch (IOException | JSONException e) {
                future.completeExceptionally(e); // Signal failure
            }
        });

        return future;
    }
}
