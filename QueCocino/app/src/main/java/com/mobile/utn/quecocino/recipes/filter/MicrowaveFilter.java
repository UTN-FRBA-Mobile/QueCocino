package com.mobile.utn.quecocino.recipes.filter;

import com.mobile.utn.quecocino.model.Recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rama on 25/10/2017.
 */

public class MicrowaveFilter implements Filter{
    @Override
    public List<Recipe> meetCriteria(List<Recipe> recipes) {
        List<Recipe> microwaveRecipes = new ArrayList<Recipe>();

        for (Recipe recipe : recipes) {
            //TODO use strings.xml
            if(recipe.getApplianceCooking().equalsIgnoreCase("MICROONDAS")){
                microwaveRecipes.add(recipe);
            }
        }
        return microwaveRecipes;
    }

    @Override
    public List<Filter> getFilterComposition() {

        ArrayList<Filter> list = new ArrayList<Filter>();
        list.add(this);

        return list;
    }
}
