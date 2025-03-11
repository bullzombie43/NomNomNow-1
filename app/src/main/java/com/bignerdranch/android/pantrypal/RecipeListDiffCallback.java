package com.bignerdranch.android.pantrypal;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

public class RecipeListDiffCallback extends DiffUtil.Callback {
    private final List<Recipe> oldList;
    private final List<Recipe> newList;

    public RecipeListDiffCallback(List<Recipe> oldList, List<Recipe> newList){
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getId().equals(newList.get(newItemPosition).getId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).areContentsSame(newList.get(newItemPosition));
    }
}
