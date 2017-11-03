package com.mobile.utn.quecocino.detailrecipe;

import java.util.List;

import com.mobile.utn.quecocino.R;
import com.mobile.utn.quecocino.model.RecipeInstruction;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class InstructionAdapter extends ArrayAdapter<RecipeInstruction> implements View.OnClickListener
{
    private LayoutInflater layoutInflater;

    public InstructionAdapter(Context context, List<RecipeInstruction> objects)
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

            convertView = layoutInflater.inflate(R.layout.detailrecipe_iteminstruction, null);
            holder.setTextViewDescription((TextView) convertView.findViewById(R.id.detailRecipe_itemInstructionDesc));
            holder.setCardView((CardView) convertView.findViewById(R.id.detailRecipe_itemCardView));
            convertView.setTag(holder);
        }
        else
        {
            holder = (Holder) convertView.getTag();
        }

        RecipeInstruction instruction = getItem(position);
        holder.getTextViewDescription().setText(instruction.getDescription());
        holder.getCardView().setTag(position);
        holder.getCardView().setOnClickListener(this);

        changeBackground(holder.getCardView(), instruction);

        return convertView;
    }

    @Override
    public void onClick(View v) {

        int position = (Integer) v.getTag();
        RecipeInstruction instruction = getItem(position);
        instruction.setChecked(!instruction.isChecked());
        changeBackground((CardView) v, instruction);

    }

    @SuppressWarnings("deprecation")
    private void changeBackground(CardView cardView, RecipeInstruction instruction) {
        if (instruction.isChecked()) {
            cardView.setCardBackgroundColor(getContext().getResources().getColor(R.color.colorSelected));
        } else {
            cardView.setCardBackgroundColor(Color.WHITE);
        }
    }

    static class Holder
    {
        TextView textViewDescription;
        CardView cardView;

        public TextView getTextViewDescription() {
            return textViewDescription;
        }

        public void setTextViewDescription(TextView textViewDescription) {
            this.textViewDescription = textViewDescription;
        }

        public CardView getCardView() {
            return cardView;
        }

        public void setCardView(CardView cardView) {
            this.cardView = cardView;
        }
    }
}