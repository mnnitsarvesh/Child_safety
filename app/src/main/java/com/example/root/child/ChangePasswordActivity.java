package com.example.root.child;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
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

public class ChangePasswordActivity extends AppCompatActivity {
    private Button changeSubmit;
    private EditText oldPassword,newPassword,ConfPassword;
    private  FirebaseAuth mAuth;
    private Toolbar mToolbar;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        changeSubmit = findViewById(R.id.changeSubmit);
        newPassword = findViewById(R.id.changeNew);
        oldPassword = findViewById(R.id.changeOld);
        ConfPassword = findViewById(R.id.changeConf);
        mToolbar=findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mAuth = FirebaseAuth.getInstance();
        changeSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValid()){
                    final DatabaseReference demodatabase = FirebaseDatabase.getInstance().getReference("demodatabase");
                    demodatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        String newPassword = ConfPassword.getText().toString().trim();
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Adduser user = snapshot.getValue(Adduser.class);
                                if(user.email.equals(mAuth.getCurrentUser().getEmail()))
                                {
                                    if(ChangepasswordAuthTable(user))
                                        Toast.makeText(getApplicationContext(), "succesfully uploaded", Toast.LENGTH_LONG).show();
                                    snapshot.getRef().removeValue();
                                    Adduser adduser = new Adduser(user.getName(),user.getEmail(),newPassword,user.getGender(),user.getUserType(),user.getImageUrl());
                                    demodatabase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(adduser);

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
        if(oldPassword.getText().toString().trim().isEmpty())
        {
            Toast.makeText(getApplicationContext(),"Enter the old password",Toast.LENGTH_LONG).show();
            return false;
        }
        if(ConfPassword.toString().length()<6)
        {
            Toast.makeText(getApplicationContext(),"Password is too small",Toast.LENGTH_LONG).show();
            return false;
        }
        if(!TextUtils.equals(ConfPassword.getText().toString().trim(),newPassword.getText().toString().trim())){
            Toast.makeText(getApplicationContext(),"password didnt match",Toast.LENGTH_LONG).show();
            return  false;
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
                    String newpass= ConfPassword.getText().toString().trim();
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

}
