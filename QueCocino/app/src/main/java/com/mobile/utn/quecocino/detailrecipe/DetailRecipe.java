package com.mobile.utn.quecocino.detailrecipe;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.mobile.utn.quecocino.fragments.OnFragmentInteractionCollapse;
import com.mobile.utn.quecocino.menu.NavigationMenu;
import com.mobile.utn.quecocino.model.RecipeIngredient;
import com.mobile.utn.quecocino.model.RecipeInstruction;
import com.mobile.utn.quecocino.recipegallery.activities.RecipeGallery;
import com.mobile.utn.quecocino.model.Recipe;
import com.mobile.utn.quecocino.recipes.RecipeUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.mobile.utn.quecocino.model.FirebaseReferences.INGREDIENT_REFERENCE;
import static com.mobile.utn.quecocino.model.FirebaseReferences.INSTRUCTION_RECIPE_REFERENCE;

public class DetailRecipe extends Fragment {

    public static final String TAG = "DetailRecipeFragment";

    private Recipe recipe;
    private OnFragmentInteractionCollapse mListener;
    private InstructionAdapter instructionAdapter;
    private IngredientAdapter ingredientAdapter;

    public ImageView mainImageView;
    public TextView recipeAuthorTextView;
    public TextView recipeCookingTimeTextView;
    public ImageView recipeApplianceImg;
    public TextView recipeApplianceTextView;
    public ListView recipeInstuctionListView;
    public ListView recipeIngredientListView;
    public ImageView recipeDifficultyImg;
    public TextView recipeDifficultyTextView;
    public ImageView recipeKcalImg;
    public TextView recipeKcalTextView;
    public List<RecipeInstruction> recipeInstructions;
    public List<RecipeIngredient> recipeIngredients;
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

        mainImageView = (ImageView) getActivity().findViewById(R.id.navigation_mainImage);
        recipeAuthorTextView = (TextView) getActivity().findViewById(R.id.navigation_authorRecipe);
        recipeCookingTimeTextView = (TextView) view.findViewById(R.id.detailRecipe_cookingTimeRecipe);
        recipeApplianceImg = (ImageView) view.findViewById(R.id.detailRecipe_applianceImg);
        recipeApplianceTextView = (TextView) view.findViewById(R.id.detailRecipe_applianceRecipe);
        recipeInstuctionListView = (ListView) view.findViewById(R.id.detailRecipe_instructionsList);
        recipeIngredientListView = (ListView) view.findViewById(R.id.detailRecipe_ingredientsList);
        recipeDifficultyImg = (ImageView) view.findViewById(R.id.detailRecipe_difficultyImg);;
        recipeDifficultyTextView = (TextView) view.findViewById(R.id.detailRecipe_difficultyRecipe);;
        recipeKcalImg = (ImageView) view.findViewById(R.id.detailRecipe_kcalImg);;
        recipeKcalTextView = (TextView) view.findViewById(R.id.detailRecipe_kcalRecipe);;

        recipeInstructions = new ArrayList<>();
        recipeIngredients = new ArrayList<>();
        instructionAdapter = new InstructionAdapter(getActivity(), recipeInstructions);
        ingredientAdapter = new IngredientAdapter(getActivity(), recipeIngredients);

        recipeAuthorTextView.setText(" por " + recipe.getAuthor());
        Picasso.with(getActivity().getApplicationContext()).load(recipe.getMainImage()).resize(1100, 600).into(mainImageView);
        recipeCookingTimeTextView.setText(recipe.getCookingTimeMinutes() + "Min.");

        String appliance = recipe.getApplianceCooking();
        recipeApplianceTextView.setText(appliance);

        if(appliance.equals("Horno"))
            Picasso.with(getActivity().getApplicationContext()).load(R.drawable.recipe_oven).into(recipeApplianceImg);
        else
            Picasso.with(getActivity().getApplicationContext()).load(R.drawable.recipe_microwave).into(recipeApplianceImg);

        String difficulty = recipe.getDifficulty();
        recipeDifficultyTextView.setText(difficulty);

        if (difficulty.equals("Baja")){
            Picasso.with(getActivity().getApplicationContext()).load(R.drawable.recipe_difficultyeasy).into(recipeDifficultyImg);
        }else if(difficulty.equals("Media")){
            Picasso.with(getActivity().getApplicationContext()).load(R.drawable.recipe_difficultymedium).into(recipeDifficultyImg);
        }else if(difficulty.equals("Alta")){
            Picasso.with(getActivity().getApplicationContext()).load(R.drawable.recipe_difficultyhard).into(recipeDifficultyImg);
        }

        recipeKcalTextView.setText(recipe.getKcal() + "Kcal.");

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
                    instruction.setTime(snapshot.getValue(RecipeInstruction.class).getTime());
                    instruction.setTag(snapshot.getValue(RecipeInstruction.class).getTag());
                    recipeInstructions.add(instruction);
                }
                instructionAdapter.notifyDataSetChanged();
                RecipeUtils.setListViewHeightBasedOnChildren(recipeInstuctionListView);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        database.getReference(INGREDIENT_REFERENCE).child(recipe.getIdRecipe()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                recipeIngredients.removeAll(recipeIngredients);
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    RecipeIngredient ingredient = new RecipeIngredient();
                    ingredient.setDescription(snapshot.getValue(RecipeIngredient.class).getDescription());
                    recipeIngredients.add(ingredient);
                }
                ingredientAdapter.notifyDataSetChanged();
                RecipeUtils.setListViewHeightBasedOnChildren(recipeIngredientListView);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        recipeInstuctionListView.setAdapter(instructionAdapter);
        recipeIngredientListView.setAdapter(ingredientAdapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mListener.enableCollapse();
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionCollapse) {
            mListener = (OnFragmentInteractionCollapse) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionCollapse");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(recipe != null)
            ((NavigationMenu) getActivity()).getCollapsingToolbar().setTitle(recipe.getTitle());
    }

}
