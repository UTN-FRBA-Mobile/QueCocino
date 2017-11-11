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

    //Others
    private List<Filter> otherFilters;
    private GPSFilter gpsFilter;
    private DifficultyFilter difficultyFilter;

    public FilterBuilder(){
        this.applianceCookingFilters = new ArrayList<Filter>();
        this.cookingTimeFilters = new ArrayList<Filter>();
        this.otherFilters = new ArrayList<Filter>();
    }

    public Filter buildFilter(){

        // List with all section filters
        List<Filter> groupsFilters = new ArrayList<Filter>();
        Filter finalFilter = null;
        groupsFilters.add(new TrivialFilter());

        //Appliance cooking
        Filter applianceFilter = null;

        for (Filter filter : applianceCookingFilters){
            if (applianceFilter == null){
                applianceFilter = filter;
            }else{
                applianceFilter = new OrFilter(applianceFilter, filter);
            }
        }

        if (applianceFilter != null) groupsFilters.add(applianceFilter);

        //Cooking Time
        Filter cookingTime = null;

        for (Filter filter : cookingTimeFilters){
            if (cookingTime == null){
                cookingTime = filter;
            }else{
                cookingTime = new OrFilter(cookingTime, filter);
            }
        }

        if (cookingTime != null)  groupsFilters.add(cookingTime);

        //Other filters
        Filter otherFilter = null;

        for (Filter filter : otherFilters){
            if (otherFilter == null){
                otherFilter = filter;
            }else{
                otherFilter = new AndFilter(cookingTime, filter);
            }
        }

        if (otherFilter != null)  groupsFilters.add(otherFilter);

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

    public void addGps(String location) {
        if (gpsFilter == null) gpsFilter = new GPSFilter(location);
        if (!otherFilters.contains(gpsFilter)) otherFilters.add(gpsFilter);
    }

    public void removeGps(){
        if (gpsFilter != null && otherFilters.contains(gpsFilter)) otherFilters.remove(gpsFilter);
    }

    public void addDifficulty(ArrayList<String> list) {
        if (difficultyFilter == null) difficultyFilter = new DifficultyFilter(list);
        if (!otherFilters.contains(difficultyFilter)) otherFilters.add(difficultyFilter);
    }
}
