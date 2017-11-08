package com.mobile.utn.quecocino.recipes;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.mobile.utn.quecocino.model.Recipe;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public abstract class RecipeUtils {

    private static final String favoritesTag = ":favorites";

    public static void addFavorite(Context context, String recipeId) {
        List<String> favoriteIds = getFavoriteIds(context);
        if (favoriteIds.contains(recipeId)) {
            return;
        }
        favoriteIds.add(recipeId);
        saveIdsInPreferences(context, favoriteIds);
    }

    public static void removeFavorite(Context context, String recipeId) {
        List<String> favoriteIds = getFavoriteIds(context);
        favoriteIds.remove(recipeId);
        saveIdsInPreferences(context, favoriteIds);
    }

    public static List<String> getFavoriteIds(Context context) {
        List<String> ids = new ArrayList<>();
        try {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            JSONArray jsonArray2 = new JSONArray(prefs.getString(context.getPackageName() + favoritesTag, "[]"));

            for (int i = 0; i < jsonArray2.length(); i++) {
                ids.add(jsonArray2.getString(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ids;
    }

    private static void saveIdsInPreferences(Context context, List<String> favoriteIds) {
        JSONArray jsonArray = new JSONArray();
        for (String recipeId : favoriteIds) {
            jsonArray.put(recipeId);
        }
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(context.getPackageName() + favoritesTag, jsonArray.toString());
        editor.apply();
    }


}
