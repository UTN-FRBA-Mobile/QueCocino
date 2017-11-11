package com.mobile.utn.quecocino.detailrecipe;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.mobile.utn.quecocino.R;
import com.mobile.utn.quecocino.model.RecipeIngredient;
import com.mobile.utn.quecocino.model.RecipeInstruction;

import java.util.List;

public class IngredientAdapter extends ArrayAdapter<RecipeIngredient>
{
    private LayoutInflater layoutInflater;

    public IngredientAdapter(Context context, List<RecipeIngredient> objects)
    {
        super(context, 0, objects);
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // holder pattern
        Holder holder = null;
        if (convertView == null)
        {
            holder = new Holder();

            convertView = layoutInflater.inflate(R.layout.detailrecipe_itemingredient, null);
            holder.setTextViewDescription((TextView) convertView.findViewById(R.id.detailRecipe_itemIngredientDesc));
            convertView.setTag(holder);
        }
        else
        {
            holder = (Holder) convertView.getTag();
        }

        RecipeIngredient ingredient = getItem(position);
        holder.getTextViewDescription().setText(ingredient.getDescription());

        return convertView;
    }

    static class Holder
    {
        TextView textViewDescription;

        public TextView getTextViewDescription() {
            return textViewDescription;
        }

        public void setTextViewDescription(TextView textViewDescription) {
            this.textViewDescription = textViewDescription;
        }
    }
}