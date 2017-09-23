package com.mobile.utn.quecocino.UploadImages.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mobile.utn.quecocino.R;
import com.mobile.utn.quecocino.UploadImages.Objects.Recipe;

import java.util.ArrayList;

/**
 * Created by Fran on 21/9/2017.
 */

public class SlideshowDialogFragment extends DialogFragment {
    private ArrayList<Recipe> recipes;
    private ViewPager viewPager;
    private MyViewPageAdapter myViewPageAdapter;
    private TextView lblCount, lblAutor;
    private int selectedPosition = 0;

    static SlideshowDialogFragment newInstance(){
        return new SlideshowDialogFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_image_slider, container, false);
        viewPager = (ViewPager) v.findViewById(R.id.gallery_viewpager);
        lblCount = (TextView) v.findViewById(R.id.gallery_lblCount);
        lblAutor = (TextView) v.findViewById(R.id.gallery_lblAutor);

        recipes = (ArrayList<Recipe>) getArguments().getSerializable("recipes");
        selectedPosition = getArguments().getInt("position");

        myViewPageAdapter = new MyViewPageAdapter();
        viewPager.setAdapter(myViewPageAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        setCurrentItem(selectedPosition);

        return v;
    }

    private void setCurrentItem(int position){
        viewPager.setCurrentItem(position, false);
        displayMetaInfo(selectedPosition);
    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            displayMetaInfo(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }
    };

    private void displayMetaInfo(int position){
        lblCount.setText((position + 1) + " of " + recipes.size());

        Recipe recipe = recipes.get(position);
        lblAutor.setText(recipe.getAutor());

    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_AppCompat_DayNight);
    }

    public class MyViewPageAdapter extends PagerAdapter{

        private LayoutInflater layoutInflater;

        public MyViewPageAdapter(){
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position){

            layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.image_fullscreen_preview, container, false);

            ImageView imageViewPreview = (ImageView) view.findViewById(R.id.gallery_fullImage);

            Recipe recipe = recipes.get(position);

            Glide.with(getActivity()).load(recipe.getUrl())
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageViewPreview);

            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return recipes.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((View) object);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object){
            container.removeView((View) object);
        }

    }
}
