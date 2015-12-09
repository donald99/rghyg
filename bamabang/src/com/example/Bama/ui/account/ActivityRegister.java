package com.example.Bama.ui.account;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.easemob.EMError;
import com.easemob.chat.EMChatManager;
import com.easemob.exceptions.EaseMobException;
import com.example.Bama.R;
import com.example.Bama.background.Account;
import com.example.Bama.background.HCApplication;
import com.example.Bama.ui.ActivityBase;
import com.example.Bama.util.ToastUtil;

public class ActivityRegister extends ActivityBase implements View.OnClickListener {

	private Account account;
	/**
	 * init views
	 * *
	 */
	private FrameLayout backFL;
	private TextView commit;
	private EditText phoneNumber;
	private EditText password;

	public static void open(Activity activity) {
		Intent intent = new Intent(activity, ActivityRegister.class);
		activity.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_register_hc);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void getViews() {
		account = HCApplication.getInstance().getAccount();
		backFL = (FrameLayout) findViewById(R.id.backFL);
		commit = (TextView) findViewById(R.id.commit);
		phoneNumber = (EditText) findViewById(R.id.phoneNumber);
		password = (EditText) findViewById(R.id.password);
	}

	@Override
	protected void initViews() {

	}

	@Override
	protected void setListeners() {
		backFL.setOnClickListener(this);
		commit.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.backFL:
			finish();
			break;
		case R.id.commit:
			tryToRegister();
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private void tryToRegister() {
		final String phoneNumberStr = phoneNumber.getText().toString();
		if (TextUtils.isEmpty(phoneNumberStr)) {
			ToastUtil.makeShortText("昵称不能为空");
			return;
		}
		final String pwdStr = password.getText().toString();
		if (TextUtils.isEmpty(pwdStr)) {
			ToastUtil.makeShortText("密码不能为空");
			return;
		}
		tryToRegisterChatServer(phoneNumberStr, pwdStr);
	}

	private void tryToRegisterChatServer(final String accountId, final String password) {
		final String st7 = getResources().getString(R.string.network_anomalies);
		final String st8 = getResources().getString(R.string.User_already_exists);
		final String st9 = getResources().getString(R.string.registration_failed_without_permission);
		final String st10 = getResources().getString(R.string.Registration_failed);
		new Thread(new Runnable() {
			public void run() {
				try {
					EMChatManager.getInstance().createAccountOnServer(accountId, password);
					runOnUiThread(new Runnable() {
						public void run() {
							account.userId = accountId;
							account.password = password;
							account.saveMeInfoToPreference();
							HCApplication.getInstance().setUserName(account.userId);
							ToastUtil.makeShortText("注册成功");
							finish();
						}
					});
				} catch (final EaseMobException e) {
					runOnUiThread(new Runnable() {
						public void run() {
							int errorCode = e.getErrorCode();
							if (errorCode == EMError.NONETWORK_ERROR) {
								Toast.makeText(getApplicationContext(), st7, Toast.LENGTH_SHORT).show();
							} else if (errorCode == EMError.USER_ALREADY_EXISTS) {
								Toast.makeText(getApplicationContext(), st8, Toast.LENGTH_SHORT).show();
							} else if (errorCode == EMError.UNAUTHORIZED) {
								Toast.makeText(getApplicationContext(), st9, Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(getApplicationContext(), st10 + e.getMessage(), Toast.LENGTH_SHORT).show();
							}
						}
					});
				}
			}
		}).start();
	}
}
