package com.example.Bama.widget;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.example.Bama.R;
import com.example.Bama.ui.ActivityBase;
import com.example.Bama.ui.ActivityMain;
import com.example.Bama.ui.ActivityWeb;
import com.example.Bama.ui.Base;
import com.example.Bama.util.ToastUtil;

public class MoreWindow extends PopupWindow implements OnClickListener{

	private String TAG = MoreWindow.class.getSimpleName();
	Activity mContext;
	private int mWidth;
	private int mHeight;
	private int statusBarHeight ;
	private Bitmap mBitmap= null;
	private Bitmap overlay = null;
	
	private Handler mHandler = new Handler();

    public MoreWindow(Activity context) {
		mContext = context;
	}

	public void init() {
		Rect frame = new Rect();
		mContext.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		statusBarHeight = frame.top;
		DisplayMetrics metrics = new DisplayMetrics();
		mContext.getWindowManager().getDefaultDisplay()
				.getMetrics(metrics);
		mWidth = metrics.widthPixels;
		mHeight = metrics.heightPixels;
		
		setWidth(mWidth);
		setHeight(mHeight);
	}
	
	private Bitmap blur() {
		if (null != overlay) {
			return overlay;
		}
		long startMs = System.currentTimeMillis();

		View view = mContext.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache(true);
		mBitmap = view.getDrawingCache();
		
		float scaleFactor = 8;//图片锟斤拷锟脚憋拷锟斤拷
		float radius = 10;//模锟斤拷潭锟�
		int width = mBitmap.getWidth();
		int height =  mBitmap.getHeight();

		overlay = Bitmap.createBitmap((int) (width / scaleFactor),(int) (height / scaleFactor),Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(overlay);
		canvas.scale(1 / scaleFactor, 1 / scaleFactor);
		Paint paint = new Paint();
		paint.setFlags(Paint.FILTER_BITMAP_FLAG);
//		canvas.drawBitmap(mBitmap, 0, 0, paint);

//        canvas.drawColor(Color.parseColor("#ff0000"));

		overlay = FastBlur.doBlur(overlay, (int) radius, true);
		Log.i(TAG, "blur time is:"+(System.currentTimeMillis() - startMs));
		return overlay;
	}
	

	public void showMoreWindow(View anchor,int bottomMargin) {
		final RelativeLayout layout = (RelativeLayout)LayoutInflater.from(mContext).inflate(R.layout.center_music_more_window, null);
		setContentView(layout);

        ImageView close= (ImageView)layout.findViewById(R.id.center_music_window_close);
        LayoutParams params =new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        params.bottomMargin = bottomMargin;
        params.addRule(RelativeLayout.BELOW, R.id.more_window_fourm);
        params.addRule(RelativeLayout.RIGHT_OF, R.id.more_window_search);
        params.topMargin = 200;
        params.leftMargin = 18;
        close.setLayoutParams(params);

        layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isShowing()) {
					closeAnimation(layout);
				}
			}

		});
		
		showAnimation(layout);
		setBackgroundDrawable(new BitmapDrawable(mContext.getResources(), blur()));
		setOutsideTouchable(true);
		setFocusable(true);
		showAtLocation(anchor, Gravity.BOTTOM, 0, statusBarHeight);
	}

	private void showAnimation(ViewGroup layout){
		for(int i=0;i<layout.getChildCount();i++){
			final View child = layout.getChildAt(i);

			child.setOnClickListener(this);
			child.setVisibility(View.INVISIBLE);
			mHandler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					child.setVisibility(View.VISIBLE);
					ValueAnimator fadeAnim = ObjectAnimator.ofFloat(child, "translationY", 600, 0);
					fadeAnim.setDuration(300);
					KickBackAnimator kickAnimator = new KickBackAnimator();
					kickAnimator.setDuration(150);
					fadeAnim.setEvaluator(kickAnimator);
					fadeAnim.start();
				}
			}, i * 50);
		}
		
	}

	private void closeAnimation(ViewGroup layout){
		for(int i=0;i<layout.getChildCount();i++){
			final View child = layout.getChildAt(i);
			child.setOnClickListener(this);
//			mHandler.postDelayed(new Runnable() {
//
//				@Override
//				public void run() {
//					child.setVisibility(View.VISIBLE);
//					ValueAnimator fadeAnim = ObjectAnimator.ofFloat(child, "translationY", 0, 600);
//					fadeAnim.setDuration(200);
//					KickBackAnimator kickAnimator = new KickBackAnimator();
//					kickAnimator.setDuration(100);
//					fadeAnim.setEvaluator(kickAnimator);
//					fadeAnim.start();
//					fadeAnim.addListener(new AnimatorListener() {
//
//						@Override
//						public void onAnimationStart(Animator animation) {
//							// TODO Auto-generated method stub
//
//						}
//
//						@Override
//						public void onAnimationRepeat(Animator animation) {
//							// TODO Auto-generated method stub
//
//						}
//
//						@Override
//						public void onAnimationEnd(Animator animation) {
//							child.setVisibility(View.INVISIBLE);
//						}
//
//						@Override
//						public void onAnimationCancel(Animator animation) {
//							// TODO Auto-generated method stub
//
//						}
//					});
//				}
//			}, (layout.getChildCount()-i-1) * 30);
			
			if(child.getId() == R.id.more_window_group){
				mHandler.postDelayed(new Runnable() {

					@Override
					public void run() {
						dismiss();
					}
				}, (layout.getChildCount()-i) * 30 + 80);
			}
		}
		
	}

	@Override
	public void onClick(View v) {
        String url = "";
		switch (v.getId()) {
		case R.id.more_window_group:
            url = Base.groupUrl;
			break;
		case R.id.more_window_picture:
            url = Base.pictureUrl;
			break;
		case R.id.more_window_risk:
            url = Base.taskUrl;
			break;
		case R.id.more_window_fourm:
            url = Base.froumUrl;
			break;
        case R.id.more_window_search:
            url = Base.searchUrl;
			break;
		default:
			break;
		}
        morelinister.onClickOpertion(url);
        closeAnimation((ViewGroup)v.getParent());
    }
	
	public void destroy() {
		if (null != overlay) {
			overlay.recycle();
			overlay = null;
			System.gc();
		}
		if (null != mBitmap) {
			mBitmap.recycle();
			mBitmap = null;
			System.gc();
		}
	}

    private ActivityMain.onMoreClickLinister morelinister;
    public void setListener(ActivityMain.onMoreClickLinister listener) {
        this.morelinister = listener;
    }
}
