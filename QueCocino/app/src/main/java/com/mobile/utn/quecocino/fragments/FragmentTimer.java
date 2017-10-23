package com.mobile.utn.quecocino.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobile.utn.quecocino.R;


public class FragmentTimer extends Fragment {

    public FragmentTimer(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_timer, container, false);
        getActivity().setTitle("Timers");
        return rootView;
    }

}
