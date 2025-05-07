package com.bignerdranch.android.pantrypal;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ChatGPTRecipeFetcher {
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String API_KEY;

    static {
        API_KEY = BuildConfig.OPENAI_API_KEY;
    }

    public static String getApiKey() {
        return API_KEY;
    }

    public static Recipe fetchRecipe(String ingredientsList) throws IOException, JSONException {
        String prompt = "You are a recipe generator. When given a list of ingredients, return a recipe in the following strict JSON format with no extra text, explanations, or comments:\n\n"
                + "{\n"
                + "  \"recipeName\": \"Recipe Title\",\n"
                + "  \"ingredients\": [\n"
                + "    { \"name\": \"Ingredient 1\", \"quantity\": \"200 grams\" },\n"
                + "    { \"name\": \"Ingredient 2\", \"quantity\": \"150 milliliters\" }\n"
                + "  ],\n"
                + "  \"instructions\": [ \"Step 1: Do this.\", \"Step 2: Do that.\" ],\n"
                + "  \"totalTimeMinutes\": 45,\n"
                + "  \"servings\": 4,\n"
                + "  \"difficulty\": 3\n"
                + "}\n\n"
                + "Rules:\n"
                + "- Output only valid JSON, with no extra text or explanations.\n"
                + "- Use metric units: grams for solids, milliliters for liquids.\n"
                + "- Difficulty is between 1 - 5.\n"
                + "- Ensure all values follow the correct data type:\n"
                + "  - totalTimeMinutes, servings, and difficulty must be numbers, not strings.\n"
                + "- Ensure ingredient names are formatted clearly and concisely.\n"
                + "- The \"instructions\" array must contain exactly one step per element.\n"
                + "  - Each string must begin with \"Step X:\" (e.g., \"Step 1:\", \"Step 2:\").\n"
                + "  - Do not combine multiple steps into one array item.\n"
                + "- If generating multiple recipes with the same input ingredients, vary the recipes by sometimes:\n"
                + "  - Using only the provided ingredients,\n"
                + "  - Adding a few common complementary ingredients to enhance the recipe while keeping it realistic.\n"
                + "- Each time, generate a complete and distinct recipe within the above JSON format.";


        JSONObject requestBody = new JSONObject();
        requestBody.put("model", "gpt-4o-mini");

        JSONArray messages = new JSONArray();
        messages.put(new JSONObject().put("role", "system").put("content", prompt));
        messages.put(new JSONObject().put("role", "user").put("content", ingredientsList));

        requestBody.put("messages", messages);
        requestBody.put("max_tokens", 1000);
        String jsonInput = requestBody.toString();

        HttpURLConnection connection = (HttpURLConnection) new URL(API_URL).openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Bearer " + API_KEY);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            os.write(jsonInput.getBytes());
            os.flush();
        }

        // Read response
        StringBuilder response = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        }

        JSONObject jsonRespone = new JSONObject(response.toString());

        // Parse JSON response
        JSONArray choices = jsonRespone.getJSONArray("choices");
        JSONObject firstChoice = choices.getJSONObject(0);
        JSONObject message = firstChoice.getJSONObject("message");

        // Check if response is truncated
        String finishReason = firstChoice.getString("finish_reason");

        if ("length".equals(finishReason)) {
            System.out.println("⚠️ Warning: The response was cut off! Consider increasing max_tokens.");
        }

        // Extract content as a JSON string
        String contentJsonString = message.getString("content");

        // Parse the inner JSON string as a JSONObject
        JSONObject recipeJson = new JSONObject(contentJsonString);

        // Convert JSON to Recipe object
        return Recipe.parseRecipe(recipeJson);
    }


    public static List<Recipe> fetchRecipes(int numRecipes, List<String> ingredientList) throws JSONException, IOException {
        String ingredients = String.join(", ", ingredientList);
        List<Recipe> recipes = new ArrayList<>(numRecipes);

        for(int i =0; i < numRecipes; i++){
            recipes.add(fetchRecipe(ingredients));
        }

        return recipes;
    }

}
