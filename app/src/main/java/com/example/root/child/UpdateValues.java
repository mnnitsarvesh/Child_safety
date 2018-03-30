package com.example.root.child;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class UpdateValues extends AppCompatActivity {

    private  String UserEmail;
    private EditText userName,userPassword,changeEmail,changeConfirmPassword;
    private Button update;
    private ImageView settingImage;
    Toolbar updateToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_values);
        UserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        changeEmail = findViewById(R.id.updateEmail);
        userName = findViewById(R.id.ChangeuserName);
        userPassword = findViewById(R.id.changePassword);
        changeConfirmPassword = findViewById(R.id.changeConfirmPassword);
        update = findViewById(R.id.updateButton);
        settingImage = findViewById(R.id.settingImage);
        updateToolbar = findViewById(R.id.UpdateChanges);
        setSupportActionBar(updateToolbar);
        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        FirebaseDatabase.getInstance().getReference().child("demodatabase")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Adduser user = snapshot.getValue(Adduser.class);
                            if(user.email.equals(UserEmail))
                            {
                                userName.setText(user.getName());
                                changeEmail.setText(UserEmail);
                                if(user.imageUrl!=null)
                                    Picasso.with(UpdateValues.this).load(user.getImageUrl()).into(settingImage);
                                break;
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValid())
                {
                    final DatabaseReference demodatabase = FirebaseDatabase.getInstance().getReference("demodatabase");
                    demodatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                String newPassword = changeConfirmPassword.getText().toString().trim();
                                String newName = userName.getText().toString().trim();
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        Adduser user = snapshot.getValue(Adduser.class);
                                        if(user.email.equals(UserEmail))
                                        {
                                            snapshot.getRef().removeValue();
                                            Adduser adduser = new Adduser(newName,user.getEmail(),newPassword,user.getGender(),user.getUserType(),user.getImageUrl());
                                            demodatabase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(adduser);
                                            if(ChangepasswordAuthTable(user))
                                            Toast.makeText(getApplicationContext(), "succesfully uploaded", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Toast.makeText(getApplicationContext(),"database error",Toast.LENGTH_LONG).show();
                                }
                            });



                }

            }
        });

    }

    public boolean isValid()
    {
        if(changeConfirmPassword.toString().length()<6)
        {
            Toast.makeText(getApplicationContext(),"Password is too small",Toast.LENGTH_LONG).show();
            return false;
        }
        if(!TextUtils.equals(changeConfirmPassword.getText().toString().trim(),userPassword.getText().toString().trim())){
            Toast.makeText(getApplicationContext(),"password didnt match",Toast.LENGTH_LONG).show();
            return  false;
        }
        if (TextUtils.isEmpty(userName.getText().toString().trim())) {
            Toast.makeText(getApplicationContext(),"user name must be entered",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


    public boolean ChangepasswordAuthTable(Adduser adduser){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        AuthCredential credential = EmailAuthProvider
                .getCredential(adduser.getEmail(),adduser.getPassword());

// Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    String newpass= changeConfirmPassword.getText().toString().trim();
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.updatePassword(newpass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        //authc changed
                                        Toast.makeText(getApplicationContext(),"password changed sucess",Toast.LENGTH_LONG).show();
                                    } else {
                                        //not updates
                                        Toast.makeText(getApplicationContext(),"password changed not sucess",Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                }
                            });
                        } else {
                            //auth failed
                            Toast.makeText(getApplicationContext(),"kuch to gadbad h bhai",Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                });
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
