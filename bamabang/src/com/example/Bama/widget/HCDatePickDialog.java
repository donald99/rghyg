package com.example.Bama.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.*;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.example.Bama.R;
import com.example.Bama.ui.ActivityBase;
import com.example.Bama.util.DisplayUtil;
import com.example.Bama.widget.wheel_view.TosGallery;
import com.example.Bama.widget.wheel_view.WheelView;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by xiaoyuPC on 2015/4/25.
 */
public class HCDatePickDialog extends FrameLayout implements View.OnClickListener {
    private TextView sureBtn;
    private TextView cancelBtn;

    private WheelView yearWheel;
    private WheelView monthWheel;
    private WheelView dayWheel;
    private WheelView hourWheel;
    private FrameLayout rootView;
    private Activity activity;

    private static final int[] DAYS_PER_MONTH = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    private static final String[] MONTH_NAME = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12",};

    private static String[] HOUR_NAME = new String[13];


    private ArrayList<TextInfo> mMonths = new ArrayList<TextInfo>();
    private ArrayList<TextInfo> mYears = new ArrayList<TextInfo>();
    private ArrayList<TextInfo> mDates = new ArrayList<TextInfo>();
    private ArrayList<TextInfo> mHours = new ArrayList<TextInfo>();

    private int mCurDate = 0;
    private int mCurMonth = 0;
    private int mCurYear = 0;
    private String mCurHour;

    private HCDatePickDialogListener listener;

    public interface HCDatePickDialogListener {
        void onDataPicked(String date);
    }

    private TosGallery.OnEndFlingListener mListener = new TosGallery.OnEndFlingListener() {
        @Override
        public void onEndFling(TosGallery v) {
            int pos = v.getSelectedItemPosition();

            if (v == dayWheel) {
                TextInfo info = mDates.get(pos);
                setDate(info.mIndex);
            } else if (v == monthWheel) {
                TextInfo info = mMonths.get(pos);
                setMonth(info.mIndex);
            } else if (v == yearWheel) {
                TextInfo info = mYears.get(pos);
                setYear(info.mIndex);
            } else if (v == hourWheel) {
                TextInfo info = mHours.get(pos);
                setHour(info.mText);
            }
        }
    };

    public HCDatePickDialog(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, null);
    }

    public HCDatePickDialog(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, null);
    }

    public HCDatePickDialog(Context context) {
        super(context);
        init(context, null);
    }

    public HCDatePickDialog(Context context, HCDatePickDialogListener l) {
        super(context);
        init(context, l);
    }

    public static HCDatePickDialog showDlg(Activity activity, HCDatePickDialogListener l) {
        HCDatePickDialog dlg = new HCDatePickDialog(activity, l);
        dlg.show();
        return dlg;
    }

    private void init(Context context, HCDatePickDialogListener l) {
        HOUR_NAME[0] = getContext().getString(R.string.shangwu8);
        HOUR_NAME[1] = getContext().getString(R.string.shangwu9);
        HOUR_NAME[2] = getContext().getString(R.string.shagnwu10);
        HOUR_NAME[3] = getContext().getString(R.string.shangwu11);
        HOUR_NAME[4] = getContext().getString(R.string.shangwu12);
        HOUR_NAME[5] = getContext().getString(R.string.xiawu1);
        HOUR_NAME[6] = getContext().getString(R.string.xiawu2);
        HOUR_NAME[7] = getContext().getString(R.string.xiawu3);
        HOUR_NAME[8] = getContext().getString(R.string.xiawu4);
        HOUR_NAME[9] = getContext().getString(R.string.xiawu5);
        HOUR_NAME[10] = getContext().getString(R.string.xiawu6);
        HOUR_NAME[11] = getContext().getString(R.string.xiawu7);
        HOUR_NAME[12] = getContext().getString(R.string.xiawu8);
        activity = (ActivityBase) context;
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.dialog_date_pick, this, true);
        sureBtn = (TextView) findViewById(R.id.sureBtn);
        cancelBtn = (TextView) findViewById(R.id.cancelBtn);
        yearWheel = (WheelView) findViewById(R.id.yearWheel);
        monthWheel = (WheelView) findViewById(R.id.monthWheel);
        dayWheel = (WheelView) findViewById(R.id.dayWheel);
        hourWheel = (WheelView) findViewById(R.id.hourWheel);

        cancelBtn.setOnClickListener(this);
        sureBtn.setOnClickListener(this);

        dayWheel.setOnEndFlingListener(mListener);
        monthWheel.setOnEndFlingListener(mListener);
        yearWheel.setOnEndFlingListener(mListener);
        hourWheel.setOnEndFlingListener(mListener);

        dayWheel.setSoundEffectsEnabled(false);
        monthWheel.setSoundEffectsEnabled(false);
        yearWheel.setSoundEffectsEnabled(false);
        hourWheel.setSoundEffectsEnabled(false);

        dayWheel.setAdapter(new WheelTextAdapter(context));
        monthWheel.setAdapter(new WheelTextAdapter(context));
        yearWheel.setAdapter(new WheelTextAdapter(context));
        hourWheel.setAdapter(new WheelTextAdapter(context));

        prepareData();

        setVisibility(View.GONE);

        rootView = getRootView(activity);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        setLayoutParams(params);
        setId(R.id.view_date_pick_dlg);
        this.listener = l;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sureBtn:
                if (listener != null) {
                    StringBuffer sb = new StringBuffer();
                    sb.append(mCurYear).append(getResources().getString(R.string.year)).append(mCurMonth + 1).append(getResources().getString(R.string.month)).append(mCurDate).append(getResources().getString(R.string.day)).append(mCurHour);
                    listener.onDataPicked(sb.toString());
                }
                dismiss();
                break;
            case R.id.cancelBtn:
                dismiss();
                break;
        }
    }

    public static boolean onBackPressed(Activity activity) {
        HCDatePickDialog dlg = getDlgView(activity);
        if (null != dlg && dlg.isShowing()) {
            dlg.dismiss();
            return true;
        }
        return false;
    }

    public static boolean hasDlg(Activity activity) {
        HCDatePickDialog dlg = getDlgView(activity);
        return dlg != null;
    }

    public static boolean isShowing(Activity activity) {
        HCDatePickDialog dlg = getDlgView(activity);
        if (null != dlg && dlg.isShowing()) {
            return true;
        }
        return false;
    }

    public static HCDatePickDialog getDlgView(Activity activity) {
        return (HCDatePickDialog) getRootView(activity).findViewById(R.id.view_date_pick_dlg);
    }

    private static FrameLayout getRootView(Activity activity) {
        return (FrameLayout) activity.findViewById(R.id.rootView);
    }

    public boolean isShowing() {
        return getVisibility() == View.VISIBLE;
    }

    public void show() {
        if (getParent() != null) {
            return;
        }
        rootView.addView(this);
        setVisibility(View.VISIBLE);
    }

    public void dismiss() {
        if (getParent() == null) {
            return;
        }
        setVisibility(View.GONE);
        rootView.removeView(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    private void setDate(int date) {
        if (date != mCurDate) {
            mCurDate = date;
        }
    }

    private void setYear(int year) {
        if (year != mCurYear) {
            mCurYear = year;
        }
    }

    private void setHour(String hourStr) {
        if (!hourStr.equals(mCurHour)) {
            mCurHour = hourStr;
        }
    }

    private void setMonth(int month) {
        if (month != mCurMonth) {
            mCurMonth = month;

            Calendar calendar = Calendar.getInstance();
            int date = calendar.get(Calendar.DATE);
            prepareDayData(mCurYear, month, date);
        }
    }

    private boolean isLeapYear(int year) {
        return ((0 == year % 4) && (0 != year % 100) || (0 == year % 400));
    }

    private void prepareData() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);

        mCurDate = day;
        mCurMonth = month;
        mCurYear = year;
        mCurHour = HOUR_NAME[0];

        for (int i = 0; i < MONTH_NAME.length; ++i) {
            mMonths.add(new TextInfo(i, MONTH_NAME[i], (i == month)));
        }

        for (int i = 2015; i <= 2030; ++i) {
            mYears.add(new TextInfo(i, String.valueOf(i), (i == year)));
        }

        for (int i = 0; i < HOUR_NAME.length; i++) {
            mHours.add(new TextInfo(i, HOUR_NAME[i], (i == 0)));
        }

        ((WheelTextAdapter) monthWheel.getAdapter()).setData(mMonths);
        ((WheelTextAdapter) yearWheel.getAdapter()).setData(mYears);
        ((WheelTextAdapter) hourWheel.getAdapter()).setData(mHours);

        prepareDayData(year, month, day);

        monthWheel.setSelection(month);
        yearWheel.setSelection(year - 2015);
        dayWheel.setSelection(day - 1);
        hourWheel.setSelection(0);
    }

    private void prepareDayData(int year, int month, int curDate) {
        mDates.clear();

        int days = DAYS_PER_MONTH[month];

        // The February.
        if (1 == month) {
            days = isLeapYear(year) ? 29 : 28;
        }

        for (int i = 1; i <= days; ++i) {
            mDates.add(new TextInfo(i, String.valueOf(i), (i == curDate)));
        }

        ((WheelTextAdapter) dayWheel.getAdapter()).setData(mDates);
    }

    protected class TextInfo {
        public TextInfo(int index, String text, boolean isSelected) {
            mIndex = index;
            mText = text;
            mIsSelected = isSelected;

            if (isSelected) {
                mColor = Color.BLUE;
            }
        }

        public int mIndex;
        public String mText;
        public boolean mIsSelected = false;
        public int mColor = Color.BLACK;
    }

    protected class WheelTextAdapter extends BaseAdapter {
        ArrayList<TextInfo> mData = null;
        int mWidth = ViewGroup.LayoutParams.MATCH_PARENT;
        int mHeight = 50;
        Context mContext = null;

        public WheelTextAdapter(Context context) {
            mContext = context;
            mHeight = DisplayUtil.dip2px(context, mHeight);
        }

        public void setData(ArrayList<TextInfo> data) {
            mData = data;
            this.notifyDataSetChanged();
        }

        public void setItemSize(int width, int height) {
            mWidth = width;
            mHeight = DisplayUtil.dip2px(mContext, height);
        }

        @Override
        public int getCount() {
            return (null != mData) ? mData.size() : 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = null;

            if (null == convertView) {
                convertView = new TextView(mContext);
                convertView.setLayoutParams(new TosGallery.LayoutParams(mWidth, mHeight));
                textView = (TextView) convertView;
                textView.setGravity(Gravity.CENTER);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                textView.setTextColor(Color.BLACK);
            }

            if (null == textView) {
                textView = (TextView) convertView;
            }

            TextInfo info = mData.get(position);
            textView.setText(info.mText);
            textView.setTextColor(info.mColor);

            return convertView;
        }
    }
}
