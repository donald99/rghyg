package com.example.Bama.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import com.example.Bama.R;
import com.example.Bama.Bean.GroupCircleEntity;
import com.example.Bama.adapter.GroupCircleAdapter;
import com.example.Bama.background.HCApplication;
import com.example.Bama.ui.ActivityGroupChat;
import com.example.Bama.ui.RequestUtil;
import com.example.Bama.ui.Views.ViewGroupListItem;
import com.example.Bama.util.ToastUtil;
import com.example.Bama.widget.RefreshListView;

import java.util.*;

/**
 * 单个群列表*
 */
public class GroupCircleFragment extends Fragment implements RefreshListView.OnRefreshListener, RefreshListView.OnLoadMoreListener {
    private final static String TAG = "GroupCircleFragment";
    private Activity activity;
    private List<GroupCircleEntity.ContentEntity> groupCircleList = new ArrayList<GroupCircleEntity.ContentEntity>();
    private RefreshListView mListView;
    private GroupCircleAdapter mAdapter;
    private String text;
    private int channel_id;
    private ImageView detail_loading;
    public final static int SET_NEWSLIST = 0;

    private int page = 1;
    private static final int pageSize = 10;

    /**
     * 界面显示隐藏*
     */
    private boolean hidden;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.group_list_fragment, null);
        mListView = (RefreshListView) view.findViewById(R.id.listView);

        mListView.setCanRefresh(true);
        mListView.setCanLoadMore(false);
        mListView.setOnRefreshListener(this);
        mListView.setOnLoadListener(this);

        TextView item_textview = (TextView) view.findViewById(R.id.item_textview);
        detail_loading = (ImageView) view.findViewById(R.id.detail_loading);
        item_textview.setText(text);

        Bundle args = getArguments();
        text = args != null ? args.getString("text") : "";
        channel_id = args != null ? args.getInt("id", 0) : 0;

        init();
        return view;
    }

    private void init(){
        detail_loading.setVisibility(View.GONE);
        mAdapter = new GroupCircleAdapter(activity, groupCircleList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (view instanceof ViewGroupListItem) {
                    ViewGroupListItem item = (ViewGroupListItem) view;
                    if (item.entity != null) {
                        String entity = HCApplication.getInstance().getGson().toJson(item.entity);
                        ActivityGroupChat.open(activity, entity);
                    } else {
                        ToastUtil.makeShortText("群id为空");
                    }
                }
            }
        });
        onRefresh();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        this.hidden = hidden;
//        if (!hidden) {
//            onRefresh();
//        }
    }

    @Override
    public void onRefresh() {
        groupCircleList.clear();
        RequestUtil.queryTagGroupList(activity, channel_id + "", 1 ,10 ,new QueryTagGroupListCallback() {
            @Override
            public void onSuccess(List<GroupCircleEntity.ContentEntity> entitys) {
                if (entitys != null) {
                    groupCircleList.addAll(entitys);
                    mListView.onRefreshComplete();
                    if(mAdapter!=null){
                        mAdapter.notifyDataSetChanged();
                    }else{
                        mAdapter = new GroupCircleAdapter(activity, groupCircleList);
                    }
                }
            }
            @Override
            public void onFail() {
                //TODO:onFail 再次请求逻辑
            }
        });
    }

    String getStrng(Context context, int resId) {
        return context.getResources().getString(resId);
    }

    @Override
    public void onLoadMore() {
        page++;

        RequestUtil.queryTagGroupList(activity, channel_id + "", page ,pageSize , new QueryTagGroupListCallback() {
            @Override
            public void onSuccess(List<GroupCircleEntity.ContentEntity> entitys) {
                if (entitys != null) {
                    groupCircleList.addAll(entitys);
                    mListView.onLoadMoreComplete();
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFail() {
                page--;
                mListView.onLoadMoreComplete();
                //TODO:onFail 再次请求逻辑
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        this.activity = activity;
        super.onAttach(activity);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mAdapter = null;
    }

    public interface QueryTagGroupListCallback {
        public void onSuccess(List<GroupCircleEntity.ContentEntity> entitys);

        public void onFail();
    }
}
