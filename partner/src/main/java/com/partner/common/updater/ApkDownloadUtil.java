/**
 * @(#)ApkDownloadUtil.java, Mar 7, 2014. Copyright 2014 Yodao, Inc. All rights
 * reserved. YODAO PROPRIETARY/CONFIDENTIAL. Use is
 * subject to license terms.
 */
package com.partner.common.updater;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;

import com.partner.PartnerApplication;
import com.partner.R;
import com.partner.common.constant.Consts;
import com.partner.common.util.Toaster;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * apk安装包下载工具类
 * 初始化顺序：
 *    1.initAppDownloadInfo()初始化安装包信息
 *    2.setDownloadListener 设置下载监听器
 *    3.startDownload 执行下载
 * @author emilyu
 */
public class ApkDownloadUtil {

    private boolean isDownloading = false;

    private String downloadUrl = null;

    private String appName = null;

    private String packageName = null;

    private DownloadHandler downloadHandler = null;

    private NotificationManager mNotifyManager = null;

    private NotificationCompat.Builder mBuilder = null;

    private DownloadThread downloadThread = null;

    private ApkDownloadListener downloadListener = null;

    private static ApkDownloadUtil apkDownloadUtil = null;

    public final static int PROGRESS_MSG = 1;

    public final static int DOWNLOAD_SUCCESS_MSG = 2;

    public final static int DOWNLOAD_FAIL_MSG = 3;

    public final static int DOWNLOAD_CANCEL_MSG = 4;

    private final static String APK_DOWNLOAD_DIR = Consts.TEMP_FILE_PATH;

    private final static String APK_DOWNLOAD_SUF = ".apk";

    private final static String APK_DOWNLOAD_TEMP = ".temp";

    private long mContentSize;

    private ApkDownloadUtil() {
    }

    public synchronized static ApkDownloadUtil getInstance() {
        if (apkDownloadUtil == null) {
            apkDownloadUtil = new ApkDownloadUtil();
        }

        return apkDownloadUtil;
    }

    /**
     * 初始化要下载的安装包信息
     * @param appName
     * @param packageName
     * @param downloadUrl
     */
    public void initAppDownloadInfo(String appName, String packageName, String downloadUrl) {
        this.appName = appName;
        this.packageName = packageName;
        this.downloadUrl = downloadUrl;
        this.downloadHandler = new DownloadHandler(this);
    }

    /**
     * 设置下载监听器
     * @param downloadListener
     */
    public void setDownloadListener(ApkDownloadListener downloadListener) {
        this.downloadListener = downloadListener;
    }

    /**
     * 在通知栏显示下载进度
     * @param context
     * @param targetClass
     */
    private void showAppDownloadNotify(Context context, Class<?> parentClass, Class<?> targetClass) {
        mNotifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // show notify
        Intent notifyIntent = new Intent(context, targetClass);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        // Adds the back stack
        if (parentClass != null) {
            stackBuilder.addNextIntent(new Intent(context, parentClass));
        }
        // Adds the Intent to the top of the stack
        stackBuilder.addNextIntent(notifyIntent);
        // Gets a PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setContentTitle(appName)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(resultPendingIntent);
        mBuilder.setProgress(100, 0, false);
        mNotifyManager.notify(appName.hashCode(), mBuilder.build());
    }

    /**
     * 判断是否正在下载
     * @return
     */
    public boolean isDownloading() {
        return isDownloading;
    }

    /**
     * 获取下载线程
     * @return
     */
    public DownloadThread getDownloadThread() {
        return downloadThread;
    }

    /**
     * 开始下载安装包
     * @param context
     * @param targetClass
     * @return
     */
    public void startDownload(Context context, Class<?> parentClass, Class<?> targetClass, boolean isUpdate) {
        downloadThread = new DownloadThread(isUpdate);
        downloadThread.start();
        showAppDownloadNotify(context, parentClass, targetClass);
    }

    private void startDownload(boolean isUpdate) {
        downloadThread = null;
        downloadThread = new DownloadThread(isUpdate);
        downloadThread.start();
    }

    public boolean checkDownloading() {
        if (isDownloading) {
            Toaster.show(PartnerApplication.getInstance(), R.string.task_downloading);
            return true;
        }

        return false;
    }

    /**
     * 取消当前下载
     * @param packageName
     */
    public void cancelDownload(String packageName) {
        if (packageName.equals(this.packageName) && downloadThread != null) {
            downloadThread.interrupt();
        }
    }

    public static void installBundledApps(File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PartnerApplication.getInstance().startActivity(intent);
    }

    /**
     * 下载线程
     *
     * @author emilyu
     *
     */
    private class DownloadThread extends Thread {
        public static final int TIME_OUT = 30000;

//        private String filePath = null;

        private String filePathTemp = null;

        private String finaFilePath = null;

        private InputStream in = null;

        private OutputStream out = null;

        public DownloadThread(boolean isUpdate) {

            File dir = new File(APK_DOWNLOAD_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            filePathTemp = APK_DOWNLOAD_DIR + packageName + APK_DOWNLOAD_TEMP;
            finaFilePath = APK_DOWNLOAD_DIR + packageName + APK_DOWNLOAD_SUF;
            //TODO 暂时修改为有安装包直接打开
            File file = new File(filePathTemp);
            if (file.exists())
                file.delete();
            if (isUpdate) {
                File desFile = new File(finaFilePath);
                if (desFile.exists()) {
                    desFile.delete();
                }
            }
        }

        @Override
        public void run() {
            isDownloading = true;
            try {
                downloadFile();
                isDownloading = false;
                defineFileName();
                downloadHandler.obtainMessage(DOWNLOAD_SUCCESS_MSG, finaFilePath)
                        .sendToTarget();
            } catch (InterruptedException e) {
                isDownloading = false;
                downloadHandler.sendEmptyMessage(DOWNLOAD_CANCEL_MSG);
            } catch (Exception e) {
                isDownloading = false;
                downloadHandler.obtainMessage(DOWNLOAD_FAIL_MSG, e).sendToTarget();
            }

        }

        private void defineFileName() {
            File file = new File(filePathTemp);
            if (file.exists()) {
                file.renameTo(new File(finaFilePath));
            }
        }

        private void downloadFile() throws Exception {

            HttpGet get = new HttpGet(downloadUrl);
            HttpClient client = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(client.getParams(), TIME_OUT);
            HttpConnectionParams.setSoTimeout(client.getParams(), TIME_OUT);
            HttpResponse response = client.execute(get);

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200 && statusCode != 206) {
                throw new Exception(response.getStatusLine().toString());
            }
            mContentSize = response.getEntity().getContentLength();
            //如果文件已经存在，并且文件符合，直接打开
            File file = new File(finaFilePath);
            if (file.exists() && file.length() == mContentSize) {
                downloadHandler.obtainMessage(DOWNLOAD_SUCCESS_MSG, finaFilePath).sendToTarget();
                return;
            }
            out = new FileOutputStream(filePathTemp, true);
            in = response.getEntity().getContent();

            byte buffer[] = new byte[1024 * 4];
            int downLength = 0;
            int position = in.read(buffer);
            long time = System.currentTimeMillis();

            while (position > 0 && isAlive()) {
                if (interrupted()) {
                    throw new InterruptedException();
                }
                downLength += position;
                out.write(buffer, 0, position);
                if (System.currentTimeMillis() - time > 500) {// 500ms刷新界面一次
                    time = System.currentTimeMillis();
                    downloadHandler.obtainMessage(PROGRESS_MSG,
                            (int) (((float) downLength) / mContentSize * 100), 0)
                            .sendToTarget();
                }
                position = in.read(buffer);
            }
            out.flush();
            out.close();

        }
    }

    /**
     * 下载线程handler
     *
     * @author emilyu
     *
     */
    private static class DownloadHandler extends Handler {

        private WeakReference<ApkDownloadUtil> utilReference = null;

        public DownloadHandler(ApkDownloadUtil apkDownloadUtil) {
            utilReference = new WeakReference<ApkDownloadUtil>(apkDownloadUtil);
        }

        @Override
        public void handleMessage(Message msg) {
            ApkDownloadUtil apkDownloadUtil = utilReference.get();
            switch (msg.what) {
                case ApkDownloadUtil.PROGRESS_MSG:
                    apkDownloadUtil.mBuilder.setProgress(100, msg.arg1, false);
                    apkDownloadUtil.mNotifyManager.notify(apkDownloadUtil.appName.hashCode(),
                            apkDownloadUtil.mBuilder.build());
                    if (apkDownloadUtil.downloadListener != null) {
                        apkDownloadUtil.downloadListener.onDownloadProgress(msg.arg1);
                    }
                    break;
                case ApkDownloadUtil.DOWNLOAD_SUCCESS_MSG:
                    File src = new File((String) msg.obj);
                    //检查安装包完整性（简单检查安装包大小）
                    if (src.length() != apkDownloadUtil.mContentSize) {
                        apkDownloadUtil.startDownload(false);
                        Toaster.show(PartnerApplication.getInstance(), R.string.download_fail_and_restart);
                        return;
                    }
                    installBundledApps(new File((String) msg.obj));
                    apkDownloadUtil.mNotifyManager.cancel(apkDownloadUtil.appName.hashCode());
                    apkDownloadUtil.downloadThread = null;
                    if (apkDownloadUtil.downloadListener != null) {
                        apkDownloadUtil.downloadListener.onDownloadSucess();
                    }
                    break;
                case ApkDownloadUtil.DOWNLOAD_FAIL_MSG:
                    Exception e = (Exception) msg.obj;
                    if (e instanceof InterruptedIOException
                            || e instanceof SocketException
                            || e instanceof UnknownHostException) {
                        Toaster.show(PartnerApplication.getInstance(), R.string.download_fail_by_network);
                    } else if (e instanceof IOException
                            || e instanceof FileNotFoundException) {
                        Toaster.show(PartnerApplication.getInstance(), R.string.download_fail_by_sdcard);
                    } else {
                        Toaster.show(PartnerApplication.getInstance(), R.string.download_fail_retry);
                    }
                    apkDownloadUtil.mNotifyManager.cancel(apkDownloadUtil.appName.hashCode());
                    apkDownloadUtil.downloadThread = null;
                    if (apkDownloadUtil.downloadListener != null) {
                        apkDownloadUtil.downloadListener.onDownloadFail();
                    }
                    break;
                case ApkDownloadUtil.DOWNLOAD_CANCEL_MSG:
                    apkDownloadUtil.mNotifyManager.cancel(apkDownloadUtil.appName.hashCode());
                    apkDownloadUtil.downloadThread = null;
                    if (apkDownloadUtil.downloadListener != null) {
                        apkDownloadUtil.downloadListener.onDownloadCancel();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public interface ApkDownloadListener {
        /**
         * 下载成功接口
         */
        public void onDownloadSucess();

        /**
         * 下载失败接口
         */
        public void onDownloadFail();

        /**
         * 取消当前下载接口
         */
        public void onDownloadCancel();

        /**
         * @param position
         * 当前下载进度
         */
        public void onDownloadProgress(int position);
    }
}
