package com.mobile.utn.quecocino.detailrecipe;

import java.util.List;

import com.mobile.utn.quecocino.R;
import com.mobile.utn.quecocino.model.RecipeInstruction;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
            holder.setCheckBox((CheckBox) convertView.findViewById(R.id.detailRecipe_itemInstructionCk));
            convertView.setTag(holder);
        }
        else
        {
            holder = (Holder) convertView.getTag();
        }

        RecipeInstruction instruction = getItem(position);
        holder.getTextViewDescription().setText(instruction.getDescription());
        holder.getCheckBox().setTag(position);
        holder.getCheckBox().setChecked(instruction.isChecked());
        holder.getCheckBox().setOnClickListener(this);

        changeBackground(getContext(), holder.getCheckBox());

        return convertView;
    }

    @Override
    public void onClick(View v) {

        CheckBox checkBox = (CheckBox) v;
        int position = (Integer) v.getTag();
        getItem(position).setChecked(checkBox.isChecked());
        changeBackground(InstructionAdapter.this.getContext(), checkBox);

    }

    @SuppressWarnings("deprecation")
    private void changeBackground(Context context, CheckBox checkBox) {
        View instruction = (View) checkBox.getParent();
        Drawable drawable = context.getResources().getDrawable(R.drawable.detailrecipe_checked);
        if (checkBox.isChecked()) {
            drawable = context.getResources().getDrawable(R.drawable.detailrecipe_checked);
        } else {
            drawable = context.getResources().getDrawable(R.drawable.detailrecipe_nochecked);
        }
        instruction.setBackgroundDrawable(drawable);
    }

    static class Holder
    {
        TextView textViewDescription;
        CheckBox checkBox;

        public TextView getTextViewDescription() {
            return textViewDescription;
        }

        public void setTextViewDescription(TextView textViewDescription) {
            this.textViewDescription = textViewDescription;
        }

        public CheckBox getCheckBox() {
            return checkBox;
        }

        public void setCheckBox(CheckBox checkBox) {
            this.checkBox = checkBox;
        }
    }
}