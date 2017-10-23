package com.mobile.utn.quecocino.fragments;


import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobile.utn.quecocino.R;


public class FragmentFavorites extends Fragment {

    public FragmentFavorites(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_favorites, container, false);
        getActivity().setTitle("Favoritos");
        return rootView;
    }

}
