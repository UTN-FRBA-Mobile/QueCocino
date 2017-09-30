package com.mobile.utn.quecocino.recipes.results;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobile.utn.quecocino.R;
import com.mobile.utn.quecocino.model.Recipe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.mobile.utn.quecocino.recipegallery.objects.FirebaseReferences.RECIPE_REFERENCE;

public class RecipesResults extends AppCompatActivity {

    @BindView(R.id.recipesRecyclerView)
    public RecyclerView recyclerView;

    private RecipesResultsAdapter adapter;
    private FirebaseDatabase database;

    List<Recipe> recipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_results);
        ButterKnife.bind(this);

        recipes = new ArrayList<Recipe>();
        adapter = new RecipesResultsAdapter(getApplicationContext(), recipes);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        database = FirebaseDatabase.getInstance();
        database.getReference(RECIPE_REFERENCE).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                recipes.removeAll(recipes);
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Recipe recipe= snapshot.getValue(Recipe.class);
                    recipes.add(recipe);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
