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
        mId = UUID.randomUUID();
    }
}
