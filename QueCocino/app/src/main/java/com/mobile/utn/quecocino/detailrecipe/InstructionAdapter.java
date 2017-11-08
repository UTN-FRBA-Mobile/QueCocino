package com.mobile.utn.quecocino.detailrecipe;

import java.io.Serializable;
import java.util.List;

import com.mobile.utn.quecocino.R;
import com.mobile.utn.quecocino.model.RecipeInstruction;
import com.mobile.utn.quecocino.timer.TimerEditFragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class InstructionAdapter extends ArrayAdapter<RecipeInstruction> implements View.OnClickListener
{
    private LayoutInflater layoutInflater;
    private AppCompatActivity context;

    public InstructionAdapter(Context context, List<RecipeInstruction> objects)
    {
        super(context, 0, objects);
        layoutInflater = LayoutInflater.from(context);
        this.context = (AppCompatActivity) context;
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
            holder.setImageTimer((ImageView) convertView.findViewById(R.id.detailRecipe_itemInstructionImgTimer));
            holder.setTextTimer((TextView) convertView.findViewById(R.id.detailRecipe_itemInstructionLinkTimer));
            convertView.setTag(holder);
        }
        else
        {
            holder = (Holder) convertView.getTag();
        }

        RecipeInstruction instruction = getItem(position);
        holder.getTextViewDescription().setText(instruction.getDescription());
        holder.getTextViewDescription().setTag(position);
        holder.getTextViewDescription().setOnClickListener(this);
        holder.getTextTimer().setTag(position);
        holder.getTextTimer().setOnClickListener(this);

        if(instruction.getTime() != null){
            holder.getTextTimer().setVisibility(View.VISIBLE);
            holder.getImageTimer().setVisibility(View.VISIBLE);
        }else{
            holder.getTextTimer().setVisibility(View.INVISIBLE);
            holder.getImageTimer().setVisibility(View.INVISIBLE);
        }

        changeBackground(holder.getCardView(), instruction);

        return convertView;
    }

    @Override
    public void onClick(View v) {

        int position = (Integer) v.getTag();
        RecipeInstruction instruction = getItem(position);

        if(v.getId() == R.id.detailRecipe_itemInstructionDesc){

            instruction.setChecked(!instruction.isChecked());
            changeBackground((CardView) v.getParent().getParent(), instruction);

        }else if(v.getId() == R.id.detailRecipe_itemInstructionLinkTimer){

            Bundle bundle = new Bundle();
            bundle.putString("hhmmss", instruction.getTime());
            bundle.putString("tag", instruction.getTag());

            FragmentTransaction ft = context.getSupportFragmentManager().beginTransaction();
            TimerEditFragment newFragment = new TimerEditFragment();
            newFragment.setArguments(bundle);
            ft.replace(R.id.navigation_container, newFragment);
            ft.addToBackStack(null);
            ft.commit();

        }

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
        ImageView imageTimer;
        TextView textTimer;

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

        public ImageView getImageTimer() {
            return imageTimer;
        }

        public void setImageTimer(ImageView imageTimer) {
            this.imageTimer = imageTimer;
        }

        public TextView getTextTimer() {
            return textTimer;
        }

        public void setTextTimer(TextView textTimer) {
            this.textTimer = textTimer;
        }
    }
}