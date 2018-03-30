package com.example.root.child;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.iceteck.silicompressorr.SiliCompressor;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileStore;

public class profileActivity extends AppCompatActivity {

    private TextView profileName,profileEmail;
    private ImageView profilePhoto;
    private Button changeImage;
    final static int READ_REQUEST_CODE = 42;
    private StorageReference profilePic;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private Toolbar profileToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
         changeImage = findViewById(R.id.profileImageChange);
         profilePhoto = findViewById(R.id.profileImage);
         profileName = findViewById(R.id.profileName);
         profileEmail = findViewById(R.id.profileEmail);
        profilePic = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("demodatabase");
        profileEmail.setText(mAuth.getCurrentUser().getEmail());
        profileToolbar = findViewById(R.id.ProfileToolbar);
        setSupportActionBar(profileToolbar);
        getSupportActionBar().setTitle("profile");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        databaseReference.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("name").exists())
                    profileName.setText(dataSnapshot.child("name").getValue(String.class));
                if(dataSnapshot.child("imageUrl").exists())
                    Picasso.with(profileActivity.this).load(dataSnapshot.child("imageUrl").getValue(String.class)).into(profilePhoto);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"error occurred",Toast.LENGTH_LONG).show();

            }
        });
        databaseReference.keepSynced(true);
         changeImage.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 performImageSelect();
             }
         });

    }
    public void performImageSelect()
    {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent,READ_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==READ_REQUEST_CODE && resultCode == RESULT_OK){
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setAspectRatio(1,1)
                    .start(profileActivity.this);


        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                final Uri resultUri = result.getUri();
                try {
                    Bitmap imageBitmap = SiliCompressor.with(getApplicationContext()).getCompressBitmap(resultUri.toString());
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                    byte[] data00 = baos.toByteArray();
                    profilePic.child("demo_profilePhotos").child(mAuth.getCurrentUser().getUid()+".jpg").putBytes(data00).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(profileActivity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                                setImage(task.getResult().getDownloadUrl().toString());
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }


    }

    private void setImage(String DownloadUrl) {

        databaseReference.child(mAuth.getCurrentUser().getUid()).child("imageUrl").setValue(DownloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"succesful changes in the datbase",Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getApplicationContext(),"no changes in the datbase",Toast.LENGTH_LONG).show();
                }
            }
        });
        Picasso.with(profileActivity.this).load(DownloadUrl).placeholder(R.drawable.image).into(profilePhoto);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
