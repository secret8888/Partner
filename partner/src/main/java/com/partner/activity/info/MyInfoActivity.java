package com.partner.activity.info;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.partner.PartnerApplication;
import com.partner.R;
import com.partner.activity.base.BaseActivity;
import com.partner.common.annotation.ViewId;
import com.partner.common.constant.Consts;
import com.partner.common.constant.IntentConsts;
import com.partner.common.util.IntentManager;
import com.partner.common.util.Toaster;
import com.partner.common.util.Utils;
import com.partner.view.TitleView;

import java.io.File;

public class MyInfoActivity extends BaseActivity {

	@ViewId(R.id.view_title)
	private TitleView titleView;

	@ViewId(R.id.im_avatar)
	private SimpleDraweeView avatarView;

	@ViewId(R.id.tv_name)
	private TextView nameView;

	private File mCurrentPhotoFile = null;

	private static final int CAMERA_WITH_DATA = 0;

	private static final int PHOTO_PICKED_WITH_DATA = 1;

	private static final String TAG = MyInfoActivity.class.getSimpleName();

	@Override
	protected int getLayoutId() {
		return R.layout.activity_info;
	}

	@Override
	protected void readIntent() {

	}

	@Override
	protected void initControls(Bundle savedInstanceState) {
		titleView.setTitle(R.string.personal_info);
		String avatarImage = PartnerApplication.getInstance().getUserInfo().getHeadImage();
		if(!TextUtils.isEmpty(avatarImage)) {
			Uri uri = Uri.parse(avatarImage);
			avatarView.setImageURI(uri);
		}
	}

	@Override
	protected void setListeners() {
		titleView.setListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		nameView.setText(PartnerApplication.getInstance().getUserInfo().getUserName());
	}

	public void onAvatarClick(View view) {
		createChooseDialog();
	}

	public void onNameClick(View view) {
		IntentManager.startInfoItemEditActivity(this, IntentConsts.USER_NAME_TYPE);
	}

	public void onNicknameClick(View view) {
		IntentManager.startInfoItemEditActivity(this, IntentConsts.NICK_NAME_TYPE);
	}

	public void onQrcodeClick(View view) {
		IntentManager.startMyQrCodeActivity(this);
	}

	private void createChooseDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setItems(new String[] {
				getResources().getString(R.string.take_photo),
				getResources().getString(R.string.choose_from_album)
		}, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				if (which == 0) {
					getPicFromCapture();
				} else {
					getPicFromContent();
				}
			}
		});
		builder.setNegativeButton(getResources().getString(R.string.cancel),
				null).show();
	}

	private void getPicFromCapture() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			File photoDir = new File(Utils.getTempDir());
			if (!photoDir.exists()) {
				photoDir.mkdir();
			}
			mCurrentPhotoFile = new File(photoDir, Consts.AVATAR_CACHE_FILE);

			Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
			intent.putExtra(MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(mCurrentPhotoFile));
			startActivityForResult(intent, CAMERA_WITH_DATA);
		} else {
			Toaster.show(R.string.no_sdcard);
		}
	}

	private void getPicFromContent() {
		try {
			Intent intent = getPhotoPickIntent();
			startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
		} catch (Exception e) {

		}
	}

	public static Intent getPhotoPickIntent() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		intent.setType("image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 80);
		intent.putExtra("outputY", 80);
		intent.putExtra("return-data", true);
		return intent;
	}

	public Intent getCropImageIntent(Uri photoUri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(photoUri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 300);
		intent.putExtra("outputY", 300);
		intent.putExtra("return-data", true);
		return intent;
	}

	protected void doCropPhoto(File f) {
		try {
			final Intent intent = getCropImageIntent(Uri.fromFile(f));
			startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
		} catch (Exception e) {

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
				case CAMERA_WITH_DATA:
					doCropPhoto(mCurrentPhotoFile);
					break;
				case PHOTO_PICKED_WITH_DATA:
					Bundle extras = data.getExtras();
					if(extras != null){
						Bitmap avatarBitmap = (Bitmap) extras.get("data");
						avatarView.setImageBitmap(avatarBitmap);
						if (Environment.getExternalStorageState().equals(
								Environment.MEDIA_MOUNTED)) {
							Utils.savePicture(Utils.getTempDir(), avatarBitmap,
									Consts.AVATAR_CACHE_FILE);
							File file = new File(Utils.getTempDir() + Consts.AVATAR_CACHE_FILE);
							if(file.exists()){

							}
						} else {
							Toaster.show(R.string.no_sdcard);
						}
					}
					break;
			}
		}
	}

}