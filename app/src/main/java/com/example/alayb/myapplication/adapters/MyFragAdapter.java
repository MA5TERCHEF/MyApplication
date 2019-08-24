package com.example.alayb.myapplication.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by alayb on 24-Jul-19.
 */

public class MyFragAdapter extends FragmentPagerAdapter {
    ArrayList<String> txt;
    ArrayList<Fragment> frag;

    public MyFragAdapter(FragmentManager fm, ArrayList<String> txt, ArrayList<Fragment> frag) {
        super(fm);
        this.txt=txt;
        this.frag=frag;
    }

    @Override
    public Fragment getItem(int position) {
        return frag.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return txt.get(position);
    }

    @Override
    public int getCount() {
        return frag.size();
    }

}