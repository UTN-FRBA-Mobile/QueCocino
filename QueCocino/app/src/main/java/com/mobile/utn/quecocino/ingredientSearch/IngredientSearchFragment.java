package com.mobile.utn.quecocino.ingredientSearch;


import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobile.utn.quecocino.R;
import com.mobile.utn.quecocino.fragments.OnFragmentInteractionCollapse;
import com.mobile.utn.quecocino.menu.NavigationMenu;
import com.mobile.utn.quecocino.model.RecipeIngredient;
import com.mobile.utn.quecocino.recipes.results.RecipesResultsFragment;
import com.mobile.utn.quecocino.utils.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.mobile.utn.quecocino.model.FirebaseReferences.INGREDIENTS_REFERENCE;

public class IngredientSearchFragment extends Fragment {

    @BindView(R.id.ingredientsearch_logo)
    public ImageView logoImaageView;

    @BindView(R.id.ingredientsearch_floating_view)
    public FloatingSearchView ingSearchView;

    @BindView(R.id.ingredientsearch_recyclerview)
    public RecyclerView ingRecyclerView;
    private IngredientsAdapter ingAdapter;

    @BindView(R.id.ingredientsearch_button)
    public Button searchButton;

    private List<RecipeIngredient> allIngredients;
    private FirebaseDatabase database;
    private OnFragmentInteractionCollapse mListener;

    public IngredientSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allIngredients = new ArrayList<>();
        ingAdapter = new IngredientsAdapter(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ingredient_search, container, false);
        ButterKnife.bind(this,view);

        setUpRecyclerView();
        setUpSearchView();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.navigation_container, new RecipesResultsFragment());
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        NavigationMenu activity = (NavigationMenu) getActivity();
        List<Integer> itemsId = Arrays.asList(R.id.navigation_action_buscarRecetas,
                                                    R.id.navigation_action_favoritos,
                                                        R.id.navigation_action_timers);
        activity.showMenuItems(itemsId);

        return view;
    }

    private void setUpRecyclerView(){
        ingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ingRecyclerView.setItemAnimator(new DefaultItemAnimator());
        ingRecyclerView.setAdapter(ingAdapter);
        ingRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(), ingRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view,final int position) {
                ingAdapter.getIngredients().remove(position);
                ingAdapter.notifyDataSetChanged();
                if(ingAdapter.getItemCount()==0){
                    searchButton.setVisibility(View.INVISIBLE);
                    animateToStartPosition();
                }
            }

            @Override
            public void onLongClick(View view, int position) { }
        }));
    }

    private void setUpSearchView(){
        if(!ingAdapter.getIngredients().isEmpty()){
            logoImaageView.setAlpha(0f);
            ingSearchView.setTranslationY(0);
        }

        ingSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {
                Runnable callback = new Runnable() {
                    @Override
                    public void run() {
                        List<SearchSuggestion> filteredSuggestions = new ArrayList<>();

                        //get suggestions based on newQuery
                        if(!newQuery.equals("")) {
                            RecipeIngredient ingredient;
                            for (int i=0; i<allIngredients.size() && i<3; i++ ) {
                                ingredient = allIngredients.get(i);
                                if(!ingAdapter.getIngredients().contains(ingredient))
                                if(allIngredients.get(i).getDescription().toLowerCase().startsWith(newQuery.toLowerCase())) {
                                    final String body = ingredient.getDescription();
                                    filteredSuggestions.add(new SearchSuggestion() {
                                        @Override
                                        public String getBody() {
                                            return body;
                                        }

                                        @Override
                                        public int describeContents() {
                                            return 0;
                                        }

                                        @Override
                                        public void writeToParcel(Parcel dest, int flags) {

                                        }
                                    });
                                }
                            }
                        }

                        //pass them on to the search view
                        ingSearchView.swapSuggestions(filteredSuggestions);
                    }
                };

                if(allIngredients.isEmpty()) {
                    loadIngredients(callback);
                } else {
                    callback.run();
                }
            }
        });

        ingSearchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {
                logoImaageView.animate().alpha(0).start();
                ingSearchView.animate().translationY(0).start();
                NavigationMenu activity = (NavigationMenu) getActivity();
                activity.getCollapsingToolbar().setVisibility(View.GONE);
                activity.getSupportActionBar().hide();
            }

            @Override
            public void onFocusCleared() {
                NavigationMenu activity = (NavigationMenu) getActivity();
                activity.getSupportActionBar().show();
                activity.getCollapsingToolbar().setVisibility(View.VISIBLE);
                if(ingAdapter.getItemCount()==0) {
                    animateToStartPosition();
                }
                ingSearchView.clearQuery();
            }
        });

        ingSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(final SearchSuggestion searchSuggestion) {
                RecipeIngredient ing = new RecipeIngredient(searchSuggestion.getBody());
                ingAdapter.getIngredients().add(0, ing);
                ingAdapter.notifyItemInserted(0);
                ingRecyclerView.smoothScrollToPosition(0);
                ingSearchView.clearQuery();
                searchButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSearchAction(String query) {
                Toast.makeText(getContext(),"onSearchAction!",Toast.LENGTH_SHORT).show();
            }
        });

        ingSearchView.setOnSuggestionsListHeightChanged(new FloatingSearchView.OnSuggestionsListHeightChanged() {
            @Override
            public void onSuggestionsListHeightChanged(float newHeight) {
                ingRecyclerView.setTranslationY(newHeight);
            }
        });
    }

    private void animateToStartPosition(){
        float distance = getResources().getDimensionPixelSize(R.dimen.ingredient_search_view_trastation);
        ingSearchView.animate().translationYBy(distance).start();
        logoImaageView.animate().alpha(1).start();
    }

    private void loadIngredients(final Runnable callback){
        ingSearchView.showProgress();
        database = FirebaseDatabase.getInstance();
        database.getReference(INGREDIENTS_REFERENCE).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allIngredients.removeAll(allIngredients);
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    allIngredients.add(snapshot.getValue(RecipeIngredient.class));
                    ingSearchView.hideProgress();
                    callback.run();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
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
