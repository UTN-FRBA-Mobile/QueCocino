package com.mobile.utn.quecocino.detailrecipe;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobile.utn.quecocino.R;
import com.mobile.utn.quecocino.model.RecipeInstruction;
import com.mobile.utn.quecocino.recipegallery.activities.RecipeGallery;
import com.mobile.utn.quecocino.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.mobile.utn.quecocino.model.FirebaseReferences.INSTRUCTION_RECIPE_REFERENCE;

public class DetailRecipe extends Fragment {

    public static final String TAG = "DetailRecipeFragment";

    private Recipe recipe;
    private OnFragmentInteractionListener mListener;
    private InstructionAdapter instructionAdapter;

    public ImageView mainImageView;
    public TextView recipeTitleTextView;
    public TextView recipeAuthorTextView;
    public TextView recipeCookingTimeTextView;
    public ImageView recipeApplianceImg;
    public TextView recipeApplianceTextView;
    public ListView recipeInstuctionListView;
    private List<RecipeInstruction> recipeInstructions;
    private FirebaseDatabase database;

    public DetailRecipe() {}

    public static DetailRecipe newInstance(Recipe recipe) {
        DetailRecipe fragment = new DetailRecipe();
        Bundle args = new Bundle();
        args.putSerializable("recipe", recipe);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            recipe = (Recipe) getArguments().getSerializable("recipe");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_recipe, container, false);

        mainImageView = (ImageView) view.findViewById(R.id.detailRecipe_mainImage);
        recipeTitleTextView = (TextView) view.findViewById(R.id.detailRecipe_titleRecipe);
        recipeAuthorTextView = (TextView) view.findViewById(R.id.detailRecipe_authorRecipe);
        recipeCookingTimeTextView = (TextView) view.findViewById(R.id.detailRecipe_cookingTimeRecipe);
        recipeApplianceImg = (ImageView) view.findViewById(R.id.detailRecipe_applianceImg);
        recipeApplianceTextView = (TextView) view.findViewById(R.id.detailRecipe_applianceRecipe);
        recipeInstuctionListView = (ListView) view.findViewById(R.id.detailRecipe_instructionsList);

        recipeInstructions = new ArrayList<>();
        instructionAdapter = new InstructionAdapter(getActivity(), recipeInstructions);

        getActivity().setTitle(recipe.getTitle());
        recipeTitleTextView.setText(recipe.getTitle());
        recipeAuthorTextView.setText(" por " + recipe.getAuthor());
        Picasso.with(getActivity().getApplicationContext()).load(recipe.getMainImage()).resize(1100, 600).into(mainImageView);
        recipeCookingTimeTextView.setText(recipe.getCookingTimeMinutes() + "Min.");

        String appliance = recipe.getApplianceCooking();
        recipeApplianceTextView.setText(appliance);

        if(appliance.equals("Oven"))
            Picasso.with(getActivity().getApplicationContext()).load(R.drawable.detailrecipe_oven).into(recipeApplianceImg);
        else
            Picasso.with(getActivity().getApplicationContext()).load(R.drawable.detailrecipe_microwave).into(recipeApplianceImg);

        mainImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RecipeGallery.class);
                intent.putExtra("idReceta", recipe.getIdRecipe());
                intent.putExtra("titleReceta",recipe.getTitle());
                getActivity().startActivity(intent);
            }
        });

        database = FirebaseDatabase.getInstance();
        database.getReference(INSTRUCTION_RECIPE_REFERENCE).child(recipe.getIdRecipe()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                recipeInstructions.removeAll(recipeInstructions);
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    RecipeInstruction instruction = new RecipeInstruction();
                    instruction.setDescription(snapshot.getValue(RecipeInstruction.class).getDescription());
                    recipeInstructions.add(instruction);
                }
                instructionAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        recipeInstuctionListView.setAdapter(instructionAdapter);

        return view;
    }

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
