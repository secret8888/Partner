package com.youdao.sharesdk.platform.qq;

import java.util.ArrayList;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;

import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.youdao.sharesdk.R;
import com.youdao.sharesdk.listener.OnShareExecuteListener;
import com.youdao.sharesdk.util.ShareToaster;

/**
 * share message to qzone
 * offical (http://wiki.open.qq.com)
 * @author yuym
 */
public class QZoneClient {

	private Activity context = null;

	private Tencent tencent = null;

	private static QZoneClient client = null;

	private QZoneClient(Activity context, String appId) {
		this.context = context;
		tencent = Tencent.createInstance(appId, context);
	}

	public static QZoneClient getInstance(Activity context, String appId) {
		if (client == null) {
			client = new QZoneClient(context, appId);
		}

		return client;
	}

	/**
	 * share to qq zone and "imageUrls" is array which can share multi images
	 * 
	 * @param title
	 *            share title
	 * @param summary
	 *            share summary
	 * @param targetUrl
	 *            share target url for click
	 * @param imageUrls
	 *            share images
	 * 
	 */
	public void shareToQZone(String title, String summary, String targetUrl,
			ArrayList<String> imageUrls, OnShareExecuteListener listener) {
		Bundle params = new Bundle();
		params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE,
				QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
		params.putString(QzoneShare.SHARE_TO_QQ_TITLE, title);
		params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, summary);
		params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, targetUrl);
		params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
		shareToQZone(params, listener);
	}

	/**
	 * send the message to qq zone
	 * 
	 * @param bundle
	 */
	public void shareToQZone(final Bundle bundle,
			final OnShareExecuteListener listener) {
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				if (listener != null) {
					listener.onPreShareExecute();
				}
			}

			@Override
			protected Void doInBackground(Void... params) {
				tencent.shareToQzone(context, bundle, new IUiListener() {

					@Override
					public void onCancel() {
					}

					@Override
					public void onError(UiError e) {
						ShareToaster.show(context, R.string.share_fail);
					}

					@Override
					public void onComplete(Object response) {
						ShareToaster.show(context, R.string.share_success);
					}

				});
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				if (listener != null) {
					listener.onPostShareExecute();
				}
			}
		}.execute();
	}
}
