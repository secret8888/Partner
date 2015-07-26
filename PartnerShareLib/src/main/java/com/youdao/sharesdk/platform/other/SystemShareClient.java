/**
 * @(#)SystemShareClient.java, Nov 1, 2013. Copyright 2013 Yodao, Inc. All rights
 *                       reserved. YODAO PROPRIETARY/CONFIDENTIAL. Use is
 *                       subject to license terms.
 */
package com.youdao.sharesdk.platform.other;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * share by system share function, if imageUri is null, share text only. or
 * share image and text
 * 
 * @author yuym
 * 
 */
public class SystemShareClient {

	private static SystemShareClient shareClient = null;

	private SystemShareClient() {
	}

	public static SystemShareClient getInstance() {
		if (shareClient == null) {
			shareClient = new SystemShareClient();
		}

		return shareClient;
	}

	/**
	 * share info by system share function
	 * 
	 * @param context
	 * @param title
	 * @param description
	 * @param imageUri
	 */
	public void sharePublic(Context context, String title, String description,
			Uri imageUri) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		if (imageUri != null) {
			intent.setType("image/*");
			intent.putExtra(Intent.EXTRA_STREAM, imageUri);
		} else {
			intent.setType("text/plain");
		}
		intent.putExtra(Intent.EXTRA_SUBJECT, title);
		intent.putExtra(Intent.EXTRA_TEXT, description);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}
}
