package com.bignerdranch.android.pantrypal;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Recipe {
    private UUID mId;
    private String mTitle;
    private List<Ingredient> mIngredients;
    private boolean mIsFavorite;
    private double mTimetoMake;
    private List<String> mInstructions;
    private int mDifficulty;
    private int mServings;
    private boolean mChanged;

    public static Recipe parseRecipe(JSONObject recipeJson) throws JSONException {
        String recipeName = recipeJson.getString("recipeName");
        List<Ingredient> ingredients = parseIngredients(recipeJson.getJSONArray("ingredients"));
        List<String> instructions = parseInstructions(recipeJson.getJSONArray("instructions"));

        // Extract numeric values
        double totalTimeMinutes = recipeJson.optDouble("totalTimeMinutes", 0); // Default to 0 if missing
        int servings = recipeJson.optInt("servings", 1); // Default to 1
        int difficulty = recipeJson.optInt("difficulty", 1); // Default to 1

        return new Recipe(
                UUID.randomUUID(),
                recipeName,
                ingredients,
                instructions,
                false,
                totalTimeMinutes,
                difficulty,
                servings
        );

    }

    public static List<String> parseInstructions(JSONArray array) throws JSONException {
        List<String> instructions = new ArrayList<>();

        for(int i = 0; i < array.length(); i++){
            String entry = array.getString(i);

            // Split on "Step X:" patterns
            String[] steps = entry.split("(?=Step \\d+:)");
            for (String step : steps) {
                String trimmed = step.trim();
                if (!trimmed.isEmpty()) {
                    instructions.add(trimmed);
                }
            }
        }

        return instructions;
    }

    public static List<Ingredient> parseIngredients(JSONArray array) throws JSONException {
        List<Ingredient> ingredients = new ArrayList<>();

        for(int i = 0; i < array.length(); i++){
            JSONObject ingredient = array.getJSONObject(i);
            String name = ingredient.getString("name");
            String quantity = ingredient.getString("quantity");
            ingredients.add(new Ingredient(name, quantity));
        }

        return ingredients;
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
        mChanged = true;

    }

    public List<Ingredient> getIngredients() {
        return mIngredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        mIngredients = ingredients;
        mChanged = true;
    }


    public boolean isFavorite() {
        return mIsFavorite;
    }

    public void setFavorite(boolean favorite) {
        mIsFavorite = favorite;
        mChanged = true;
    }

    public double getTimetoMake() {
        return mTimetoMake;
    }

    public void setTimetoMake(double timetoMake) {
        mTimetoMake = timetoMake;
        mChanged = true;
    }

    public List<String> getInstructions() {
        return mInstructions;
    }

    public void setInstructions(List<String> instructions) {
        mInstructions = instructions;
        mChanged = true;
    }

    public void setInstructions(String instructions){
        mInstructions = Arrays.asList((TextUtils.split(", ", instructions)));
    }

    public int getDifficulty() {
        return mDifficulty;
    }

    public String getDifficultyString() {
        switch (mDifficulty) {
            case 1:
                return "Easy";
            case 2:
            case 3:
                return "Medium";
            case 4:
            case 5:
                return "Hard";
            default:
                return "Unknown"; // Or handle the default case as appropriate
        }
    }

    public void setDifficulty(int difficulty) {
        mDifficulty = difficulty;
        mChanged = true;
    }

    public int getServings(){return mServings;}

    public boolean areContentsSame(Recipe other){
        return
                (this.mId == other.mId) &&
                        (this.mTitle.equals(other.mTitle));
    }

    public Recipe withFavorite(boolean isFav){
        return new Recipe(mId, mTitle, mIngredients, mInstructions, isFav, mTimetoMake, mDifficulty, mServings);
    }

    public Recipe (){
        this(
                UUID.randomUUID(),
                "basic",
                List.of("Ingredient 1", "Ingredient 2"),
                List.of("Step 1", "Step 2"),
                false,
                30.0,
                1);
    }

    private Recipe(UUID id, String title, List<String> ingredients, List<String> instructions, boolean isFav, double timeToMake, int difficulty){
        this(
                UUID.randomUUID(),
                "title",
                List.of(new Ingredient("Ingredient 1", "100ml"), new Ingredient("Ingredient 1", "100ml")),
                List.of("Step 1", "Step 2"),
                false,
                30,
                3,
                1
        );
    }

    public boolean isChanged() {
        return mChanged;
    }

    public void setChanged(boolean changed) {
        mChanged = changed;
    }

    private Recipe (UUID id, String title, List<Ingredient> ingredients, List<String> steps, boolean isFavorite, double timeToMake,int difficulty, int servings){
        mId = id;
        mTitle = title;
        mIngredients = ingredients;
        mIsFavorite = isFavorite;
        mTimetoMake = timeToMake;
        mInstructions = steps;
        mDifficulty = difficulty;
        mServings = servings;
    }
}



