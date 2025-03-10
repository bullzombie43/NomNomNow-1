package com.bignerdranch.android.pantrypal;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class RecipeActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new RecipeFragment();
    }
}