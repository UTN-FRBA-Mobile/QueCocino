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

    public FilterBuilder(){
        this.applianceCookingFilters = new ArrayList<Filter>();
    }

    public Filter buildFilter(){

        Filter applianceFilter = null;

        for (Filter filter : applianceCookingFilters){
            if (applianceFilter == null){
                applianceFilter = filter;
            }else{
                applianceFilter = new OrFilter(applianceFilter, filter);
            }
        }

        return applianceFilter;
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

}
