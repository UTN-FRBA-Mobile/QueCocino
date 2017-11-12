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
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.appyvet.materialrangebar.RangeBar;
import com.mobile.utn.quecocino.R;
import com.mobile.utn.quecocino.menu.NavigationMenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    @BindView(R.id.gpsButton)
    public FloatingActionButton gpsButton;
    private boolean gpsPresed;

    @BindView(R.id.filterButton)
    public Button filterButton;

    @BindView(R.id.gpsProgress)
    public ProgressBar gpsProgress;

    @BindView(R.id.filter_layout_maxCookingTimeMinutes)
    public TextView maxCookingTimeMinutesTextView;

    @BindView(R.id.filter_layout_minCookingTimeMinutes)
    public TextView minCookingTimeMinutesTextView;

    @BindView(R.id.difficultyEasyButton)
    public FloatingActionButton difficultyEasyButton;
    private boolean difficultyEasyPressed;

    @BindView(R.id.difficultyMediumButton)
    public FloatingActionButton difficultyMediumButton;
    private boolean difficultyMediumPressed;

    @BindView(R.id.difficultyHardButton)
    public FloatingActionButton difficultyHardButton;
    private boolean difficultyHardPressed;

    private FilterBuilder filterBuilder;

    private int cookingTimeMinPin;
    private int cookingTimeMaxPin;

    private FiltersFragment self;

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

        self = this;

        NavigationMenu activity = (NavigationMenu) getActivity();

        initFields();

        for (Filter filter : activity.getFilter().getFilterComposition()){
            if (filter instanceof OvenFilter){
                ovenButton.setBackgroundTintList(ColorStateList.valueOf(fetchAccentColor()));
                ovenPresed = true;
            }
            if (filter instanceof MicrowaveFilter){
                microwaveButton.setBackgroundTintList(ColorStateList.valueOf(fetchAccentColor()));
                microwavePresed = true;
            }
            if (filter instanceof CookingTimeFilter){
                CookingTimeFilter cookingTimeFilter = (CookingTimeFilter) filter;
                cookingTimeMinutesRangeBar.setRangePinsByIndices(cookingTimeFilter.getMinMinutes(), cookingTimeFilter.getMaxMinutes());
                cookingTimeMinPin = cookingTimeFilter.getMinMinutes();
                cookingTimeMaxPin = cookingTimeFilter.getMaxMinutes();
            }
            if (filter instanceof DifficultyFilter){
                DifficultyFilter cookingTimeFilter = (DifficultyFilter) filter;
                if (cookingTimeFilter.getDifficultiesList().contains("Baja")){
                    difficultyEasyButton.setBackgroundTintList(ColorStateList.valueOf(fetchAccentColor()));
                    difficultyEasyPressed = true;
                }
                if (cookingTimeFilter.getDifficultiesList().contains("Media")){
                    difficultyMediumButton.setBackgroundTintList(ColorStateList.valueOf(fetchAccentColor()));
                    difficultyMediumPressed = true;
                }
                if (cookingTimeFilter.getDifficultiesList().contains("Alta")){
                    difficultyHardButton.setBackgroundTintList(ColorStateList.valueOf(fetchAccentColor()));
                    difficultyHardPressed = true;
                }

            }
            if (filter instanceof GPSFilter){
                gpsButton.setBackgroundTintList(ColorStateList.valueOf(fetchAccentColor()));
                gpsPresed = true;
            }
        }

        udpateTextViews();

        ovenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!ovenPresed){
                    ovenPresed = true;
                    ovenButton.setBackgroundTintList(ColorStateList.valueOf(fetchAccentColor()));
                }else{
                    ovenPresed = false;
                    ovenButton.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
                }

            }
        });

        microwaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!microwavePresed){
                    microwavePresed = true;
                    microwaveButton.setBackgroundTintList(ColorStateList.valueOf(fetchAccentColor()));
                }else{
                    microwavePresed = false;
                    microwaveButton.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
                }
            }
        });

        cookingTimeMinutesRangeBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue,
                                              String rightPinValue) {
                cookingTimeMinPin = (leftPinIndex < rightPinIndex ? leftPinIndex : rightPinIndex);
                cookingTimeMaxPin = (leftPinIndex < rightPinIndex ? rightPinIndex : leftPinIndex);

                udpateTextViews();
            }

        });

        gpsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!gpsPresed){
                    gpsPresed = true;
                    gpsButton.setBackgroundTintList(ColorStateList.valueOf(fetchAccentColor()));
                    NavigationMenu activity = (NavigationMenu) getActivity();
                    if (activity.getLocation() == null ){
                        gpsButton.setVisibility(View.INVISIBLE);
                        gpsProgress.setVisibility(View.VISIBLE);
                        filterButton.setEnabled(false);
                        activity.createLocations(self);
                    }
                }else{
                    gpsPresed = false;
                    gpsButton.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
                }
            }
        });

        difficultyEasyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!difficultyEasyPressed){
                    difficultyEasyPressed = true;
                    difficultyEasyButton.setBackgroundTintList(ColorStateList.valueOf(fetchAccentColor()));
                }else{
                    difficultyEasyPressed = false;
                    difficultyEasyButton.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
                }
            }
        });

        difficultyMediumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!difficultyMediumPressed){
                    difficultyMediumPressed = true;
                    difficultyMediumButton.setBackgroundTintList(ColorStateList.valueOf(fetchAccentColor()));
                }else{
                    difficultyMediumPressed = false;
                    difficultyMediumButton.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
                }
            }
        });

        difficultyHardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!difficultyHardPressed){
                    difficultyHardPressed = true;
                    difficultyHardButton.setBackgroundTintList(ColorStateList.valueOf(fetchAccentColor()));
                }else{
                    difficultyHardPressed = false;
                    difficultyHardButton.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
                }
            }
        });

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavigationMenu activity = (NavigationMenu) getActivity();
                if (microwavePresed) filterBuilder.addMicrowave();
                if (ovenPresed) filterBuilder.addOven();
                filterBuilder.addCookingTimeFilter(cookingTimeMinPin, cookingTimeMaxPin);
                if (gpsPresed) filterBuilder.addGps(activity.getLocation());
                if (difficultyHardPressed || difficultyEasyPressed ||difficultyMediumPressed){
                    ArrayList<String> list = new ArrayList<String>();
                    if (difficultyEasyPressed) list.add("Baja");
                    if (difficultyMediumPressed) list.add("Media");
                    if (difficultyHardPressed) list.add("Alta");
                    filterBuilder.addDifficulty(list);
                }
                activity.setFilter(filterBuilder.buildFilter());
                Toast.makeText(getContext(), R.string.filter_applied, Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
            }
        });

        List<Integer> itemsId = Arrays.asList(R.id.filters_buttonApply);
        activity.showMenuItems(itemsId);

        return rootView;

    }

    private void initFields() {
        ovenButton.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
        microwaveButton.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
        gpsButton.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
        difficultyEasyButton.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
        difficultyMediumButton.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
        difficultyHardButton.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
        cookingTimeMinutesRangeBar.setTickInterval(1f);
        //TODO Desharcodear el 180 y el 0 ese feo, pasarlo a strings.xml
        cookingTimeMinutesRangeBar.setTickStart(0f);
        cookingTimeMinPin = 0;
        cookingTimeMinutesRangeBar.setTickEnd(180f);
        cookingTimeMaxPin = 180;
    }

    private void udpateTextViews() {
        minCookingTimeMinutesTextView.setText(getResources().getString(R.string.filter_layout_minCookingTimeMinutes) + " " + cookingTimeMinPin + " min.");
        maxCookingTimeMinutesTextView.setText(getResources().getString(R.string.filter_layout_maxCookingTimeMinutes) + " " + cookingTimeMaxPin + " min.");
    }

    public void gpsLoaded(){
        gpsProgress.setVisibility(View.INVISIBLE);
        gpsButton.setVisibility(View.VISIBLE);
        filterButton.setEnabled(true);
    }

    private int fetchAccentColor() {
        TypedValue typedValue = new TypedValue();

        TypedArray a = getContext().obtainStyledAttributes(typedValue.data, new int[] { R.attr.colorAccent });
        int color = a.getColor(0, 0);

        a.recycle();

        return color;
    }
}