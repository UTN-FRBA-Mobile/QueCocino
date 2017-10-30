package com.mobile.utn.quecocino.recipes.filter;


import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appyvet.materialrangebar.RangeBar;
import com.mobile.utn.quecocino.R;
import com.mobile.utn.quecocino.menu.NavigationMenu;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class FiltersFragment extends Fragment {

    @BindView(R.id.microwaveButton)
    public FloatingActionButton microwaveButton;
    private boolean microwavePresed;

    @BindView(R.id.ovenButton)
    public FloatingActionButton ovenButton;
    private boolean ovenPresed;

    @BindView(R.id.cookingTimeMinutesRangeBar)
    public RangeBar cookingTimeMinutesRangeBar;

    private Filter filter;

    private FilterBuilder filterBuilder;

    public FiltersFragment() {
        // Required empty public constructor
    }

    public static FiltersFragment newInstance() {
        FiltersFragment fragment = new FiltersFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        filterBuilder = new FilterBuilder();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_filters, container, false);
        getActivity().setTitle("QueCocino");
        ButterKnife.bind(this,rootView);

        ovenButton.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
        microwaveButton.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
        cookingTimeMinutesRangeBar.setTickInterval(1f);
        cookingTimeMinutesRangeBar.setTickStart(0f);
        cookingTimeMinutesRangeBar.setTickEnd(180f);

        ovenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!ovenPresed){
                    ovenPresed = true;
                    filterBuilder.addOven();
                    ovenButton.setBackgroundTintList(ColorStateList.valueOf(fetchAccentColor()));
                }else{
                    ovenPresed = false;
                    filterBuilder.removeOven();
                    ovenButton.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
                }

            }
        });

        microwaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!microwavePresed){
                    microwavePresed = true;
                    filterBuilder.addMicrowave();
                    microwaveButton.setBackgroundTintList(ColorStateList.valueOf(fetchAccentColor()));
                }else{
                    microwavePresed = false;
                    filterBuilder.removeMicrowave();
                    microwaveButton.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
                }
            }
        });

        /*cookingTimeMinutesRangeBar.setFormatter(new IRangeBarFormatter() {
            @Override
            public String format(String s) {
                //TODO usar strings.xml
                return s + " min";
            }
        });*/

        cookingTimeMinutesRangeBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue,
                                              String rightPinValue) {
                filterBuilder.addCookingTimeFilter(leftPinIndex < rightPinIndex ? leftPinIndex : rightPinIndex, leftPinIndex < rightPinIndex ? rightPinIndex : leftPinIndex );
            }

        });

        return rootView;

    }

    @Override
    public void onDestroyView() {
        NavigationMenu activity = (NavigationMenu) getActivity();
        activity.setFilter(filterBuilder.buildFilter());
        super.onDestroyView();
    }

    private int fetchAccentColor() {
        TypedValue typedValue = new TypedValue();

        TypedArray a = getContext().obtainStyledAttributes(typedValue.data, new int[] { R.attr.colorAccent });
        int color = a.getColor(0, 0);

        a.recycle();

        return color;
    }
}
