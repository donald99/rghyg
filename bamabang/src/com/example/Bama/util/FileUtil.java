package com.example.Bama.util;

import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;
import com.example.Bama.background.HCApplicaton;

import java.io.*;


/**
 * �ļ�������
 */
public final class FileUtil {
	//���Ӧ�õĴ洢Ŀ¼
	public static String getRootPath() {
		String rootPath;
		boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); //�ж�sd���Ƿ����
		if (sdCardExist) {
			rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();//��ȡ��Ŀ¼
		} else {
			rootPath = HCApplicaton.getInstance().getApplicationContext().getFilesDir().getAbsolutePath();
		}
		return rootPath;
	}

	public static boolean hasSDCard() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	// ------------------------------------�ļ�����ط���--------------------------------------------

	/**
	 * �����д��һ���ļ�
	 *
	 * @param destFilePath Ҫ�������ļ���·��
	 * @param data         ��д����ļ����
	 * @param startPos     ��ʼƫ����
	 * @param length       Ҫд�����ݳ���
	 * @return �ɹ�д���ļ�����true, ʧ�ܷ���false
	 */
	public static boolean writeFile(String destFilePath, byte[] data, int startPos, int length) {
		try {
			if (!createFile(destFilePath)) {
				return false;
			}
			FileOutputStream fos = new FileOutputStream(destFilePath);
			fos.write(data, startPos, length);
			fos.flush();
			if (null != fos) {
				fos.close();
				fos = null;
			}
			return true;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * ��һ����������д�ļ�
	 *
	 * @param destFilePath Ҫ�������ļ���·��
	 * @param in           Ҫ��ȡ��������
	 * @return д��ɹ�����true, д��ʧ�ܷ���false
	 */
	public static boolean writeFile(String destFilePath, InputStream in) {
		try {
			if (!createFile(destFilePath)) {
				return false;
			}
			FileOutputStream fos = new FileOutputStream(destFilePath);
			int readCount = 0;
			int len = 1024;
			byte[] buffer = new byte[len];
			while ((readCount = in.read(buffer)) != -1) {
				fos.write(buffer, 0, readCount);
			}
			fos.flush();
			if (null != fos) {
				fos.close();
				fos = null;
			}
			if (null != in) {
				in.close();
				in = null;
			}
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

	public static boolean appendFile(String filename, byte[] data, int datapos, int datalength) {
		try {
			createFile(filename);
			RandomAccessFile rf = new RandomAccessFile(filename, "rw");
			rf.seek(rf.length());
			rf.write(data, datapos, datalength);
			if (rf != null) {
				rf.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	/**
	 * ��ȡ�ļ���������byte������ʽ�����
	 *
	 * @param filePath Ҫ��ȡ���ļ�·����
	 * @return
	 */
	public static byte[] readFile(String filePath) {
		try {
			if (isFileExist(filePath)) {
				FileInputStream fi = new FileInputStream(filePath);
				return readInputStream(fi);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ��һ�����������ȡ���,������byte������ʽ����ݡ�
	 * </br></br>
	 * ��Ҫע����ǣ��������������ڴӱ����ļ���ȡ���ʱ��һ�㲻���������⣬���������������������;���������һЩ�鷳(available()����������)�������������������Ӧ��ʹ�����������
	 *
	 * @param in Ҫ��ȡ��������
	 * @return
	 * @throws java.io.IOException
	 */
	public static byte[] readInputStream(InputStream in) {
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();

			byte[] b = new byte[in.available()];
			int length = 0;
			while ((length = in.read(b)) != -1) {
				os.write(b, 0, length);
			}

			b = os.toByteArray();

			in.close();
			in = null;

			os.close();
			os = null;

			return b;

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ��ȡ������
	 *
	 * @param in
	 * @return
	 */
	public static byte[] readNetWorkInputStream(InputStream in) {
		ByteArrayOutputStream os = null;
		try {
			os = new ByteArrayOutputStream();

			int readCount = 0;
			int len = 1024;
			byte[] buffer = new byte[len];
			while ((readCount = in.read(buffer)) != -1) {
				os.write(buffer, 0, readCount);
			}

			in.close();
			in = null;

			return os.toByteArray();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != os) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				os = null;
			}
		}
		return null;
	}

	/**
	 * ��һ���ļ�����������һ���ط�
	 *
	 * @param sourceFile    Դ�ļ���ַ
	 * @param destFile      Ŀ�ĵ�ַ
	 * @param shouldOverlay �Ƿ񸲸�
	 * @return
	 */
	public static boolean copyFiles(String sourceFile, String destFile, boolean shouldOverlay) {
		try {
			if (shouldOverlay) {
				deleteFile(destFile);
			}
			FileInputStream fi = new FileInputStream(sourceFile);
			writeFile(destFile, fi);
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * �ж��ļ��Ƿ����
	 *
	 * @param filePath ·����
	 * @return
	 */
	public static boolean isFileExist(String filePath) {
		File file = new File(filePath);
		return file.exists();
	}

	/**
	 * ����һ���ļ��������ɹ�����true
	 *
	 * @param filePath
	 * @return
	 */
	public static boolean createFile(String filePath) {
		try {
			File file = new File(filePath);
			if (!file.exists()) {
				if (!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}

				return file.createNewFile();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * ɾ��һ���ļ�
	 *
	 * @param filePath Ҫɾ����ļ�·����
	 * @return true if this file was deleted, false otherwise
	 */
	public static boolean deleteFile(String filePath) {
		try {
			File file = new File(filePath);
			if (file.exists()) {
				return file.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * ɾ�� directoryPathĿ¼�µ������ļ�������ɾ��ɾ���ļ���
	 *
	 * @param
	 */
	public static void deleteDirectory(File dir) {
		if (dir.isDirectory()) {
			File[] listFiles = dir.listFiles();
			for (int i = 0; i < listFiles.length; i++) {
				deleteDirectory(listFiles[i]);
			}
		}
		dir.delete();
	}

	/**
	 * ��ȡ�ļ��д�С
	 *
	 * @param file Fileʵ��
	 * @return long ��λΪM
	 * @throws Exception
	 */
	public static long getFolderSize(File file) throws Exception {
		long size = 0;
		File[] fileList = file.listFiles();
		for (int i = 0; i < fileList.length; i++) {
			if (fileList[i].isDirectory()) {
				size = size + getFolderSize(fileList[i]);
			} else {
				size = size + fileList[i].length();
			}
		}
		return size / 1048576;
	}

	/**
	 * �ַ�ת��
	 *
	 * @param str
	 * @return
	 */
	public static InputStream String2InputStream(String str) {
		ByteArrayInputStream stream = new ByteArrayInputStream(str.getBytes());
		return stream;
	}

	/**
	 * ��ת�ַ�
	 *
	 * @param is
	 * @return
	 */
	public static String inputStream2String(InputStream is) {
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		StringBuffer buffer = new StringBuffer();
		String line = "";

		try {
			while ((line = in.readLine()) != null) {
				buffer.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}

	//��������ļ���׺
	public static void reNameSuffix(File dir, String oldSuffix, String newSuffix) {
		if (dir.isDirectory()) {
			File[] listFiles = dir.listFiles();
			for (int i = 0; i < listFiles.length; i++) {
				reNameSuffix(listFiles[i], oldSuffix, newSuffix);
			}
		} else {
			dir.renameTo(new File(dir.getPath().replace(oldSuffix, newSuffix)));
		}
	}

	public static void writeImage(Bitmap bitmap, String destPath, int quality) {
		try {
			FileUtil.deleteFile(destPath);
			if (FileUtil.createFile(destPath)) {
				FileOutputStream out = new FileOutputStream(destPath);
				if (bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)) {
					out.flush();
					out.close();
					out = null;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ����sdcard����ʱ������time�Ļ���ͼƬ
	 *
	 * @param time
	 */
	public static void clear(long time, String path) {
		if (hasSDCard()) {
			return;
		}
		clearFile(time, System.currentTimeMillis(), path);
	}

	/**
	 * @param time
	 * @param currentTime
	 * @param path
	 */
	private static void clearFile(long time, long currentTime, String path) {
		if (TextUtils.isEmpty(path)) {
			return;
		}
		File file = new File(path);
		if (!file.exists()) {
			return;
		}
		if (file.isFile()) {
			// ɾ������time���ļ�
			if (file.lastModified() < time) {
				file.delete();
			}
			// ��������ϵͳʱ�䲻׼ȷ������������ļ�
			else if (file.lastModified() > currentTime + 1000 * 60 * 60 * 24) {
				file.delete();
			} else {
				//todo
			}
		} else {
			File[] files = file.listFiles();
			if (files != null) {
				for (File file2 : files) {
					clearFile(time, currentTime, file2.getAbsolutePath());
				}
			}
		}
	}
}
