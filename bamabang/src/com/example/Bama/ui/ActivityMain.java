package com.example.Bama.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.example.Bama.R;
import com.example.Bama.ui.fragment.GroupFragment;
import com.example.Bama.ui.fragment.MeFragment;
import com.example.Bama.ui.fragment.NewsFragment;
import com.example.Bama.ui.fragment.ShopFragment;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ActivityMain extends ActivityBase {

    /**
     * Called when the activity is first created.
     */

    public static boolean isConflict = false;

    private FrameLayout newsFL;
    private ImageView newsImage;
    private FrameLayout groupFL;
    private ImageView groupImage;
    private FrameLayout shopFL;
    private ImageView shopImage;
    private FrameLayout mineFL;
    private ImageView mineImage;

    private Fragment mFragmentCurrent;
    private NewsFragment fragmentNews;
    private GroupFragment fragmentGroup;
    private ShopFragment fragmentShop;
    private MeFragment fragmentMine;

    private List<ImageView> imageViews = new ArrayList<ImageView>();

	public static void open(Activity activity){
		Intent intent = new Intent(activity,ActivityMain.class);
		activity.startActivity(intent);
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.main);
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void getViews() {
        newsFL = (FrameLayout) findViewById(R.id.newsFl);
        newsImage = (ImageView) findViewById(R.id.newsImage);
        groupFL = (FrameLayout) findViewById(R.id.groupFL);
        groupImage = (ImageView) findViewById(R.id.groupImage);
        shopFL = (FrameLayout) findViewById(R.id.shopFL);
        shopImage = (ImageView) findViewById(R.id.shopImage);
        mineFL = (FrameLayout) findViewById(R.id.mineFL);
        mineImage = (ImageView) findViewById(R.id.mineImage);

        imageViews.add(newsImage);
        imageViews.add(groupImage);
        imageViews.add(shopImage);
        imageViews.add(mineImage);
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void setListeners() {
        newsFL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateImageViewsStatus(0);
                if (fragmentNews == null) {
                    fragmentNews = new NewsFragment();
                }
                switchContent(mFragmentCurrent, fragmentNews);
            }
        });

        groupFL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateImageViewsStatus(1);
                if (fragmentGroup == null) {
                    fragmentGroup = new GroupFragment();
                }
                switchContent(mFragmentCurrent, fragmentGroup);
            }
        });

        shopFL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateImageViewsStatus(2);
                if (fragmentShop == null) {
                    fragmentShop = new ShopFragment();
                }
                switchContent(mFragmentCurrent, fragmentShop);
            }
        });

        mineFL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateImageViewsStatus(3);
                if (fragmentMine == null) {
                    fragmentMine = new MeFragment();
                }
                switchContent(mFragmentCurrent, fragmentMine);
            }
        });

        setDefaultFragment();
    }

    private void updateImageViewsStatus(int index) {
        for (int i = 0; i < imageViews.size(); i++) {
            ImageView view = imageViews.get(i);
            if (i == index) {
                view.setSelected(true);
            } else {
                view.setSelected(false);
            }
        }
    }

    public void switchContent(Fragment from, Fragment to) {
        if (from != to) {
            mFragmentCurrent = to;
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (!to.isAdded()) {
                if (from != null && from.isAdded()) {
                    transaction.hide(from);
                }
                // 隐藏当前的fragment，add下一个到Activity中
                transaction.add(R.id.fragementLayout, to).commitAllowingStateLoss();
            } else {
                // 隐藏当前的fragment，显示下一个
                transaction.hide(from).show(to).commitAllowingStateLoss();
            }
        }
    }

    private void setDefaultFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        fragmentNews = new NewsFragment();
        transaction.add(R.id.fragementLayout, fragmentNews);
        transaction.commit();
        mFragmentCurrent = fragmentNews;
        updateImageViewsStatus(0);
    }
}
