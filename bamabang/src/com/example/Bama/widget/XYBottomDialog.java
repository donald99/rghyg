package com.example.Bama.widget;

import android.app.Activity;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.Bama.R;

/**
 * Created by xiaoyu on 15-11-3.
 */
public class XYBottomDialog extends FrameLayout implements View.OnClickListener {
    private LinearLayout container;
    private TextView groupTips;
    private TextView reportGroup;
    private TextView exitGroup;
    private TextView cancel;
    private XYShareDialogListener listener;

    private FrameLayout rootView;

    private TranslateAnimation moveInAni;
    private TranslateAnimation moveOutAni;
    private static final int kAniDuration = 300;

    public interface XYShareDialogListener {
        public void groupMessageTip();

        public void reportGroup();

        public void exitGroup();

        public void cancel();
    }

    public static void showDialog(Activity context, XYShareDialogListener listener) {
        new XYBottomDialog(context, listener).show();
    }

    public XYBottomDialog(Activity context, XYShareDialogListener listener) {
        super(context);
        this.listener = listener;
        LayoutInflater li = LayoutInflater.from(context);
        li.inflate(R.layout.xy_bottom_dialog, this, true);
        container = (LinearLayout) findViewById(R.id.container);
        groupTips = (TextView) findViewById(R.id.groupTips);
        reportGroup = (TextView) findViewById(R.id.reportGroup);
        exitGroup = (TextView) findViewById(R.id.exitGroup);
        cancel = (TextView) findViewById(R.id.cancel);

        groupTips.setOnClickListener(this);
        reportGroup.setOnClickListener(this);
        exitGroup.setOnClickListener(this);
        cancel.setOnClickListener(this);

        setVisibility(View.GONE);
        rootView = getRootView(context);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        setLayoutParams(params);
        setId(R.id.xy_share_dialog);
        rootView.addView(this, params);
        moveInAni = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0);
        moveInAni.setDuration(kAniDuration);

        moveOutAni = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1);
        moveOutAni.setDuration(kAniDuration);
        moveOutAni.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    public static boolean onBackPressed(Activity activity) {
        XYBottomDialog dlg = getDlgView(activity);
        if (null != dlg && dlg.isShowing()) {
            dlg.dismiss();
            return true;
        }
        return false;
    }

    public static boolean hasDlg(Activity activity) {
        XYBottomDialog dlg = getDlgView(activity);
        return dlg != null;
    }

    public static boolean isShowing(Activity activity) {
        XYBottomDialog dlg = getDlgView(activity);
        if (null != dlg && dlg.isShowing()) {
            return true;
        }
        return false;
    }

    public static XYBottomDialog getDlgView(Activity activity) {
        return (XYBottomDialog) getRootView(activity).findViewById(R.id.xy_share_dialog);
    }

    private static FrameLayout getRootView(Activity activity) {
        return (FrameLayout) activity.findViewById(R.id.rootView);
    }

    public boolean isShowing() {
        return getVisibility() == View.VISIBLE;
    }

    public void show() {
        setVisibility(View.VISIBLE);
        container.startAnimation(moveInAni);
    }

    public void dismiss() {
        if (getParent() == null) {
            return;
        }
        container.startAnimation(moveOutAni);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getRawX();
        float y = event.getRawY();
        Rect rect = new Rect();
        container.getGlobalVisibleRect(rect);
        if (!rect.contains((int) x, (int) y)) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                dismiss();
            }
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        dismiss();
        switch (v.getId()) {
            case R.id.groupTips:
                if (listener != null) {
                    listener.groupMessageTip();
                }
                break;
            case R.id.reportGroup:
                if (listener != null) {
                    listener.reportGroup();
                }
                break;
            case R.id.exitGroup:
                if (listener != null) {
                    listener.exitGroup();
                }
                break;
        }
    }
}
