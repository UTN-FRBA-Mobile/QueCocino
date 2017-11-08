package com.mobile.utn.quecocino.recipes.filter;

import android.content.Context;

import com.mobile.utn.quecocino.model.Recipe;
import com.mobile.utn.quecocino.recipes.RecipeUtils;

import java.util.ArrayList;
import java.util.List;

public class FavoriteFilter extends Filter {

    private Context context;

    public FavoriteFilter(Context context) {
        this.context = context;
    }

    @Override
    public List<Recipe> meetCriteria(List<Recipe> recipes) {
        List<Recipe> favoriteRecipes = new ArrayList<>();
        List<String> favoriteIds = RecipeUtils.getFavoriteIds(context);
        for (Recipe recipe : recipes) {
            if(favoriteIds.contains(recipe.getIdRecipe())){
                favoriteRecipes.add(recipe);
            }
        }
        return favoriteRecipes;
    }
}
