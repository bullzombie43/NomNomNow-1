package com.bignerdranch.android.pantrypal.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bignerdranch.android.pantrypal.database.RecipeDbSchema.RecipeTable;

public class RecipeBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 2;
    private static final String DATABASE_NAME = "recipeBase.db";
    public RecipeBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + RecipeTable.NAME + "(" + " _id integer primary key autoincrement, " + RecipeTable.Cols.UUID + ", " + RecipeTable.Cols.TITLE + ", " + RecipeTable.Cols.TIME + ", " + RecipeTable.Cols.FAVORITED + ", " + RecipeTable.Cols.INGREDIENTS + ", " + RecipeTable.Cols.INSTRUCTIONS + ", "+ RecipeTable.Cols.DIFFICULTY +
                ")");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            // One-time wipe of all existing recipes
            db.delete(RecipeDbSchema.RecipeTable.NAME, null, null);
        }
    }

}
