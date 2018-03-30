package com.example.root.child;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ViewChild extends AppCompatActivity {

    private TextView viewName,viewEmail;
    private ImageView viewImage;
    private Button sendRequest,cancelRequest;
    private FirebaseAuth mAuth;
    private DatabaseReference request;
    private Toolbar toolbarview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        final String demo[] = bundle.getStringArray("userInfo");
        setContentView(R.layout.activity_view_child);
        viewEmail = findViewById(R.id.viewChildEmail);
        viewName = findViewById(R.id.viewChildName);
        viewImage = findViewById(R.id.viewChildImage);
        sendRequest = findViewById(R.id.sentRequest);
        cancelRequest = findViewById(R.id.cancelRequest);
        toolbarview = findViewById(R.id.viewChildToolar);
        setSupportActionBar(toolbarview);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        request = FirebaseDatabase.getInstance().getReference("demorequest");
        mAuth = FirebaseAuth.getInstance();
        viewName.setText(demo[0]);
        viewEmail.setText(demo[1]);
        Picasso.with(ViewChild.this).load(demo[2]).into(viewImage);
        sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> str = new ArrayList<>();
                str.add(demo[0]);
                str.add(demo[1]);
                str.add(demo[2]);
                requestDetails demoadd =  new requestDetails(str);
                request.child(mAuth.getCurrentUser().getUid()).setValue(demoadd);
            }
        });
        cancelRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
