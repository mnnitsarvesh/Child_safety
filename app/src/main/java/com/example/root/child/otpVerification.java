package com.example.root.child;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;
import java.util.HashMap;

public class otpVerification extends AppCompatActivity {
    Button verify;
    ArrayList <String> userData;
    DatabaseReference demodatabase;
    EditText mEmail,mPassword;
    FirebaseAuth mAuth;
    ImageView signUpImage;
    String imageURL;
    Uri imageuri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        userData = bundle.getStringArrayList("databalue");
        setContentView(R.layout.activity_otp_verification);
        verify = findViewById(R.id.verify);
        mEmail = findViewById(R.id.number);
        mPassword = findViewById(R.id.otp);
        mEmail.setText(userData.get(1));
        mPassword.setText(userData.get(2));
        demodatabase = FirebaseDatabase.getInstance().getReference().child("demodatabase");
        mAuth = FirebaseAuth.getInstance();
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email,password;
                email=mEmail.getText().toString();
                password=mPassword.getText().toString();
                if(isVerified()) {
                    mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String id = mAuth.getCurrentUser().getUid();
                                    Adduser adduser = new Adduser(userData.get(0), userData.get(1), userData.get(2), userData.get(3), userData.get(4), null);
                                    demodatabase.child(id).setValue(adduser);
                                    Toast.makeText(getApplicationContext(), "succesfully uploaded", Toast.LENGTH_LONG).show();
                                    ConncectActivity();
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "not sucessfull", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Enter details",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    public void ConncectActivity()
    {
        Intent intent = new Intent(otpVerification.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
    public boolean isVerified()
    {
        if(TextUtils.isEmpty(mEmail.getText().toString().trim()))
        {
            return  false;
        }
        if(TextUtils.isEmpty(mPassword.getText().toString().trim()))
        {
            return false;
        }
        return true;
    }


}
