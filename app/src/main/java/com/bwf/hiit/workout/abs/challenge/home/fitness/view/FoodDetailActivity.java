package com.bwf.hiit.workout.abs.challenge.home.fitness.view;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;

import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.fragments.DetailFragment;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.Url;
import com.bwf.hiit.workout.abs.challenge.home.fitness.utils.KKViewPager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.viewModel.FoodViewModel;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class FoodDetailActivity extends AppCompatActivity {

    KKViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);
        ButterKnife.bind(this);

        int pos = getIntent().getIntExtra("pos", 0);

        mPager = findViewById(R.id.pager);
        TabLayout tabLayout = findViewById(R.id.tabDots);
        tabLayout.setupWithViewPager(mPager, true);

        FoodViewModel mViewModel = ViewModelProviders.of(this).get(FoodViewModel.class);

        mViewModel.getAllRecords().observe(this, foodList -> {
            if (foodList != null) {

                PagerAdapter myPagerAdapter = new PagerAdapter(getSupportFragmentManager(),
                        foodList.get(pos).getFoodDetail());
                mPager.setAdapter(myPagerAdapter);
                mPager.setAnimationEnabled(true);
                mPager.setFadeEnabled(true);
                mPager.setFadeFactor(0.6f);
                mPager.setPageMargin(100);
            }
        });

    }

    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        finish();
    }

    public class PagerAdapter extends FragmentPagerAdapter {

        List<Url> mList;

        PagerAdapter(FragmentManager fm, List<Url> mList) {
            super(fm);
            this.mList = mList;
        }

        @Override
        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();
            bundle.putString("url", mList.get(position).getUrl());
            Fragment fragment = new DetailFragment();
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }
}
