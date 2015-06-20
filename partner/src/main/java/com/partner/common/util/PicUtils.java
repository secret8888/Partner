package com.partner.common.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.partner.common.constant.Consts;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PicUtils {

    public static final void saveBitmap2Jpg(Bitmap bm, File file) {
        BufferedOutputStream bos;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(file));
            bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (Exception e) {
        }
    }

    public static final Bitmap getBitmapFromSDCard(String filePath) {
        File pic = new File(filePath);
        if (pic.exists()) {
            Bitmap bm = BitmapFactory.decodeFile(filePath);
            return bm;
        } else {
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

    public static final void savePngAtRoot(Bitmap bm, String fileName) {
        File rootDir = Environment.getExternalStorageDirectory();
        File pic = new File(rootDir.getAbsoluteFile() + "/" + fileName);
        saveBitmap2Png(bm, pic);
    }

    public static final void wsavePngAtTemp(Bitmap bm, String fileName) {
        File rootDir = Environment.getExternalStorageDirectory();
        File tempDir = new File(rootDir.getAbsoluteFile() + Consts.TEMP_FILE_PATH);
        if (!tempDir.exists()) {
            tempDir.mkdir();
        }
        File pic = new File(rootDir.getAbsoluteFile() + Consts.TEMP_FILE_PATH + fileName);
        if (pic.exists()) {
            pic.delete();
        }
        saveBitmap2Png(bm, pic);
    }

    public static boolean hasUserPhoto(String fileName) {
        File rootDir = Environment.getExternalStorageDirectory();
        File tempDir = new File(rootDir.getAbsoluteFile() + Consts.TEMP_FILE_PATH);
        if (!tempDir.exists()) {
            return false;
        }
        File pic = new File(rootDir.getAbsoluteFile() + Consts.TEMP_FILE_PATH + fileName);
        if (pic.exists()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 将图片字节数组保存为png格式图片，并存储到sd卡跟目录
     *
     * @param context
     * @param image    图片byte数组
     * @param width    图片宽度
     * @param height   图片高度
     * @param fileName 推荐png格式 eg. xxx.png
     */
    public static final void saveBitmap2SDCard(Context context, byte[] image, int width, int height, String fileName) {
        if (image != null) {
            boolean sdExist = Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED);
            if (!sdExist) {
                Toaster.showMsg(context, "SD卡不可用！");
                return;
            }
            File rootDir = Environment.getExternalStorageDirectory();
            File png = new File(rootDir.getAbsoluteFile() + "/" + fileName);
            int[] rgb = new int[width * height];
            PicUtils.decodeYUV420SP(rgb, image, width, height);
            Bitmap bmp = Bitmap.createBitmap(rgb, width, height, Bitmap.Config.ARGB_8888);
            PicUtils.saveBitmap2Png(bmp, png);
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

    public static final Bitmap Bytes2Bitmap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

    public static final byte[] Bitmap2Bytes(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static Bitmap readBmpFromAsset(Context context, String fileName) {
        Bitmap bmp = null;
        InputStream is;
        try {
            AssetManager am = context.getAssets();
            is = am.open(fileName);
            bmp = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            bmp = null;
        }
        return bmp;
    }

    public static final Bitmap getBitmapFromTemp(String fileName) {
        File rootDir = Environment.getExternalStorageDirectory();
        String picPath = rootDir.getAbsoluteFile() + Consts.TEMP_FILE_PATH + fileName;
        File pic = new File(picPath);
        if (pic.exists()) {
            Bitmap bm = BitmapFactory.decodeFile(picPath);
            return bm;
        } else {
            return null;
        }
    }

    static public final void decodeYUV420SP(int[] rgb, byte[] yuv420sp,
                                            int width, int height) {
        final int frameSize = width * height;

        for (int j = 0, yp = 0; j < height; j++) {
            int uvp = frameSize + (j >> 1) * width, u = 0, v = 0;
            for (int i = 0; i < width; i++, yp++) {
                //防止数组越界
                if (yp >= yuv420sp.length) {
                    continue;
                }
                int y = (0xff & ((int) yuv420sp[yp])) - 16;
                if (y < 0)
                    y = 0;
                if ((i & 1) == 0) {
                    //防止数组越界
                    if (uvp >= yuv420sp.length) {
                        continue;
                    }
                    v = (0xff & yuv420sp[uvp++]) - 128;
                    u = (0xff & yuv420sp[uvp++]) - 128;
                }

                int y1192 = 1192 * y;
                int r = (y1192 + 1634 * v);
                int g = (y1192 - 833 * v - 400 * u);
                int b = (y1192 + 2066 * u);

                if (r < 0)
                    r = 0;
                else if (r > 262143)
                    r = 262143;
                if (g < 0)
                    g = 0;
                else if (g > 262143)
                    g = 262143;
                if (b < 0)
                    b = 0;
                else if (b > 262143)
                    b = 262143;

                rgb[yp] = 0xff000000 | ((r << 6) & 0xff0000)
                        | ((g >> 2) & 0xff00) | ((b >> 10) & 0xff);
            }
        }
    }

    public static byte[] rgb2Gray(int[] pixels, int width, int height) {
        int size = width * height;
        byte[] grayData = new byte[size];
        for (int i = 0; i < size; i++) {
            int rgb = pixels[i] & 0x00FFFFFF;
            int r = rgb & 0xFF;
            int g = (rgb >> 8) & 0xFF;
            int b = (rgb >> 16) & 0xFF;
            grayData[i] = (byte) ((r + g + b) / 3);
        }
        return grayData;
    }

    private Bitmap getDiskBitmap(String pathString) {
        Bitmap bitmap = null;
        try {
            File file = new File(pathString);
            if (file.exists()) {
                bitmap = BitmapFactory.decodeFile(pathString);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
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
}
