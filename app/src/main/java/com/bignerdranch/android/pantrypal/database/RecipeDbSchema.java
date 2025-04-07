package com.bignerdranch.android.pantrypal.database;

public class RecipeDbSchema {
    public static final class RecipeTable {
        public static final String NAME = "recipes";
        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String TIME = "time";
            public static final String FAVORITED = "favorited";
            public static final String INGREDIENTS = "ingredients";
            public static final String INSTRUCTIONS = "instructions";
            public static final String DIFFICULTY = "difficulty";
        }
    }
}
