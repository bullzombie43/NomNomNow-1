package com.bignerdranch.android.pantrypal;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

class RecipeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    private final RecipeListFragment mRecipeListFragment;
    private TextView mTitleTextView;

    private Recipe mRecipe;

    public RecipeHolder(View itemView, RecipeListFragment fragment) {
        super(itemView);
        itemView.setOnClickListener(this);

        mTitleTextView = (TextView) itemView.findViewById(R.id.recipe_title);
        mRecipeListFragment = fragment;
    }

    public void bind(Recipe recipe){
        mRecipe = recipe;
        mTitleTextView.setText(mRecipe.getTitle());
    }

    @Override
    public void onClick(View v) {
        Intent intent = RecipeActivity.newIntent(mRecipeListFragment.getActivity(), mRecipe.getId());
        mRecipeListFragment.startActivity(intent);
    }
}
