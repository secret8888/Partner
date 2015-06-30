package com.partner.common.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.partner.PartnerApplication;
import com.partner.R;
import com.partner.common.constant.Consts;
import com.squareup.okhttp.Response;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Utils {

	private static final String TAG = Utils.class.getSimpleName();

//	/**
//	 * 初始化系统信息 获取系统当前版本号和唯一标识
//	 * 
//	 * @param context
//	 *            为全局参数
//	 */
//	public static void initSystemInfo() {
//		Context context = CoinvsApplication.getInstance();
//		TelephonyManager telephonyManager = (TelephonyManager) context
//				.getSystemService(Context.TELEPHONY_SERVICE);
//		try {
//			PackageManager manager = context.getPackageManager();
//			PackageInfo info = manager.getPackageInfo(context.getPackageName(),
//					0);
//			SystemConfig.getInstance().setSystemInfo(info.versionName,
//					info.versionCode, Build.VERSION.RELEASE,
//					telephonyManager.getDeviceId());
//		} catch (NameNotFoundException e) {
//			Logcat.e(TAG, "Utils.initSystemInfo() cause NameNotFoundException");
//		}
//	}

	/**
	 * 获取系统时间戳 秒
	 * 
	 * @return
	 */
	public static Long getTimestamp() {
		return System.currentTimeMillis() / 1000;
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * check if network is available
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}
	
	public static boolean checkMetworkConnected(Context context) {
		if(!isNetworkConnected(context)) {
			Toaster.show(context, R.string.network_connect_unavailable);
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 输入法显示隐藏开关
	 * 
	 * @param context
	 * @param view
	 * @param isShowInputMethod
	 */
	public static void inputMethodToggle(Context context, View view,
			boolean isShowInputMethod) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (isShowInputMethod) {
			imm.showSoftInput(view, 0);
		} else {
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}

	/**
	 * 获取屏幕宽度
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenWidth(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		dm = context.getResources().getDisplayMetrics();
		return dm.widthPixels;
	}

	/**
	 * 获取屏幕高度
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenHeight(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		dm = context.getResources().getDisplayMetrics();
		return dm.heightPixels;
	}

	/**
	 * 获取文件缓存路径
	 * 
	 * @return
	 */
	public static String getExternStorage() {
		String filePath = null;
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			filePath = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/Partner/";
		}
		if (TextUtils.isEmpty(filePath)) {
			PartnerApplication app = PartnerApplication.getInstance();
			filePath = app.getApplicationContext().getCacheDir()
					.getAbsolutePath()
					+ "/Partner/";
		}
		return filePath;
	}

	public static String getTempDir() {
		return getExternStorage() + "temp/";
	}

	/**
	 * 从view 得到图片
	 * 
	 * @param view
	 * @return
	 */
	public static Bitmap getBitmapFromView(View view) {
		view.destroyDrawingCache();
		view.measure(View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
				.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		view.setDrawingCacheEnabled(true);
		Bitmap bitmap = view.getDrawingCache(true);
		return bitmap;
	}

	/**
	 * 根据路径删除指定的目录或文件，无论存在与否
	 * 
	 * @param sPath
	 *            要删除的目录或文件
	 * @return 删除成功返回 true，否则返回 false。
	 */
	public static boolean deleteFolder(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		// 判断目录或文件是否存在
		if (!file.exists()) { // 不存在返回 false
			return flag;
		} else {
			// 判断是否为文件
			if (file.isFile()) { // 为文件时调用删除文件方法
				return deleteFile(sPath);
			} else { // 为目录时调用删除目录方法
				return deleteDirectory(sPath);
			}
		}
	}

	/**
	 * 删除目录（文件夹）以及目录下的文件
	 * 
	 * @param sPath
	 *            被删除目录的文件路径
	 * @return 目录删除成功返回true，否则返回false
	 */
	public static boolean deleteDirectory(String sPath) {
		// 如果sPath不以文件分隔符结尾，自动添加文件分隔符
		if (!sPath.endsWith(File.separator)) {
			sPath = sPath + File.separator;
		}
		File dirFile = new File(sPath);
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return false;
		}
		boolean flag = true;
		// 删除文件夹下的所有文件(包括子目录)
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag)
					break;
			} // 删除子目录
			else {
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
		}
		if (!flag)
			return false;
		// 删除当前目录
		if (dirFile.delete()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 删除单个文件
	 * 
	 * @param sPath
	 *            被删除文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public static boolean deleteFile(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		return flag;
	}

	/**
	 * 保存图片到本地指定目录
	 * 
	 * @param path
	 * @param bitmap
	 * @param fileName
	 * @return
	 */
	public static int savePicture(String path, Bitmap bitmap, String fileName) {
		File dir = new File(path);
		if (!dir.exists()) {
			if (!dir.mkdirs()) {
				Log.w("Make Dir", "Make Dir Failed!");
			}
		}

		File file = null;
		try {
			file = new File(dir, fileName);
			if (file.exists())
				file.delete();
			file.createNewFile();
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(file));
			bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bos);
			bos.flush();
			bos.close();
			bos = null;
		} catch (IOException e) {
			return 3;
		}

		file = null;
		dir = null;
		return 0;
	}

	/**
	 * 知道文件路径的情况下，覆盖原先的文件
	 * 
	 * @param filePath
	 * @param bitmap
	 * @return
	 */
	public static int replacePicture(String filePath, Bitmap bitmap) {
		File file = null;
		try {
			file = new File(filePath);
			if (!file.exists()) {

			} else {
				file.delete();
			}
			file.createNewFile();
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(file));
			bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bos);
			bos.flush();
			bos.close();
			bos = null;
		} catch (IOException e) {
			return 3;
		}

		file = null;
		return 0;
	}

	/**
	 * 获取限定大小的bitmap
	 * 
	 * @param photoUri
	 * @return
	 */
	public static Bitmap getImage(Context context, Uri photoUri) {
		Bitmap photoBitmap = null;
		try {
			ContentResolver contentResolver = context.getContentResolver();
			BitmapFactory.Options newOpts = new BitmapFactory.Options();
			// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
			newOpts.inJustDecodeBounds = true;
			photoBitmap = BitmapFactory.decodeStream(
					contentResolver.openInputStream(photoUri), null, newOpts);
			newOpts.inJustDecodeBounds = false;
			int w = newOpts.outWidth;
			int h = newOpts.outHeight;
			// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
			float hh = 800f;// 这里设置高度为800f
			float ww = 480f;// 这里设置宽度为480f
			// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
			int be = 1;// be=1表示不缩放
			if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
				be = (int) (newOpts.outWidth / ww);
			} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
				be = (int) (newOpts.outHeight / hh);
			}
			if (be <= 0)
				be = 1;
			newOpts.inSampleSize = be;// 设置缩放比例
			// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
			photoBitmap = BitmapFactory.decodeStream(
					contentResolver.openInputStream(photoUri), null, newOpts);
		} catch (OutOfMemoryError e) {
			return null;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}

		return photoBitmap;
	}

	/**
	 * 获取MD5加密结果
	 * 
	 * @param str
	 * @return
	 */
	public static String getMD5Code(String str) {
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(str.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			System.out.println("NoSuchAlgorithmException caught!");
			System.exit(-1);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		byte[] byteArray = messageDigest.digest();
		StringBuffer md5StrBuff = new StringBuffer();
		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				md5StrBuff.append("0").append(
						Integer.toHexString(0xFF & byteArray[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
		}
		// 16位加密，从第9位到25位
		return md5StrBuff.toString();
	}

	/**
	 * 获取版本号(内部识别号)
	 * 
	 * @param context
	 * @return
	 */
	public static int getVersionCode(Context context) {
		try {
			PackageInfo pi = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			return pi.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public static Bitmap getBitmapByUrl(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            return null;
        }
    }
	
	public static final void saveBitmap2Png(Bitmap bm, File file) {
        BufferedOutputStream bos;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(file));
            bm.compress(Bitmap.CompressFormat.PNG, 100, bos);
            bos.flush();
            bos.close();
        } catch (Exception e) {
        }
    }
	
	public static final void savePngAtTemp(Bitmap bm, String fileName) {
        File tempDir = new File(Consts.TEMP_FILE_PATH);
        if (!tempDir.exists()) {
            tempDir.mkdir();
        }
        File pic = new File(Consts.TEMP_FILE_PATH + fileName);
        if (pic.exists()) {
            pic.delete();
        }
        saveBitmap2Png(bm, pic);
    }

	public static String getTime() {
		return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA)
				.format(new Date());
	}
}
