package com.bwf.hiit.workout.abs.challenge.home.fitness.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.bwf.hiit.workout.abs.challenge.home.fitness.fragments.DetailFragment;
import com.bwf.hiit.workout.abs.challenge.home.fitness.fragments.FoodFragment;
import com.bwf.hiit.workout.abs.challenge.home.fitness.fragments.HomeFragment;
import com.bwf.hiit.workout.abs.challenge.home.fitness.fragments.UtilitiesFragment;


public class PagerAdapter extends FragmentPagerAdapter {

    public PagerAdapter(FragmentManager fm){
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new DetailFragment();
            case 1:
                return new DetailFragment();
            case 2:
                return new DetailFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }

}