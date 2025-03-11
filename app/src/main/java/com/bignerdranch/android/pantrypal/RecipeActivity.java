package com.bignerdranch.android.pantrypal;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import java.util.UUID;

public class RecipeActivity extends SimpleFragmentActivity {
    public static final String EXTRA_RECIPE_ID =
            "com.bignerdranch.android.criminalintent.crime_id";

    public static Intent newIntent(Context packageContext, UUID recipeID) {
        Intent intent = new Intent(packageContext, RecipeActivity.class);
        intent.putExtra(EXTRA_RECIPE_ID, recipeID);
        return intent;
    }


    @Override
    protected Fragment createFragment() {
        UUID recipeID = (UUID) getIntent().getSerializableExtra(
                EXTRA_RECIPE_ID);
        return RecipeFragment.newInstance(recipeID);
    }
}