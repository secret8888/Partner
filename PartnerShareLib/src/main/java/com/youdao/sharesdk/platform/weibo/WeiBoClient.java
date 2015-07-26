package com.youdao.sharesdk.platform.weibo;

import java.io.File;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.sina.weibo.sdk.api.BaseMediaObject;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.MusicObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.VideoObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMessage;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.utils.Utility;
import com.youdao.sharesdk.R;
import com.youdao.sharesdk.listener.OnShareExecuteListener;
import com.youdao.sharesdk.util.ShareToaster;
import com.youdao.sharesdk.util.Util;

/**
 * a singleton pattern class which includes weibo share functions. current sdk
 * version is 2.5.0 and since this version, only sso share is supported which
 * means you must install weibo client.
 * 
 * offical : http://open.weibo.com/
 * 
 * @author yuym
 */
public class WeiBoClient {

	private Activity context = null;

	private IWeiboShareAPI mWeiboShareAPI = null;

	private static WeiBoClient client = null;

	private WeiBoClient(Activity context, String appKey) {
		this.context = context;
	}

	public static WeiBoClient getInstance(Activity context, String appKey) {
		if (client == null) {
			client = new WeiBoClient(context, appKey);
		}
		client.registerWeiBoApp(appKey);
		return client;
	}

	/**
	 * register weibo app
	 * 
	 * @param appKey
	 */
	public void registerWeiBoApp(String appKey) {
		mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(context, appKey);
		mWeiboShareAPI.registerApp();
	}

	/**
	 * check whether weibo is installed
	 * 
	 * @return
	 */
	public boolean isWBAppInstalled() {
		return mWeiboShareAPI.isWeiboAppInstalled();
	}

	/**
	 * ge weibo support api level
	 * 
	 * @return
	 */
	public int getSupportApiLevel() {
		return mWeiboShareAPI.getWeiboAppSupportAPI();
	}

	/**
	 * send message to weibo
	 * 
	 * @param textObject
	 *            store text info
	 * @param imageObject
	 *            store image info
	 * @param mediaObject
	 *            store other media info
	 */
	private void sendMessage(TextObject textObject, ImageObject imageObject,
			BaseMediaObject mediaObject) {

		if (mWeiboShareAPI.isWeiboAppSupportAPI()) {
			int supportApi = mWeiboShareAPI.getWeiboAppSupportAPI();
			if (supportApi >= 10351) {
				sendMultiMessage(textObject, imageObject, mediaObject);
			} else {
				sendSingleMessage(textObject, imageObject, mediaObject);
			}
		} else {
			ShareToaster.show(context,
					R.string.weibosdk_demo_not_support_api_hint);
		}
	}

	/**
	 * if supportApi >= 10351, send multi messsage to weibo
	 * 
	 * @param textObject
	 * @param imageObject
	 * @param mediaObject
	 */
	private void sendMultiMessage(TextObject textObject,
			ImageObject imageObject, BaseMediaObject mediaObject) {

		WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
		if (textObject != null) {
			weiboMessage.textObject = textObject;
		}

		if (imageObject != null) {
			weiboMessage.imageObject = imageObject;
		}

		// one of web page、music、video、voice
		if (mediaObject != null) {
			weiboMessage.mediaObject = mediaObject;
		}

		// init message request to weibo
		SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
		// transaction tag a request
		request.transaction = String.valueOf(System.currentTimeMillis());
		request.multiMessage = weiboMessage;

		// send message and call weibo share interface(view)
		mWeiboShareAPI.sendRequest(request);
	}

	/**
	 * if supportApi < 10351, send only single messsage to weibo
	 * 
	 * @param textObject
	 * @param imageObject
	 * @param mediaObject
	 */
	private void sendSingleMessage(TextObject textObject,
			ImageObject imageObject, BaseMediaObject mediaObject) {

		// init weibo share message
		// you only can share one of web page、music、video、voice
		WeiboMessage weiboMessage = new WeiboMessage();
		if (textObject != null) {
			weiboMessage.mediaObject = textObject;
		}

		if (imageObject != null) {
			weiboMessage.mediaObject = imageObject;
		}

		if (mediaObject != null) {
			weiboMessage.mediaObject = mediaObject;
		}

		SendMessageToWeiboRequest request = new SendMessageToWeiboRequest();
		request.transaction = String.valueOf(System.currentTimeMillis());
		request.message = weiboMessage;

		mWeiboShareAPI.sendRequest(request);
	}

	/**
	 * get formated text objext
	 * 
	 * @param shareText
	 * @return
	 */
	private TextObject getTextObj(String shareText) {
		TextObject textObject = new TextObject();
		textObject.text = shareText;
		return textObject;
	}

	/**
	 * get formated image onject
	 * 
	 * @param bitmap
	 * @return
	 */
	private ImageObject getImageObj(Bitmap bitmap) {
		ImageObject imageObject = new ImageObject();
		imageObject.setImageObject(bitmap);
		return imageObject;
	}

	/**
	 * share text only to weibo
	 * 
	 * @param shareText
	 */
	public void shareText(String shareText) {
		if (!mWeiboShareAPI.checkEnvironment(true)) {
			return;
		}
		TextObject textObject = getTextObj(shareText);
		sendMessage(textObject, null, null);
	}

	/**
	 * share image by the form of bitmap
	 * 
	 * @param shareText
	 * @param shareBitmap
	 */
	public void shareImageByBitmap(String shareText, Bitmap shareBitmap) {
		if (!mWeiboShareAPI.checkEnvironment(true)) {
			return;
		}
		TextObject textObject = getTextObj(shareText);
		ImageObject imageObject = getImageObj(shareBitmap);
		sendMessage(textObject, imageObject, null);
	}

	/**
	 * share image by the form of local path
	 * 
	 * @param shareText
	 * @param localPath
	 */
	public void shareImageByPath(String shareText, String localPath) {
		if (!mWeiboShareAPI.checkEnvironment(true)) {
			return;
		}
		File file = new File(localPath);
		if (!file.exists()) {
			ShareToaster.show(context, R.string.image_not_null);
			return;
		}

		Bitmap shareBitmap = BitmapFactory.decodeFile(localPath);
		TextObject textObject = getTextObj(shareText);
		ImageObject imageObject = getImageObj(shareBitmap);
		sendMessage(textObject, imageObject, null);
		shareBitmap.recycle();
	}

	/**
	 * share image by the form of web url
	 * 
	 * @param shareText
	 * @param url
	 * @param listener
	 */
	public void shareImageByUrl(final String shareText, final String url,
			final OnShareExecuteListener listener) {
		if (!mWeiboShareAPI.checkEnvironment(true)) {
			return;
		}
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
				try {
					Bitmap shareBitmap = Util.getBitmapByUrl(url);
					TextObject textObject = getTextObj(shareText);
					ImageObject imageObject = getImageObj(shareBitmap);
					sendMessage(textObject, imageObject, null);
					shareBitmap.recycle();
				} catch (Exception e) {
					e.printStackTrace();
					ShareToaster.show(context, R.string.share_fail);
				}
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

	/**
	 * get formated web page object
	 * 
	 * @param pageTitle
	 *            web page title
	 * @param pageDesc
	 *            web page description
	 * @param thumb
	 *            web page thumb
	 * @param actionUrl
	 *            action url when you click
	 * @return the object weibo need
	 */
	private WebpageObject getWebpageObj(String pageTitle, String pageDesc,
			Bitmap thumb, String actionUrl) {
		WebpageObject mediaObject = new WebpageObject();
		mediaObject.identify = Utility.generateGUID();
		mediaObject.title = pageTitle;
		mediaObject.description = pageDesc;

		// set thumb for page
		mediaObject.setThumbImage(thumb);
		mediaObject.actionUrl = actionUrl;
		return mediaObject;
	}

	/**
	 * share webpage to weibo
	 * 
	 * @param shareText
	 *            share text which will show in the body of weibo item
	 * @param shareBitmap
	 *            share image which will show in the body of weibo item, too
	 * @param pageTitle
	 * @param pageDesc
	 * @param thumbBmp
	 *            thumb of webpage which different from shareBitmap
	 * @param actionUrl
	 */
	public void shareWebPage(String shareText, Bitmap shareBitmap,
			String pageTitle, String pageDesc,
			Bitmap thumbBmp, String actionUrl) {
		if (!mWeiboShareAPI.checkEnvironment(true)) {
			return;
		}

		TextObject textObject = getTextObj(shareText);
		WebpageObject pageObject = getWebpageObj(pageTitle, pageDesc, thumbBmp,
				actionUrl);
		sendMessage(textObject, shareBitmap == null ? null
				: getImageObj(shareBitmap), pageObject);
	}

	/**
	 * get formated music object
	 * 
	 * @param musicTitle
	 * @param musicDes
	 * @param thumb
	 * @param actionUrl
	 * @param dataUrl
	 * @return
	 */
	private MusicObject getMusicObj(String musicTitle, String musicDes,
			Bitmap thumb, String actionUrl, String dataUrl) {
		MusicObject musicObject = new MusicObject();
		musicObject.identify = Utility.generateGUID();
		musicObject.title = musicTitle;
		musicObject.description = musicDes;

		// set thumb for music
		musicObject.setThumbImage(thumb);
		musicObject.actionUrl = actionUrl;
		musicObject.dataUrl = dataUrl;
		// musicObject.dataHdUrl = "http://music.163.com/#/song?id=390782";
		musicObject.duration = 10;
		return musicObject;
	}

	/**
	 * share web music to weibo
	 * 
	 * @param shareText
	 * @param shareBitmap
	 * @param musicTitle
	 * @param musicDes
	 * @param thumbBmp
	 * @param actionUrl
	 * @param dataUrl
	 */
	public void shareMusic(final String shareText, final Bitmap shareBitmap,
			final String musicTitle, final String musicDes,
			final Bitmap thumbBmp, final String actionUrl,
			final String dataUrl) {
		TextObject textObject = getTextObj(shareText);
		MusicObject musicObject = getMusicObj(musicTitle, musicDes, thumbBmp,
				actionUrl, dataUrl);
		sendMessage(textObject, shareBitmap == null ? null
				: getImageObj(shareBitmap), musicObject);
	}

	/**
	 * get formated video object
	 * 
	 * @param videoTitle
	 * @param videoDes
	 * @param thumb
	 * @param actionUrl
	 * @param dataUrl
	 * @return
	 */
	private VideoObject getVideoObj(String videoTitle, String videoDes,
			Bitmap thumb, String actionUrl, String dataUrl) {
		VideoObject videoObject = new VideoObject();
		videoObject.identify = Utility.generateGUID();
		videoObject.title = videoTitle;
		videoObject.description = videoDes;

		videoObject.setThumbImage(thumb);
		videoObject.actionUrl = actionUrl;
		videoObject.dataUrl = dataUrl;
		// videoObject.dataHdUrl = "www.weibo.com";
		videoObject.duration = 10;
		// videoObject.defaultText = "Video default text";
		return videoObject;
	}

	/**
	 * share web video to weibo
	 * 
	 * @param shareText
	 * @param shareBitmap
	 * @param videoTitle
	 * @param videoDes
	 * @param thumbBmp
	 * @param actionUrl
	 * @param dataUrl
	 */
	public void shareVideo(final String shareText, final Bitmap shareBitmap,
			final String videoTitle, final String videoDes,
			final Bitmap thumbBmp, final String actionUrl,
			final String dataUrl) {
		TextObject textObject = getTextObj(shareText);
		VideoObject videoObject = getVideoObj(videoTitle, videoDes, thumbBmp,
				actionUrl, dataUrl);
		sendMessage(textObject, shareBitmap == null ? null
				: getImageObj(shareBitmap), videoObject);
	}

}
