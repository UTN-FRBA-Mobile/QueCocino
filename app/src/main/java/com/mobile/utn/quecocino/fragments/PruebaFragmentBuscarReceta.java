package com.mobile.utn.quecocino.fragments;



import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import com.mobile.utn.quecocino.R;
import com.mobile.utn.quecocino.detailrecipe.DetailRecipe;
import com.mobile.utn.quecocino.model.Recipe;

import butterknife.ButterKnife;

public class PruebaFragmentBuscarReceta extends AppCompatActivity implements FragmentRecipeSearch.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba_fragment_buscar_receta);
        ButterKnife.bind(this);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment f = new FragmentRecipeSearch();
        ft.replace(R.id.fragment_container, f, "FragmentRecipeSearch");
        ft.addToBackStack(null);
        ft.commit();

        /*Fragment f = new FragmentRecipeSearch();
        android.support.v4.app.FragmentManager manager = PruebaFragmentBuscarReceta.this.getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fragment_container,f);
        transaction.commit();*/
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
