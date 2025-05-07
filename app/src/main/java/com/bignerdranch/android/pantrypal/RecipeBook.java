package com.bignerdranch.android.pantrypal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.bignerdranch.android.pantrypal.database.RecipeBaseHelper;
import com.bignerdranch.android.pantrypal.database.RecipeCursorWrapper;
import com.bignerdranch.android.pantrypal.database.RecipeDbSchema;

import org.json.JSONArray;
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
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static RecipeBook get(Context context) {
        if (sRecipeBook == null) {
            sRecipeBook = new RecipeBook(context);
        }
        return sRecipeBook;
    }

    private RecipeCursorWrapper queryRecipes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                RecipeDbSchema.RecipeTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new RecipeCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(Recipe recipe) {
        ContentValues values = new ContentValues();
        values.put(RecipeDbSchema.RecipeTable.Cols.UUID, recipe.getId().toString());
        values.put(RecipeDbSchema.RecipeTable.Cols.TITLE, recipe.getTitle());
        values.put(RecipeDbSchema.RecipeTable.Cols.TIME, recipe.getTimetoMake());
        values.put(RecipeDbSchema.RecipeTable.Cols.FAVORITED, recipe.isFavorite() ? 1 : 0);
        values.put(RecipeDbSchema.RecipeTable.Cols.INGREDIENTS, serializeIngredients(recipe.getIngredients()));
        values.put(RecipeDbSchema.RecipeTable.Cols.INSTRUCTIONS, serializeInstructions(recipe.getInstructions()));
        values.put(RecipeDbSchema.RecipeTable.Cols.DIFFICULTY, recipe.getDifficulty());
        return values;
    }


    private RecipeBook(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new RecipeBaseHelper(mContext).getWritableDatabase();
        mGeneratedRecipes = new ArrayList<>();
    }

    public List<Recipe> getGeneratedRecipes() {
        Log.d("RecipeBook", "Generated Recipes Size: " + mGeneratedRecipes.size());
        return mGeneratedRecipes;
    }

    public Recipe getSavedRecipe(UUID id) {
        RecipeCursorWrapper cursor = queryRecipes(
                RecipeDbSchema.RecipeTable.Cols.UUID + " = ?",
                new String[] { id.toString() }
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getRecipe();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } finally {
            cursor.close();
        }
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
        List<Recipe> favorites = new ArrayList<>();
        RecipeCursorWrapper cursor = queryRecipes(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Recipe recipe = cursor.getRecipe();
                if (recipe.isFavorite()) {
                    favorites.add(recipe);
                }
                cursor.moveToNext();
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } finally {
            cursor.close();
        }

        return favorites;
    }

    public List<Recipe> getAllRecipes() {
        List<Recipe> allRecipes = new ArrayList<>(mGeneratedRecipes);
        allRecipes.addAll(getFavoriteRecipes());  // Use getFavoriteRecipes() to get the favorite recipes
        return allRecipes;
    }

    public void addFavoriteRecipe(Recipe recipe) {
        if (recipe.getId() == null) {
            recipe.setId(UUID.randomUUID());
        }
        recipe.setFavorite(true);

        ContentValues values = getContentValues(recipe);
        mDatabase.insert(RecipeDbSchema.RecipeTable.NAME, null, values);
    }

    public void removeFavoriteRecipe(Recipe recipe) {
        mDatabase.delete(
                RecipeDbSchema.RecipeTable.NAME,
                RecipeDbSchema.RecipeTable.Cols.UUID + " = ?",
                new String[] { recipe.getId().toString() }
        );
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

    public void resetGeneratedRecipes(){
        mGeneratedRecipes.clear();
    }

    private static String serializeIngredients(List<Ingredient> ingredients) {
        StringBuilder sb = new StringBuilder();
        for (Ingredient ingredient : ingredients) {
            sb.append(ingredient.getName()).append("|").append(ingredient.getQuantity()).append(";");
        }
        return sb.toString();
    }

    private static String serializeInstructions(List<String> instructions) {
        JSONArray jsonArray = new JSONArray(instructions);
        return jsonArray.toString();
    }

}
