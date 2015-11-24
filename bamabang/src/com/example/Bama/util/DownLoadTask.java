package com.example.Bama.util;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import com.example.Bama.R;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by sreay on 15-11-24.
 */
public class DownLoadTask {
	private long length;
	private Activity context;
	private int progress;
	private RemoteViews view;
	private Notification nf;
	private NotificationManager manager;
	private int count = 0;
	private ProgressBar pb;
	private PendingIntent pIntent;
	private String DownLoad_Url = "http://higo.meilishuo.com/downloads/higo.apk";
	private String downLoadPath = Environment.getExternalStorageDirectory().getPath() + "/higo.apk";

	public DownLoadTask(Context context) {
		this.context = (Activity) context;
		manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
	}

	public void startDownLoad(String url) {
		if (url != null) {
			DownLoad_Url = url;
		}
		new TaskDownLoad().execute();
	}

	class TaskDownLoad extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			showNotification();
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			File file = new File(downLoadPath);
			HttpURLConnection conn;
			try {
				URL url = new URL(DownLoad_Url);
				conn = (HttpURLConnection) url.openConnection();
				length = conn.getContentLength();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (file.exists()) {
				file.delete();
			}
			downFile();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			File file = new File(downLoadPath);
			if (file.exists()) {
				if (file.length() != length)
					return;
				//			showNoticication();
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_VIEW);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setDataAndType(Uri.fromFile(new File(downLoadPath)), "application/vnd.android.package-archive");
				context.startActivity(intent);
				manager.cancelAll();
				//			manager.notify();
				//				context.finish();
			}
		}

		private void downFile() {
			HttpClient client = new DefaultHttpClient();//声明请求端
			HttpGet get;
			try {
				get = new HttpGet(DownLoad_Url);//声明请求URL
				HttpResponse response = client.execute(get); //向服务器请求,返回response
				HttpEntity entity = response.getEntity();//获取数据实体
				length = entity.getContentLength();//获取数据流的长度
				InputStream is = entity.getContent();//获取数据流
				FileOutputStream fileOutputStream = null;//声明文件流
				if (is != null) {//判断数据流部位空
					File file = new File(downLoadPath);//声明文件
					fileOutputStream = new FileOutputStream(file);//文件流用到文件
					byte[] buf = new byte[1024];//声明字节数组，在后面读书数据流的时候用
					int ch = -1;  //后面用来保存数据流字节大小
					while ((ch = is.read(buf)) != -1) {  //读取数据流到自己数组和判断是否读取完
						fileOutputStream.write(buf, 0, ch);  //将读取的数据写入文件中
						count += buf.length;
						if (count >= length / 10) {
							count = 0;
							if (progress < 100) {
								progress += 10;
								mHandler.sendEmptyMessage(1);
							}

						}
							/*fileOutputStream.write(byte[] buffer, int offset, int byteCount)的使用说明
							 * offset:起始位置
		                     * byteCount：字节流长度
		                     */
					}
					fileOutputStream.flush();  //flush缓存
					if (fileOutputStream != null) {
						fileOutputStream.close();  //将文件流关闭
					}
				}
				is.close();
				//		         mHandler.sendEmptyMessage(100);
			} catch (Exception e) {

			}
		}
	}

	private void showNotification() {
		nf = new Notification(R.drawable.ic_launcher, "HIGO", System.currentTimeMillis());
		nf.icon = R.drawable.ic_launcher;
		nf.flags = Notification.FLAG_AUTO_CANCEL;
		Intent intent = new Intent();
		pIntent = PendingIntent.getActivity(context, 0, intent, 0);
		nf.contentIntent = pIntent;
		view = new RemoteViews(context.getPackageName(), R.layout.notification_progress_layout);
		nf.contentView = view;
		manager.notify(0, nf);
	}

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				view.setProgressBar(R.id.progressBar1, 100, progress, false);
				nf.contentView = view;
				manager.notify(0, nf);
			}
			super.handleMessage(msg);
		}

	};
}
