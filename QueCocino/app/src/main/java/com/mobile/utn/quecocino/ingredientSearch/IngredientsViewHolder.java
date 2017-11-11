package com.mobile.utn.quecocino.ingredientSearch;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobile.utn.quecocino.R;

public class IngredientsViewHolder extends RecyclerView.ViewHolder {

    private IngredientSearchFragment fragment;

    private TextView ingredientText;
    private ImageView closeButton;

    public IngredientsViewHolder(View itemView, IngredientSearchFragment fragment) {
        super(itemView);
        this.fragment = fragment;

        ingredientText = (TextView) itemView.findViewById(R.id.ingredientsearch_ingredientText);
        closeButton = (ImageView) itemView.findViewById(R.id.ingredientsearch_closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IngredientsViewHolder.this.fragment.removeIngredientItem(getAdapterPosition());
            }
        });

    }

    public TextView getIngredientText() {
        return ingredientText;
    }

}
