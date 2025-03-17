package com.bignerdranch.android.pantrypal;

import androidx.annotation.NonNull;

public class Ingredient {
    private String name;
    private String quantity;

    // Constructor
    public Ingredient(String name, String quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    // Getters and setters
    public String getName() { return name; }
    public String getQuantity() { return quantity; }

    @NonNull
    @Override
    public String toString(){
        return name + " (" + quantity + ")";
    }

}