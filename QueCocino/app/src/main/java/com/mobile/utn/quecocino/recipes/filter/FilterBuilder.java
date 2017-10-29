package com.mobile.utn.quecocino.recipes.filter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rama on 28/10/2017.
 */

public class FilterBuilder {

    //Appliance Cooking
    private List<Filter> applianceCookingFilters;
    private OvenFilter ovenFilter;
    private MicrowaveFilter microwaveFilter;

    //Cooking Time
    private List<Filter> cookingTimeFilters;
    private CookingTimeFilter cookingTimeFilter;

    public FilterBuilder(){
        this.applianceCookingFilters = new ArrayList<Filter>();
        this.cookingTimeFilters = new ArrayList<Filter>();
    }

    public Filter buildFilter(){

        // List with all section filters
        List<Filter> groupsFilters = new ArrayList<Filter>();
        Filter finalFilter = null;

        //Appliance cooking
        Filter applianceFilter = null;

        for (Filter filter : applianceCookingFilters){
            if (applianceFilter == null){
                applianceFilter = filter;
            }else{
                applianceFilter = new OrFilter(applianceFilter, filter);
            }
        }

        groupsFilters.add(applianceFilter);

        //Cooking Time
        Filter cookingTime = null;

        for (Filter filter : cookingTimeFilters){
            if (cookingTime == null){
                cookingTime = filter;
            }else{
                cookingTime = new OrFilter(cookingTime, filter);
            }
        }

        groupsFilters.add(cookingTime);

        // Final filter

        for (Filter filter : groupsFilters){
            if (finalFilter == null){
                finalFilter = filter;
            }else{
                finalFilter = new AndFilter(finalFilter, filter);
            }
        }

        return finalFilter;
    }

    public void addOven(){

        if (ovenFilter == null) ovenFilter = new OvenFilter();
        if (!applianceCookingFilters.contains(ovenFilter)) applianceCookingFilters.add(ovenFilter);

    }

    public void removeOven(){
        if (ovenFilter != null && applianceCookingFilters.contains(ovenFilter)) applianceCookingFilters.remove(ovenFilter);
    }

    public void addMicrowave(){

        if (microwaveFilter == null) microwaveFilter = new MicrowaveFilter();
        if (!applianceCookingFilters.contains(microwaveFilter)) applianceCookingFilters.add(microwaveFilter);

    }

    public void removeMicrowave(){
        if (microwaveFilter != null && applianceCookingFilters.contains(microwaveFilter)) applianceCookingFilters.remove(microwaveFilter);
    }

    public void addCookingTimeFilter(int min, int max){
        if (cookingTimeFilter == null) cookingTimeFilter = new CookingTimeFilter();
        cookingTimeFilter.setValues(min, max);
        if (!cookingTimeFilters.contains(cookingTimeFilter)) cookingTimeFilters.add(cookingTimeFilter);
    }

}
