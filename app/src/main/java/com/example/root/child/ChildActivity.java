package com.example.root.child;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ChildActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ArrayList<SingleUserclass> ChildDemo;
    private RecyclerView mSingleUserList;
    private TextView userName,Email;
    private ImageView image;
    private ProgressDialog childDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("parentActivity");
        ChildDemo=new ArrayList<>();
        mSingleUserList=findViewById(R.id.singleuserlistchild);
        mSingleUserList.setHasFixedSize(true);
        mSingleUserList.setLayoutManager(new LinearLayoutManager(this));
        childDialog = new ProgressDialog(this,ProgressDialog.THEME_HOLO_DARK);
        childDialog.setTitle("loading");
        childDialog.setMessage("please wait");
        childDialog.setCanceledOnTouchOutside(false);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_Child);
        navigationView.setNavigationItemSelectedListener(this);
        View headerview = navigationView.getHeaderView(0);
        userName = headerview.findViewById(R.id.ChilduserName);
        Email = headerview.findViewById(R.id.ChilduserEmail);
        image = headerview.findViewById(R.id.imageViewChild);
        synchronized (this) {
            FirebaseDatabase.getInstance().getReference("demodatabase").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
//                    childDialog.show();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Adduser user = snapshot.getValue(Adduser.class);
                        if (snapshot.child("imageUrl").exists())
                            ChildDemo.add(new SingleUserclass(user.getName(), user.getEmail(), user.getImageUrl()));
                        else
                            ChildDemo.add(new SingleUserclass(user.getName(), user.getEmail(), "Default"));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();

                }
            });

//            ChildDemo.add(new SingleUserclass("demo","demo","Default"));
//            ChildDemo.add(new SingleUserclass("demo","demo","Default"));
//            ChildDemo.add(new SingleUserclass("demo","demo","Default"));
            mSingleUserList.setAdapter(new userAdapter(ChildDemo, new userAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(SingleUserclass item) {
                    Intent intent = new Intent(ChildActivity.this, ViewChild.class);
                    String[] data = new String[3];
                    data[0] = item.getName();
                    data[1] = item.getEmail();
                    data[2] = item.getImage();
                    intent.putExtra("userInfo", data);
                    startActivity(intent);
                }
            }));
            childDialog.dismiss();
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.child, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.radius) {
            Intent intent = new Intent(ChildActivity.this, RadiusActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.logoutchild) {
            FirebaseAuth.getInstance().signOut();
            stopService(new Intent(ChildActivity.this,EmergencyDetection.class));
            Intent intent = new Intent(ChildActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        if (id == R.id.action_settings_child) {
            Intent intent = new Intent(ChildActivity.this, SettingActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.changeProfile) {
            Intent intent = new Intent(ChildActivity.this, ChangeDatabaseActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent intent = new Intent(ChildActivity.this,profileActivity.class);
            startActivity(intent);
        }
        if (id == R.id.nav_addchild) {
            Intent intent = new Intent(ChildActivity.this,AddChild.class);
            startActivity(intent);
        }
        if (id == R.id.nav_request_child) {
            Intent intent = new Intent(ChildActivity.this,Request.class);
            startActivity(intent);
        }

        if (id == R.id.nav_parentDetail) {
            Intent intent = new Intent(ChildActivity.this,ParentDetailActivity.class);
            startActivity(intent);
        }
        if(id==R.id.nav_aboutUs)
        {
            Intent intent=new Intent(ChildActivity.this,AboutusActivity.class);
            startActivity(intent);
        }
        if(id==R.id.nav_feedback)
        {
            Intent intent=new Intent(ChildActivity.this,FeedbackActivity.class);
            startActivity(intent);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void setAdapter(){

    }
}
