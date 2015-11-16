package com.example.Bama.ui.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.easemob.*;
import com.easemob.chat.*;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.EMLog;
import com.easemob.util.PathUtil;
import com.easemob.util.VoiceRecorder;
import com.example.Bama.R;
import com.example.Bama.background.Account;
import com.example.Bama.background.HCApplication;
import com.example.Bama.chat.applib.controller.HXSDKHelper;
import com.example.Bama.chat.chatuidemo.DemoHXSDKHelper;
import com.example.Bama.chat.chatuidemo.activity.*;
import com.example.Bama.chat.chatuidemo.adapter.ExpressionAdapter;
import com.example.Bama.chat.chatuidemo.adapter.ExpressionPagerAdapter;
import com.example.Bama.chat.chatuidemo.adapter.MessageAdapter;
import com.example.Bama.chat.chatuidemo.adapter.VoicePlayClickListener;
import com.example.Bama.chat.chatuidemo.utils.CommonUtils;
import com.example.Bama.chat.chatuidemo.utils.ImageUtils;
import com.example.Bama.chat.chatuidemo.utils.SmileUtils;
import com.example.Bama.chat.chatuidemo.widget.ExpandGridView;
import com.example.Bama.chat.chatuidemo.widget.PasteEditText;
import com.example.Bama.ui.ActivityGroupChat;
import com.example.Bama.ui.ActivityGroupMembers;
import com.example.Bama.util.ToastUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 群聊页面*
 */
public class GroupChatFragment extends Fragment implements View.OnClickListener, EMEventListener, ActivityGroupChat.ATTAListener {
	private static final String TAG = "ChatActivity";
	private static final int REQUEST_CODE_EMPTY_HISTORY = 2;
	public static final int REQUEST_CODE_CONTEXT_MENU = 3;
	private static final int REQUEST_CODE_MAP = 4;
	public static final int REQUEST_CODE_TEXT = 5;
	public static final int REQUEST_CODE_VOICE = 6;
	public static final int REQUEST_CODE_PICTURE = 7;
	public static final int REQUEST_CODE_LOCATION = 8;
	public static final int REQUEST_CODE_NET_DISK = 9;
	public static final int REQUEST_CODE_FILE = 10;
	public static final int REQUEST_CODE_COPY_AND_PASTE = 11;
	public static final int REQUEST_CODE_PICK_VIDEO = 12;
	public static final int REQUEST_CODE_DOWNLOAD_VIDEO = 13;
	public static final int REQUEST_CODE_VIDEO = 14;
	public static final int REQUEST_CODE_DOWNLOAD_VOICE = 15;
	public static final int REQUEST_CODE_SELECT_USER_CARD = 16;
	public static final int REQUEST_CODE_SEND_USER_CARD = 17;
	public static final int REQUEST_CODE_CAMERA = 18;
	public static final int REQUEST_CODE_LOCAL = 19;
	public static final int REQUEST_CODE_CLICK_DESTORY_IMG = 20;
	public static final int REQUEST_CODE_GROUP_DETAIL = 21;
	public static final int REQUEST_CODE_SELECT_VIDEO = 23;
	public static final int REQUEST_CODE_SELECT_FILE = 24;
	public static final int REQUEST_CODE_ADD_TO_BLACKLIST = 25;

	public static final int RESULT_CODE_COPY = 1;
	public static final int RESULT_CODE_DELETE = 2;
	public static final int RESULT_CODE_FORWARD = 3;
	public static final int RESULT_CODE_OPEN = 4;
	public static final int RESULT_CODE_DWONLOAD = 5;
	public static final int RESULT_CODE_TO_CLOUD = 6;
	public static final int RESULT_CODE_EXIT_GROUP = 7;

	public static final String COPY_IMAGE = "EASEMOBIMG";
	private View recordingContainer;
	private ImageView micImage;
	private TextView recordingHint;
	private ListView listView;
	private PasteEditText mEditTextContent;
	private View buttonSetModeKeyboard;
	private View buttonSetModeVoice;
	/**
	 * 相应点击事件的view*
	 */
	private View buttonSend;
	private View btn_set_mode_voice;
	private View btn_take_picture;
	private View btn_picture;
	private View btn_location;
	private View btn_video;
	private View btn_file;

	private View buttonPressToSpeak;
	// private ViewPager expressionViewpager;
	private LinearLayout emojiIconContainer;
	private LinearLayout btnContainer;
	private ImageView locationImgview;
	private View more;
	private int position;
	private ClipboardManager clipboard;
	private ViewPager expressionViewpager;
	private InputMethodManager manager;
	private List<String> reslist;
	private Drawable[] micImages;
	private EMConversation conversation;
	public static Activity activityInstance = null;

	private VoiceRecorder voiceRecorder;
	private MessageAdapter adapter;
	private File cameraFile;
	static int resendPos;

	private GroupListener groupListener;

	private ImageView iv_emoticons_normal;
	private ImageView iv_emoticons_checked;
	private RelativeLayout edittext_layout;
	private ProgressBar loadmorePB;
	private boolean isloading;
	private final int pagesize = 20;
	private boolean haveMoreData = true;
	private Button btnMore;
	public String playMsgId;

	private Handler micImageHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			// 切换msg切换图片
			micImage.setImageDrawable(micImages[msg.what]);
		}
	};
	public EMChatRoom room;
	public View rootView;

	/**
	 * 给谁发送消息*
	 */
	public static final String kGroupId = "kGroupId";
	private String toChatUsername;

	/**
	 * 2是群聊类型*
	 */
	private int CHATTYPE_GROUP = 2;
	private int chatType = CHATTYPE_GROUP;

	/**
	 * Account*
	 */
	private Account account;

	/**
	 * 是否加入群的标识位*
	 */
	private boolean isJoinGroup = false;

	/**
	 * 群的model*
	 */
	private EMGroup emGroup;

	/**
	 * 跳转到群成员列表页面*
	 */
	private static final int RequestCodeToActivityGroupMembers = 10012;
	private boolean isAlreadyToActivityGroupMembers = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_group_chat, container, false);
		initView(rootView);
		setUpView(rootView);
		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activityInstance = activity;
		((ActivityGroupChat) activityInstance).setATTAListener(this);
	}

	/**
	 * initView
	 */
	protected void initView(View rootView) {
		recordingContainer = rootView.findViewById(R.id.recording_container);
		micImage = (ImageView) rootView.findViewById(R.id.mic_image);
		recordingHint = (TextView) rootView.findViewById(R.id.recording_hint);
		listView = (ListView) rootView.findViewById(R.id.list);
		mEditTextContent = (PasteEditText) rootView.findViewById(R.id.et_sendmessage);
		buttonSetModeKeyboard = rootView.findViewById(R.id.btn_set_mode_keyboard);
		edittext_layout = (RelativeLayout) rootView.findViewById(R.id.edittext_layout);
		buttonSetModeVoice = rootView.findViewById(R.id.btn_set_mode_voice);
		buttonSend = rootView.findViewById(R.id.btn_send);
		btn_set_mode_voice = rootView.findViewById(R.id.btn_set_mode_voice);
		btn_take_picture = rootView.findViewById(R.id.btn_take_picture);
		btn_picture = rootView.findViewById(R.id.btn_picture);
		btn_location = rootView.findViewById(R.id.btn_location);
		btn_video = rootView.findViewById(R.id.btn_video);
		btn_file = rootView.findViewById(R.id.btn_file);
		buttonSend.setOnClickListener(this);
		btn_set_mode_voice.setOnClickListener(this);
		btn_take_picture.setOnClickListener(this);
		btn_picture.setOnClickListener(this);
		btn_location.setOnClickListener(this);
		btn_video.setOnClickListener(this);
		btn_file.setOnClickListener(this);
		buttonPressToSpeak = rootView.findViewById(R.id.btn_press_to_speak);
		expressionViewpager = (ViewPager) rootView.findViewById(R.id.vPager);
		emojiIconContainer = (LinearLayout) rootView.findViewById(R.id.ll_face_container);
		btnContainer = (LinearLayout) rootView.findViewById(R.id.ll_btn_container);
		locationImgview = (ImageView) rootView.findViewById(R.id.btn_location);
		iv_emoticons_normal = (ImageView) rootView.findViewById(R.id.iv_emoticons_normal);
		iv_emoticons_checked = (ImageView) rootView.findViewById(R.id.iv_emoticons_checked);
		loadmorePB = (ProgressBar) rootView.findViewById(R.id.pb_load_more);
		btnMore = (Button) rootView.findViewById(R.id.btn_more);
		btnMore.setOnClickListener(this);
		iv_emoticons_normal.setVisibility(View.VISIBLE);
		iv_emoticons_checked.setVisibility(View.INVISIBLE);
		more = rootView.findViewById(R.id.more);
		edittext_layout.setBackgroundResource(R.drawable.input_bar_bg_normal);

		// 动画资源文件,用于录制语音时
		micImages = new Drawable[] { getResources().getDrawable(R.drawable.record_animate_01),
				getResources().getDrawable(R.drawable.record_animate_02),
				getResources().getDrawable(R.drawable.record_animate_03),
				getResources().getDrawable(R.drawable.record_animate_04),
				getResources().getDrawable(R.drawable.record_animate_05),
				getResources().getDrawable(R.drawable.record_animate_06),
				getResources().getDrawable(R.drawable.record_animate_07),
				getResources().getDrawable(R.drawable.record_animate_08),
				getResources().getDrawable(R.drawable.record_animate_09),
				getResources().getDrawable(R.drawable.record_animate_10),
				getResources().getDrawable(R.drawable.record_animate_11),
				getResources().getDrawable(R.drawable.record_animate_12),
				getResources().getDrawable(R.drawable.record_animate_13),
				getResources().getDrawable(R.drawable.record_animate_14), };

		// 表情list
		reslist = getExpressionRes(35);
		// 初始化表情viewpager
		List<View> views = new ArrayList<View>();
		View gv1 = getGridChildView(1);
		View gv2 = getGridChildView(2);
		views.add(gv1);
		views.add(gv2);
		expressionViewpager.setAdapter(new ExpressionPagerAdapter(views));
		edittext_layout.requestFocus();
		voiceRecorder = new VoiceRecorder(micImageHandler);
		buttonPressToSpeak.setOnTouchListener(new PressToSpeakListen());
		mEditTextContent.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					edittext_layout.setBackgroundResource(R.drawable.input_bar_bg_active);
				} else {
					edittext_layout.setBackgroundResource(R.drawable.input_bar_bg_normal);
				}

			}
		});
		mEditTextContent.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				edittext_layout.setBackgroundResource(R.drawable.input_bar_bg_active);
				more.setVisibility(View.GONE);
				iv_emoticons_normal.setVisibility(View.VISIBLE);
				iv_emoticons_checked.setVisibility(View.INVISIBLE);
				emojiIconContainer.setVisibility(View.GONE);
				btnContainer.setVisibility(View.GONE);
			}
		});
		// 监听文字框
		mEditTextContent.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (!TextUtils.isEmpty(s)) {
					btnMore.setVisibility(View.GONE);
					buttonSend.setVisibility(View.VISIBLE);
				} else {
					btnMore.setVisibility(View.VISIBLE);
					buttonSend.setVisibility(View.GONE);
					isAlreadyToActivityGroupMembers = false;
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (!isAlreadyToActivityGroupMembers) {
					/**监听是否输入了@符号**/
					String content = s.toString();
					if (!TextUtils.isEmpty(content) && content.startsWith("@")) {
						ActivityGroupMembers.open(GroupChatFragment.this, toChatUsername, RequestCodeToActivityGroupMembers);
						isAlreadyToActivityGroupMembers = true;
					}
				}
			}
		});
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}

	private void setUpView(View rootView) {
		iv_emoticons_normal.setOnClickListener(this);
		iv_emoticons_checked.setOnClickListener(this);
		// position = getIntent().getIntExtra("position", -1);
		clipboard = (ClipboardManager) activityInstance.getSystemService(Context.CLIPBOARD_SERVICE);
		manager = (InputMethodManager) activityInstance.getSystemService(Context.INPUT_METHOD_SERVICE);
		activityInstance.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		wakeLock = ((PowerManager) activityInstance.getSystemService(Context.POWER_SERVICE)).newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "demo");

		/**群初始化**/
		rootView.findViewById(R.id.container_to_group).setVisibility(View.VISIBLE);
		rootView.findViewById(R.id.container_remove).setVisibility(View.GONE);
		rootView.findViewById(R.id.container_voice_call).setVisibility(View.GONE);
		rootView.findViewById(R.id.container_video_call).setVisibility(View.GONE);
		toChatUsername = getArguments().getString(kGroupId);
		onGroupViewCreation(rootView);
		onConversationInit();
		onListViewCreation();

		/**show forward message if the message is not null**/
		String forward_msg_id = getArguments().getString("forward_msg_id");
		if (forward_msg_id != null) {
			/**显示发送要转发的消息**/
			forwardMessage(forward_msg_id);
		}

		/**判断用户是否在这个群里面，如果不在的话，显示加群按钮**/
		account = HCApplication.getInstance().getAccount();
		isInGroup();
	}

	private void isInGroup() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					emGroup = EMGroupManager.getInstance().getGroupFromServer(toChatUsername);
					List<String> accountIds = emGroup.getMembers();
					/**判断自己是否加入群**/
					if (emGroup !=null && !TextUtils.isEmpty(account.userId) && accountIds.contains(account.userId)) {
						isJoinGroup = true;
					} else {
						isJoinGroup = false;
					}
					getActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							if (adapter != null) {
								adapter.setOwner(emGroup.getOwner());
							}
						}
					});
				} catch (EaseMobException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * 初始化群的会话*
	 */
	protected void onConversationInit() {
		conversation = EMChatManager.getInstance().getConversationByType(toChatUsername, EMConversation.EMConversationType.GroupChat);
		// 把此会话的未读数置为0
		conversation.markAllMessagesAsRead();

		// 初始化db时，每个conversation加载数目是getChatOptions().getNumberOfMessagesLoaded
		// 这个数目如果比用户期望进入会话界面时显示的个数不一样，就多加载一些
		final List<EMMessage> msgs = conversation.getAllMessages();
		int msgCount = msgs != null ? msgs.size() : 0;
		if (msgCount < conversation.getAllMsgCount() && msgCount < pagesize) {
			String msgId = null;
			if (msgs != null && msgs.size() > 0) {
				msgId = msgs.get(0).getMsgId();
			}
			conversation.loadMoreGroupMsgFromDB(msgId, pagesize);
		}

		EMChatManager.getInstance().addChatRoomChangeListener(new EMChatRoomChangeListener() {
			@Override
			public void onChatRoomDestroyed(String roomId, String roomName) {
				if (roomId.equals(toChatUsername)) {
					getActivity().finish();
				}
			}

			@Override
			public void onMemberJoined(String roomId, String participant) {
			}

			@Override
			public void onMemberExited(String roomId, String roomName, String participant) {
			}

			@Override
			public void onMemberKicked(String roomId, String roomName, String participant) {
				if (roomId.equals(toChatUsername)) {
					String curUser = EMChatManager.getInstance().getCurrentUser();
					if (curUser.equals(participant)) {
						EMChatManager.getInstance().leaveChatRoom(toChatUsername);
						getActivity().finish();
					}
				}
			}

		});
	}

	protected void onListViewCreation() {
		adapter = new MessageAdapter(getActivity(), toChatUsername, chatType);
		// 显示消息
		listView.setAdapter(adapter);

		listView.setOnScrollListener(new ListScrollListener());
		adapter.refreshSelectLast();

		listView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				hideKeyboard();
				more.setVisibility(View.GONE);
				iv_emoticons_normal.setVisibility(View.VISIBLE);
				iv_emoticons_checked.setVisibility(View.INVISIBLE);
				emojiIconContainer.setVisibility(View.GONE);
				btnContainer.setVisibility(View.GONE);
				return false;
			}
		});
	}

	protected void onGroupViewCreation(View rootView) {
        emGroup = EMChatManager.getInstance().getGroup(toChatUsername);
		if (emGroup != null) {
			((TextView) rootView.findViewById(R.id.name)).setText(emGroup.getGroupName());
		} else {
			((TextView) rootView.findViewById(R.id.name)).setText(toChatUsername);
		}

		/**监听当前会话的群聊解散被T事件**/
		groupListener = new GroupListener();
		EMChatManager.getInstance().addGroupChangeListener(groupListener);
	}

	/**
	 * onActivityResult
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_CODE_EXIT_GROUP) {
			//            setResult(RESULT_OK);
			//            finish();
			return;
		}
		/**输入@符号后的返回结果**/
		if (requestCode == RequestCodeToActivityGroupMembers && resultCode == getActivity().RESULT_OK) {
			String userId = data.getStringExtra(ActivityGroupMembers.kUserId);
			String newContent = "@"+userId;
			mEditTextContent.setText(newContent);
			mEditTextContent.setSelection(newContent.length());
		}

		if (requestCode == REQUEST_CODE_CONTEXT_MENU) {
			switch (resultCode) {
			case RESULT_CODE_COPY: // 复制消息
				EMMessage copyMsg = ((EMMessage) adapter.getItem(data.getIntExtra("position", -1)));
				// clipboard.setText(SmileUtils.getSmiledText(ChatActivity.this,
				// ((TextMessageBody) copyMsg.getBody()).getMessage()));
				clipboard.setText(((TextMessageBody) copyMsg.getBody()).getMessage());
				break;
			case RESULT_CODE_DELETE: // 删除消息
				EMMessage deleteMsg = (EMMessage) adapter.getItem(data.getIntExtra("position", -1));
				conversation.removeMessage(deleteMsg.getMsgId());
				adapter.refreshSeekTo(data.getIntExtra("position", adapter.getCount()) - 1);
				break;

			case RESULT_CODE_FORWARD: // 转发消息
				EMMessage forwardMsg = (EMMessage) adapter.getItem(data.getIntExtra("position", 0));
				Intent intent = new Intent(activityInstance, ForwardMessageActivity.class);
				intent.putExtra("forward_msg_id", forwardMsg.getMsgId());
				startActivity(intent);
				break;
			default:
				break;
			}
		}
		if (resultCode == activityInstance.RESULT_OK) { // 清空消息
			if (requestCode == REQUEST_CODE_EMPTY_HISTORY) {
				// 清空会话
				EMChatManager.getInstance().clearConversation(toChatUsername);
				adapter.refresh();
			} else if (requestCode == REQUEST_CODE_CAMERA) { // 发送照片
				if (cameraFile != null && cameraFile.exists())
					sendPicture(cameraFile.getAbsolutePath());
			} else if (requestCode == REQUEST_CODE_SELECT_VIDEO) { // 发送本地选择的视频
				int duration = data.getIntExtra("dur", 0);
				String videoPath = data.getStringExtra("path");
				File file = new File(PathUtil.getInstance().getImagePath(), "thvideo" + System.currentTimeMillis());
				Bitmap bitmap = null;
				FileOutputStream fos = null;
				try {
					if (!file.getParentFile().exists()) {
						file.getParentFile().mkdirs();
					}
					bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, 3);
					if (bitmap == null) {
						EMLog.d("chatactivity", "problem load video thumbnail bitmap,use default icon");
						bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.app_panel_video_icon);
					}
					fos = new FileOutputStream(file);

					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);

				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (fos != null) {
						try {
							fos.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
						fos = null;
					}
					if (bitmap != null) {
						bitmap.recycle();
						bitmap = null;
					}

				}
				sendVideo(videoPath, file.getAbsolutePath(), duration / 1000);

			} else if (requestCode == REQUEST_CODE_LOCAL) { // 发送本地图片
				if (data != null) {
					Uri selectedImage = data.getData();
					if (selectedImage != null) {
						sendPicByUri(selectedImage);
					}
				}
			} else if (requestCode == REQUEST_CODE_SELECT_FILE) { // 发送选择的文件
				if (data != null) {
					Uri uri = data.getData();
					if (uri != null) {
						sendFile(uri);
					}
				}

			} else if (requestCode == REQUEST_CODE_MAP) { // 地图
				double latitude = data.getDoubleExtra("latitude", 0);
				double longitude = data.getDoubleExtra("longitude", 0);
				String locationAddress = data.getStringExtra("address");
				if (locationAddress != null && !locationAddress.equals("")) {
					more(more);
					sendLocationMsg(latitude, longitude, "", locationAddress);
				} else {
					String st = getResources().getString(R.string.unable_to_get_loaction);
					Toast.makeText(activityInstance, st, Toast.LENGTH_SHORT).show();
				}
				// 重发消息
			} else if (requestCode == REQUEST_CODE_TEXT || requestCode == REQUEST_CODE_VOICE
					|| requestCode == REQUEST_CODE_PICTURE || requestCode == REQUEST_CODE_LOCATION
					|| requestCode == REQUEST_CODE_VIDEO || requestCode == REQUEST_CODE_FILE) {
				resendMessage();
			} else if (requestCode == REQUEST_CODE_COPY_AND_PASTE) {
				// 粘贴
				if (!TextUtils.isEmpty(clipboard.getText())) {
					String pasteText = clipboard.getText().toString();
					if (pasteText.startsWith(COPY_IMAGE)) {
						// 把图片前缀去掉，还原成正常的path
						sendPicture(pasteText.replace(COPY_IMAGE, ""));
					}

				}
			} else if (requestCode == REQUEST_CODE_ADD_TO_BLACKLIST) { // 移入黑名单
				EMMessage deleteMsg = (EMMessage) adapter.getItem(data.getIntExtra("position", -1));
				addUserToBlacklist(deleteMsg.getFrom());
			} else if (conversation.getMsgCount() > 0) {
				adapter.refresh();
				//                setResult(RESULT_OK);
			} else if (requestCode == REQUEST_CODE_GROUP_DETAIL) {
				adapter.refresh();
			}
		}
	}

	/**
	 * 消息图标点击事件
	 *
	 * @param view
	 */
	@Override
	public void onClick(View view) {
		String st1 = getResources().getString(R.string.not_connect_to_server);
		int id = view.getId();
		if (id == R.id.btn_send) {// 点击发送按钮(发文字和表情)
			if (!isJoinGroup) {
				ToastUtil.makeShortText("请先加入群再发送消息");
				return;
			}
			String s = mEditTextContent.getText().toString();
			sendText(s);
		} else if (id == R.id.btn_take_picture) {
			if (!isJoinGroup) {
				ToastUtil.makeShortText("请先加入群再发送消息");
				return;
			}
			selectPicFromCamera();// 点击照相图标
		} else if (id == R.id.btn_picture) {
			if (!isJoinGroup) {
				ToastUtil.makeShortText("请先加入群再发送消息");
				return;
			}
			selectPicFromLocal(); // 点击图片图标
		} else if (id == R.id.btn_location) { // 位置
			if (!isJoinGroup) {
				ToastUtil.makeShortText("请先加入群再发送消息");
				return;
			}
			startActivityForResult(new Intent(activityInstance, BaiduMapActivity.class), REQUEST_CODE_MAP);
		} else if (id == R.id.iv_emoticons_normal) { // 点击显示表情框
			more.setVisibility(View.VISIBLE);
			iv_emoticons_normal.setVisibility(View.INVISIBLE);
			iv_emoticons_checked.setVisibility(View.VISIBLE);
			btnContainer.setVisibility(View.GONE);
			emojiIconContainer.setVisibility(View.VISIBLE);
			hideKeyboard();
		} else if (id == R.id.iv_emoticons_checked) { // 点击隐藏表情框
			iv_emoticons_normal.setVisibility(View.VISIBLE);
			iv_emoticons_checked.setVisibility(View.INVISIBLE);
			btnContainer.setVisibility(View.VISIBLE);
			emojiIconContainer.setVisibility(View.GONE);
			more.setVisibility(View.GONE);

		} else if (id == R.id.btn_video) {
			if (!isJoinGroup) {
				ToastUtil.makeShortText("请先加入群再发送消息");
				return;
			}
			// 点击摄像图标
			Intent intent = new Intent(activityInstance, ImageGridActivity.class);
			startActivityForResult(intent, REQUEST_CODE_SELECT_VIDEO);
		} else if (id == R.id.btn_file) { // 点击文件图标
			if (!isJoinGroup) {
				ToastUtil.makeShortText("请先加入群再发送消息");
				return;
			}
			selectFileFromLocal();
		} else if (id == R.id.btn_voice_call) { // 点击语音电话图标
			if (!isJoinGroup) {
				ToastUtil.makeShortText("请先加入群再发送消息");
				return;
			}
			if (!EMChatManager.getInstance().isConnected())
				Toast.makeText(activityInstance, st1, Toast.LENGTH_SHORT).show();
			else
				startActivity(new Intent(activityInstance, VoiceCallActivity.class).putExtra("username",
						toChatUsername).putExtra("isComingCall", false));
		} else if (id == R.id.btn_video_call) { // 视频通话
			if (!isJoinGroup) {
				ToastUtil.makeShortText("请先加入群再发送消息");
				return;
			}
			if (!EMChatManager.getInstance().isConnected())
				Toast.makeText(activityInstance, st1, Toast.LENGTH_SHORT).show();
			else
				startActivity(new Intent(activityInstance, VideoCallActivity.class).putExtra("username", toChatUsername).putExtra(
						"isComingCall", false));
		} else if (id == R.id.btn_more) {
			more(more);
		} else if (id == R.id.btn_set_mode_voice) {
			if (!isJoinGroup) {
				ToastUtil.makeShortText("请先加入群再发送消息");
				return;
			}
			setModeVoice();
		}
	}

	/**
	 * 事件监听
	 * <p/>
	 * see {@link com.easemob.EMNotifierEvent}
	 */
	@Override
	public void onEvent(EMNotifierEvent event) {
		switch (event.getEvent()) {
		case EventNewMessage: {
			//获取到message
			EMMessage message = (EMMessage) event.getData();

			String username = null;
			//群组消息
			if (message.getChatType() == EMMessage.ChatType.GroupChat || message.getChatType() == EMMessage.ChatType.ChatRoom) {
				username = message.getTo();
			} else {
				//单聊消息
				username = message.getFrom();
			}

			//如果是当前会话的消息，刷新聊天页面
			if (username.equals(getToChatUsername())) {
				refreshUIWithNewMessage();
				//声音和震动提示有新消息
				HXSDKHelper.getInstance().getNotifier().viberateAndPlayTone(message);
			} else {
				//如果消息不是和当前聊天ID的消息
				HXSDKHelper.getInstance().getNotifier().onNewMsg(message);
			}

			break;
		}
		case EventDeliveryAck: {
			//获取到message
			EMMessage message = (EMMessage) event.getData();
			refreshUI();
			break;
		}
		case EventReadAck: {
			//获取到message
			EMMessage message = (EMMessage) event.getData();
			refreshUI();
			break;
		}
		case EventOfflineMessage: {
			//a list of offline messages
			//List<EMMessage> offlineMessages = (List<EMMessage>) event.getData();
			refreshUI();
			break;
		}
		default:
			break;
		}

	}


	private void refreshUIWithNewMessage() {
		if (adapter == null) {
			return;
		}

		activityInstance.runOnUiThread(new Runnable() {
			public void run() {
				adapter.refreshSelectLast();
			}
		});
	}

	private void refreshUI() {
		if (adapter == null) {
			return;
		}

		activityInstance.runOnUiThread(new Runnable() {
			public void run() {
				adapter.refresh();
			}
		});
	}

	/**
	 * 照相获取图片
	 */
	public void selectPicFromCamera() {
		if (!CommonUtils.isExitsSdcard()) {
			String st = getResources().getString(R.string.sd_card_does_not_exist);
			Toast.makeText(activityInstance, st, Toast.LENGTH_SHORT).show();
			return;
		}

		cameraFile = new File(PathUtil.getInstance().getImagePath(), HCApplication.getInstance().getUserName()
				+ System.currentTimeMillis() + ".jpg");
		cameraFile.getParentFile().mkdirs();
		startActivityForResult(
				new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile)),
				REQUEST_CODE_CAMERA);
	}

	/**
	 * 选择文件
	 */
	private void selectFileFromLocal() {
		Intent intent = null;
		if (Build.VERSION.SDK_INT < 19) {
			intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("*/*");
			intent.addCategory(Intent.CATEGORY_OPENABLE);

		} else {
			intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		}
		startActivityForResult(intent, REQUEST_CODE_SELECT_FILE);
	}

	/**
	 * 从图库获取图片
	 */
	public void selectPicFromLocal() {
		Intent intent;
		if (Build.VERSION.SDK_INT < 19) {
			intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");

		} else {
			intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		}
		startActivityForResult(intent, REQUEST_CODE_LOCAL);
	}

	/**
	 * 发送文本消息
	 *
	 * @param content message content
	 * @param
	 */
	private void sendText(String content) {
		if (content.length() > 0) {
			EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
			message.setChatType(EMMessage.ChatType.GroupChat);

			TextMessageBody txtBody = new TextMessageBody(content);
			// 设置消息body
			message.addBody(txtBody);
			// 设置要发给谁,用户username或者群聊groupid
			message.setReceipt(toChatUsername);
			// 把messgage加到conversation中
			conversation.addMessage(message);
			// 通知adapter有消息变动，adapter会根据加入的这条message显示消息和调用sdk的发送方法
			adapter.refreshSelectLast();
			mEditTextContent.setText("");
			activityInstance.setResult(activityInstance.RESULT_OK);
		}
	}

	/**
	 * 发送语音
	 *
	 * @param filePath
	 * @param fileName
	 * @param length
	 * @param isResend
	 */
	private void sendVoice(String filePath, String fileName, String length, boolean isResend) {
		if (!(new File(filePath).exists())) {
			return;
		}
		try {
			final EMMessage message = EMMessage.createSendMessage(EMMessage.Type.VOICE);
			// 如果是群聊，设置chattype,默认是单聊
			message.setChatType(EMMessage.ChatType.GroupChat);
			message.setReceipt(toChatUsername);
			int len = Integer.parseInt(length);
			VoiceMessageBody body = new VoiceMessageBody(new File(filePath), len);
			message.addBody(body);

			conversation.addMessage(message);
			adapter.refreshSelectLast();
			//setResult(RESULT_OK);
			// send file
			// sendVoiceSub(filePath, fileName, message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 发送图片
	 *
	 * @param filePath
	 */
	private void sendPicture(final String filePath) {
		String to = toChatUsername;
		// create and add image message in view
		final EMMessage message = EMMessage.createSendMessage(EMMessage.Type.IMAGE);
		// 如果是群聊，设置chattype,默认是单聊
		message.setChatType(EMMessage.ChatType.GroupChat);

		message.setReceipt(to);
		ImageMessageBody body = new ImageMessageBody(new File(filePath));
		// 默认超过100k的图片会压缩后发给对方，可以设置成发送原图
		// body.setSendOriginalImage(true);
		message.addBody(body);
		conversation.addMessage(message);

		listView.setAdapter(adapter);
		adapter.refreshSelectLast();
		//setResult(RESULT_OK);
		// more(more);
	}

	/**
	 * 发送视频消息
	 */
	private void sendVideo(final String filePath, final String thumbPath, final int length) {
		final File videoFile = new File(filePath);
		if (!videoFile.exists()) {
			return;
		}
		try {
			EMMessage message = EMMessage.createSendMessage(EMMessage.Type.VIDEO);
			// 如果是群聊，设置chattype,默认是单聊
			message.setChatType(EMMessage.ChatType.GroupChat);
			String to = toChatUsername;
			message.setReceipt(to);
			VideoMessageBody body = new VideoMessageBody(videoFile, thumbPath, length, videoFile.length());
			message.addBody(body);
			conversation.addMessage(message);
			listView.setAdapter(adapter);
			adapter.refreshSelectLast();
			// setResult(RESULT_OK);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 根据图库图片uri发送图片
	 *
	 * @param selectedImage
	 */
	private void sendPicByUri(Uri selectedImage) {
		// String[] filePathColumn = { MediaStore.Images.Media.DATA };
		Cursor cursor = activityInstance.getContentResolver().query(selectedImage, null, null, null, null);
		String st8 = getResources().getString(R.string.cant_find_pictures);
		if (cursor != null) {
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex("_data");
			String picturePath = cursor.getString(columnIndex);
			cursor.close();
			cursor = null;

			if (picturePath == null || picturePath.equals("null")) {
				Toast toast = Toast.makeText(activityInstance, st8, Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				return;
			}
			sendPicture(picturePath);
		} else {
			File file = new File(selectedImage.getPath());
			if (!file.exists()) {
				Toast toast = Toast.makeText(activityInstance, st8, Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				return;

			}
			sendPicture(file.getAbsolutePath());
		}

	}

	/**
	 * 发送位置信息
	 *
	 * @param latitude
	 * @param longitude
	 * @param imagePath
	 * @param locationAddress
	 */
	private void sendLocationMsg(double latitude, double longitude, String imagePath, String locationAddress) {
		EMMessage message = EMMessage.createSendMessage(EMMessage.Type.LOCATION);
		// 如果是群聊，设置chattype,默认是单聊
		message.setChatType(EMMessage.ChatType.GroupChat);
		LocationMessageBody locBody = new LocationMessageBody(locationAddress, latitude, longitude);
		message.addBody(locBody);
		message.setReceipt(toChatUsername);
		conversation.addMessage(message);
		listView.setAdapter(adapter);
		adapter.refreshSelectLast();
		//setResult(RESULT_OK);

	}

	/**
	 * 发送文件
	 *
	 * @param uri
	 */
	private void sendFile(Uri uri) {
		String filePath = null;
		if ("content".equalsIgnoreCase(uri.getScheme())) {
			String[] projection = { "_data" };
			Cursor cursor = null;

			try {
				cursor = activityInstance.getContentResolver().query(uri, projection, null, null, null);
				int column_index = cursor.getColumnIndexOrThrow("_data");
				if (cursor.moveToFirst()) {
					filePath = cursor.getString(column_index);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if ("file".equalsIgnoreCase(uri.getScheme())) {
			filePath = uri.getPath();
		}
		File file = new File(filePath);
		if (file == null || !file.exists()) {
			String st7 = getResources().getString(R.string.File_does_not_exist);
			Toast.makeText(activityInstance, st7, Toast.LENGTH_SHORT).show();
			return;
		}
		if (file.length() > 10 * 1024 * 1024) {
			String st6 = getResources().getString(R.string.The_file_is_not_greater_than_10_m);
			Toast.makeText(activityInstance, st6, Toast.LENGTH_SHORT).show();
			return;
		}

		// 创建一个文件消息
		EMMessage message = EMMessage.createSendMessage(EMMessage.Type.FILE);
		// 如果是群聊，设置chattype,默认是单聊
		message.setChatType(EMMessage.ChatType.GroupChat);

		message.setReceipt(toChatUsername);
		// add message body
		NormalFileMessageBody body = new NormalFileMessageBody(new File(filePath));
		message.addBody(body);
		conversation.addMessage(message);
		listView.setAdapter(adapter);
		adapter.refreshSelectLast();
		//setResult(RESULT_OK);
	}

	/**
	 * 重发消息
	 */
	private void resendMessage() {
		EMMessage msg = null;
		msg = conversation.getMessage(resendPos);
		// msg.setBackSend(true);
		msg.status = EMMessage.Status.CREATE;
		adapter.refreshSeekTo(resendPos);
	}

	/**
	 * 显示语音图标按钮
	 *
	 * @param view
	 */
	public void setModeVoice() {
		hideKeyboard();
		edittext_layout.setVisibility(View.GONE);
		more.setVisibility(View.GONE);
		btn_set_mode_voice.setVisibility(View.GONE);
		buttonSetModeKeyboard.setVisibility(View.VISIBLE);
		buttonSend.setVisibility(View.GONE);
		btnMore.setVisibility(View.VISIBLE);
		buttonPressToSpeak.setVisibility(View.VISIBLE);
		iv_emoticons_normal.setVisibility(View.VISIBLE);
		iv_emoticons_checked.setVisibility(View.INVISIBLE);
		btnContainer.setVisibility(View.VISIBLE);
		emojiIconContainer.setVisibility(View.GONE);
	}

	/**
	 * 显示键盘图标
	 *
	 * @param view
	 */
	public void setModeKeyboard(View view) {
		// mEditTextContent.setOnFocusChangeListener(new OnFocusChangeListener()
		// {
		// @Override
		// public void onFocusChange(View v, boolean hasFocus) {
		// if(hasFocus){
		// getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		// }
		// }
		// });
		edittext_layout.setVisibility(View.VISIBLE);
		more.setVisibility(View.GONE);
		view.setVisibility(View.GONE);
		buttonSetModeVoice.setVisibility(View.VISIBLE);
		// mEditTextContent.setVisibility(View.VISIBLE);
		mEditTextContent.requestFocus();
		// buttonSend.setVisibility(View.VISIBLE);
		buttonPressToSpeak.setVisibility(View.GONE);
		if (TextUtils.isEmpty(mEditTextContent.getText())) {
			btnMore.setVisibility(View.VISIBLE);
			buttonSend.setVisibility(View.GONE);
		} else {
			btnMore.setVisibility(View.GONE);
			buttonSend.setVisibility(View.VISIBLE);
		}

	}

	/**
	 * 点击清空聊天记录
	 *
	 * @param view
	 */
	public void emptyHistory(View view) {
		String st5 = getResources().getString(R.string.Whether_to_empty_all_chats);
		startActivityForResult(new Intent(activityInstance, AlertDialog.class).putExtra("titleIsCancel", true).putExtra("msg", st5)
				.putExtra("cancel", true), REQUEST_CODE_EMPTY_HISTORY);
	}

	/**
	 * 点击进入群组详情
	 *
	 * @param view
	 */
	public void toGroupDetails(View view) {
		if (room == null && emGroup == null) {
			Toast.makeText(activityInstance, R.string.gorup_not_found, Toast.LENGTH_SHORT).show();
			return;
		}
		if (chatType == CHATTYPE_GROUP) {
			startActivityForResult((new Intent(activityInstance, GroupDetailsActivity.class).putExtra("groupId", toChatUsername)),
					REQUEST_CODE_GROUP_DETAIL);
		} else {
			startActivityForResult((new Intent(activityInstance, ChatRoomDetailsActivity.class).putExtra("roomId", toChatUsername)),
					REQUEST_CODE_GROUP_DETAIL);
		}
	}

	/**
	 * 显示或隐藏图标按钮页
	 *
	 * @param view
	 */
	public void more(View view) {
		if (more.getVisibility() == View.GONE) {
			EMLog.d(TAG, "more gone");
			hideKeyboard();
			more.setVisibility(View.VISIBLE);
			btnContainer.setVisibility(View.VISIBLE);
			emojiIconContainer.setVisibility(View.GONE);
		} else {
			if (emojiIconContainer.getVisibility() == View.VISIBLE) {
				emojiIconContainer.setVisibility(View.GONE);
				btnContainer.setVisibility(View.VISIBLE);
				iv_emoticons_normal.setVisibility(View.VISIBLE);
				iv_emoticons_checked.setVisibility(View.INVISIBLE);
			} else {
				more.setVisibility(View.GONE);
			}

		}

	}

	/**
	 * 点击文字输入框
	 *
	 * @param v
	 */
	public void editClick(View v) {
		listView.setSelection(listView.getCount() - 1);
		if (more.getVisibility() == View.VISIBLE) {
			more.setVisibility(View.GONE);
			iv_emoticons_normal.setVisibility(View.VISIBLE);
			iv_emoticons_checked.setVisibility(View.INVISIBLE);
		}

	}

	private PowerManager.WakeLock wakeLock;

	/**
	 * 按住说话listener
	 */
	class PressToSpeakListen implements View.OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (!CommonUtils.isExitsSdcard()) {
					String st4 = getResources().getString(R.string.Send_voice_need_sdcard_support);
					Toast.makeText(activityInstance, st4, Toast.LENGTH_SHORT).show();
					return false;
				}
				try {
					v.setPressed(true);
					wakeLock.acquire();
					if (VoicePlayClickListener.isPlaying)
						VoicePlayClickListener.currentPlayListener.stopPlayVoice();
					recordingContainer.setVisibility(View.VISIBLE);
					recordingHint.setText(getString(R.string.move_up_to_cancel));
					recordingHint.setBackgroundColor(Color.TRANSPARENT);
					voiceRecorder.startRecording(null, toChatUsername, activityInstance);
				} catch (Exception e) {
					e.printStackTrace();
					v.setPressed(false);
					if (wakeLock.isHeld())
						wakeLock.release();
					if (voiceRecorder != null)
						voiceRecorder.discardRecording();
					recordingContainer.setVisibility(View.INVISIBLE);
					Toast.makeText(activityInstance, R.string.recoding_fail, Toast.LENGTH_SHORT).show();
					return false;
				}

				return true;
			case MotionEvent.ACTION_MOVE: {
				if (event.getY() < 0) {
					recordingHint.setText(getString(R.string.release_to_cancel));
					recordingHint.setBackgroundResource(R.drawable.recording_text_hint_bg);
				} else {
					recordingHint.setText(getString(R.string.move_up_to_cancel));
					recordingHint.setBackgroundColor(Color.TRANSPARENT);
				}
				return true;
			}
			case MotionEvent.ACTION_UP:
				v.setPressed(false);
				recordingContainer.setVisibility(View.INVISIBLE);
				if (wakeLock.isHeld())
					wakeLock.release();
				if (event.getY() < 0) {
					// discard the recorded audio.
					voiceRecorder.discardRecording();

				} else {
					// stop recording and send voice file
					String st1 = getResources().getString(R.string.Recording_without_permission);
					String st2 = getResources().getString(R.string.The_recording_time_is_too_short);
					String st3 = getResources().getString(R.string.send_failure_please);
					try {
						int length = voiceRecorder.stopRecoding();
						if (length > 0) {
							sendVoice(voiceRecorder.getVoiceFilePath(), voiceRecorder.getVoiceFileName(toChatUsername),
									Integer.toString(length), false);
						} else if (length == EMError.INVALID_FILE) {
							Toast.makeText(activityInstance, st1, Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(activityInstance, st2, Toast.LENGTH_SHORT).show();
						}
					} catch (Exception e) {
						e.printStackTrace();
						Toast.makeText(activityInstance, st3, Toast.LENGTH_SHORT).show();
					}

				}
				return true;
			default:
				recordingContainer.setVisibility(View.INVISIBLE);
				if (voiceRecorder != null)
					voiceRecorder.discardRecording();
				return false;
			}
		}
	}

	/**
	 * 获取表情的gridview的子view
	 *
	 * @param i
	 * @return
	 */
	private View getGridChildView(int i) {
		View view = View.inflate(activityInstance, R.layout.expression_gridview, null);
		ExpandGridView gv = (ExpandGridView) view.findViewById(R.id.gridview);
		List<String> list = new ArrayList<String>();
		if (i == 1) {
			List<String> list1 = reslist.subList(0, 20);
			list.addAll(list1);
		} else if (i == 2) {
			list.addAll(reslist.subList(20, reslist.size()));
		}
		list.add("delete_expression");
		final ExpressionAdapter expressionAdapter = new ExpressionAdapter(activityInstance, 1, list);
		gv.setAdapter(expressionAdapter);
		gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String filename = expressionAdapter.getItem(position);
				try {
					// 文字输入框可见时，才可输入表情
					// 按住说话可见，不让输入表情
					if (buttonSetModeKeyboard.getVisibility() != View.VISIBLE) {

						if (filename != "delete_expression") { // 不是删除键，显示表情
							// 这里用的反射，所以混淆的时候不要混淆SmileUtils这个类
							Class clz = Class.forName("com.example.Bama.chat.chatuidemo.utils.SmileUtils");
							Field field = clz.getField(filename);
							mEditTextContent.append(SmileUtils.getSmiledText(activityInstance,
									(String) field.get(null)));
						} else { // 删除文字或者表情
							if (!TextUtils.isEmpty(mEditTextContent.getText())) {

								int selectionStart = mEditTextContent.getSelectionStart();// 获取光标的位置
								if (selectionStart > 0) {
									String body = mEditTextContent.getText().toString();
									String tempStr = body.substring(0, selectionStart);
									int i = tempStr.lastIndexOf("[");// 获取最后一个表情的位置
									if (i != -1) {
										CharSequence cs = tempStr.substring(i, selectionStart);
										if (SmileUtils.containsKey(cs.toString()))
											mEditTextContent.getEditableText().delete(i, selectionStart);
										else
											mEditTextContent.getEditableText().delete(selectionStart - 1,
													selectionStart);
									} else {
										mEditTextContent.getEditableText().delete(selectionStart - 1, selectionStart);
									}
								}
							}

						}
					}
				} catch (Exception e) {
				}

			}
		});
		return view;
	}

	public List<String> getExpressionRes(int getSum) {
		List<String> reslist = new ArrayList<String>();
		for (int x = 1; x <= getSum; x++) {
			String filename = "ee_" + x;

			reslist.add(filename);

		}
		return reslist;

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		activityInstance = null;
		if (groupListener != null) {
			EMChatManager.getInstance().removeGroupChangeListener(groupListener);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (emGroup != null)
			((TextView) rootView.findViewById(R.id.name)).setText(emGroup.getGroupName());

		if (adapter != null) {
			adapter.refresh();
		}

		DemoHXSDKHelper sdkHelper = (DemoHXSDKHelper) DemoHXSDKHelper.getInstance();
		// register the event listener when enter the foreground
		EMChatManager.getInstance().registerEventListener(
				this,
				new EMNotifierEvent.Event[] { EMNotifierEvent.Event.EventNewMessage, EMNotifierEvent.Event.EventOfflineMessage,
						EMNotifierEvent.Event.EventDeliveryAck, EMNotifierEvent.Event.EventReadAck });
	}

	@Override
	public void onStop() {
		// unregister this event listener when this activity enters the
		// background
		EMChatManager.getInstance().unregisterEventListener(this);

		DemoHXSDKHelper sdkHelper = (DemoHXSDKHelper) DemoHXSDKHelper.getInstance();

		super.onStop();
	}

	@Override
	public void onPause() {
		super.onPause();
		if (wakeLock.isHeld())
			wakeLock.release();
		if (VoicePlayClickListener.isPlaying && VoicePlayClickListener.currentPlayListener != null) {
			// 停止语音播放
			VoicePlayClickListener.currentPlayListener.stopPlayVoice();
		}

		try {
			// 停止录音
			if (voiceRecorder.isRecording()) {
				voiceRecorder.discardRecording();
				recordingContainer.setVisibility(View.INVISIBLE);
			}
		} catch (Exception e) {
		}
	}

	/**
	 * 隐藏软键盘
	 */
	private void hideKeyboard() {
		if (activityInstance.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (activityInstance.getCurrentFocus() != null)
				manager.hideSoftInputFromWindow(activityInstance.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	/**
	 * 加入到黑名单
	 *
	 * @param username
	 */
	private void addUserToBlacklist(final String username) {
		final ProgressDialog pd = new ProgressDialog(activityInstance);
		pd.setMessage(getString(R.string.Is_moved_into_blacklist));
		pd.setCanceledOnTouchOutside(false);
		pd.show();
		new Thread(new Runnable() {
			public void run() {
				try {
					EMContactManager.getInstance().addUserToBlackList(username, false);
					activityInstance.runOnUiThread(new Runnable() {
						public void run() {
							pd.dismiss();
							Toast.makeText(activityInstance, R.string.Move_into_blacklist_success, Toast.LENGTH_SHORT).show();
						}
					});
				} catch (EaseMobException e) {
					e.printStackTrace();
					activityInstance.runOnUiThread(new Runnable() {
						public void run() {
							pd.dismiss();
							Toast.makeText(activityInstance, R.string.Move_into_blacklist_failure, Toast.LENGTH_SHORT).show();
						}
					});
				}
			}
		}).start();
	}

	/**
	 * 返回
	 *
	 * @param view
	 */
	public void back(View view) {
		EMChatManager.getInstance().unregisterEventListener(this);
		//finish();
	}

	/**
	 * 覆盖手机返回键
	 */
	//    @Override
	public void onBackPressed() {
		if (more.getVisibility() == View.VISIBLE) {
			more.setVisibility(View.GONE);
			iv_emoticons_normal.setVisibility(View.VISIBLE);
			iv_emoticons_checked.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * listview滑动监听listener
	 */
	private class ListScrollListener implements AbsListView.OnScrollListener {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			switch (scrollState) {
			case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
				if (view.getFirstVisiblePosition() == 0 && !isloading && haveMoreData && conversation.getAllMessages().size() != 0) {
					isloading = true;
					loadmorePB.setVisibility(View.VISIBLE);
					// sdk初始化加载的聊天记录为20条，到顶时去db里获取更多
					List<EMMessage> messages;
					EMMessage firstMsg = conversation.getAllMessages().get(0);
					try {
						// 获取更多messges，调用此方法的时候从db获取的messages
						// sdk会自动存入到此conversation中
						messages = conversation.loadMoreGroupMsgFromDB(firstMsg.getMsgId(), pagesize);
					} catch (Exception e1) {
						loadmorePB.setVisibility(View.GONE);
						return;
					}
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
					}
					if (messages.size() != 0) {
						// 刷新ui
						if (messages.size() > 0) {
							adapter.refreshSeekTo(messages.size() - 1);
						}

						if (messages.size() != pagesize)
							haveMoreData = false;
					} else {
						haveMoreData = false;
					}
					loadmorePB.setVisibility(View.GONE);
					isloading = false;

				}
				break;
			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

		}

	}

	//    @Override
	public void onNewIntent(Intent intent) {
		// 点击notification bar进入聊天页面，保证只有一个聊天页面
		String username = intent.getStringExtra("userId");
		if (toChatUsername.equals(username))
			onNewIntent(intent);
		else {
			//            finish();
			startActivity(intent);
		}

	}

	/**
	 * 转发消息
	 *
	 * @param forward_msg_id
	 */
	protected void forwardMessage(String forward_msg_id) {
		final EMMessage forward_msg = EMChatManager.getInstance().getMessage(forward_msg_id);
		EMMessage.Type type = forward_msg.getType();
		switch (type) {
		case TXT:
			// 获取消息内容，发送消息
			String content = ((TextMessageBody) forward_msg.getBody()).getMessage();
			sendText(content);
			break;
		case IMAGE:
			// 发送图片
			String filePath = ((ImageMessageBody) forward_msg.getBody()).getLocalUrl();
			if (filePath != null) {
				File file = new File(filePath);
				if (!file.exists()) {
					// 不存在大图发送缩略图
					filePath = ImageUtils.getThumbnailImagePath(filePath);
				}
				sendPicture(filePath);
			}
			break;
		default:
			break;
		}

		if (forward_msg.getChatType() == EMMessage.ChatType.ChatRoom) {
			EMChatManager.getInstance().leaveChatRoom(forward_msg.getTo());
		}
	}

	/**
	 * 监测群组解散或者被T事件
	 */
	class GroupListener implements EMGroupChangeListener {

		@Override
		public void onUserRemoved(final String groupId, String groupName) {
			activityInstance.runOnUiThread(new Runnable() {
				String st13 = getResources().getString(R.string.you_are_group);

				public void run() {
					if (toChatUsername.equals(groupId)) {
						Toast.makeText(activityInstance, st13, Toast.LENGTH_SHORT).show();
						if (GroupDetailsActivity.instance != null)
							GroupDetailsActivity.instance.finish();
						// finish();
					}
				}
			});
		}

		@Override
		public void onGroupDestroy(final String groupId, String groupName) {
			// 群组解散正好在此页面，提示群组被解散，并finish此页面
			activityInstance.runOnUiThread(new Runnable() {
				String st14 = getResources().getString(R.string.the_current_group);

				public void run() {
					if (toChatUsername.equals(groupId)) {
						Toast.makeText(activityInstance, st14, Toast.LENGTH_SHORT).show();
						if (GroupDetailsActivity.instance != null)
							GroupDetailsActivity.instance.finish();
						//finish();
					}
				}
			});
		}

		@Override
		public void onInvitationReceived(String groupId, String groupName,
				String inviter, String reason) {
		}

		@Override
		public void onApplicationReceived(String groupId, String groupName,
				String applyer, String reason) {
		}

		@Override
		public void onApplicationAccept(String groupId, String groupName,
				String accepter) {

		}

		@Override
		public void onApplicationDeclined(String groupId, String groupName,
				String decliner, String reason) {
		}

		@Override
		public void onInvitationAccpted(String groupId, String inviter,
				String reason) {
		}

		@Override
		public void onInvitationDeclined(String groupId, String invitee,
				String reason) {
		}

	}

	public String getToChatUsername() {
		return toChatUsername;
	}

	public ListView getListView() {
		return listView;
	}

	@Override
	public void onAttalistener(String atusername) {
		mEditTextContent.setText("@" + atusername);
	}
}