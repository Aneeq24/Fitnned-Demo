//package com.bwf.hiit.workout.abs.challenge.home.fitness.adapter;
//
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentStatePagerAdapter;
//import android.util.Log;
//
//import com.bwf.hiit.workout.abs.challenge.home.fitness.fragments.CalenderFragment;
//import com.bwf.hiit.workout.abs.challenge.home.fitness.fragments.Workout;
//
//public class PagerAdapter extends FragmentStatePagerAdapter {
//
//
//    int noOfTabs;
//
//    public PagerAdapter(FragmentManager fm , int tabs)
//    {
//        super(fm);
//
//        this.noOfTabs = tabs;
//    }
//
//    @Override
//    public Fragment getItem(int position) {
//
//        switch (position)
//        {
//            case  0:
//                Workout tab1 = new Workout();
//                Log.d("Case 1" , "WorkOut Tab");
//                return  tab1;
//            case  1:
//                CalenderFragment tab2 = new CalenderFragment();
//                Log.d("Case 2" , "CalenderTab");
//                return  tab2;
//
//                default:
//                    Log.d("Default" , "WorkOut Tab");
//                    return  null;
//        }
//
//
//    }
//
//    @Override
//    public int getCount() {
//        return noOfTabs;
//    }
//}
