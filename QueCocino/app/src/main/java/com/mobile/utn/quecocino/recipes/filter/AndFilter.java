package com.mobile.utn.quecocino.recipes.filter;

import com.mobile.utn.quecocino.model.Recipe;

import java.util.List;

/**
 * Created by Rama on 25/10/2017.
 */

public class AndFilter extends Filter {

    private Filter criteria;
    private Filter otherCriteria;

    public AndFilter(Filter criteria, Filter otherCriteria) {
        this.criteria = criteria;
        this.otherCriteria = otherCriteria;
    }

    @Override
    public List<Recipe> meetCriteria(List<Recipe> recipes) {

        List<Recipe> firstCriteriaRecipes = criteria.meetCriteria(recipes);
        return otherCriteria.meetCriteria(firstCriteriaRecipes);
    }
}