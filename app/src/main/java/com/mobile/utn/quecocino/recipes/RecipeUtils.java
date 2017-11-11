package com.mobile.utn.quecocino.recipes;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mobile.utn.quecocino.detailrecipe.InstructionAdapter;
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

    public static boolean isFavorite(Context context, String recipeId) {
        List<String> favoriteIds = getFavoriteIds(context);
        return favoriteIds.contains(recipeId);
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

    public static void setListViewHeightBasedOnChildren(ListView listView) {

        ArrayAdapter adapter = (ArrayAdapter) listView.getAdapter();

        if (adapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }


}
