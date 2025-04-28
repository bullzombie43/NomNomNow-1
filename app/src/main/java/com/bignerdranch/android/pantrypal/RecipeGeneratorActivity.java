package com.bignerdranch.android.pantrypal;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;

import com.bignerdranch.android.pantrypal.R;
import com.bignerdranch.android.pantrypal.RecipeBook;
import com.bignerdranch.android.pantrypal.RecipeListFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class RecipeGeneratorActivity extends AppCompatActivity {
    private Button generateButton;
    private EditText ingredientsEditText;
    private FragmentContainerView fragmentContainerView;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor(); // Thread pool for background tasks

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generator);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        // Initialize views
        ingredientsEditText = findViewById(R.id.ingredientsEditText);
        generateButton = findViewById(R.id.generateButton);
        fragmentContainerView = findViewById(R.id.fragment_container);

        RecipeBook.get(this).resetGeneratedRecipes();

        loadRecipeListFragment();
        setupGenerateButton();


    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    private void setupGenerateButton() {
        generateButton.setOnClickListener(v -> {
            String ingredientsInput = ingredientsEditText.getText().toString().trim();
            handleIngredientInput(ingredientsInput);
        });
    }

    private void handleIngredientInput(String ingredientsInput) {
        if (TextUtils.isEmpty(ingredientsInput)) {
            showToast("Please enter some ingredients");
            return;
        }

        List<String> ingredientsList = parseIngredients(ingredientsInput);
        if (ingredientsList.isEmpty()) {
            showToast("Please enter valid ingredients");
            return;
        }

        generateRecipesAsync(ingredientsList);
    }

    private List<String> parseIngredients(String ingredientsInput) {
        return Arrays.stream(ingredientsInput.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    private void generateRecipesAsync(List<String> ingredients) {
        RecipeBook.get(this).generateRecipes(ingredients, executorService)
                .whenCompleteAsync((result, throwable) -> {
                    if (throwable == null) {
                        // Success
                        runOnUiThread(this::refreshFragment);
                    } else {
                        // Failure
                        runOnUiThread(() -> showToast("Failed to generate recipes: " + throwable.getMessage()));
                    }
        }, getMainExecutor());

    }

    private void showToast(String message) {
        runOnUiThread(() -> Toast.makeText(RecipeGeneratorActivity.this, message, Toast.LENGTH_SHORT).show());
    }

    private void loadRecipeListFragment() {
        RecipeListFragmentBarebones fragment = RecipeListFragmentBarebones.newInstance(true);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commitNow();
    }

    private void refreshFragment() {
        loadRecipeListFragment(); // Reload fragment with new data
        showToast("Fragment refreshed");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown(); // Shutdown the thread pool when the activity is destroyed
    }
}