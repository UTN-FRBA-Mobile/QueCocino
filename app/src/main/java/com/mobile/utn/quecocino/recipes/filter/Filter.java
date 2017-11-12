package com.mobile.utn.quecocino.recipes.filter;

import com.mobile.utn.quecocino.model.Recipe;

import java.util.List;

/**
 * Created by Rama on 25/10/2017.
 */

public interface Filter {
    public List<Recipe> meetCriteria(List<Recipe> recipes);
    public List<Filter> getFilterComposition();
}
