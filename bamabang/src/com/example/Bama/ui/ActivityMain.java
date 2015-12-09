package com.example.Bama.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.example.Bama.R;
import com.example.Bama.background.HCApplication;
import com.example.Bama.background.config.ServerConfig;
import com.example.Bama.ui.fragment.*;
import com.example.Bama.util.DownLoadTask;
import com.example.Bama.util.Request;
import com.example.Bama.util.VersionCompareUtil;
import com.example.Bama.widget.HGAlertDlg;
import com.example.Bama.widget.MoreWindow;
import com.meilishuo.gson.annotations.SerializedName;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.List;

public class ActivityMain extends ActivityBase {
	public static boolean isConflict = false;
    private final int SPLASH_DISPLAY_LENGHT = 3000; //延迟三秒

    private MoreWindow mMoreWindow;

	private LinearLayout mainLL;
	private ImageView mainImage;
	private LinearLayout newsLL;
	private ImageView newsImage;
	private LinearLayout shopLL;
	private ImageView shopImage;
	private LinearLayout findLL;
	private ImageView findImage;
	private LinearLayout mineLL;
	private ImageView mineImage;

	private Fragment mFragmentCurrent;
	private MainFragment fragmentMain;
	private NewsFragment fragmentNews;
	private ShopFragment fragmentShop;
	private FindFragment fragmentFind;
	private MeFragment fragmentMine;

	private List<ImageView> imageViews = new ArrayList<ImageView>();

	public static void open(Activity activity) {
		Intent intent = new Intent(activity, ActivityMain.class);
		activity.startActivity(intent);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.main);
		super.onCreate(savedInstanceState);
		MobclickAgent.updateOnlineConfig(ActivityMain.this);
		AnalyticsConfig.enableEncrypt(false);
        RequestUtil.checkVersion(ActivityMain.this);
        new Handler().postDelayed(new Runnable(){

            @Override
            public void run() {
                findViewById(R.id.splashview).setVisibility(View.GONE);
            }

        }, SPLASH_DISPLAY_LENGHT);
	}

	@Override
	protected void getViews() {
		mainLL = (LinearLayout) findViewById(R.id.mainLL);
		mainImage = (ImageView) findViewById(R.id.mainImage);
		newsLL = (LinearLayout) findViewById(R.id.newsLL);
		newsImage = (ImageView) findViewById(R.id.newsImage);
		shopLL = (LinearLayout) findViewById(R.id.shopLL);
		shopImage = (ImageView) findViewById(R.id.shopImage);
		findLL = (LinearLayout) findViewById(R.id.findLL);
		findImage = (ImageView) findViewById(R.id.findImage);
		mineLL = (LinearLayout) findViewById(R.id.mineLL);
		mineImage = (ImageView) findViewById(R.id.mineImage);

		imageViews.add(mainImage);
		imageViews.add(newsImage);
		imageViews.add(shopImage);
		imageViews.add(findImage);
		imageViews.add(mineImage);
	}

	@Override
	protected void initViews() {

	}

	@Override
	protected void setListeners() {
		mainLL.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				updateImageViewsStatus(0);
				if (fragmentMain == null) {
					fragmentMain = new MainFragment();
				}
				switchContent(mFragmentCurrent, fragmentMain);
			}
		});
		newsLL.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				updateImageViewsStatus(1);
				if (fragmentNews == null) {
					fragmentNews = new NewsFragment();
				}
				switchContent(mFragmentCurrent, fragmentNews);
			}
		});

		shopLL.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				updateImageViewsStatus(2);
				if (fragmentShop == null) {
					fragmentShop = new ShopFragment();
				}
				switchContent(mFragmentCurrent, fragmentShop);
			}
		});

		findLL.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

                showMoreWindow(view);

			}
		});

		mineLL.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				updateImageViewsStatus(4);
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
		fragmentMain = new MainFragment();
		transaction.add(R.id.fragementLayout, fragmentMain);
		transaction.commit();
		mFragmentCurrent = fragmentMain;
		updateImageViewsStatus(0);
	}

    private void showMoreWindow(View view) {
        if (null == mMoreWindow) {
            mMoreWindow = new MoreWindow(this);
            mMoreWindow.init();
        }
        mMoreWindow.setListener(new onMoreClickLinister() {
            @Override
            public void onClickOpertion(String string) {
                updateImageViewsStatus(3);
                fragmentFind = new FindFragment();
                switchContent(mFragmentCurrent, fragmentFind);
                fragmentFind.url = string;
            }
        });
        mMoreWindow.showMoreWindow(view,100);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mMoreWindow){
            mMoreWindow.destroy();
        }
    }

    public onMoreClickLinister moreClickLinister;
    public interface onMoreClickLinister{
        public void onClickOpertion(String string);
    }
}
