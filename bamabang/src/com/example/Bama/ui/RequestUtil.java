package com.example.Bama.ui;

import android.content.Context;
import android.text.TextUtils;
import com.example.Bama.Bean.ChannelItem;
import com.example.Bama.Bean.GroupCircleEntity;
import com.example.Bama.Bean.GroupCreateInfoEntity;
import com.example.Bama.background.HCApplication;
import com.example.Bama.background.config.ServerConfig;
import com.example.Bama.ui.fragment.GroupCircleFragment;
import com.example.Bama.ui.fragment.GroupFragment;
import com.example.Bama.util.Request;
import com.example.Bama.util.ToastUtil;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class RequestUtil {
    private RequestUtil mRequestUtil;
    public RequestUtil getIntance(){
        if (mRequestUtil==null){
            mRequestUtil = new RequestUtil();
            return mRequestUtil;
        }
        return mRequestUtil;
    }

    public static void jubaoGroup(Context context,String fortype,String froid, String jubaoInfo){
        if (TextUtils.isEmpty(HCApplication.getInstance().getAccount().userId)){
            ToastUtil.makeLongText("请先登录");
            return;
        }
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("uid",HCApplication.getInstance().getAccount().userId));
        params.add(new BasicNameValuePair("fortype",fortype));
        params.add(new BasicNameValuePair("froid",froid));
        params.add(new BasicNameValuePair("msg",jubaoInfo));

        Request.doRequest(context, params, ServerConfig.URL_GET_CONTACT_REPORT, Request.GET, new Request.RequestListener() {

            @Override
            public void onException(Request.RequestException e) {
            }

            @Override
            public void onComplete(String response) {

                if (!TextUtils.isEmpty(response)) {
                    try {
                        JSONObject object = new JSONObject(response);

                        if (response.contains("OK")){
                            ToastUtil.makeLongText("举报成功");
                        }else{
                            ToastUtil.makeLongText("举报失败");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else{
                    ToastUtil.makeLongText("举报失败");
                }
            }
        });
    }

    public static void queryTagList(Context context,final GroupFragment.QueryTagListCallback callback){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        Request.doRequest(context, params, ServerConfig.URL_GET_TAG_LIST, Request.GET, new Request.RequestListener() {

            @Override
            public void onException(Request.RequestException e) {
                if (callback!=null){
                    callback.onFail();
                }
            }

            @Override
            public void onComplete(String response) {

                if (!TextUtils.isEmpty(response)) {
                    ChannelItem item = HCApplication.getInstance().getGson().fromJsonWithNoException(response,ChannelItem.class);
                    if (item!=null && item.status && item.content!=null && callback != null){
                        callback.onSuccess(item.content);
                    }
                }else{
                    if (callback!=null){
                        callback.onFail();
                    }
//                    ToastUtil.makeLongText("获取群组失败");
                }
            }
        });
    }

    public static void queryTagGroupList(Context context, String tagId,final GroupCircleFragment.QueryTagGroupListCallback callback){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        Request.doRequest(context, params, ServerConfig.URL_GET_TAG_DETAIL, Request.GET, new Request.RequestListener() {

            @Override
            public void onException(Request.RequestException e) {
                if (callback!=null){
                    callback.onFail();
                }
            }

            @Override
            public void onComplete(String response) {

                if (!TextUtils.isEmpty(response)) {
                    GroupCircleEntity item = HCApplication.getInstance().getGson().fromJsonWithNoException(response,GroupCircleEntity.class);
                    if (item!=null && item.status && item.content!=null && callback != null){
                        callback.onSuccess(item.content);
                    }
                }else{
                    if (callback!=null){
                        callback.onFail();
                    }
                    ToastUtil.makeLongText("获取群组列表失败");
                }
            }
        });
    }

    /**
     * 创建群组
     * @param uid: 用户id
     * @param name: 聊天群名称
     * @param description: 聊天群描述
     * @param tagid: 聊天群组id
     * @param callback
     */
    public static void createGroup(Context context,final ActivityCreateGroup.CreateGroupCallBack callback){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        Request.doRequest(context, params, ServerConfig.URL_GROUP_ADD, Request.GET, new Request.RequestListener() {

            @Override
            public void onException(Request.RequestException e) {
                ToastUtil.makeLongText("创建群组失败");
                if(callback!=null){
                    callback.onFail();
                }
            }

            @Override
            public void onComplete(String response) {
                if (!TextUtils.isEmpty(response)) {
                    GroupCreateInfoEntity groupCreateInfo = HCApplication.getInstance().getGson().fromJsonWithNoException(response, GroupCreateInfoEntity.class);
                    if(groupCreateInfo!=null && groupCreateInfo.status && groupCreateInfo.content != null && callback!=null){
                        callback.onSuccess(groupCreateInfo.content);
                    }
                }else{
                    ToastUtil.makeLongText("创建群组失败");
                }
            }
        });
    }
}
