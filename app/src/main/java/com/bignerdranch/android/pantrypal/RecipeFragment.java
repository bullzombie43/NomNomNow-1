package com.bignerdranch.android.pantrypal;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import java.util.UUID;

public class RecipeFragment extends Fragment {
    private static final String ARG_RECIPE_ID = "recipe_id";

    private Recipe mRecipe;
    private CheckBox mFavoritedCheckBox;
    private EditText mTitleField;
    private EditText mIngredientsField;
    private EditText mTimeToMakeField;
    private EditText mInstructionsField;
    private TextView mDifficultyField;

    public static RecipeFragment newInstance(UUID recipeID){
        Bundle args = new Bundle();
        args.putSerializable(ARG_RECIPE_ID, recipeID);


        RecipeFragment fragment = new RecipeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID recipeID = (UUID) requireArguments().getSerializable(ARG_RECIPE_ID);
        RecipeBook recipeBook = RecipeBook.get(getActivity());
        mRecipe = recipeBook.getSavedRecipe(recipeID); // Check if it's saved

        if (mRecipe == null) {
            mRecipe = recipeBook.getGeneratedRecipe(recipeID); // Otherwise, check AI-generated
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recipe, container, false);

        mTitleField = (EditText) v.findViewById(R.id.recipe_title);
        mTitleField.setText(mRecipe.getTitle());
        Log.d("mRecipe Is null", "" + (mRecipe == null));
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {
                // This space intentionally left blank
            }
            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {
                mRecipe.setTitle(s.toString());
                mRecipe.setChanged(true);
            }
            @Override
            public void afterTextChanged(Editable s) {
                // This one too
            }
        });

        mIngredientsField = (EditText) v.findViewById(R.id.recipe_ingredients);
        mIngredientsField.setText(android.text.TextUtils.join(", ", mRecipe.getIngredients()));
        mIngredientsField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {
                mRecipe.setTitle(s.toString());
                mRecipe.setChanged(true);
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mInstructionsField = (EditText) v.findViewById(R.id.recipe_instructions);
        mInstructionsField.setText(android.text.TextUtils.join(", ", mRecipe.getInstructions()));
        mIngredientsField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {
                mRecipe.setTitle(s.toString());
                mRecipe.setChanged(true);
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mTimeToMakeField = (EditText) v.findViewById(R.id.recipe_timetomake);
        mTimeToMakeField.setText("Time: " + formatTime(mRecipe.getTimetoMake()));
        mDifficultyField = (TextView)v.findViewById(R.id.recipe_difficulty);
        mDifficultyField.setText("Difficulty: " + getStarRating(mRecipe.getDifficulty()));

        mFavoritedCheckBox = (CheckBox) v.findViewById(R.id.recipe_favorite);
        mFavoritedCheckBox.setChecked(mRecipe.isFavorite());

        mFavoritedCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mRecipe.setFavorite(isChecked);

            RecipeBook recipeBook = RecipeBook.get(getActivity());

            if (isChecked) {
                if (recipeBook.getSavedRecipe(mRecipe.getId()) == null) {
                    recipeBook.addFavoriteRecipe(mRecipe);
                }
            } else {
                recipeBook.removeFavoriteRecipe(mRecipe);
            }
        });


        return v;
    }

    private String formatTime(double minutes) {
        if (minutes < 60) {
            return String.format("%.0f minutes", minutes);
        } else {
            int hours = (int) (minutes / 60);
            int mins = (int) (minutes % 60);
            return mins > 0 ? String.format("%d hr %d min", hours, mins) : String.format("%d hr", hours);
        }
    }

    private String getStarRating(double difficulty) {
        int fullStars = (int) difficulty;
        boolean halfStar = (difficulty - fullStars) >= 0.5;

        StringBuilder stars = new StringBuilder("★".repeat(fullStars));
        if (halfStar) stars.append("☆");
        while (stars.length() < 5) stars.append("☆");

        return stars.toString();
    }
}
