package com.bignerdranch.android.pantrypal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

public class RecipeListFragment extends Fragment {
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";
    public static final String SHOW_FAVORITES_EXTRA = "SHOW_FAVORITES";
    public static final String SHOW_GENERATED_EXTRA = "SHOW_GENERATED";

    private RecyclerView mRecipeRecyclerView;
    private RecipeAdapter mRecipeAdapter;
    private boolean mSubtitleVisible;
    private TextView mNoRecipesText;
    private Button mAddRecipe;
    private List<Recipe> mRecipeList;
    private boolean showFavorite = true;

    public static RecipeListFragment newInstance(boolean showGenerated) {
        RecipeListFragment fragment = new RecipeListFragment();
        Bundle args = new Bundle();
        args.putBoolean(SHOW_GENERATED_EXTRA, showGenerated);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.d("RecipeListFragment", "On Create");

        showFavorite = requireActivity().getIntent().getBooleanExtra(SHOW_FAVORITES_EXTRA, false);
        boolean showGenerated = getArguments() != null && getArguments().getBoolean(SHOW_GENERATED_EXTRA, false);

        RecipeBook recipeBook = RecipeBook.get(getActivity());

        if (showFavorite) {
            mRecipeList = recipeBook.getFavoriteRecipes();
            Log.d("Num Favorites", "" + recipeBook.getFavoriteRecipes().size());
        } else if (showGenerated) {
            mRecipeList = recipeBook.getGeneratedRecipes();
        } else {
            mRecipeList = recipeBook.getAllRecipes();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        mRecipeRecyclerView = view.findViewById(R.id.crime_recycler_view);
        mRecipeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAddRecipe = view.findViewById(R.id.add_crime);
        mAddRecipe.setOnClickListener(v -> {
            Recipe recipe = new Recipe();
            if (getActivity() != null) {
                RecipeBook.get(getActivity()).addFavoriteRecipe(recipe);
            }
            updateUI();
            Intent intent = RecipeActivity.newIntent(getActivity(), recipe.getId());
            startActivity(intent);
        });
        mAddRecipe.setVisibility(View.GONE);

        mNoRecipesText = view.findViewById(R.id.no_crimes_text);
        mNoRecipesText.setVisibility(View.GONE);

        if (savedInstanceState != null) {
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }

        updateUI();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.fragment_recipe_list, menu);
                MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
                subtitleItem.setTitle(mSubtitleVisible ? R.string.hide_subtitle : R.string.show_subtitle);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.new_crime) {
                    Recipe recipe = new Recipe();
                    RecipeBook.get(getActivity()).addFavoriteRecipe(recipe);
                    updateUI();
                    Intent intent = RecipeActivity.newIntent(getActivity(), recipe.getId());
                    startActivity(intent);
                    return true;
                } else if (menuItem.getItemId() == R.id.show_subtitle) {
                    mSubtitleVisible = !mSubtitleVisible;
                    requireActivity().invalidateOptionsMenu();
                    updateSubtitle();
                    return true;
                }
                return false;
            }
        });
    }

    private void updateUI() {
        RecipeBook recipeBook = RecipeBook.get(getActivity());
        List<Recipe> recipes = showFavorite ? recipeBook.getFavoriteRecipes() : recipeBook.getGeneratedRecipes();

        Log.d("New Size", "" + recipes.size());

        if (mRecipeAdapter == null) {
            mRecipeAdapter = new RecipeAdapter(recipes);
            mRecipeRecyclerView.setAdapter(mRecipeAdapter);
        } else {
            mRecipeAdapter.updateRecipeList(recipes);
        }

        if (recipes.isEmpty()) {
            mRecipeRecyclerView.setVisibility(View.GONE);
            mNoRecipesText.setVisibility(View.VISIBLE);
            mAddRecipe.setVisibility(View.VISIBLE);
            Log.d("is empty", "is empty");
        } else {
            mRecipeRecyclerView.setVisibility(View.VISIBLE);
            mNoRecipesText.setVisibility(View.GONE);
            mAddRecipe.setVisibility(View.GONE);
        }

        updateSubtitle();
    }

    @Override
    public void onResume(){
        super.onResume();
        updateUI();
    }

    private void updateSubtitle() {
        RecipeBook recipeBook = RecipeBook.get(getActivity());
        int recipeCount = recipeBook.getFavoriteRecipes().size();

        String subtitle = getString(R.string.subtitle_format, recipeCount);

        if (recipeCount == 1) {
            subtitle = subtitle.substring(0, subtitle.length() - 1);
        }

        if (!mSubtitleVisible) {
            subtitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        Objects.requireNonNull(activity.getSupportActionBar()).setSubtitle(subtitle);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }
}
