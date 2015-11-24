package com.example.Bama.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
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
import com.meilishuo.gson.annotations.SerializedName;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.List;

public class ActivityMain extends ActivityBase {
	public static boolean isConflict = false;

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
				updateImageViewsStatus(1);
				if (fragmentMain == null) {
					fragmentMain = new MainFragment();
				}
				switchContent(mFragmentCurrent, fragmentMain);
			}
		});
		newsLL.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				updateImageViewsStatus(0);
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
				updateImageViewsStatus(1);
				if (fragmentFind == null) {
					fragmentFind = new FindFragment();
				}
				switchContent(mFragmentCurrent, fragmentFind);
			}
		});

		mineLL.setOnClickListener(new View.OnClickListener() {
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
		fragmentMain = new MainFragment();
		transaction.add(R.id.fragementLayout, fragmentMain);
		transaction.commit();
		mFragmentCurrent = fragmentMain;
		updateImageViewsStatus(0);
	}

	/**
	 * 检查版本更新*
	 */
	private void showUpdateDialog() {
		Request.doRequest(this, new ArrayList<NameValuePair>(), ServerConfig.URL_VERSION_UPDATE, Request.GET, new Request.RequestListener() {
			@Override
			public void onException(Request.RequestException e) {

			}

			@Override
			public void onComplete(String response) {
				final VersionModel versionModel = HCApplication.getInstance().getGson().fromJsonWithNoException(response, VersionModel.class);
				if (versionModel != null) {
					if (!TextUtils.isEmpty(versionModel.version_code) && !TextUtils.isEmpty(versionModel.feature) && !TextUtils.isEmpty(versionModel.url)) {
						try {
							PackageInfo packInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
							String version = packInfo.versionName;
							if (!TextUtils.isEmpty(version) && VersionCompareUtil.compareVersion(versionModel.version_code, version) > 0) {
								HGAlertDlg.showDlg("发现新版本", versionModel.feature, ActivityMain.this, new HGAlertDlg.HGAlertDlgClickListener() {
									@Override
									public void onAlertDlgClicked(boolean isConfirm) {
										if (isConfirm) {
											new DownLoadTask(ActivityMain.this).startDownLoad(versionModel.url);
										}
									}
								});
							}
						} catch (PackageManager.NameNotFoundException e) {
							e.printStackTrace();
						}
					}
				}
			}
		});
	}

	class VersionModel {
		@SerializedName("version_code")
		public String version_code;

		@SerializedName("feature")
		public String feature;

		@SerializedName("url")
		public String url;
	}
}
