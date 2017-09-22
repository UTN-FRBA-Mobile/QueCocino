package com.mobile.utn.quecocino.UploadImages.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mobile.utn.quecocino.R;
import com.mobile.utn.quecocino.UploadImages.Objects.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Fran on 7/9/2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>{

    private List<Recipe> recipes;
    private Context context;

    public RecyclerAdapter(Context context, List<Recipe> recipes) {
        this.context = context;
        this.recipes = recipes;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_image, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String urlImagen = recipes.get(position).getUrl();
        Picasso.with(context).load(urlImagen).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imgItemGridImage);
        }
    }
}
