package com.example.Bama.ui;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import com.example.Bama.Bean.ChannelItem;
import com.example.Bama.Bean.GroupCircleEntity;
import com.example.Bama.Bean.GroupCreateInfoEntity;
import com.example.Bama.Bean.VersionModel;
import com.example.Bama.background.HCApplication;
import com.example.Bama.background.config.ServerConfig;
import com.example.Bama.ui.fragment.GroupCircleFragment;
import com.example.Bama.ui.fragment.GroupFragment;
import com.example.Bama.util.DownLoadTask;
import com.example.Bama.util.Request;
import com.example.Bama.util.ToastUtil;
import com.example.Bama.util.VersionCompareUtil;
import com.example.Bama.widget.HGAlertDlg;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
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

    public static void queryTagGroupList(Context context, String channel_id,int page ,int pageSize,final GroupCircleFragment.QueryTagGroupListCallback callback){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tagid",channel_id));
        params.add(new BasicNameValuePair("page",page+""));
        params.add(new BasicNameValuePair("pageSize",pageSize+""));
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
                    if (item!=null && item.content!=null && callback != null){
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
    public static void createGroup(Context context,String picUrl,String groupName,String groupType,String groupDesc,
                                   final ActivityCreateGroup.CreateGroupCallBack callback){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("uid",HCApplication.getInstance().getAccount().userId));
        params.add(new BasicNameValuePair("picurl",picUrl));
        params.add(new BasicNameValuePair("name",groupName));
        params.add(new BasicNameValuePair("tagid",groupType));
        params.add(new BasicNameValuePair("description",groupDesc));
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

    /**
     * 检查版本更新*
     */
    public static void checkVersion(final Context context) {
        Request.doRequest(context, new ArrayList<NameValuePair>(), ServerConfig.URL_VERSION_UPDATE, Request.GET, new Request.RequestListener() {
            @Override
            public void onException(Request.RequestException e) {

            }

            @Override
            public void onComplete(String response) {
                final VersionModel versionModel = HCApplication.getInstance().getGson().fromJsonWithNoException(response, VersionModel.class);
                if (versionModel != null) {
                    if (!TextUtils.isEmpty(versionModel.version_code) && !TextUtils.isEmpty(versionModel.feature) && !TextUtils.isEmpty(versionModel.url)) {
                        try {
                            PackageInfo packInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                            String version = packInfo.versionName;
                            if (!TextUtils.isEmpty(version) && VersionCompareUtil.compareVersion(versionModel.version_code, version) > 0) {
                                HGAlertDlg.showDlg("发现新版本", versionModel.feature, (ActivityBase)context, new HGAlertDlg.HGAlertDlgClickListener() {
                                    @Override
                                    public void onAlertDlgClicked(boolean isConfirm) {
                                        if (isConfirm) {
                                            new DownLoadTask(context).startDownLoad(versionModel.url);
                                        }
                                    }
                                });
                            }
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    public static void imUserPing(final Context context,String uid,Long param) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("uid",uid));
        params.add(new BasicNameValuePair("type","chat"));
        params.add(new BasicNameValuePair("param",param+""));
        Request.doRequest(context, params ,ServerConfig.URL_IM_USER_PING, Request.GET, new Request.RequestListener() {
            @Override
            public void onException(Request.RequestException e) {

            }

            @Override
            public void onComplete(String response) {

            }
        });
    }

    public static void getUserInfo(final Context context,String data, final JavaScriptInterfaceUtil.GetJsonLinister linister) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("data",data));
        Request.doRequest(context, params ,ServerConfig.URL_USER_GET, Request.GET, new Request.RequestListener() {
            @Override
            public void onException(Request.RequestException e) {
                if (linister!=null)
                    linister.onFail();
            }

            @Override
            public void onComplete(String response) {
                if (linister !=null)
                    linister.onSuccess(response);
            }
        });
    }

    public static void uploadFile(final Context context,File file, final JavaScriptInterfaceUtil.GetJsonLinister linister) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        Request.doFileUploadRequest(context, params ,file ,ServerConfig.URL_FILE_UP,new Request.RequestListener() {
            @Override
            public void onException(Request.RequestException e) {
                if (linister!=null)
                    linister.onFail();
            }

            @Override
            public void onComplete(String response) {
                if (linister !=null)
                    linister.onSuccess(response);
            }
        });

    }


}
