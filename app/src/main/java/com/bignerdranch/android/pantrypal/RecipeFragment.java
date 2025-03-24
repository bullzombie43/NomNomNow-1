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
import android.widget.RatingBar;
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
        Log.d("RecipeFragment", "Received recipe ID: " + recipeID);

        RecipeBook recipeBook = RecipeBook.get(getActivity());
        mRecipe = recipeBook.getSavedRecipe(recipeID); // Check saved recipes
        Log.d("RecipeFragment", "Retrieved saved recipe: " + (mRecipe != null));

        if (mRecipe == null) {
            mRecipe = recipeBook.getGeneratedRecipe(recipeID); // Check AI-generated
            Log.d("RecipeFragment", "Retrieved generated recipe: " + (mRecipe != null));
        }

        if (mRecipe == null) {
            Log.e("RecipeFragment", "Error: Recipe not found!");
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
        RatingBar ratingBar = v.findViewById(R.id.recipe_difficulty);


        ratingBar.setRating(mRecipe.getDifficulty());

        ratingBar.setOnRatingBarChangeListener((bar, rating, fromUser) -> {
            if (fromUser) {
                mRecipe.setDifficulty((int) rating);
            }
        });

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

}
