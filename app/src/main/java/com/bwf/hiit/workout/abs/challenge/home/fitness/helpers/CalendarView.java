package com.bwf.hiit.workout.abs.challenge.home.fitness.helpers;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.bwf.hiit.workout.abs.challenge.home.fitness.R;
import com.bwf.hiit.workout.abs.challenge.home.fitness.adapter.CalendarPageAdapter;
import com.bwf.hiit.workout.abs.challenge.home.fitness.exceptions.OutOfDateRangeException;
import com.bwf.hiit.workout.abs.challenge.home.fitness.extensions.CalendarViewPager;
import com.bwf.hiit.workout.abs.challenge.home.fitness.interfaces.OnCalendarPageChangeListener;
import com.bwf.hiit.workout.abs.challenge.home.fitness.interfaces.OnDayClickListener;
import com.bwf.hiit.workout.abs.challenge.home.fitness.models.EventDay;
import com.bwf.hiit.workout.abs.challenge.home.fitness.utils.AppearanceUtils;
import com.bwf.hiit.workout.abs.challenge.home.fitness.utils.CalendarProperties;
import com.bwf.hiit.workout.abs.challenge.home.fitness.utils.DateUtils;
import com.bwf.hiit.workout.abs.challenge.home.fitness.utils.SelectedDay;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.bwf.hiit.workout.abs.challenge.home.fitness.adapter.CalendarPageAdapter.CALENDAR_SIZE;


public class CalendarView extends LinearLayout {

    public static final int CLASSIC = 0;
    public static final int ONE_DAY_PICKER = 1;
    public static final int MANY_DAYS_PICKER = 2;
    public static final int RANGE_PICKER = 3;

    private static final int FIRST_VISIBLE_PAGE = CALENDAR_SIZE / 2;

    private Context mContext;
    private CalendarPageAdapter mCalendarPageAdapter;

    private TextView mCurrentMonthLabel;
    private int mCurrentPage;
    private CalendarViewPager mViewPager;

    private CalendarProperties mCalendarProperties;

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initControl(context, attrs);
        initCalendar();
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initControl(context, attrs);
        initCalendar();
    }

    //protected constructor to create CalendarView for the dialog date picker
    protected CalendarView(Context context, CalendarProperties calendarProperties) {
        super(context);
        mContext = context;
        mCalendarProperties = calendarProperties;

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        inflater.inflate(R.layout.calendar_view, this);

        initUiElements();
        initAttributes();
        initCalendar();
    }

    private void initControl(Context context, AttributeSet attrs) {
        mContext = context;
        mCalendarProperties = new CalendarProperties(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        inflater.inflate(R.layout.calendar_view, this);

        initUiElements();
        setAttributes(attrs);
        initCalendar();
    }


    private void setAttributes(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CalendarView);

        try {
            initCalendarProperties(typedArray);
            initAttributes();
        } finally {
            typedArray.recycle();
        }
    }

    private void initCalendarProperties(TypedArray typedArray) {
        int headerColor = typedArray.getColor(R.styleable.CalendarView_headerColor, 0);
        mCalendarProperties.setHeaderColor(headerColor);

        int headerLabelColor = typedArray.getColor(R.styleable.CalendarView_headerLabelColor, 0);
        mCalendarProperties.setHeaderLabelColor(headerLabelColor);

        int abbreviationsBarColor = typedArray.getColor(R.styleable.CalendarView_abbreviationsBarColor, 0);
        mCalendarProperties.setAbbreviationsBarColor(abbreviationsBarColor);

        int abbreviationsLabelsColor = typedArray.getColor(R.styleable.CalendarView_abbreviationsLabelsColor, 0);
        mCalendarProperties.setAbbreviationsLabelsColor(abbreviationsLabelsColor);

        int pagesColor = typedArray.getColor(R.styleable.CalendarView_pagesColor, 0);
        mCalendarProperties.setPagesColor(pagesColor);

        int daysLabelsColor = typedArray.getColor(R.styleable.CalendarView_daysLabelsColor, 0);
        mCalendarProperties.setDaysLabelsColor(daysLabelsColor);

        int anotherMonthsDaysLabelsColor = typedArray.getColor(R.styleable.CalendarView_anotherMonthsDaysLabelsColor, 0);
        mCalendarProperties.setAnotherMonthsDaysLabelsColor(anotherMonthsDaysLabelsColor);

        int todayLabelColor = typedArray.getColor(R.styleable.CalendarView_todayLabelColor, 0);
        mCalendarProperties.setTodayLabelColor(todayLabelColor);

        int selectionColor = typedArray.getColor(R.styleable.CalendarView_selectionColor, 0);
        mCalendarProperties.setSelectionColor(selectionColor);

        int selectionLabelColor = typedArray.getColor(R.styleable.CalendarView_selectionLabelColor, 0);
        mCalendarProperties.setSelectionLabelColor(selectionLabelColor);

        int disabledDaysLabelsColor = typedArray.getColor(R.styleable.CalendarView_disabledDaysLabelsColor, 0);
        mCalendarProperties.setDisabledDaysLabelsColor(disabledDaysLabelsColor);

        int calendarType = typedArray.getInt(R.styleable.CalendarView_type, CLASSIC);
        mCalendarProperties.setCalendarType(calendarType);

        // Set picker mode !DEPRECATED!
        if (typedArray.getBoolean(R.styleable.CalendarView_datePicker, false)) {
            mCalendarProperties.setCalendarType(ONE_DAY_PICKER);
        }

        Drawable previousButtonSrc = typedArray.getDrawable(R.styleable.CalendarView_previousButtonSrc);
        mCalendarProperties.setPreviousButtonSrc(previousButtonSrc);

        Drawable forwardButtonSrc = typedArray.getDrawable(R.styleable.CalendarView_forwardButtonSrc);
        mCalendarProperties.setForwardButtonSrc(forwardButtonSrc);
    }

    private void initAttributes() {
        AppearanceUtils.setHeaderColor(getRootView(), mCalendarProperties.getHeaderColor());

        AppearanceUtils.setHeaderLabelColor(getRootView(), mCalendarProperties.getHeaderLabelColor());

        AppearanceUtils.setAbbreviationsBarColor(getRootView(), mCalendarProperties.getAbbreviationsBarColor());

        AppearanceUtils.setAbbreviationsLabelsColor(getRootView(), mCalendarProperties.getAbbreviationsLabelsColor());

        AppearanceUtils.setPagesColor(getRootView(), mCalendarProperties.getPagesColor());

        AppearanceUtils.setPreviousButtonImage(getRootView(), mCalendarProperties.getPreviousButtonSrc());

        AppearanceUtils.setForwardButtonImage(getRootView(), mCalendarProperties.getForwardButtonSrc());

        // Sets layout for date picker or normal calendar
        setCalendarRowLayout();
    }

    private void setCalendarRowLayout() {
        mCalendarProperties.setItemLayoutResource(R.layout.calendar_view_day);
    }

    private void initUiElements() {
        // This line subtracts a half of all calendar months to set calendar
        // in the correct position (in the middle)
        mCalendarProperties.getCurrentDate().set(Calendar.MONTH, -FIRST_VISIBLE_PAGE);

        ImageButton forwardButton = findViewById(R.id.forwardButton);
        forwardButton.setOnClickListener(onNextClickListener);

        ImageButton previousButton = findViewById(R.id.previousButton);
        previousButton.setOnClickListener(onPreviousClickListener);

        mCurrentMonthLabel = findViewById(R.id.currentDateLabel);

        mViewPager = findViewById(R.id.calendarViewPager);
    }

    private void initCalendar() {
        mCalendarPageAdapter = new CalendarPageAdapter(mContext, mCalendarProperties);

        mViewPager.setAdapter(mCalendarPageAdapter);
        mViewPager.addOnPageChangeListener(onPageChangeListener);

        // This line move calendar to the middle page
        mViewPager.setCurrentItem(FIRST_VISIBLE_PAGE);
    }

    public void setOnPreviousPageChangeListener(OnCalendarPageChangeListener listener) {
        mCalendarProperties.setOnPreviousPageChangeListener(listener);
    }

    public void setOnForwardPageChangeListener(OnCalendarPageChangeListener listener) {
        mCalendarProperties.setOnForwardPageChangeListener(listener);
    }

    private final OnClickListener onNextClickListener =
            v -> mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);

    private final OnClickListener onPreviousClickListener =
            v -> mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);

    private final ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }


        @Override
        public void onPageSelected(int position) {
            Calendar calendar = (Calendar) mCalendarProperties.getCurrentDate().clone();
            calendar.add(Calendar.MONTH, position);

            if (!isScrollingLimited(calendar, position)) {
                setHeaderName(calendar, position);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    private boolean isScrollingLimited(Calendar calendar, int position) {
        if (DateUtils.isMonthBefore(mCalendarProperties.getMinimumDate(), calendar)) {
            mViewPager.setCurrentItem(position + 1);
            return true;
        }

        if (DateUtils.isMonthAfter(mCalendarProperties.getMaximumDate(), calendar)) {
            mViewPager.setCurrentItem(position - 1);
            return true;
        }

        return false;
    }

    private void setHeaderName(Calendar calendar, int position) {
        mCurrentMonthLabel.setText(DateUtils.getMonthAndYearDate(mContext, calendar));
        callOnPageChangeListeners(position);
    }

    // This method calls page change listeners after swipe calendar or click arrow buttons
    private void callOnPageChangeListeners(int position) {
        if (position > mCurrentPage && mCalendarProperties.getOnForwardPageChangeListener() != null) {
            mCalendarProperties.getOnForwardPageChangeListener().onChange();
        }

        if (position < mCurrentPage && mCalendarProperties.getOnPreviousPageChangeListener() != null) {
            mCalendarProperties.getOnPreviousPageChangeListener().onChange();
        }

        mCurrentPage = position;
    }


    public void setOnDayClickListener(OnDayClickListener onDayClickListener) {
        mCalendarProperties.setOnDayClickListener(onDayClickListener);
    }


    public void setDate(Calendar date) throws OutOfDateRangeException {
        if (mCalendarProperties.getMinimumDate() != null && date.before(mCalendarProperties.getMinimumDate())) {
            throw new OutOfDateRangeException("SET DATE EXCEEDS THE MINIMUM DATE");
        }

        if (mCalendarProperties.getMaximumDate() != null && date.after(mCalendarProperties.getMaximumDate())) {
            throw new OutOfDateRangeException("SET DATE EXCEEDS THE MAXIMUM DATE");
        }

        DateUtils.setMidnight(date);

        mCalendarProperties.getSelectedDate().setTime(date.getTime());

        mCalendarProperties.getCurrentDate().setTime(date.getTime());
        mCalendarProperties.getCurrentDate().add(Calendar.MONTH, -FIRST_VISIBLE_PAGE);
        mCurrentMonthLabel.setText(DateUtils.getMonthAndYearDate(mContext, date));

        mViewPager.setCurrentItem(FIRST_VISIBLE_PAGE);
        mCalendarPageAdapter.notifyDataSetChanged();
    }

    /**
     * This method set a current and selected date of the calendar using Date object.
     *
     * @param currentDate A date to which the calendar will be set
     */
    public void setDate(Date currentDate) throws OutOfDateRangeException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);

        setDate(calendar);
    }


    public void setEvents(List<EventDay> eventDays) {
        if (mCalendarProperties.getCalendarType() == CLASSIC) {
            mCalendarProperties.setEventDays(eventDays);
            mCalendarPageAdapter.notifyDataSetChanged();
        }
    }

    public List<Calendar> getSelectedDates() {
        return Stream.of(mCalendarPageAdapter.getSelectedDays())
                .map(SelectedDay::getCalendar)
                .sortBy(calendar -> calendar).toList();
    }

    public void setSelectedDates(List<SelectedDay> mList) {
        mCalendarPageAdapter.setmSelectedDays(mList);
    }


    @Deprecated
    public Calendar getSelectedDate() {
        return getFirstSelectedDate();
    }

    public Calendar getFirstSelectedDate() {
        return Stream.of(mCalendarPageAdapter.getSelectedDays())
                .map(SelectedDay::getCalendar).findFirst().get();
    }


    public Calendar getCurrentPageDate() {
        Calendar calendar = (Calendar) mCalendarProperties.getCurrentDate().clone();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MONTH, mViewPager.getCurrentItem());
        return calendar;
    }


    public void setMinimumDate(Calendar calendar) {
        mCalendarProperties.setMinimumDate(calendar);
    }


    public void setMaximumDate(Calendar calendar) {
        mCalendarProperties.setMaximumDate(calendar);
    }


    public void showCurrentMonthPage() {
        mViewPager.setCurrentItem(mViewPager.getCurrentItem()
                - DateUtils.getMonthsBetweenDates(DateUtils.getCalendar(), getCurrentPageDate()), true);
    }

    public void setDisabledDays(List<Calendar> disabledDays) {
        mCalendarProperties.setDisabledDays(disabledDays);
    }
}
