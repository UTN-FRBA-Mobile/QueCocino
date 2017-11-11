package com.mobile.utn.quecocino.recipes.results;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobile.utn.quecocino.R;
import com.mobile.utn.quecocino.detailrecipe.DetailRecipe;
import com.mobile.utn.quecocino.fragments.OnFragmentInteractionCollapse;
import com.mobile.utn.quecocino.menu.NavigationMenu;
import com.mobile.utn.quecocino.model.Recipe;
import com.mobile.utn.quecocino.recipes.filter.FavoriteFilter;
import com.mobile.utn.quecocino.recipes.filter.PopularFilter;
import com.mobile.utn.quecocino.recipes.filter.TrivialFilter;
import com.mobile.utn.quecocino.utils.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mobile.utn.quecocino.model.FirebaseReferences.RECIPE_REFERENCE;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipesResultsFragment extends Fragment {

    public RecyclerView recyclerView;

    private RecipesResultsAdapter adapter;
    private FirebaseDatabase database;
    private OnFragmentInteractionCollapse mListener;

    List<Recipe> recipes;

    public RecipesResultsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_recipes_results, container, false);
        getActivity().setTitle("Que Cocino");

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recipesRecyclerView);

        recipes = new ArrayList<Recipe>();
        adapter = new RecipesResultsAdapter(getActivity().getApplicationContext(), recipes);


        // 2. set layoutManger
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Recipe recipe = recipes.get(position);

                NavigationMenu activity = (NavigationMenu) getActivity();
                activity.setCurrentRecipe(recipe);
                activity.refreshFavoriteIcon();

                FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                DetailRecipe newFragment = DetailRecipe.newInstance(recipe);
                ft.replace(R.id.navigation_container, newFragment);
                ft.addToBackStack(null);
                ft.commit();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        database = FirebaseDatabase.getInstance();
        database.getReference(RECIPE_REFERENCE).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                recipes.removeAll(recipes);
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Recipe recipe = snapshot.getValue(Recipe.class);
                    recipe.setIdRecipe(snapshot.getKey());
                    recipes.add(recipe);
                }

                NavigationMenu activity = (NavigationMenu) getActivity();
                //activity.setFilter(new TrivialFilter());

                Bundle args = RecipesResultsFragment.this.getArguments();
                if(args!=null) {
                    String page = args.containsKey("page") ? args.getString("page") : "";
                    switch (page){
                        case "popular":
                            activity.setFilter(new PopularFilter());
                            activity.setLastPage(page);
                            break;
                        case "favorites":
                            activity.setFilter(new FavoriteFilter(getContext()));
                            activity.setLastPage(page);
                            break;
                        case "search":
                            if (!page.equalsIgnoreCase(activity.getLastPage()))activity.setFilter(new TrivialFilter());
                            activity.setLastPage(page);
                            break;
                        default:
                            break;
                    }
                }

                recipes = activity.getFilter().meetCriteria(RecipesResultsFragment.this.recipes);
                adapter.setRecipes(recipes);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        NavigationMenu activity = (NavigationMenu) getActivity();
        List<Integer> itemsId = Arrays.asList(R.id.recipeResults_buttonFilters,
                                                R.id.navigation_action_buscarRecetas,
                                                    R.id.navigation_action_favoritos,
                                                        R.id.navigation_action_timers);
        activity.showMenuItems(itemsId);

        return rootView;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mListener.disableCollapse();
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
}
