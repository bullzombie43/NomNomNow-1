package com.bignerdranch.android.pantrypal;

import java.util.List;
import java.util.UUID;

public class Recipe {
    private UUID mId;
    private String mTitle;
    private List<String> mIngredients;
    private boolean mIsFavorite;
    private double mTimetoMake;
    private List<String> mInstructions;
    private int mDifficulty;

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
    }

    public List<String> getIngredients() {
        return mIngredients;
    }

    public void setIngredients(List<String> ingredients) {
        mIngredients = ingredients;
    }

    public boolean isFavorite() {
        return mIsFavorite;
    }

    public void setFavorite(boolean favorite) {
        mIsFavorite = favorite;
    }

    public double getTimetoMake() {
        return mTimetoMake;
    }

    public void setTimetoMake(double timetoMake) {
        mTimetoMake = timetoMake;
    }

    public List<String> getInstructions() {
        return mInstructions;
    }

    public void setInstructions(List<String> instructions) {
        mInstructions = instructions;
    }

    public int getDifficulty() {
        return mDifficulty;
    }

    public void setDifficulty(int difficulty) {
        mDifficulty = difficulty;
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
                List.of("Ingredient 1", "Ingredient 2"),
                false,
                30,
                List.of("Step 1", "Step 2"),
                3
        );
    }

    private Recipe (UUID id, String title, List<String> ingredients, boolean isFavorite, double timeToMake, List<String> steps,int difficulty){
        mId = id;
        mTitle = title;
        mIngredients = ingredients;
        mIsFavorite = isFavorite;
        mTimetoMake = timeToMake;
        mInstructions = steps;
        mDifficulty = difficulty;
    }
}
