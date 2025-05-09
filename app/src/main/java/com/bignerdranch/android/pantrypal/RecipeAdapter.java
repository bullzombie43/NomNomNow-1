package com.bignerdranch.android.pantrypal;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeHolder> {
    private List<Recipe> mRecipes;

    public RecipeAdapter(List<Recipe> recipes) {
        if (recipes == null) {
            Log.e("RecipeAdapter", "Received null recipe list!");
            recipes = new ArrayList<>();
        }
        mRecipes = recipes;
    }

    @NonNull
    @Override
    public RecipeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_item_recipe, parent, false);
        return new RecipeHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeHolder holder, int position) {
        Recipe recipe = mRecipes.get(position);

        if (recipe != null) {
            holder.bind(recipe);

            holder.itemView.setOnClickListener(v -> {
                Log.d("RecipeAdapter", "Clicked recipe ID: " + recipe.getId());

                Intent intent = RecipeActivity.newIntent(v.getContext(), recipe.getId());
                v.getContext().startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return mRecipes == null ? 0 : mRecipes.size();
    }

    public void updateRecipeList(List<Recipe> newRecipes) {
        mRecipes = newRecipes;
        notifyDataSetChanged();
    }

    static class RecipeHolder extends RecyclerView.ViewHolder {
        private final TextView mTitleTextView;
        private Recipe mRecipe;
        private final Button mDifficultyButton;

        public RecipeHolder(View itemView) {
            super(itemView);
            mTitleTextView = itemView.findViewById(R.id.recipe_title);
            mDifficultyButton = itemView.findViewById(R.id.button);

        }

        public void bind(Recipe recipe) {
            mRecipe = recipe;
            mTitleTextView.setText(recipe.getTitle());

            mDifficultyButton.setEnabled(false);
            mDifficultyButton.setText(recipe.getDifficultyString());
        }
    }
}
