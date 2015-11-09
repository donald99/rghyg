package com.example.Bama.widget;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.example.Bama.R;

/**
 * Created by xiaoyu on 15-11-3.
 */
public class XYTipsDialog extends FrameLayout implements View.OnClickListener {
    private TextView tipTextView;
    private TextView sure;
    private FrameLayout rootView;
    private Activity activity;

    public static void showDialog(Activity context, String tipStr) {
        new XYTipsDialog(context, tipStr).show();
    }

    public XYTipsDialog(Activity context, String tipStr) {
        super(context);
        this.activity = context;
        LayoutInflater li = LayoutInflater.from(context);
        li.inflate(R.layout.xy_tips_dialog, this, true);
        tipTextView = (TextView) findViewById(R.id.tipTextView);
        sure = (TextView) findViewById(R.id.sure);

        if (!TextUtils.isEmpty(tipStr)) {
            tipTextView.setText(tipStr);
        }
        sure.setOnClickListener(this);

        setVisibility(View.GONE);
        rootView = getRootView(context);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        setLayoutParams(params);
        setId(R.id.xy_tips_dialog);
        rootView.addView(this, params);
    }

    public static boolean onBackPressed(Activity activity) {
        XYTipsDialog dlg = getDlgView(activity);
        if (null != dlg && dlg.isShowing()) {
            dlg.dismiss();
            return true;
        }
        return false;
    }

    public static boolean hasDlg(Activity activity) {
        XYTipsDialog dlg = getDlgView(activity);
        return dlg != null;
    }

    public static boolean isShowing(Activity activity) {
        XYTipsDialog dlg = getDlgView(activity);
        if (null != dlg && dlg.isShowing()) {
            return true;
        }
        return false;
    }

    public static XYTipsDialog getDlgView(Activity activity) {
        return (XYTipsDialog) getRootView(activity).findViewById(R.id.xy_tips_dialog);
    }

    private static FrameLayout getRootView(Activity activity) {
        return (FrameLayout) activity.findViewById(R.id.rootView);
    }

    public boolean isShowing() {
        return getVisibility() == View.VISIBLE;
    }

    public void show() {
        setVisibility(View.VISIBLE);
    }

    public void dismiss() {
        if (getParent() == null) {
            return;
        }
        setVisibility(View.GONE);
        getRootView(activity).removeView(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }
}
