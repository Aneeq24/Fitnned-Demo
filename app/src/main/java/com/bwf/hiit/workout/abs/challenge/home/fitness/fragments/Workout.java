package com.bwf.hiit.workout.abs.challenge.home.fitness.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.adapter.MainMenuAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Workout.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Workout#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Workout extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public View root;
    private OnFragmentInteractionListener mListener;

    public Workout() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Workout.
     */
    // TODO: Rename and change types and number of parameters
    public static Workout newInstance(String param1, String param2) {
        Workout fragment = new Workout();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    void initApp()
    {
        RecyclerView recycleViewActivity =  root.findViewById(R.id.menuData);
        recycleViewActivity.setLayoutManager(new LinearLayoutManager(getContext()));
        MenuDataClass dataClass = new MenuDataClass();

        recycleViewActivity.setAdapter(new MainMenuAdapter(dataClass.tilte , dataClass.image,dataClass.description));
    }

    MenuDataClass obj1;
    MenuDataClass obj2;
    MenuDataClass obj3;
    MenuDataClass[] array;

    List<MenuDataClass> list = new ArrayList<MenuDataClass>();

    public  class  MenuDataClass
    {
        //1994 populate main screen

        //ToDo need to get images from the data base
        String[] tilte = {"BEGINEER" , "INTERMEDIATE" , "ADVANCED"};

        String[] description = {
                "",
                "",
                ""
        };

                Bitmap[] image = {  BitmapFactory.decodeResource(getResources(),R.drawable.main_screen_beginner_image),
                BitmapFactory.decodeResource(getResources(),R.drawable.main_screen_intermediate_image),
                BitmapFactory.decodeResource(getResources(),R.drawable.main_screen_advanced_image)
        };

        MenuDataClass()
        {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         root= inflater.inflate(R.layout.fragment_workout, container, false);
        Log.d("1994 :" , "oncreateview");
        initApp();
        return root;
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

}
