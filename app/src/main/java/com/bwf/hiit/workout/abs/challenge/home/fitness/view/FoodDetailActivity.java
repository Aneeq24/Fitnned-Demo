package com.bwf.hiit.workout.abs.challenge.home.fitness.view;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.fragments.DetailFragment;
import com.bwf.hiit.workout.abs.challenge.home.fitness.managers.AdsManager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.Url;
import com.bwf.hiit.workout.abs.challenge.home.fitness.utils.KKViewPager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.viewModel.FoodViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class FoodDetailActivity extends AppCompatActivity {

    KKViewPager mPager;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);
        ButterKnife.bind(this);

        int pos = getIntent().getIntExtra("pos", 0);
        mPager = findViewById(R.id.pager);
        title = findViewById(R.id.tv_title);

        AdsManager.getInstance().showInterstitialAd(getString(R.string.AM_Int_Main_Menu));

        String[] foodName = getResources().getStringArray(R.array.food_list);
        title.setText(foodName[pos]);
        TabLayout tabLayout = findViewById(R.id.tabDots);
        tabLayout.setupWithViewPager(mPager, true);

        FoodViewModel mViewModel = ViewModelProviders.of(this).get(FoodViewModel.class);

        mViewModel.getAllRecords().observe(this, foodList -> {
            if (foodList != null) {
                PagerAdapter myPagerAdapter = new PagerAdapter(getSupportFragmentManager(),
                        foodList.get(pos).getFoodDetail());
                mPager.setAdapter(myPagerAdapter);
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
            bundle.putString("heading", mList.get(position).getTitle());
            bundle.putParcelableArrayList("content", (ArrayList<? extends Parcelable>) mList.get(position).getContent());
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
