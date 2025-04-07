package com.bignerdranch.android.pantrypal.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.bignerdranch.android.pantrypal.Ingredient;
import com.bignerdranch.android.pantrypal.Recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class RecipeCursorWrapper extends CursorWrapper {
    public RecipeCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Recipe getRecipe() {
        String uuidString = getString(getColumnIndex(RecipeDbSchema.RecipeTable.Cols.UUID));
        String title = getString(getColumnIndex(RecipeDbSchema.RecipeTable.Cols.TITLE));
        double time = getDouble(getColumnIndex(RecipeDbSchema.RecipeTable.Cols.TIME));
        int favorited = getInt(getColumnIndex(RecipeDbSchema.RecipeTable.Cols.FAVORITED));
        String ingredientsString = getString(getColumnIndex(RecipeDbSchema.RecipeTable.Cols.INGREDIENTS));
        String instructionsString = getString(getColumnIndex(RecipeDbSchema.RecipeTable.Cols.INSTRUCTIONS));
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

        // Convert comma-separated instructions to List<String>
        List<String> instructions = new ArrayList<>();
        if (instructionsString != null && !instructionsString.isEmpty()) {
            String[] steps = instructionsString.split(",");
            for (String step : steps) {
                instructions.add(step.trim());
            }
        }
        recipe.setInstructions(instructions);

        return recipe;
    }


}

