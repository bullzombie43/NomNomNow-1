package com.bignerdranch.android.pantrypal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.UUID;

public class HomeScreenActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        Button findRecipesButton = findViewById(R.id.button_find_recipes);
        Button savedRecipesButton = findViewById(R.id.button_saved_recipes);

        findRecipesButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeScreenActivity.this, RecipeListActivity.class);
            startActivity(intent);
        });

        savedRecipesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreenActivity.this, RecipeListActivity.class);
                intent.putExtra("SHOW_FAVORITES", true);
                startActivity(intent);
            }
        });
    }

}
