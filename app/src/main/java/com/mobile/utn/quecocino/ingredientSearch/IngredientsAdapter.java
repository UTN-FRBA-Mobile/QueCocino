package com.mobile.utn.quecocino.ingredientSearch;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
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
    private IngredientSearchFragment fragment;
    private ItemTouchHelper itemTouchHelper;

    public List<RecipeIngredient> getIngredients() {
        return ingredients;
    }

    public ItemTouchHelper getItemTouchHelper() {
        return itemTouchHelper;
    }

    public IngredientsAdapter(IngredientSearchFragment fragment) {
        this.fragment = fragment;
        this.ingredients = new ArrayList<>();

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                IngredientsAdapter.this.fragment.removeIngredientItem(viewHolder.getAdapterPosition());
            }
        };
        itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
    }

    @Override
    public IngredientsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredientsearch_ingredient, parent, false);
        return new IngredientsViewHolder(view,fragment);
    }

    @Override
    public void onBindViewHolder(IngredientsViewHolder holder, int position) {
        final RecipeIngredient ingredient = ingredients.get(position);
        holder.getIngredientText().setText(ingredient.getDescription());
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

}
