package com.example.root.child;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SettingActivity extends AppCompatActivity {

    private TextView settingUser,settingEmail;
    private Toolbar settingToolbar;
    private ImageView imageView;
    private FirebaseAuth mAuth;
    private String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        settingToolbar = findViewById(R.id.settingtoolbar);
        imageView = findViewById(R.id.settingImage);
        settingUser = findViewById(R.id.settingUser);
        settingEmail= findViewById(R.id.settingEmail);
        mAuth = FirebaseAuth.getInstance();
        setSupportActionBar(settingToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        FirebaseDatabase.getInstance().getReference().child("demodatabase")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Adduser user = snapshot.getValue(Adduser.class);
                            if (user.email.equals(mAuth.getCurrentUser().getEmail())) {
                                settingUser.setText(user.getName());
                                settingEmail.setText(user.getEmail());
                                setPassword(user);
                                if (user.imageUrl != null)
                                    Picasso.with(SettingActivity.this).load(user.getImageUrl()).into(imageView);
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });


    }
    public void Account(View view)
    {
        Intent intent=new Intent(SettingActivity.this,profileActivity.class);
        startActivity(intent);
    }
    public void ChangePasswd(View view)
    {
        Intent intent=new Intent(SettingActivity.this,ChangePasswordActivity.class);
        startActivity(intent);
    }
    public void DeleteAccount(View view)
    {
        Intent intent=new Intent(SettingActivity.this,DeleteActivity.class);
        intent.putExtra("password",password);
        startActivity(intent);
    }
    public void Time(View view)
    {
        Intent intent=new Intent(SettingActivity.this,TimeActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
    public void setPassword(Adduser user)
    {
        password = user.getPassword();
    }

}
