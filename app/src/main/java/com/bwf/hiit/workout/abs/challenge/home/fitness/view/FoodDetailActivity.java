package com.bwf.hiit.workout.abs.challenge.home.fitness.view;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.adapter.PagerAdapter;
import com.bwf.hiit.workout.abs.challenge.home.fitness.utils.KKViewPager;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class FoodDetailActivity extends AppCompatActivity {

    KKViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);
        ButterKnife.bind(this);

        mPager = findViewById(R.id.pager);
        TabLayout tabLayout = findViewById(R.id.tabDots);
        tabLayout.setupWithViewPager(mPager, true);

        PagerAdapter myPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(myPagerAdapter);
        mPager.setAnimationEnabled(true);
        mPager.setFadeEnabled(true);
        mPager.setFadeFactor(0.6f);
        mPager.setPageMargin(100);

    }

    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        finish();
    }
}
