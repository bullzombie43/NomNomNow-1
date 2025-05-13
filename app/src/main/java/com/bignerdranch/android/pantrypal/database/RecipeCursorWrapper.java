package com.bignerdranch.android.pantrypal.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.bignerdranch.android.pantrypal.Ingredient;
import com.bignerdranch.android.pantrypal.Recipe;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class RecipeCursorWrapper extends CursorWrapper {
    public RecipeCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Recipe getRecipe() throws JSONException {
        String uuidString = getString(getColumnIndex(RecipeDbSchema.RecipeTable.Cols.UUID));
        String title = getString(getColumnIndex(RecipeDbSchema.RecipeTable.Cols.TITLE));
        double time = getDouble(getColumnIndex(RecipeDbSchema.RecipeTable.Cols.TIME));
        int favorited = getInt(getColumnIndex(RecipeDbSchema.RecipeTable.Cols.FAVORITED));
        String ingredientsString = getString(getColumnIndex(RecipeDbSchema.RecipeTable.Cols.INGREDIENTS));
        List<String> instructionList = getInstructionList();
        int difficulty = getInt(getColumnIndex(RecipeDbSchema.RecipeTable.Cols.DIFFICULTY));


        Recipe recipe = new Recipe();
        recipe.setId(UUID.fromString(uuidString));
        recipe.setTitle(title);
        recipe.setTimetoMake(time);
        recipe.setFavorite(favorited != 0);
        recipe.setDifficulty(difficulty);

        // Convert comma-separated ingredients to List<Ingredient>
        List<Ingredient> ingredients = new ArrayList<>();
        if (ingredientsString != null && !ingredientsString.isEmpty()) {
            String[] ingredientItems = ingredientsString.split(";");
            for (String item : ingredientItems) {
                String[] parts = item.split("\\|");
                if (parts.length == 2) {
                    ingredients.add(new Ingredient(parts[0], parts[1]));
                }
            }
        }
        recipe.setIngredients(ingredients);

        recipe.setInstructions(instructionList);

        return recipe;
    }

    public List<String> getInstructionList() throws JSONException {
        String instructionsString = getString(getColumnIndex(RecipeDbSchema.RecipeTable.Cols.INSTRUCTIONS));
        JSONArray array = new JSONArray(instructionsString);

        return Recipe.parseInstructions(array);
    }


}

