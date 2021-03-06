package com.example.Bama.widget;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.Bama.R;
import com.example.Bama.background.HCApplication;
import com.example.Bama.util.ImageLoaderUtil;
import com.example.Bama.util.UserInfoManager;

public class XYGroupCustomerDialog extends FrameLayout implements View.OnClickListener {
    private ImageView avatar;
    private TextView nickName;
    private TextView isGroupMater;
    private TextView groupAge;
    private ImageView sexImage;
    private TextView atTa;
    private TextView report;
    private TextView delete;
    private TextView cancel;
    private View layout;

    private FrameLayout rootView;
    private Activity activity;
    private XYGroupCustomerDialogListener listener;

    public interface XYGroupCustomerDialogListener {
        public void onAtTaClicked();

        public void onReportClicked();

        public void onDeleteClicked();

        public void onCancelClicked();
    }

    public static void showDialog(Activity context, View view,XYGroupCustomerDialogListener listener) {
        new XYGroupCustomerDialog(context, view,listener).show();
    }

    public XYGroupCustomerDialog(Activity context, View view,XYGroupCustomerDialogListener listener) {
        super(context);
        this.listener = listener;
        this.activity = context;
        LayoutInflater li = LayoutInflater.from(context);
        li.inflate(R.layout.xy_group_customer_dialog, this, true);

        UserInfoManager.UserInfoModel member = (UserInfoManager.UserInfoModel) view.getTag();

        avatar = (ImageView) findViewById(R.id.avatar);
        nickName = (TextView) findViewById(R.id.nickName);
        isGroupMater = (TextView) findViewById(R.id.isGroupMater);
        groupAge = (TextView) findViewById(R.id.groupAge);
        sexImage = (ImageView) findViewById(R.id.sexImage);
        atTa = (TextView) findViewById(R.id.atTa);
        report = (TextView) findViewById(R.id.report);
        delete = (TextView) findViewById(R.id.delete);
        cancel = (TextView) findViewById(R.id.cancel);
        layout = (View) findViewById(R.id.layout);

        atTa.setOnClickListener(this);
        report.setOnClickListener(this);
        delete.setOnClickListener(this);
        cancel.setOnClickListener(this);
        layout.setOnClickListener(this);

        if(member!=null){
            HCApplication.getInstance().getImageLoader().displayImage(member.avatar,avatar, ImageLoaderUtil.Options_Common_memory_Pic);
            nickName.setText(member.username);
            if("0".equals(member.gender)){
                sexImage.setBackground(getResources().getDrawable(R.drawable.girl));
            }else{
                sexImage.setBackground(getResources().getDrawable(R.drawable.boy));
            }
            isGroupMater.setVisibility(GONE);
            groupAge.setText(member.baby);
        }

        setVisibility(View.GONE);
        rootView = getRootView(context);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        setLayoutParams(params);
        setId(R.id.xy_group_customer_dialog);
        rootView.addView(this, params);
    }

    public static boolean onBackPressed(Activity activity) {
        XYGroupCustomerDialog dlg = getDlgView(activity);
        if (null != dlg && dlg.isShowing()) {
            dlg.dismiss();
            return true;
        }
        return false;
    }

    public static boolean hasDlg(Activity activity) {
        XYGroupCustomerDialog dlg = getDlgView(activity);
        return dlg != null;
    }

    public static boolean isShowing(Activity activity) {
        XYGroupCustomerDialog dlg = getDlgView(activity);
        if (null != dlg && dlg.isShowing()) {
            return true;
        }
        return false;
    }

    public static XYGroupCustomerDialog getDlgView(Activity activity) {
        return (XYGroupCustomerDialog) getRootView(activity).findViewById(R.id.xy_group_customer_dialog);
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
        switch (v.getId()) {
            case R.id.atTa:
                if (listener != null) {
                    listener.onAtTaClicked();
                }
                break;
            case R.id.report:
                if (listener != null) {
                    listener.onReportClicked();
                }
                break;
            case R.id.delete:
                if (listener != null) {
                    listener.onDeleteClicked();
                }
                break;
            case R.id.layout:
            case R.id.cancel:
                if (listener != null) {
                    listener.onCancelClicked();
                }
                break;
        }
    }
}
