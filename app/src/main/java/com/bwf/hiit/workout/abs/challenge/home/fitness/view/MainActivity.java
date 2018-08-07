/*
package com.bwf.hiit.workout.abs.challenge.home.fitness.view;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.adapter.PagerAdapter;
import com.bwf.hiit.workout.abs.challenge.home.fitness.database.AppDataBase;
import com.bwf.hiit.workout.abs.challenge.home.fitness.fragments.Calender;
import com.bwf.hiit.workout.abs.challenge.home.fitness.fragments.Workout;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements Workout.OnFragmentInteractionListener,Calender.OnFragmentInteractionListener {

    static int selectedIndex = 0;
    AppDataBase appDataBase;

    //UserData data = new UserData();



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

      //  appDataBase = Room.databaseBuilder(getApplicationContext(),AppDataBase.class ,"userdb").build();


        String [] plans = getResources().getStringArray(R.array.plans);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = findViewById(R.id.viewPager);
        final PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager() , tabLayout.getTabCount());

        viewPager.setAdapter(pagerAdapter);

        int  k = viewPager.getCurrentItem();





        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        viewPager.setCurrentItem(selectedIndex);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                Log.d("Main Activity : " , "Taped " + tab.getPosition());
                viewPager.setCurrentItem(tab.getPosition());
                //pagerAdapter.getItem(tab.getPosition());
                int  k = viewPager.getCurrentItem();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab)
            {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab)
            {

            }
        });

       try
       {
           initApp();
       }
       catch (Exception e)
       {
           e.printStackTrace();
       }

     //  initDb();

    }


    void  initDb()
    {
//        Plan plan;
//      int val =   appDataBase.planDao().getCount();
//      int dayCount = appDataBase.dayDao().getCount();
//      int exerciseCount = appDataBase.exerciseDao().getCount();



    }


    void  initTabLayout() {

    }


  //  List<UserData> userDataList  = new ArrayList<UserData>();

    //UserData dat= new UserData();

    void  getDbData() {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                  //  dat = appDataBase.myDao().loadAllBy(1);
                } catch (Exception e)
                {
                    e.printStackTrace();
                }


//
//                appDataBase.myDao().delete(dat);
//
//                dat.id = 5;
//
//                appDataBase.myDao().addUser(dat);
//


            }

        });
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();

      //  Toast.makeText(MainActivity.this, "Welcome "+dat.name, Toast.LENGTH_SHORT).show();
    }


    void  getDataThroughAsynTask() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                return null;
            }

            @Override
            protected void onPreExecute() {
            }

            @Override
            protected void onProgressUpdate(Void... values) {
            }


        }.execute();
    }

     static int valueIndex =0;


  //  UserData tempData = new UserData();

    ArrayList<Integer> arr = new ArrayList<Integer>() ;

    int plane = 0;
    int day = 0;
    int exercise=0;
    int id;
*/
/*
    void  saveDataInDd() {


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run()
            {
                for(int i =1; i <=900 ; i++)
                {


                    Log.d("array:", "" + arr.toString() );

                    tempData = new UserData();

                    for (int plan =1 ; plan <= 3 ; plan++)
                    {
                        for (int day = 1 ; day <= 30 ; day ++)
                        {
                            for (int exercise = 1 ; exercise <=10 ; exercise ++ )
                            {

//                                if (!arr.contains(tempData.id))
//                                {
                                    id++;
                                    tempData.id=id;
                                    tempData.plan= plan;
                                    tempData.day = day;
                                    tempData.exercise = exercise;
                                    appDataBase.myDao().addUser(tempData);
                                   // arr.add(i);
//                                }
//                                else
//                                {
//                                    continue;
//                                }
                            }
                        }
                    }
                }

            }
        });
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();


    }
*//*

    void initApp()
    {
//        RecyclerView recycleViewActivity = findViewById(R.id.menuData);
//        recycleViewActivity.setLayoutManager(new LinearLayoutManager(this));
//        populateData();
//        MenuDataClass dataClass = new MenuDataClass();
//        recycleViewActivity.setAdapter(new HomeAdapter(dataClass.tilte , dataClass.image,dataClass.description));
//

    }


//    MenuDataClass obj1;
//    MenuDataClass obj2;
//    MenuDataClass obj3;
//    MenuDataClass[] array;
//
//    List<MenuDataClass> list = new ArrayList<Workout.MenuDataClass>();

    void  populateData() {
//        obj1 = new MenuDataClass("Classic","ffffffffff", BitmapFactory.decodeResource(getResources(),R.drawable.ic_card_biceps));
//        obj2 = new MenuDataClass("Full Body","2222222222222", BitmapFactory.decodeResource(getResources(),R.drawable.ic_card_chest));
//        obj3 = new MenuDataClass("Abs Workout","333333", BitmapFactory.decodeResource(getResources(),R.drawable.ic_card_chest));

//        list.add(obj1);
//        list.add(obj2);
//        list.add(obj3);

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


//    public  class  MenuDataClass
//    {
//        String[] tilte = {"Classic" , "Full Body" , "Abs Workout"};
//        String[] description = {
//                        "Scientifically proven to assist weight loss and improve cardiovasular function",
//                        "Get flat and firm abdominal muscles using effective abs training methods",
//                        "Strengthen and tighten your legs "
//                };
//        Bitmap[] image = {  BitmapFactory.decodeResource(getResources(),R.drawable.ic_card_biceps),
//                            BitmapFactory.decodeResource(getResources(),R.drawable.ic_card_chest),
//                            BitmapFactory.decodeResource(getResources(),R.drawable.ic_card_triceps)
//                    };
//
//        MenuDataClass()
//        {
//
//
//        }
//
//    }



//    private class DownloadFilesTask extends AsyncTask<UserData, Integer, Long> {
//        protected Long doInBackground(UserData... urls) {
//            int count = urls.length;
//            long totalSize = 0;
//            for (int i = 0; i < count; i++) {
//                totalSize += Downloader.downloadFile(urls[i]);
//                publishProgress((int) ((i / (float) count) * 100));
//                // Escape early if cancel() is called
//                if (isCancelled()) break;
//            }
//            return totalSize;
//        }
//
//        protected void onProgressUpdate(Integer... progress) {
//            setProgressPercent(progress[0]);
//        }
//
//        protected void onPostExecute(Long result) {
//            showDialog("Downloaded " + result + " bytes");
//        }
//    }

}


*/
