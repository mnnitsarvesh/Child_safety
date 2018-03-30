package com.example.root.child;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddChild extends AppCompatActivity {

    private RecyclerView mSingleUserList;
    private Toolbar toolbar;
    private EditText searchView;
    private Button search;
    private ArrayList<SingleUserclass> demo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child);
        toolbar = findViewById(R.id.addchildToolbar);
        searchView = findViewById(R.id.searchView);
        search = findViewById(R.id.searchChild);
        mSingleUserList = findViewById(R.id.userList);
        mSingleUserList.setHasFixedSize(true);
        mSingleUserList.setLayoutManager(new LinearLayoutManager(this));
        demo = new ArrayList<>();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("add child");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = searchView.getText().toString().trim();

            }
        });
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
            mSingleUserList.setAdapter(new userAdapter(demo, new userAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(SingleUserclass item) {
                    Intent intent = new Intent(AddChild.this,ViewChild.class);
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
