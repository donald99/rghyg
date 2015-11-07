package com.example.Bama.util;

import android.text.TextUtils;
import android.util.Log;

/**
 * ��־��Ϣ��ӡ������
 *
 * @title:
 * @description:
 * @company: ����˵������������Ƽ����޹�˾
 * @author yinxinya
 * @version 1.0
 * @created
 * @changeRecord
 */
public class Debug {

	private static final String TAG = "HG";
	public static boolean DEBUG = true;

	private static String checkTag(String tag) {
		if (TextUtils.isEmpty(tag)) {
			tag = TAG;
		}
		return tag;

	}

	public static void debug(String msg) {
		if (DEBUG) {
			Log.d(TAG, "debug: " + msg);
		}
	}

	public static void trace(String msg) {
		if (DEBUG) {
			Log.v(TAG, "trace: " + msg);
		}
	}

	public static void warn(String msg) {
		if (DEBUG) {
			Log.w(TAG, "warning: " + msg);
		}
	}

	public static void error(String msg) {
		if (DEBUG) {
			Log.e(TAG, "error: " + msg);
		}
	}

	public static void info(String msg) {
		if (DEBUG) {
			Log.i(TAG, "info: " + msg);
		}
	}

	/**
	 *
	 * 2011 -10 - 25 �����bytag ��׺֧������Լ��Ķ����tag �� by zxy
	 *
	 */

	public static void info(String tag, String msg) {
		if (DEBUG) {
			Log.i(checkTag(tag), "info: " + msg);
		}
	}

	public static void debug(String tag, String msg) {
		if (DEBUG) {
			Log.d(checkTag(tag), "debug: " + msg);
		}
	}

	public static void trace(String tag, String msg) {
		if (DEBUG) {
			Log.v(checkTag(tag), "trace: " + msg);
		}
	}

	public static void warn(String tag, String msg) {
		if (DEBUG) {
			Log.w(checkTag(tag), "warning: " + msg);
		}
	}

	public static void error(String tag, String msg) {
		if (DEBUG) {
			Log.e(checkTag(tag), "error" + msg);
		}
	}

	public static void info(String tag, String msg, Throwable throwable) {
		if (DEBUG) {
			Log.i(checkTag(tag), "info: " + msg, throwable);
		}
	}

	public static void debug(String tag, String msg, Throwable throwable) {
		if (DEBUG) {
			Log.d(checkTag(tag), "debug: " + msg, throwable);
		}
	}

	public static void trace(String tag, String msg, Throwable throwable) {
		if (DEBUG) {
			Log.v(checkTag(tag), "trace: " + msg, throwable);
		}
	}

	public static void warn(String tag, String msg, Throwable throwable) {
		if (DEBUG) {
			Log.w(checkTag(tag), "warn: " + msg, throwable);
		}
	}

	public static void error(String tag, String msg, Throwable throwable) {
		if (DEBUG) {
			Log.e(checkTag(tag), "error: " + msg, throwable);
		}
	}

	/**
	 * ǿ�ƴ�ӡmsg��Ϣ
	 *
	 * @param msg
	 */
	public static void forcePrint(String msg) {
		Log.i(TAG, "forcePrint: " + msg);
	}

	/**
	 * ǿ�ƴ�ӡmsg��Ϣ
	 *
	 * @param tag
	 * @param msg
	 */
	public static void forcePrint(String tag, String msg) {
		Log.i(checkTag(tag), "forcePrint: " + msg);
	}

	/**
	 * ǿ�ƴ�ӡmsg��Ϣ
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void forcePrint(String tag, String msg, Throwable throwable) {
		Log.i(checkTag(tag), "forcePrint: " + msg, throwable);
	}

}
