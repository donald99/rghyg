package com.example.Bama.util;

public class VersionCompareUtil {
	//可以比较不同的大版本和小版本
	//1 需要更新  0，－1 不需要更新
	public static int compareVersion(String newVersion, String oldVersion) {
		if (newVersion.equals(oldVersion)) {
			return 0;//版本号码相同
		}
		String[] newVersionArray = newVersion.split("\\.");
		String[] oldVersionArray = oldVersion.split("\\.");
		int index = 0;
		int minLen = Math.min(newVersionArray.length, oldVersionArray.length);
		int diff = 0;
		while (index < minLen && (diff = Integer.parseInt(newVersionArray[index]) - Integer.parseInt(oldVersionArray[index])) == 0) {
			index++;
		}
		if (diff == 0) {
			for (int i = index; i < newVersionArray.length; i++) {
				if (Integer.parseInt(newVersionArray[i]) > 0) {
					return 1;
				}
			}
			for (int i = index; i < oldVersionArray.length; i++) {
				if (Integer.parseInt(oldVersionArray[i]) > 0) {
					return -1;
				}
			}
			return 0;
		} else {
			return diff > 0 ? 1 : -1;
		}
	}
}
