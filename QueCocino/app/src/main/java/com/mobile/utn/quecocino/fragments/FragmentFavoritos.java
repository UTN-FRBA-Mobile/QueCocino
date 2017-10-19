package com.mobile.utn.quecocino.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobile.utn.quecocino.R;


public class FragmentFavoritos extends Fragment {

    public FragmentFavoritos(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_favoritos, container, false);
        return rootView;
    }

}
