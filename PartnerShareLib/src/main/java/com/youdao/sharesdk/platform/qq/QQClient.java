package com.youdao.sharesdk.platform.qq;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;

import com.tencent.connect.auth.QQAuth;
import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.youdao.sharesdk.R;
import com.youdao.sharesdk.util.ShareToaster;

/**
 * share message to qq friends.
 * offical website: http://wiki.open.qq.com
 * 
 * @author yuym
 */
public class QQClient {

	private Activity context = null;

	private QQAuth mQQAuth = null;

	private QQShare mQQShare = null;

	/*
	 * when share to qq, don't show the dialog of sharing to qzone
	 */
	private int mExtarFlag = QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE;

	private static QQClient client = null;

	private QQClient(Activity context, String appId) {
		this.context = context;
		mQQAuth = QQAuth.createInstance(appId, context);
		mQQShare = new QQShare(context, mQQAuth.getQQToken());
		// if (mQQAuth != null && mQQAuth.isSessionValid()) {
		// mQQShare = new QQShare(context, mQQAuth.getQQToken());
		// } else {
		// mQQAuth = QQAuth.createInstance(appId, context);
		// }
	}

	public static QQClient getInstance(Activity context, String appId) {
		if (client == null) {
			client = new QQClient(context, appId);
		}

		return client;
	}

	/**
	 * init bundle for params
	 * 
	 * @param params
	 * @param title
	 * @param summary
	 * @param targetUrl
	 *            target url
	 * @param imageUrl
	 *            thumb image url
	 * @param appName
	 * @param type
	 */
	private void initBundle(Bundle params, String title, String summary,
			String targetUrl, String imageUrl, String appName, int type) {
		params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
		params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, targetUrl);
		params.putString(QQShare.SHARE_TO_QQ_SUMMARY, summary);

		params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imageUrl);

		params.putString(QQShare.SHARE_TO_QQ_APP_NAME, appName);
		params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, type);
		params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, mExtarFlag);
	}

	/**
	 * share to qq friends by default. note: the parameter "imageUrl" can be
	 * either network url or local path
	 * 
	 * @param title
	 *            share title
	 * @param summary
	 *            share summary
	 * @param targetUrl
	 *            share target url for click
	 * @param imageUrl
	 *            thumb image for displaying
	 * @param appName
	 *            app name
	 * 
	 */
	public void shareToQQByDefault(String title, String summary,
			String targetUrl, String imageUrl, String appName) {
		Bundle params = new Bundle();
		initBundle(params, title, summary, targetUrl, imageUrl, appName,
				QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
		shareToQQ(params);
	}

	/**
	 * share network audio to qq friends
	 * 
	 * @param title
	 *            share title
	 * @param summary
	 *            share summary
	 * @param targetUrl
	 *            share target url for click
	 * @param imageUrl
	 *            thumb image for displaying
	 * @param appName
	 *            app name
	 * @param audioUrl
	 *            the audio url for playing on display
	 */
	public void shareToQQByAudio(String title, String summary,
			String targetUrl, String imageUrl, String appName, String audioUrl) {
		Bundle params = new Bundle();
		initBundle(params, title, summary, targetUrl, imageUrl, appName,
				QQShare.SHARE_TO_QQ_TYPE_AUDIO);
		params.putString(QQShare.SHARE_TO_QQ_AUDIO_URL, audioUrl);
		shareToQQ(params);
	}

	/**
	 * share image only to friends. note: the parameter "localImageUrl" can be
	 * only local image path
	 * 
	 * @param imageUrl
	 *            image path
	 * @param appName
	 *            app name
	 */
	public void shareToQQByImage(String localImageUrl, String appName) {
		Bundle params = new Bundle();
		params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, localImageUrl);
		params.putString(QQShare.SHARE_TO_QQ_APP_NAME, appName);
		params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE,
				QQShare.SHARE_TO_QQ_TYPE_IMAGE);
		params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, mExtarFlag);
		shareToQQ(params);
	}

	/**
	 * send the message to qq friends
	 * 
	 * @param bundle
	 */
	public void shareToQQ(final Bundle bundle) {
		// if (mQQShare == null && mQQAuth != null && mQQAuth.isSessionValid())
		// {
		// mQQShare = new QQShare(context, mQQAuth.getQQToken());
		// }
		if (mQQShare != null) {
			new AsyncTask<Void, Void, Void>() {
				@Override
				protected Void doInBackground(Void... params) {
					mQQShare.shareToQQ(context, bundle, new IUiListener() {

						@Override
						public void onCancel() {
						}

						@Override
						public void onComplete(Object response) {
							context.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									ShareToaster.show(context,
											R.string.share_success);
								}
							});
						}

						@Override
						public void onError(UiError e) {
							context.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									ShareToaster.show(context,
											R.string.share_fail);
								}
							});
						}

					});
					return null;
				}
			}.execute();
		}
	}
}
