package com.mobile.utn.quecocino.recipes.filter;

import com.mobile.utn.quecocino.model.Recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rama on 11/11/2017.
 */

public class DifficultyFilter implements Filter {

    private List<String> difficultiesList;

    public DifficultyFilter(List<String> difficulties){
        this.difficultiesList = difficulties;
    }

    @Override
    public List<Recipe> meetCriteria(List<Recipe> recipes) {
        List<Recipe> difficultyRecipes = new ArrayList<Recipe>();

        for (Recipe recipe : recipes) {
            //TODO use strings.xml
            for (String difficulty : difficultiesList){
                if(recipe.getDifficulty().equalsIgnoreCase(difficulty)){
                    difficultyRecipes.add(recipe);
                }
            }
        }
        return difficultyRecipes;
    }

    @Override
    public List<Filter> getFilterComposition() {

        ArrayList<Filter> list = new ArrayList<Filter>();
        list.add(this);

        return list;
    }

    public List<String> getDifficultiesList() {
        return difficultiesList;
    }
}
