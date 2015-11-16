package com.example.Bama.util;

import android.util.Base64;

import java.io.UnsupportedEncodingException;

public class Base64Util {

	/**
	 * BASE64 加密
	 * @param str
	 * @return
	 */
	public static  String encryptBASE64(String str) {
		if (str == null || str.length() == 0) {
			return null;
		}
		try {
			byte[] encode = str.getBytes("UTF-8");
			// base64 加密
			return new String(Base64.encode(encode, 0, encode.length, Base64.DEFAULT), "UTF-8");

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * BASE64 解密
	 * @param str
	 * @return
	 */
	public static  String decryptBASE64(String str) {
		if (str == null || str.length() == 0) {
			return null;
		}
		try {
			byte[] encode = str.getBytes("UTF-8");
			// base64 解密
			return new String(Base64.decode(encode, 0, encode.length, Base64.DEFAULT), "UTF-8");

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return null;
	}


	public static void main(String [] args) {


	}
}