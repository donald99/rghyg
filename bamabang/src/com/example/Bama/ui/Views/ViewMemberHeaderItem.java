package com.example.Bama.ui.Views;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.Bama.Bean.GroupMemberEntity;
import com.example.Bama.R;
import com.example.Bama.background.HCApplication;
import com.example.Bama.ui.ActivityBase;
import com.example.Bama.util.ImageLoaderUtil;

public class ViewMemberHeaderItem  extends LinearLayout {
    private ImageView imageView;
    private GroupMemberEntity model;
    private ActivityBase activity;
    private TextView name;

    public ViewMemberHeaderItem(Context context) {
        super(context);
        initView(context);
    }

    public ViewMemberHeaderItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        activity = (ActivityBase) context;
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.view_group_member, this);
        imageView = (ImageView) findViewById(R.id.image);
        name = (TextView) findViewById(R.id.name);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity,"avatar",Toast.LENGTH_LONG).show();
            }
        });
    }
    public void setData(GroupMemberEntity modelCategory) {
        this.model = modelCategory;
        name.setText(modelCategory.name);
        if (TextUtils.isEmpty(model.avatar)) {
            return;
        }
        HCApplication.getInstance().getImageLoader().displayImage(model.avatar, imageView, ImageLoaderUtil.Options_Memory_Rect_Avatar);

    }

}
