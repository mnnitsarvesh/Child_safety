package com.example.root.child;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DeleteActivity extends AppCompatActivity {
    private EditText delEmail,delConf;
    private Toolbar delToolbar;
    private Button delAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        Bundle bundle = getIntent().getExtras();
        final String passWord = bundle.getString("password");
        delConf = findViewById(R.id.delConf);
        delEmail = findViewById(R.id.delEmail);
        delToolbar = findViewById(R.id.delToolbar);
        delAccount = findViewById(R.id.delAccount);
        setSupportActionBar(delToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        delAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {
                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), passWord);
                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        synchronized (this) {
                                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("demodatabase");
                                            reference.child(user.getUid()).setValue(null);
                                            Intent intent = new Intent(DeleteActivity.this, MainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                        }
                                    }
                                }
                            });
                        }
                    });
                }
            }
        });
    }
    public boolean isValid()
    {
        if(delEmail.getText().toString().trim().isEmpty())
        {
            Toast.makeText(getApplicationContext(),"enter the email",Toast.LENGTH_LONG).show();
            return  false;
        }
        if(!delConf.getText().toString().trim().equals("CONFIRM"))
        {
            Toast.makeText(getApplicationContext(),"Enter Confirm in the confirm field",Toast.LENGTH_LONG).show();
            return false;
        }
        if(!FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(delEmail.getText().toString().trim())){
            Toast.makeText(getApplicationContext(),"Enter your email",Toast.LENGTH_LONG).show();
            return false;
        }
        return  true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
