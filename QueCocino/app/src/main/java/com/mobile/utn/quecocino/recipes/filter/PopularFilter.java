package com.mobile.utn.quecocino.recipes.filter;

import com.mobile.utn.quecocino.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class PopularFilter extends Filter {
    @Override
    public List<Recipe> meetCriteria(List<Recipe> recipes) {
        List<Recipe> popularRecipes = new ArrayList<>();
        for (Recipe recipe : recipes) {
            if(recipe.isPopular()){
                popularRecipes.add(recipe);
            }
        }
        return popularRecipes;
    }
}
