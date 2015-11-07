package com.example.Bama.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class DisplayUtil {

	public static int px2dip(float pxValue, float scale) {
		return (int) (pxValue / scale + 0.5f);
	}

	public static int dip2px(float dipValue, float scale) {
		return (int) (dipValue * scale + 0.5f);
	}

	public static int dipToPixels(Context context, int dip) {
		Resources r = context.getResources();
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip,
				r.getDisplayMetrics());
		return (int) px;
	}

	public static int dip2px(Context context, float dipValue){
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int)(dipValue * scale + 0.5f);
	}

	public static int px2dip(Context context, float pxValue){
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int)(pxValue / scale + 0.5f);
	}


	public static int px2sp(float pxValue, float fontScale) {
		return (int) (pxValue / fontScale + 0.5f);
	}

	public static int sp2px(float spValue, float fontScale) {
		return (int) (spValue * fontScale + 0.5f);
	}

	public static int getReSizeHeight(int nowWidth, int bmpWidth, int bmpHeight) {
		return (int) (nowWidth * (bmpHeight * 1.0 / bmpWidth));
	}

	public static int getReSizeWidth(int nowHeight, int bmpWidth, int bmpHeight) {
		return (int) (nowHeight * (bmpWidth * 1.0 / bmpHeight));
	}

	public static void hideSolftInput(Context context, View v) {
		if (v == null) {
			return;
		}
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive()) {
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		}
	}

	public static void openSoftInput(Context context, View v) {
		if (v == null) {
			return;
		}
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);

		imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
		imm.showSoftInput(v, 0);
		// imm.showSoftInputFromInputMethod(v.getWindowToken(), 0);
	}


	public static void setBackgroundKeepPadding(View view, int resid) {
		int bottom = view.getPaddingBottom();
		int top = view.getPaddingTop();
		int right = view.getPaddingRight();
		int left = view.getPaddingLeft();
		view.setBackgroundResource(resid);
		view.setPadding(left, top, right, bottom);
	}

	@SuppressLint("NewApi")
	public static void setBackgroundKeepPadding(View view, Drawable drawable) {
		int bottom = view.getPaddingBottom();
		int top = view.getPaddingTop();
		int right = view.getPaddingRight();
		int left = view.getPaddingLeft();
		if (Build.VERSION.SDK_INT >= 16) {
			view.setBackground(drawable);
		} else {
			view.setBackgroundDrawable(drawable);
		}
		view.setPadding(left, top, right, bottom);
	}

	public static Rect getNinePatchPading(Context context, int drawalbeId) {
		Rect rect = new Rect();
		Drawable drawable = context.getResources().getDrawable(drawalbeId);
		if (drawable instanceof NinePatchDrawable) {
			NinePatchDrawable ninePatchDrawable = (NinePatchDrawable) drawable;
			ninePatchDrawable.getPadding(rect);
		}
		return rect;

	}

	public static float getFontHeight(float fontSize)
	{
	    Paint paint = new Paint();
	    paint.setTextSize(fontSize);
	    FontMetrics fm = paint.getFontMetrics();
	    return fm.bottom - fm.top ;
	}

	public static float getFontHeightOnlyText(float fontSize)  
	{  
	    Paint paint = new Paint();  
	    paint.setTextSize(fontSize);  
	    FontMetrics fm = paint.getFontMetrics();  
	    return fm.descent - fm.ascent ;  
	}

    public final static int getWindowsWidth(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }
	
}