package com.mobile.utn.quecocino.ingredientSearch;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobile.utn.quecocino.R;

public class IngredientsViewHolder extends RecyclerView.ViewHolder{

    private TextView ingredientText;

    public IngredientsViewHolder(View itemView) {
        super(itemView);

        ingredientText = (TextView) itemView.findViewById(R.id.ingredientsearch_ingredientText);

    }

    public TextView getIngredientText() {
        return ingredientText;
    }

    public void setIngredientText(TextView ingredientText) {
        this.ingredientText = ingredientText;
    }
}
