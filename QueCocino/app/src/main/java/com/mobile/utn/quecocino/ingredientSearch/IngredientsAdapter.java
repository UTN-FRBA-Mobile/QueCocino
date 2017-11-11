package com.mobile.utn.quecocino.ingredientSearch;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobile.utn.quecocino.R;
import com.mobile.utn.quecocino.model.Recipe;
import com.mobile.utn.quecocino.model.RecipeIngredient;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsViewHolder>{

    private List<RecipeIngredient> ingredients;
    private Context context;

    public List<RecipeIngredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<RecipeIngredient> ingredients) {
        this.ingredients = ingredients;
    }

    public IngredientsAdapter(Context context) {
        this.context = context;
        this.ingredients = new ArrayList<>();
    }

    @Override
    public IngredientsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredientsearch_ingredient, parent, false);
        return new IngredientsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IngredientsViewHolder holder, int position) {
        RecipeIngredient ingredient = ingredients.get(position);

        //Textos
        Resources res = this.context.getResources();
        holder.getIngredientText().setText(ingredient.getDescription());
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

}
