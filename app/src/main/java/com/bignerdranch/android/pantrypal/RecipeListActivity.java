package com.bignerdranch.android.pantrypal;

import androidx.fragment.app.Fragment;

public class RecipeListActivity extends SimpleFragmentActivity{
    @Override
    protected Fragment createFragment() {
        return new RecipeListFragment();
    }
}
