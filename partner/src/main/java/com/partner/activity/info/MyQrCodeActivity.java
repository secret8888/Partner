package com.partner.activity.info;

import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.partner.PartnerApplication;
import com.partner.R;
import com.partner.activity.base.BaseActivity;
import com.partner.common.annotation.ViewId;
import com.partner.common.constant.IntentConsts;
import com.partner.view.TitleView;

import java.util.Hashtable;

public class MyQrCodeActivity extends BaseActivity {

	@ViewId(R.id.view_title)
	private TitleView titleView;

	@ViewId(R.id.im_avatar)
	private SimpleDraweeView avatarView;

	@ViewId(R.id.tv_name)
	private TextView nameView;

	@ViewId(R.id.im_qrcode)
	private ImageView qrcodeView;

	private static final String TAG = MyQrCodeActivity.class.getSimpleName();

	@Override
	protected int getLayoutId() {
		return R.layout.activity_my_qrcode;
	}

	@Override
	protected void readIntent() {
	}

	@Override
	protected void initControls(Bundle savedInstanceState) {
		titleView.setTitle(R.string.my_qrcode);
		nameView.setText(PartnerApplication.getInstance().getUserInfo().getUserName());
		String avatarImage = PartnerApplication.getInstance().getUserInfo().getHeadImage();
		if(!TextUtils.isEmpty(avatarImage)) {
			Uri uri = Uri.parse(avatarImage);
			avatarView.setImageURI(uri);
		}

		try {
			Bitmap bitmap = create2DCode(PartnerApplication.getInstance().getUserInfo().getToken());
			qrcodeView.setImageBitmap(bitmap);
		} catch (Exception e) {
		}
	}

	@Override
	protected void setListeners() {
		titleView.setListener(this);
	}

	/**
	 * 用字符串生成二维码
	 *
	 * @param str
	 * @author J!nl!n
	 * @return
	 * @throws WriterException
	 */
	public Bitmap create2DCode(String str) throws WriterException {
		Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		// 生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
		BitMatrix matrix = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, 480, 480, hints);
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		// 二维矩阵转为一维像素数组,也就是一直横着排了
		int[] pixels = new int[width * height];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (matrix.get(x, y)) {
					pixels[y * width + x] = 0xff000000;
				}
			}
		}

		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		// 通过像素数组生成bitmap,具体参考api
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}
}