package com.mobile.utn.quecocino.recipes.filter;

import com.mobile.utn.quecocino.model.Recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rama on 30/10/2017.
 */

public class GPSFilter implements Filter {

    private String origin;

    public GPSFilter(String location) {
        origin = location;
    }

    @Override
    public List<Recipe> meetCriteria(List<Recipe> recipes) {
        List<Recipe> originRecipes = new ArrayList<Recipe>();

        if (origin == null || origin.equalsIgnoreCase("")) return recipes;

        for (Recipe recipe : recipes) {
            //TODO use strings.xml
            if(recipe.getOrigin() != null && recipe.getOrigin().equalsIgnoreCase(origin)){
                originRecipes.add(recipe);
            }
        }
        return originRecipes;
    }

    @Override
    public List<Filter> getFilterComposition() {

        ArrayList<Filter> list = new ArrayList<Filter>();
        list.add(this);

        return list;
    }

}
