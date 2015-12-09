package com.example.Bama.widget.photo;import android.content.Intent;import android.graphics.Bitmap;import android.text.TextUtils;import com.example.Bama.R;import com.example.Bama.ui.ActivityBase;import java.util.ArrayList;/** *  * @title:拍照或者从图库选择图片 */public abstract class PhotoChooseActicity extends ActivityBase {	protected final int REQUEST_PIC = 100;	protected String r;	public abstract void onChoosePhoto(String path);	public abstract void onChoosePhoto(String[] paths);	public abstract void onChoosePhoto(Bitmap bitmap);	public void showPicPhotoDialog() {		showPicPhotoDialog(getString(R.string.text_chooce_photo), null, 1,false);	}	/**	 * 	 * @param resId	 * @param needCrop	 *            是否需要剪切图片	 */	public void showPicPhotoDialog(int resId, boolean needCrop) {		showPicPhotoDialog(getString(resId), null, 1, needCrop);	}	/**	 * 	 * @param	 */	public void showPicPhotoDialog(final ArrayList<String> selectPaths,final int max) {		showPicPhotoDialog(getString(R.string.text_chooce_photo), selectPaths,max, false);	}	/**	 * 	 * @param	 */	public void showPicPhotoDialog(String title,final ArrayList<String> selectPaths, final int max, boolean needCrop) {		Intent intent = new Intent(this, PhotoChooseImplActivity.class);		intent.putExtra("title", title);		intent.putExtra("select", selectPaths);		intent.putExtra("max", max);		intent.putExtra("needcrop", needCrop);		intent.putExtra("r", r);		startActivityForResult(intent, REQUEST_PIC);		overridePendingTransition(-1, -1);	}	/**	 * 预览照片	 * 	 * @param position	 * @param paths	 */	public void preView(int position, ArrayList<PreviewAdapter.PreviewItem> paths, String r) {		Intent intent = new Intent(this, PhotoChooseImplActivity.class);		intent.putExtra("mode", PreviewActivity.MODE_DEL);		intent.putExtra("items", paths);		intent.putExtra("position", position);		intent.putExtra("r", r);		startActivityForResult(intent, REQUEST_PIC);	}	@Override	public void onActivityResult(int requestCode, int resultCode, Intent data) {		super.onActivityResult(requestCode, resultCode, data);		if (resultCode == RESULT_OK && requestCode == REQUEST_PIC) {			String path = data.getStringExtra("path");			if (!TextUtils.isEmpty(path)) {				onChoosePhoto(path);			}			String[] paths = data.getStringArrayExtra("paths");			if (paths != null) {				onChoosePhoto(paths);			}			Bitmap bitmap = data.getParcelableExtra("bitmap");			if (bitmap != null) {				onChoosePhoto(bitmap);			}		}	}}