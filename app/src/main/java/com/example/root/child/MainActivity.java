package com.example.root.child;

import android.nfc.Tag;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {


    private SectionPageAdapter adapter;
    private ViewPager mViewpager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseUtil.getDatabase();
        adapter=new SectionPageAdapter(getSupportFragmentManager());
        mViewpager=findViewById(R.id.container);
        setupViewPager(mViewpager);
        mViewpager.setAdapter(adapter);
        TabLayout tabLayout= findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewpager);


    }

    private void setupViewPager(ViewPager viewPager)
    {
        adapter.addFragment(new logIn(),"login");
        adapter.addFragment(new signUp(),"Sigup");
    }

}
