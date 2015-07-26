package com.youdao.sharesdk.listener;

public interface OnShareExecuteListener {

	/**
	 * before share excuting
	 */
	public void onPreShareExecute();
	
	/**
	 * after share excuting
	 */
	public void onPostShareExecute();
}
