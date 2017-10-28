package com.mobile.utn.quecocino.recipes.filter;

import com.mobile.utn.quecocino.model.Recipe;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Rama on 25/10/2017.
 */

public abstract class Filter implements Serializable {
    public abstract List<Recipe> meetCriteria(List<Recipe> recipes);
}
