package com.mobile.utn.quecocino.recipes.filter;

import com.mobile.utn.quecocino.model.Recipe;

import java.util.List;

public class TrivialFilter extends Filter {
    @Override
    public List<Recipe> meetCriteria(List<Recipe> recipes) {
        return recipes;
    }
}
