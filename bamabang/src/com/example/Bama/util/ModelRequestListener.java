package com.example.Bama.util;

/**
 * Created by sreay on 15-11-24.
 */
public abstract class ModelRequestListener<T> extends Request.RequestListener {
	@Override
	public void onComplete(String response) {

	}

	public abstract void onModelComplete(T model);
}
