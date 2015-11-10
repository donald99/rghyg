package com.example.Bama.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.example.Bama.Bean.GroupCircleEntity;
import com.example.Bama.Bean.GroupMemberRankEntity;
import com.example.Bama.ui.Views.ViewGroupListItem;
import com.example.Bama.ui.Views.ViewGroupMemberRankItem;

import java.util.ArrayList;
import java.util.List;

public class GroupMemberRankAdapter extends BaseAdapter{
    private Activity activity;
    private List<GroupMemberRankEntity.ContentEntity.RankEntity> ranklist = new ArrayList<GroupMemberRankEntity.ContentEntity.RankEntity>();

    public GroupMemberRankAdapter(Activity activity, List<GroupMemberRankEntity.ContentEntity.RankEntity> groupsList) {
        this.activity = activity;
        this.ranklist = groupsList;
    }

    @Override
    public int getCount() {
       return ranklist.size();
    }

    @Override
    public GroupMemberRankEntity.ContentEntity.RankEntity getItem(int i) {
        return ranklist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewGroupMemberRankItem item = new ViewGroupMemberRankItem(activity);
        item.setModelItem(getItem(i));
        return item;
    }
}
