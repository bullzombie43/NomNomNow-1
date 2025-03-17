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
    private double mDifficulty;
    private boolean mChanged;

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

    public List<String> getIngredients() {
        return mIngredients;
    }

    public void setIngredients(List<String> ingredients) {
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

    public double getDifficulty() {
        return mDifficulty;
    }

    public void setDifficulty(double difficulty) {
        mDifficulty = difficulty;
        mChanged = true;
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

    public boolean isChanged() {
        return mChanged;
    }

    public void setChanged(boolean changed) {
        mChanged = changed;
    }

    private Recipe(UUID id, String title, List<String> ingredients, List<String> instructions, boolean isFav, double timeToMake, double difficulty){
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

    private Recipe (UUID id, String title, List<String> ingredients, boolean isFavorite, double timeToMake, List<String> steps,double difficulty){
        mId = id;
        mTitle = title;
        mIngredients = ingredients;
        mIsFavorite = isFavorite;
        mTimetoMake = timeToMake;
        mInstructions = steps;
        mDifficulty = difficulty;
    }

    public boolean areContentsSame(Recipe other){
        return
                (this.mId == other.mId) &&
                (this.mTitle.equals(other.mTitle));
    }

    public Recipe withFavorite(boolean favorite){
        return new Recipe(
                this.mId,
                this.mTitle,
                this.mIngredients,
                favorite,
                this.mTimetoMake,
                this.mInstructions,
                this.mDifficulty
        );
    }
}
