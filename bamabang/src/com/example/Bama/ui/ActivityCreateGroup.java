package com.example.Bama.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.*;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.exceptions.EaseMobException;
import com.example.Bama.Bean.ChannelItem;
import com.example.Bama.Bean.GroupCreateInfoEntity;
import com.example.Bama.R;
import com.example.Bama.background.HCApplication;
import com.example.Bama.ui.fragment.GroupFragment;
import com.example.Bama.util.ImageLoaderUtil;
import com.example.Bama.util.IntentUtils;
import com.example.Bama.util.ToastUtil;
import com.example.Bama.widget.HCPopListView;
import com.example.Bama.widget.cropimage.ActivityCropImage;
import com.example.Bama.widget.photo.ActivityCapture;
import com.example.Bama.widget.photo.PhotoAlbumActivity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActivityCreateGroup extends ActivityBase implements View.OnClickListener {
	private View backBtn;
	private ImageView headerView;
	private EditText etInputGroupName, et_description;
	private TextView et_input_group_category;
	private FrameLayout groupTypeFL;
	private TextView mCreateGroup;

    public static final int kRequestCameraOne = 101;
    public static final int kRequestAlbumOne = 102;
    public static final int kRequestCameraTwo = 103;
    public static final int kRequestAlbumTwo = 104;
    public static final int kRequestcodeCropImage = 105;

    private static String KGroupInfo = "groupInfo";

    private String groupInfo = "";
    private String serverImage = "";
    private String tagId = "";
    private String groupId = "";

    private static boolean isEditGroupInfo = false;
	/**
	 * poplist data*
	 */
	private List<ChannelItem.ContentEntity> groupTypeList = new ArrayList<ChannelItem.ContentEntity>();

    public static void open(Activity activity) {
        Intent intent = new Intent(activity, ActivityCreateGroup.class);
        isEditGroupInfo = false;
        activity.startActivity(intent);
    }

    public static void open(Activity activity,String groupInfo) {
        Intent intent = new Intent(activity, ActivityCreateGroup.class);
        intent.putExtra(KGroupInfo,groupInfo);
        isEditGroupInfo = true;
        activity.startActivity(intent);
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_create_group);
		super.onCreate(savedInstanceState);
        groupInfo = getIntent().getStringExtra(KGroupInfo);
        if(isEditGroupInfo){
            initGroupInfoView();
        }
        queryGroupTags();
	}

    private void initGroupInfoView() {
        if (!TextUtils.isEmpty(groupInfo)){
            GroupCreateInfoEntity.ContentEntity entity = HCApplication.getInstance().getGson().fromJsonWithNoException(groupInfo, GroupCreateInfoEntity.ContentEntity.class);
            HCApplication.getInstance().getImageLoader().displayImage(entity.picurl,headerView,ImageLoaderUtil.Options_Memory_Rect_Avatar);
            etInputGroupName.setText(entity.name);
            et_description.setText(entity.description);
            tagId = entity.tagid;
            groupId = entity.groupid;
            serverImage = entity.picurl;
            mCreateGroup.setText("编辑");
        }else{
            mCreateGroup.setText("创建");
        }
    }


    @Override
	protected void getViews() {
		backBtn = findViewById(R.id.back_btn);
		headerView = (ImageView) findViewById(R.id.groupImage);
		etInputGroupName = (EditText) findViewById(R.id.et_input_group_name);
		groupTypeFL = (FrameLayout) findViewById(R.id.groupTypeFL);
		et_input_group_category = (TextView) findViewById(R.id.et_input_group_category);
		et_description = (EditText) findViewById(R.id.et_description);
		mCreateGroup = (TextView) findViewById(R.id.creategroup);
	}

	@Override
	protected void initViews() {

	}

	@Override
	protected void setListeners() {
		backBtn.setOnClickListener(this);
		headerView.setOnClickListener(this);
		groupTypeFL.setOnClickListener(this);
		mCreateGroup.setOnClickListener(this);
	}

	@Override
	public void onBackPressed() {
//		if (HCPopListView.getDlgView(this).isShowing()) {
//			HCPopListView.onBackPressed(this);
//			return;
//		}
		super.onBackPressed();
        finish();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.back_btn:
			finish();
			break;
        case R.id.groupImage:
            showPicChooseDialog(kRequestCameraOne, kRequestAlbumOne);
			break;
		case R.id.groupTypeFL:
            List<String> groupTypeStrings = new ArrayList<String>();
            for (int i = 0; i < groupTypeList.size(); i++) {
                groupTypeStrings.add(groupTypeList.get(i).name);
            }
			HCPopListView.showDialog(this, "群组类型", "取消", groupTypeStrings, new HCPopListView.HCPopListViewListener() {
				@Override
				public void onItemClicked(int index, String content) {
					if (!TextUtils.isEmpty(content)) {
						et_input_group_category.setText(content);
                        tagId = ""+groupTypeList.get(index).tagid;
					}
				}

				@Override
				public void onCancelClicked() {

				}
			});
			break;
		case R.id.creategroup:
            tryToPublishGroup();
			break;
		}
	}

	private void tryToPublishGroup() {
		final String groupName = etInputGroupName.getText().toString();
		if (TextUtils.isEmpty(groupName)) {
			ToastUtil.makeShortText("请输入群名称");
			return;
		}
		String groupType = et_input_group_category.getText().toString();
		if (TextUtils.isEmpty(groupType) || groupType.equals("请选择群组类型")) {
			ToastUtil.makeShortText("请选择群类型");
			return;
		}
		final String groupDesc = et_description.getText().toString();
		if (TextUtils.isEmpty(groupDesc)) {
			ToastUtil.makeShortText("请输入群简介");
			return;
		}
		/**调用环信sdk的方法注册群组,环信的只需要群名称和群简介，这里创建公开群**/
		showDialog("正在创建群...");

        RequestUtil.createGroup(ActivityCreateGroup.this,groupId,serverImage,groupName,tagId,groupDesc,new CreateGroupCallBack(){
            @Override
            public void onSuccess(final GroupCreateInfoEntity.ContentEntity entity) {
                if (entity==null){
                    ToastUtil.makeShortText("群创建失败");
                    return;
                }
                dismissDialog();
                ToastUtil.makeShortText("群创建成功");
                finish();
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            /**创建公开群，此种方式创建的群，可以自由加入,环信上线是2000人
//                             创建公开群，此种方式创建的群，用户需要申请，等群主同意后才能加入此群**/
//                            /**得到群的对象,得到了群ID后就可以把群的头像，类型等一系列信息放到自己的服务器上面**/
//                            EMGroup emGroup = EMGroupManager.getInstance().createPublicGroup(entity.groupid, entity.description, null, false, 2000);
//                            final String groupId = emGroup.getGroupId();
//                            runOnUiThread(new Runnable() {
//                                public void run() {
//                                    dismissDialog();
//                                    ToastUtil.makeShortText("群创建成功");
//                                    setResult(RESULT_OK);
//                                    finish();
//                                }
//                            });
//                        } catch (final EaseMobException e) {
//                            runOnUiThread(new Runnable() {
//                                public void run() {
//                                    dismissDialog();
//                                    ToastUtil.makeShortText("群创建失败");
//                                }
//                            });
//                        }
//
//                    }
//                }).start();
            }

            @Override
            public void onFail() {
                dismissDialog();
                ToastUtil.makeLongText("群创建失败");
            }
        });
	}

    private void queryGroupTags(){
        RequestUtil.queryTagList(this,new GroupFragment.QueryTagListCallback() {
            @Override
            public void onSuccess(List list) {
                if(list!=null){
                    groupTypeList = list;
                    for (final ChannelItem.ContentEntity entity : groupTypeList){
                        if(tagId.equals(""+entity.tagid)){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    et_input_group_category.setText(entity.name);
                                }
                            });
                            break;
                        }
                    }
                }
            }

            @Override
            public void onFail() {
                ToastUtil.makeLongText("获取群分类错误，请检查网络");
            }
        });
    }

    public interface CreateGroupCallBack{
        public void onSuccess(GroupCreateInfoEntity.ContentEntity entity);
        public void onFail();
    }

    private void showPicChooseDialog(final int cameraRequestCode, final int albumRequest) {
        final AlertDialog dlg = new AlertDialog.Builder(this).create();
        dlg.setCanceledOnTouchOutside(true);
        dlg.setCancelable(true);
        dlg.show();
        Window window = dlg.getWindow();
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.alert_camera, null);
        window.setContentView(layout);

        TextView title = (TextView) layout.findViewById(R.id.nametip);
        title.setText("请选择" + "                 ");
        TextView camera = (TextView) layout.findViewById(R.id.privatechatwithsomebody);
        TextView gallery = (TextView) layout.findViewById(R.id.jubao);

        camera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivityForResult(IntentUtils.goToCameraIntent(ActivityCreateGroup.this, 750, 750), cameraRequestCode);
                dlg.dismiss();
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivityForResult(IntentUtils.goToAlbumIntent(new ArrayList<String>(), 1, "预览",true,ActivityCreateGroup.this), albumRequest);
                dlg.dismiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == kRequestAlbumOne && resultCode == RESULT_OK) {
            if (data == null) {
                return;
            } else {
                String[] selectPaths = data.getStringArrayExtra(PhotoAlbumActivity.Key_SelectPaths);
                if (selectPaths != null && selectPaths.length > 0 && !TextUtils.isEmpty(selectPaths[0])) {
                    ActivityCropImage.openForResult(ActivityCreateGroup.this, selectPaths[0], 750, 750, true,kRequestcodeCropImage);
                    return;
                }
            }
        } else if (requestCode == kRequestCameraOne && resultCode == RESULT_OK) {
            String path = data.getStringExtra(ActivityCapture.kPhotoPath);
            ActivityCropImage.openForResult(ActivityCreateGroup.this, path, 750, 750,true, kRequestcodeCropImage);
            return;
        } else if (requestCode == kRequestAlbumTwo && resultCode == RESULT_OK) {
            if (data == null) {
                return;
            } else {
                String path = data.getStringExtra(ActivityCapture.kPhotoPath);
                HCApplication.getInstance().getImageLoader().displayImage(path,headerView, ImageLoaderUtil.Options_Memory_Rect_Avatar);
            }
        } else if (requestCode == kRequestCameraTwo && resultCode == RESULT_OK) {
            String path = data.getStringExtra(ActivityCapture.kPhotoPath);
            HCApplication.getInstance().getImageLoader().displayImage(path,headerView, ImageLoaderUtil.Options_Memory_Rect_Avatar);
        }else if (requestCode == kRequestcodeCropImage && resultCode == RESULT_OK) {
            String path = data.getStringExtra(ActivityCropImage.kCropImagePath);
            HCApplication.getInstance().getImageLoader().displayImage("file://" + path, headerView, ImageLoaderUtil.Options_Memory_Rect_Avatar);
            if(!TextUtils.isEmpty(path)){
                RequestUtil.uploadFile(this,new File(path),new JavaScriptInterfaceUtil.GetJsonLinister(){

                    @Override
                    public void onSuccess(String json) {
                        if(!TextUtils.isEmpty(json)){
                            try {
                                JSONObject object = new JSONObject(json);
                                if(object.has("content")){
                                    serverImage = object.optString("content");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else{

                        }
                    }

                    @Override
                    public void onFail() {

                    }
                });
            }
        }
    }
}
