package com.example.root.child;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Request extends AppCompatActivity {
    private Toolbar requestToolbar;
    private RecyclerView requestList;
    private ArrayList<SingleUserclass> demo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        requestList = findViewById(R.id.requstList);
        requestToolbar = findViewById(R.id.requestToobar);
        setSupportActionBar(requestToolbar);
        getSupportActionBar().setTitle("Request");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        demo = new ArrayList<>();
        requestList.setLayoutManager(new LinearLayoutManager(this));
        requestList.setHasFixedSize(true);
        synchronized (this) {
            FirebaseDatabase.getInstance().getReference("demodatabase").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Adduser user = snapshot.getValue(Adduser.class);
                        if(snapshot.child("imageUrl").exists())
                            demo.add(new SingleUserclass(user.getName(), user.getEmail(), user.getImageUrl()));
                        else
                            demo.add(new SingleUserclass(user.getName(), user.getEmail(), "Default"));

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();

                }
            });

            requestList.setAdapter(new userAdapter(demo, new userAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(SingleUserclass item) {
                    Intent intent = new Intent(Request.this,RequestResult.class);
                    String[] data = new String[3];
                    data[0] = item.getName();
                    data[1] = item.getEmail();
                    data[2] = item.getImage();
                    intent.putExtra("userInfo",data);
                    startActivity(intent);
                }
            }));
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();

    }
}
