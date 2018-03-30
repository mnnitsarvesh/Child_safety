package com.example.root.child;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
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

public class ParentActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ImageView imageView;
    DatabaseReference demoDatabase;
    FirebaseAuth mAuth;
    private Button EmergencyButton;
    private LocationManager locationManager;
    private LocationListener locationListener;
    public final static int REQUEST_CODE = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mAuth = FirebaseAuth.getInstance();
        demoDatabase = FirebaseDatabase.getInstance().getReference("demodatabase");
        EmergencyButton = findViewById(R.id.EmergencyButton);
        startService(new Intent(this, EmergencyDetection.class));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("childActivity");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"hidden camera Activates",Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), "this is emergency", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), RecorderService.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startService(intent);
            }
        });
        EmergencyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findme();
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerview = navigationView.getHeaderView(0);
        final TextView userName = headerview.findViewById(R.id.userName);
        TextView userEmail = headerview.findViewById(R.id.userEmail);
        userEmail.setText(mAuth.getCurrentUser().getEmail());
        imageView = headerview.findViewById(R.id.imageViewParent);

        demoDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("name").exists()){
                    userName.setText(dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("name").getValue(String.class));
                }
                if(dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("imageUrl").exists()){
                    Picasso.with(ParentActivity.this).load(dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("imageUrl").getValue(String.class)).placeholder(R.drawable.image).into(imageView);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //we can exit the app on back click option
            super.onBackPressed();
           // FirebaseAuth.getInstance().signOut();
            this.finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.parent, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void checkDrawOverlayPermission() {
        /** check if we already  have permission to draw over other apps */
        if (!Settings.canDrawOverlays(this)) {
            /** if not construct intent to request permission */
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            /** request permission via start activity for result */
            startActivityForResult(intent, REQUEST_CODE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
       // if so check once again if we have permission
            if (Settings.canDrawOverlays(this)) {
                // continue here - permission was granted


            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.profileView) {
            Intent intent = new Intent(ParentActivity.this,ChangeDatabaseActivity.class);
            startActivity(intent);
        }
        if (id == R.id.LogOut) {
            FirebaseAuth.getInstance().signOut();
            stopService(new Intent(ParentActivity.this,EmergencyDetection.class));
            Intent intent = new Intent(ParentActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(ParentActivity.this, SettingActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

         if (id == R.id.viewParent) {
            Intent intent = new Intent(ParentActivity.this, ViewPArentActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_AddChild) {
            Intent intent = new Intent(ParentActivity.this,AddChild.class);
            startActivity(intent);
            //adding the child in list and then push that list in database
        } else if (id == R.id.nav_Request) {
            Intent intent = new Intent(ParentActivity.this,Request.class);
            startActivity(intent);
            //toshow new request

        } else if (id == R.id.nav_ChildLocation) {
            Intent intent = new Intent(ParentActivity.this, MapsActivity.class);
            startActivity(intent);
            //to show on map the location of the child

        }
        if(id==R.id.nav_aboutUs)
        {
            Intent intent=new Intent(ParentActivity.this,AboutusActivity.class);
            startActivity(intent);
        }
        if(id==R.id.nav_feedback)
        {
            Intent intent=new Intent(ParentActivity.this,FeedbackActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void findme() {
         locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

// Define a listener that responds to location updates
         locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                Toast.makeText(getApplicationContext(),"longitute "+location.getLongitude()+"latitude"+location.getLatitude(),Toast.LENGTH_LONG).show();
                locationManager.removeUpdates(locationListener);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

// Register the listener with the Location Manager to receive location updates
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            return;
        }


    }



}
