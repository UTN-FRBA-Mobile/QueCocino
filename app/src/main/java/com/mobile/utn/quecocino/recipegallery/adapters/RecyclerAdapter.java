package com.mobile.utn.quecocino.recipegallery.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mobile.utn.quecocino.R;
import com.mobile.utn.quecocino.model.RecipeImage;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Fran on 7/9/2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>{

    private List<RecipeImage> images;
    private Context context;

    public RecyclerAdapter(Context context, List<RecipeImage> images) {
        this.context = context;
        this.images = images;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_itemgrid, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String urlImagen = images.get(position).getUrl();
        Picasso.with(context).load(urlImagen).resize(200, 200).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.gallery_itemGridImage);
        }
    }
}
