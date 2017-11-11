package com.mobile.utn.quecocino.recipes.results;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobile.utn.quecocino.R;
import com.mobile.utn.quecocino.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Rama on 30/09/2017.
 */

public class RecipesResultsAdapter extends RecyclerView.Adapter<RecipesResultsViewHolder>{

    private List<Recipe> recipes;
    private Context context;

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    public RecipesResultsAdapter(Context context, List<Recipe> recipes) {
        this.context = context;
        this.recipes = recipes;
    }

    @Override
    public RecipesResultsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recipe_result, parent, false);
        return new RecipesResultsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipesResultsViewHolder holder, int position) {
        //holder.getMainImageView().setImageResource(R.drawable.mail);
        //Picasso.with(context).load(recipes.get(position).getMainImage()).resize(200, 200).fit().centerCrop().into(holder.getMainImageView());
        Recipe recipe = recipes.get(position);

        //Imagenes
        Picasso.with(context).load(recipe.getMainImage()).fit().centerCrop().into(holder.getMainImageView());
        String appliance = recipe.getApplianceCooking();

        //TODO use strings.xml
        if(appliance.equalsIgnoreCase("HORNO"))
            holder.getApplianceCookingImageView().setImageResource(R.drawable.recipe_oven);
        else
            holder.getApplianceCookingImageView().setImageResource(R.drawable.recipe_microwave);

        holder.getCookingTimeMinutesImageView().setImageResource(R.drawable.recipe_cookingtime);
        holder.getOriginImageView().setImageResource(R.drawable.recipe_location);

        //Textos
        Resources res = this.context.getResources();
        holder.getAuthorTextView().setText(res.getString(R.string.recipe_results_author) + recipe.getAuthor());
        holder.getTitleTextView().setText(res.getString(R.string.recipe_results_title) + recipe.getTitle());
        holder.getApplianceCookingTextView().setText(res.getString(R.string.recipe_results_applianceCooking) + recipe.getApplianceCooking());
        holder.getCookingTimeMinutesTextView().setText(res.getString(R.string.recipe_results_cookingTimeMinutes) + recipe.getCookingTimeMinutes());
        holder.getOriginTextView().setText(res.getString(R.string.recipe_results_origin) + recipe.getOrigin());
    }


    @Override
    public int getItemCount() {
        return recipes.size();
    }

}
