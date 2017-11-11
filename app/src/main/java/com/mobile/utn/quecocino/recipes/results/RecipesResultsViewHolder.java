package com.mobile.utn.quecocino.recipes.results;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobile.utn.quecocino.R;

/**
 * Created by Rama on 30/09/2017.
 */

public class RecipesResultsViewHolder extends RecyclerView.ViewHolder{

    private TextView titleTextView;
    private TextView authorTextView;
    private TextView cookingTimeMinutesTextView;
    private TextView applianceCookingTextView;
    private TextView originTextView;
    private ImageView mainImageView;
    private ImageView applianceCookingImageView;
    private ImageView cookingTimeMinutesImageView;
    private ImageView originImageView;

    public RecipesResultsViewHolder(View itemView) {
        super(itemView);

        mainImageView = (ImageView) itemView.findViewById(R.id.recipeImage);
        applianceCookingImageView = (ImageView) itemView.findViewById(R.id.applianceCookingImage);
        cookingTimeMinutesImageView = (ImageView) itemView.findViewById(R.id.cookingTimeMinutesImage);
        originImageView = (ImageView) itemView.findViewById(R.id.originImage);

        titleTextView = (TextView) itemView.findViewById(R.id.recipeTitle);
        authorTextView = (TextView) itemView.findViewById(R.id.recipeAuthor);
        cookingTimeMinutesTextView = (TextView) itemView.findViewById(R.id.recipeCookingTimeMinutes);
        applianceCookingTextView = (TextView) itemView.findViewById(R.id.recipeApplianceCooking);
        originTextView = (TextView) itemView.findViewById(R.id.recipeOrigin);

    }

    public TextView getTitleTextView() {
        return titleTextView;
    }

    public void setTitleTextView(TextView titleTextView) {
        this.titleTextView = titleTextView;
    }

    public TextView getAuthorTextView() {
        return authorTextView;
    }

    public void setAuthorTextView(TextView authorTextView) {
        this.authorTextView = authorTextView;
    }

    public ImageView getMainImageView() {
        return mainImageView;
    }

    public void setMainImageView(ImageView mainImageView) {
        this.mainImageView = mainImageView;
    }

    public TextView getCookingTimeMinutesTextView() {
        return cookingTimeMinutesTextView;
    }

    public void setCookingTimeMinutesTextView(TextView cookingTimeMinutesTextView) {
        this.cookingTimeMinutesTextView = cookingTimeMinutesTextView;
    }

    public TextView getApplianceCookingTextView() {
        return applianceCookingTextView;
    }

    public void setApplianceCookingTextView(TextView applianceCookingTextView) {
        this.applianceCookingTextView = applianceCookingTextView;
    }

    public ImageView getApplianceCookingImageView() {
        return applianceCookingImageView;
    }

    public void setApplianceCookingImageView(ImageView applianceCookingImageView) {
        this.applianceCookingImageView = applianceCookingImageView;
    }

    public ImageView getCookingTimeMinutesImageView() {
        return cookingTimeMinutesImageView;
    }

    public void setCookingTimeMinutesImageView(ImageView cookingTimeMinutesImageView) {
        this.cookingTimeMinutesImageView = cookingTimeMinutesImageView;
    }

    public TextView getOriginTextView() {
        return originTextView;
    }

    public void setOriginTextView(TextView originTextView) {
        this.originTextView = originTextView;
    }

    public ImageView getOriginImageView() {
        return originImageView;
    }

    public void setOriginImageView(ImageView originImageView) {
        this.originImageView = originImageView;
    }
}
