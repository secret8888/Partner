package com.youdao.sharesdk.util;

import java.io.ByteArrayOutputStream;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;

public class Util {

	/**
	 * change bitmap to byte array
	 * 
	 * @param bmp
	 * @param needRecycle
	 *            whether bitmap need to be recycled
	 * @return
	 */
	public static byte[] bmpToByteArray(final Bitmap bmp,
			final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 100, output);
		if (needRecycle) {
			bmp.recycle();
		}

		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * get bitmap from the url
	 * 
	 * @param url
	 *            web url
	 * @return
	 */
	public static Bitmap getBitmapByUrl(String url) {
		try {
			return BitmapFactory.decodeStream(new URL(url).openStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
