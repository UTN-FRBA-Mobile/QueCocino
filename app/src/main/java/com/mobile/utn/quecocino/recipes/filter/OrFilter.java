package com.mobile.utn.quecocino.recipes.filter;

import com.mobile.utn.quecocino.model.Recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rama on 25/10/2017.
 */

public class OrFilter implements Filter {

    private Filter criteria;
    private Filter otherCriteria;

    public OrFilter(Filter criteria, Filter otherCriteria) {
        this.criteria = criteria;
        this.otherCriteria = otherCriteria;
    }

    @Override
    public List<Recipe> meetCriteria(List<Recipe> recipes) {
        List<Recipe> firstCriteriaItems = criteria.meetCriteria(recipes);
        List<Recipe> otherCriteriaItems = otherCriteria.meetCriteria(recipes);

        for (Recipe recipe : otherCriteriaItems) {
            if(!firstCriteriaItems.contains(recipe)){
                firstCriteriaItems.add(recipe);
            }
        }
        return firstCriteriaItems;
    }

    @Override
    public List<Filter> getFilterComposition() {

        ArrayList<Filter> list = new ArrayList<Filter>();

        list.addAll(criteria.getFilterComposition());
        list.addAll(otherCriteria.getFilterComposition());

        return list;
    }
}
