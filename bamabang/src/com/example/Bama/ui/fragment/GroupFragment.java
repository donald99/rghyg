package com.example.Bama.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.Bama.Bean.ChannelItem;
import com.example.Bama.Bean.ChannelManage;
import com.example.Bama.R;
import com.example.Bama.adapter.GroupCircleFragmentPagerAdapter;
import com.example.Bama.ui.ActivityBase;
import com.example.Bama.ui.ActivityCreateGroup;
import com.example.Bama.util.DisplayUtil;
import com.example.Bama.widget.ColumnHorizontalScrollView;

import java.util.ArrayList;

public class GroupFragment extends Fragment implements View.OnClickListener{
    private ActivityBase activity;
    private ColumnHorizontalScrollView mColumnHorizontalScrollView;
    private LinearLayout mRadioGroup_content;
    public ImageView shade_left;
    public ImageView shade_right;
    private ViewPager mViewPager;
    private TextView creatGroup;

    ArrayList<ChannelItem> userChannelList;
    private int columnSelectIndex = 0;
    private int mScreenWidth = 0;
    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mygroup,null);
        initView(rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (ActivityBase) getActivity();
        mScreenWidth = DisplayUtil.getWindowsWidth(activity);
        setChangelView();
    }

    private void initView(View rootView) {
        creatGroup = (TextView) rootView.findViewById(R.id.creatgroup);
        mColumnHorizontalScrollView =  (ColumnHorizontalScrollView)rootView.findViewById(R.id.mColumnHorizontalScrollView);
        mRadioGroup_content = (LinearLayout)rootView.findViewById(R.id.mRadioGroup_content);
        mViewPager = (ViewPager) rootView.findViewById(R.id.mViewPager);
        shade_left = (ImageView) rootView.findViewById(R.id.shade_left);
        shade_right = (ImageView) rootView.findViewById(R.id.shade_right);

        creatGroup.setOnClickListener(this);
    }

    private void setChangelView() {
        initColumnData();
        initTabColumn();
        initFragment();
    }

    private void initColumnData() {
        userChannelList = ((ArrayList<ChannelItem>) ChannelManage.getManage().defaultChannels);
    }

    private void initTabColumn() {
        mRadioGroup_content.removeAllViews();
        int count =  userChannelList.size();
        mColumnHorizontalScrollView.setParam(activity, DisplayUtil.getWindowsWidth(activity), mRadioGroup_content, shade_left, shade_right);
        for(int i = 0; i< count; i++){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT , ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 5;
            params.rightMargin = 5;
            TextView columnTextView = new TextView(activity);
            columnTextView.setTextAppearance(activity, R.style.top_category_scroll_view_item_text);
            columnTextView.setBackgroundResource(R.drawable.radio_buttong_bg);
            columnTextView.setGravity(Gravity.CENTER);
            columnTextView.setPadding(5, 5, 5, 5);
            columnTextView.setId(i);
            columnTextView.setText(userChannelList.get(i).getName());
            columnTextView.setTextColor(getResources().getColorStateList(R.color.top_category_scroll_text_color_day));
            if(columnSelectIndex == i){
                columnTextView.setSelected(true);
            }
            columnTextView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    for(int i = 0;i < mRadioGroup_content.getChildCount();i++){
                        View localView = mRadioGroup_content.getChildAt(i);
                        if (localView != v)
                            localView.setSelected(false);
                        else{
                            localView.setSelected(true);
                            mViewPager.setCurrentItem(i);
                        }
                    }
                }
            });
            mRadioGroup_content.addView(columnTextView, i ,params);
        }
    }

    private void selectTab(int tab_postion) {
        columnSelectIndex = tab_postion;
        for (int i = 0; i < mRadioGroup_content.getChildCount(); i++) {
            View checkView = mRadioGroup_content.getChildAt(tab_postion);
            int k = checkView.getMeasuredWidth();
            int l = checkView.getLeft();
            int i2 = l + k / 2 - mScreenWidth / 2;
            mColumnHorizontalScrollView.smoothScrollTo(i2, 0);
        }
        for (int j = 0; j <  mRadioGroup_content.getChildCount(); j++) {
            View checkView = mRadioGroup_content.getChildAt(j);
            boolean ischeck;
            if (j == tab_postion) {
                ischeck = true;
            } else {
                ischeck = false;
            }
            checkView.setSelected(ischeck);
        }
    }

    private void initFragment() {
        fragments.clear();
        int count =  userChannelList.size();
        for(int i = 0; i< count;i++){
            Bundle data = new Bundle();
            data.putString("text", userChannelList.get(i).getName());
            data.putInt("id", userChannelList.get(i).getId());
            GroupCircleFragment newfragment = new GroupCircleFragment();
            newfragment.setArguments(data);
            fragments.add(newfragment);
        }
        GroupCircleFragmentPagerAdapter mAdapetr = new GroupCircleFragmentPagerAdapter(getFragmentManager(), fragments);
		mViewPager.setOffscreenPageLimit(6);
        mViewPager.setAdapter(mAdapetr);
        mViewPager.setOnPageChangeListener(pageListener);
    }

    public ViewPager.OnPageChangeListener pageListener= new ViewPager.OnPageChangeListener(){

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int position) {
            mViewPager.setCurrentItem(position);
            selectTab(position);
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.creatgroup:
                ActivityCreateGroup.open(activity);
                break;
        }
    }
}
