package com.example.Bama.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.Bama.Bean.ChannelItem;
import com.example.Bama.R;
import com.example.Bama.adapter.GroupCircleFragmentPagerAdapter;
import com.example.Bama.ui.ActivityBase;
import com.example.Bama.ui.ActivityCreateGroup;
import com.example.Bama.ui.RequestUtil;
import com.example.Bama.util.DisplayUtil;
import com.example.Bama.widget.ColumnHorizontalScrollView;
import com.example.Bama.widget.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

public class GroupFragment extends Fragment implements View.OnClickListener{
    private ActivityBase activity;
    private ViewPager mViewPager;
    private TextView creatGroup;

    ArrayList<ChannelItem.ContentEntity> userChannelList;
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
        if(activity.getIntent() != null){
            columnSelectIndex = activity.getIntent().getIntExtra("tab",0);
        }
        initColumnData();
    }

    private void initView(View rootView) {
        creatGroup = (TextView) rootView.findViewById(R.id.creatgroup);
        mViewPager = (ViewPager) rootView.findViewById(R.id.mViewPager);
        tabStrip = (PagerSlidingTabStrip) rootView.findViewById(R.id.id_stickynavlayout_indicator);

        creatGroup.setOnClickListener(this);

        rootView.findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
    }

    private void initColumnData() {
        RequestUtil.queryTagList(activity,new QueryTagListCallback(){
            @Override
            public void onSuccess(List list) {
                if(list!=null){
                    userChannelList  = (ArrayList<ChannelItem.ContentEntity>)list;

                    initFragment();
                    initTabsValue();
                }
            }

            @Override
            public void onFail() {

            }
        });
    }

    @Override
    public void setRetainInstance(boolean retain) {
        super.setRetainInstance(retain);
    }

    private void initFragment() {
        fragments.clear();
        int count =  userChannelList.size();
        for(int i = 0; i< count;i++){
            Bundle data = new Bundle();
            data.putString("text", userChannelList.get(i).name);
            data.putInt("id", userChannelList.get(i).tagid);
            GroupCircleFragment newfragment = new GroupCircleFragment();
            newfragment.setArguments(data);
            fragments.add(newfragment);
        }
        CirclesAdapter mAdapetr = new CirclesAdapter(activity.getSupportFragmentManager());
		mViewPager.setOffscreenPageLimit(count);
        mViewPager.setAdapter(mAdapetr);
        mViewPager.setOnPageChangeListener(pageListener);
        tabStrip.setViewPager(mViewPager);

        if (userChannelList != null && userChannelList.size() > 2) {
            mViewPager.setOffscreenPageLimit(((userChannelList.size() / 3) * 2) + 1);
        } else {
            mViewPager.setOffscreenPageLimit(userChannelList.size());
        }

        if (userChannelList.size() <= 4) {//TODO
            tabStrip.setShouldExpand(true);
        } else {
            tabStrip.setShouldExpand(false);
        }

        mViewPager.setCurrentItem(columnSelectIndex);
    }

    class CirclesAdapter extends FragmentPagerAdapter {
        public CirclesAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return userChannelList.get(position).name;
        }

        @Override
        public int getCount() {
            return userChannelList.size();
        }
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

    public interface QueryTagListCallback{
        public void onSuccess(List list);
        public void onFail();
    }

    private PagerSlidingTabStrip tabStrip;
    private void initTabsValue() {
        // 底部游标颜色
        tabStrip.setIndicatorColor(Color.parseColor("#FED9DB"));
        // tab的分割线颜色
        tabStrip.setDividerColor(Color.parseColor("#FED9DB"));

        tabStrip.setUnderlineColorResource(R.color.second_color);
        // tab背景
        tabStrip.setBackgroundColor(getResources().getColor(R.color.common_white));
        // tab底线高度
        tabStrip.setUnderlineHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 1, getResources().getDisplayMetrics()));
        // 游标高度
        tabStrip.setIndicatorHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics()));
        // 正常文字颜色
        tabStrip.setTextColor(getResources().getColor(R.color.second_tab_text_color2));
        tabStrip.setTextSize(14);
        tabStrip.setSelectedTextColor(getResources().getColor(R.color.select_tab_text_color));
        tabStrip.setShouldExpand(true);
    }
}
