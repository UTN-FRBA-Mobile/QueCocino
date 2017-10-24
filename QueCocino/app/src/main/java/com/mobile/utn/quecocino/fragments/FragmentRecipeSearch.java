package com.mobile.utn.quecocino.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobile.utn.quecocino.R;
import com.mobile.utn.quecocino.model.RecipeIngredient;

import static com.mobile.utn.quecocino.model.FirebaseReferences.INGREDIENT_REFERENCE;

import java.util.List;
import java.util.ArrayList;


public class FragmentRecipeSearch extends Fragment {


    private FirebaseDatabase database;
    private List<String> ingredients;
    private ArrayAdapter<String> ingredientAdapter;

    private OnFragmentInteractionListener mListener;

    public FragmentRecipeSearch() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static FragmentRecipeSearch newInstance(String param1, String param2) {
        FragmentRecipeSearch fragment = new FragmentRecipeSearch();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Button addIngredientButton = (Button) this.getView().findViewById(R.id.addIngredientButton);
        addIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        AutoCompleteTextView textView = (AutoCompleteTextView) this.getView().findViewById(R.id.editText);
        ingredients = new ArrayList<>();
        ingredientAdapter = new ArrayAdapter<>(getActivity(), R.layout.list_ingredient, ingredients);

        database = FirebaseDatabase.getInstance();
        database.getReference(INGREDIENT_REFERENCE).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    RecipeIngredient ingredient = new RecipeIngredient();
                    ingredient.setDescription(snapshot.getValue(RecipeIngredient.class).getDescription());
                    ingredients.add(ingredient.getDescription());
                }
                ingredientAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        textView.setAdapter(ingredientAdapter);
        return inflater.inflate(R.layout.fragment_recipe_search, container, false);
        //TextView textView = new TextView(getActivity());
        //textView.setText(R.string.hello_blank_fragment);
        //return textView;
    }

    public void addIngredient(View view)
    {
        EditText ingredientText = (EditText) view.findViewById(R.id.ingredientToAddText);

        ingredientText.getText();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
