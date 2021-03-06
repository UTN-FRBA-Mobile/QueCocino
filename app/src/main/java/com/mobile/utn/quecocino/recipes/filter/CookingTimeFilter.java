package com.mobile.utn.quecocino.recipes.filter;

import com.mobile.utn.quecocino.model.Recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rama on 29/10/2017.
 */

public class CookingTimeFilter implements Filter{

    private int minMinutes;
    private int maxMinutes;

    @Override
    public List<Recipe> meetCriteria(List<Recipe> recipes) {
        List<Recipe> cookingTimeRecipes = new ArrayList<Recipe>();

        for (Recipe recipe : recipes) {
            if(recipe.getCookingTimeMinutes() >= minMinutes && recipe.getCookingTimeMinutes() <= maxMinutes){
                cookingTimeRecipes.add(recipe);
            }
        }
        return cookingTimeRecipes;
    }

    public void setValues(int min, int max) {
        this.minMinutes = min;
        this.maxMinutes = max;
    }

    public int getMinMinutes() {
        return minMinutes;
    }

    public int getMaxMinutes() {
        return maxMinutes;
    }

    @Override
    public List<Filter> getFilterComposition() {

        ArrayList<Filter> list = new ArrayList<Filter>();
        list.add(this);

        return list;
    }
}