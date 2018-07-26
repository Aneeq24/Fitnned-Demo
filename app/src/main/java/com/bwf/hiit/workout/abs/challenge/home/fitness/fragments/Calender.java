package com.bwf.hiit.workout.abs.challenge.home.fitness.fragments;

import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.jjoe64.graphview.GraphView;

import java.util.Calendar;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Calender.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Calender#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Calender extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    CalendarView simpleCalendarView ;
    View rootView;

    private HorizontalCalendar horizontalCalendar;


    private OnFragmentInteractionListener mListener;

    public Calender() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Calender.
     */
    // TODO: Rename and change types and number of parameters
    public static Calender newInstance(String param1, String param2) {
        Calender fragment = new Calender();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       rootView = inflater.inflate(R.layout.fragment_calender, container, false);


//       getFragmgetWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);





        // simpleCalendarView  = rootView.findViewById(R.id.calendarView);

       // simpleCalendarView = findViewById(R.id.simpleCalendarView); // get the reference of CalendarView
//        simpleCalendarView.setFocusedMonthDateColor(Color.RED); // set the red color for the dates of  focused month
//        simpleCalendarView.setUnfocusedMonthDateColor(Color.BLUE); // set the yellow color for the dates of an unfocused month
//       // simpleCalendarView.setSelectedWeekBackgroundColor(Color.p); // red color for the selected week's background
//        simpleCalendarView.setWeekSeparatorLineColor(Color.GREEN); // green color for the week separator line



       //setShart();
        createGraph();
        return  rootView;
    }



    void  createGraph()
    {
//        Calendar calendar = Calendar.getInstance();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd hh:mm:ss 'GMT'Z yyyy");
//        System.out.println(dateFormat.format(calendar.getTime()));

        // generate Dates
//        Calendar calendar = Calendar.getInstance();
//        Date d1 = calendar.getTime();
//        calendar.add(Calendar.DATE, 1);
//        Date d2 = calendar.getTime();
//        calendar.add(Calendar.DATE, 1);
//        Date d3 = calendar.getTime();

        GraphView graph =  rootView.findViewById(R.id.graph);

        graph.getViewport().setScalable(true);

//        graph.getViewport().

// activate horizontal scrolling
//        graph.getViewport().setScrollable(true);

// activate horizontal and vertical zooming and scrolling
//        graph.getViewport().setScalableY(true);

// activate vertical scrolling
//        graph.getViewport().setScrollableY(true);

// you can directly pass Date objects to DataPoint-Constructor
// this will convert the Date to double via Date#getTime()
//        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
//                new DataPoint(d1, 1),
//                new DataPoint(d2, 5),
//                new DataPoint(d3, 3)
//        });
//
//        graph.addSeries(series);

// set date label formatter
//        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
//        graph.getGridLabelRenderer().setNumHorizontalLabels(5); // only 4 because of the space
//
//// set manual x bounds to have nice steps
//        graph.getViewport().setMinX(d1.getTime());
//        graph.getViewport().setMaxX(d3.getTime());
//        graph.getViewport().setXAxisBoundsManual(true);
//
//// as we use dates as labels, the human rounding to nice readable numbers
//// is not necessary
//        graph.getGridLabelRenderer().setHumanRounding(false);


//
//        GraphView graph = (GraphView) rootView.findViewById(R.id.graph);
//        graph.getViewport().setXAxisBoundsManual(true);
//        graph.getViewport().setMinX(0.5);
//        graph.getViewport().setMaxX(3.5);
//
//// set manual Y bounds
//        graph.getViewport().setYAxisBoundsManual(true);
////        graph.getViewport().setMinY(calendar);
//        graph.getViewport().setMaxY(8);
//        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
//                new DataPoint(0, 1),
//                new DataPoint(1, 5),
//                new DataPoint(2, 3),
//                new DataPoint(3, 2),
//                new DataPoint(4, 6)
//        });
//        graph.addSeries(series);
    }

    void  setCalendar()
    {
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.WEEK_OF_MONTH, 0);

        /* end after 1 month from now */
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.WEEK_OF_MONTH, 0);

        horizontalCalendar = new HorizontalCalendar.Builder(rootView, R.id.calendarView)
                .range(startDate, endDate)
                .datesNumberOnScreen(7)
                .configure()
                .formatTopText("MMM")
                .formatMiddleText("dd")
                .formatBottomText("EEE")
                .textSize(14f, 24f, 14f)
                .showTopText(true)
                .showBottomText(true)
                .textColor(Color.LTGRAY, Color.WHITE)
                .end()
                .build();


        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                Toast.makeText(getContext(), DateFormat.format("EEE, MMM d, yyyy", date) + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCalendarScroll(HorizontalCalendarView calendarView,
                                         int dx, int dy) {

            }

            @Override
            public boolean onDateLongClicked(Calendar date, int position) {
                return true;
            }

        });
    }



    void  initCalener()
   {
//        CalendarView calenderView = rootView.findViewById(R.id.calendarView);
//
//        calenderView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
//            @Override
//            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
//
//            }
//        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


//    private void setData(int count, float range) {
//
//        ArrayList<Entry> values = new ArrayList<Entry>();
//
//        for (int i = 0; i < count; i++) {
//
//            float val = (float) (Math.random() * range) + 3;
//            values.add(new Entry(i, val, getResources().getDrawable(R.drawable.star)));
//        }
//
//        LineDataSet set1;
//
//        if (mChart.getData() != null &&
//                mChart.getData().getDataSetCount() > 0) {
//            set1 = (LineDataSet)mChart.getData().getDataSetByIndex(0);
//            set1.setValues(values);
//            mChart.getData().notifyDataChanged();
//            mChart.notifyDataSetChanged();
//        } else {
//            // create a dataset and give it a type
//            set1 = new LineDataSet(values, "DataSet 1");
//
//            set1.setDrawIcons(false);
//
//            // set the line to be drawn like this "- - - - - -"
//            set1.enableDashedLine(10f, 5f, 0f);
//            set1.enableDashedHighlightLine(10f, 5f, 0f);
//            set1.setColor(Color.BLACK);
//            set1.setCircleColor(Color.BLACK);
//            set1.setLineWidth(1f);
//            set1.setCircleRadius(3f);
//            set1.setDrawCircleHole(false);
//            set1.setValueTextSize(9f);
//            set1.setDrawFilled(true);
//            set1.setFormLineWidth(1f);
//            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
//            set1.setFormSize(15.f);
//
//            if (Utils.getSDKInt() >= 18) {
//                // fill drawable only supported on api level 18 and above
//                Drawable drawable = ContextCompat.getDrawable(Application.getContext(), R.drawable.fade_red);
//                set1.setFillDrawable(drawable);
//            }
//            else {
//                set1.setFillColor(Color.BLACK);
//            }
//
//            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
//            dataSets.add(set1); // add the datasets
//
//            // create a data object with the datasets
//            LineData data = new LineData(dataSets);
//
//            // set data
//            mChart.setData(data);
//        }
  //  }
}
