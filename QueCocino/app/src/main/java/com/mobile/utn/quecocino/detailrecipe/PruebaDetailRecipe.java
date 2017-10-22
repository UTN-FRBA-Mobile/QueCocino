package com.mobile.utn.quecocino.detailrecipe;

import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mobile.utn.quecocino.R;
import com.mobile.utn.quecocino.model.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PruebaDetailRecipe extends AppCompatActivity implements DetailRecipe.OnFragmentInteractionListener {

    @BindView(R.id.buttonFragment)
    public Button buttonFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba_detail_recipe);
        ButterKnife.bind(this);

        buttonFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                buttonFragment.setVisibility(View.INVISIBLE);

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                DetailRecipe fragment = DetailRecipe.newInstance(new Recipe("r1", "Ensalada Caesar", "Franco Pesce", 45, "http://www.buenapetitopr.com/images/recetas/ensaladas/ensalada_caesar_300x200.png", "Oven"));
                ft.replace(android.R.id.content, fragment, fragment.TAG);
                ft.addToBackStack(null);
                ft.commit();

            }
        });
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}