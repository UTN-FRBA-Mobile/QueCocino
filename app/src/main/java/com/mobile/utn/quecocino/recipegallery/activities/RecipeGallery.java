package com.mobile.utn.quecocino.recipegallery.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mobile.utn.quecocino.R;
import com.mobile.utn.quecocino.model.RecipeImage;
import com.mobile.utn.quecocino.recipegallery.adapters.RecyclerAdapter;
import com.mobile.utn.quecocino.utils.RecyclerTouchListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.BindView;

import static com.mobile.utn.quecocino.model.FirebaseReferences.IMAGE_RECIPE_REFERENCE;


public class RecipeGallery extends AppCompatActivity {

    @BindView(R.id.gallery_rvImages)
    public RecyclerView rv;

    @BindView(R.id.gallery_fabtakepicture)
    public FloatingActionButton fabTakePicture;

    @BindView(R.id.gallery_fabinsertphoto)
    public FloatingActionButton fabInsertPhoto;

    @BindView(R.id.gallery_fabmenu)
    public FloatingActionMenu fabMenu;

    private String idReceta;
    private List<RecipeImage> images;
    private RecyclerAdapter adapter;
    private StorageReference mStorage;
    private ProgressDialog progressDialog;
    private static final int GALLERY_INTENT = 1;
    private static final int CAMERA_INTENT = 0;
    private FirebaseDatabase database;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 0;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        ButterKnife.bind(this);

        setTitle(getIntent().getStringExtra("titleReceta"));

        idReceta = getIntent().getStringExtra("idReceta");
        images = new ArrayList<>();
        adapter = new RecyclerAdapter(getApplicationContext(), images);

        rv.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        rv.setAdapter(adapter);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), rv, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("images", (Serializable) images);
                bundle.putInt("position", position);

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                newFragment.setArguments(bundle);
                newFragment.show(ft, "slideshow");
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        database = FirebaseDatabase.getInstance();
        database.getReference(IMAGE_RECIPE_REFERENCE).child(idReceta).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                images.removeAll(images);
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    RecipeImage image = new RecipeImage();
                    image.setAuthor(snapshot.getValue(RecipeImage.class).getAuthor());
                    image.setUrl(snapshot.getValue(RecipeImage.class).getUrl());
                    images.add(image);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        progressDialog = new ProgressDialog(this);
        mStorage = FirebaseStorage.getInstance().getReference();

        fabTakePicture.setOnClickListener(onButtonClick());
        fabInsertPhoto.setOnClickListener(onButtonClick());

    }

    private View.OnClickListener onButtonClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == fabTakePicture) {
                    openCamera();
                } else if (view == fabInsertPhoto) {
                    openGallery();
                }
                fabMenu.close(true);
            }
        };
    }

    private void openCamera() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_CAMERA);

        }else{
            startCamera();
        }
    }

    private void startCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_INTENT);
    }

    private void openGallery() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

        }else{
            startGallery();
        }

    }

    private void startGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_INTENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){

            if(requestCode == GALLERY_INTENT){

                startProgressDialog();

                Uri uri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    pushImage(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }else if(requestCode == CAMERA_INTENT){

                startProgressDialog();
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                pushImage(bitmap);

            }

        }
    }

    private void pushImage(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);

        String nameFile = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        StorageReference filePath = mStorage.child(idReceta + "_images").child(nameFile);
        filePath.putBytes(stream.toByteArray()).addOnSuccessListener(getOnSuccessListener());
    }


    private OnSuccessListener<UploadTask.TaskSnapshot> getOnSuccessListener() {
        return new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                @SuppressWarnings("VisibleForTests")Uri uri = taskSnapshot.getMetadata().getDownloadUrl();

                RecipeImage image = new RecipeImage("Franco Pesce", uri.toString());
                database.getReference(IMAGE_RECIPE_REFERENCE).child(idReceta).push().setValue(image);

                progressDialog.dismiss();
                Toast.makeText(RecipeGallery.this, R.string.toast_uploadSuccess, Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void startProgressDialog() {
        String strProgress = getResources().getString(R.string.progress_uploadImage);
        progressDialog.setMessage(strProgress);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startGallery();
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startCamera();
                }
                return;
            }
        }
    }

}
