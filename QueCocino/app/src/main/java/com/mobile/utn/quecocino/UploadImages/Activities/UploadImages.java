package com.mobile.utn.quecocino.UploadImages.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mobile.utn.quecocino.R;

import butterknife.ButterKnife;
import butterknife.BindView;


public class UploadImages extends AppCompatActivity {

    @BindView(R.id.uploadButton)
    public Button uploadButton;

    @BindView(R.id.image0)
    public ImageView image0;

    private StorageReference mStorage;
    private ProgressDialog progressDialog;

    private static final int GALLERY_INTENT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_images);
        ButterKnife.bind(this);

        progressDialog = new ProgressDialog(this);
        mStorage = FirebaseStorage.getInstance().getReference();

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK){
            progressDialog.setTitle(R.string.progressUploadImage_title);
            progressDialog.setCancelable(false);
            progressDialog.show();

            Uri uri = data.getData();
            StorageReference filePath = mStorage.child("images").child(uri.getLastPathSegment());
            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(UploadImages.this, R.string.uploadSuccess, Toast.LENGTH_SHORT).show();

                    @SuppressWarnings("VisibleForTests")Uri uriImage0 = taskSnapshot.getDownloadUrl();

                    Glide.with(UploadImages.this)
                            .load(uriImage0)
                            .fitCenter()
                            .centerCrop()
                            .into(image0);
                }
            });

        }
    }
}
