package com.example.root.child;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 23/2/18.
 */

public class SectionPageAdapter extends FragmentPagerAdapter {
     private final List<Fragment> mFragmentList=new ArrayList<>();
     private final List<String> mFramentListName=new ArrayList<>();

    public SectionPageAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Fragment fragment, String tittle)
    {
        mFragmentList.add(fragment);
        mFramentListName.add(tittle);
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFramentListName.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }


}
