package com.example.Bama.util;

public abstract class ModelRequestListener<T> extends Request.RequestListener {
	@Override
	public void onComplete(String response) {

	}

	public abstract void onModelComplete(T model);
}
