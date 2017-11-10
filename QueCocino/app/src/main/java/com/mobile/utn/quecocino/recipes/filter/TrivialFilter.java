package com.mobile.utn.quecocino.recipes.filter;

import com.mobile.utn.quecocino.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class TrivialFilter implements Filter {
    @Override
    public List<Recipe> meetCriteria(List<Recipe> recipes) {
        return recipes;
    }

    @Override
    public List<Filter> getFilterComposition() {

        ArrayList<Filter> list = new ArrayList<Filter>();
        list.add(this);

        return list;
    }
}
