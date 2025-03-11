package com.bignerdranch.android.pantrypal;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

class RecipeAdapter extends RecyclerView.Adapter<RecipeHolder>{
    private final RecipeListFragment mRecipeListFragment;
    private List<Recipe> mRecipes;

    public RecipeAdapter(RecipeListFragment recipeListFragment, List<Recipe> recipes) {
        mRecipeListFragment = recipeListFragment;
        mRecipes = recipes;
        Log.d("CrimeAdapter", "new construction");
    }

    @NonNull
    @Override
    public RecipeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mRecipeListFragment.getActivity());
        int resourceID;

        resourceID = R.layout.list_item_recipe;

        return new RecipeHolder(
                layoutInflater.inflate(resourceID, parent, false),
                mRecipeListFragment);
    }

    @Override
    public void onBindViewHolder(RecipeHolder holder, int position) {
        Recipe recipe = mRecipes.get(position);
        holder.bind(recipe);
    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }

    @Override
    public int getItemViewType(int position){
        return R.id.list_item_crime_serious;
    }

    public void updateCrimeList(List<Recipe> newRecipes){

        RecipeListDiffCallback diffCallback =
                new RecipeListDiffCallback(mRecipes, newRecipes);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
        mRecipes = newRecipes;
        diffResult.dispatchUpdatesTo(this);
    }

}
