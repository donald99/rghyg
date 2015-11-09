package com.example.Bama.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.example.Bama.Bean.GroupCircleEntity;
import com.example.Bama.ui.Views.ViewGroupListItem;

import java.util.ArrayList;

public class GroupCircleAdapter extends BaseAdapter{
    private Activity activity;
    private ArrayList<GroupCircleEntity> groupsList = new ArrayList<GroupCircleEntity>();

    public GroupCircleAdapter(Activity activity, ArrayList<GroupCircleEntity> groupsList) {
        this.activity = activity;
        this.groupsList = groupsList;
    }

    @Override
    public int getCount() {
       return groupsList.size();
    }

	@Override
	public GroupCircleEntity getItem(int position) {
		return groupsList.get(position);
	}

	@Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewGroupListItem item = null;
		if(view != null){
			item = (ViewGroupListItem)view;
		}else{
			item = new ViewGroupListItem(activity);
		}
        item.setGroupListItem(groupsList.get(i));
        return item;
    }
}
