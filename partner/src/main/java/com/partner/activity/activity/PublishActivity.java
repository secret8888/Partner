package com.partner.activity.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.partner.PartnerApplication;
import com.partner.R;
import com.partner.activity.base.BaseActivity;
import com.partner.common.annotation.ViewId;
import com.partner.common.constant.Consts;
import com.partner.common.constant.IntentConsts;
import com.partner.common.http.AsyncHttpCallback;
import com.partner.common.http.HttpManager;
import com.partner.common.util.DateTimePickDialogUtil;
import com.partner.common.util.HttpUtils;
import com.partner.common.util.IntentManager;
import com.partner.common.util.Toaster;
import com.partner.common.util.Utils;
import com.partner.model.ActivityInfo;
import com.partner.model.UserInfo;
import com.partner.view.TitleView;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.youdao.yjson.YJson;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PublishActivity extends BaseActivity implements View.OnClickListener{

	@ViewId(R.id.view_title)
	private TitleView titleView;

	@ViewId(R.id.edit_subject)
	private EditText subjectView;

	@ViewId(R.id.edit_address)
	private EditText addressView;

	@ViewId(R.id.edit_num)
	private EditText numView;

	@ViewId(R.id.tv_start_time)
	private TextView startTimeView;

	@ViewId(R.id.tv_end_time)
	private TextView endTimeView;

	@ViewId(R.id.im_image)
	private SimpleDraweeView imageView;

	private String description;

	private String transportInfo;

	private String cost;

	private String arrange;

	private String equipment;

	private Date startDate;

	private Date endDate;

	private File mCurrentPhotoFile;

	private ActivityInfo mInfo;

	private static final int CAMERA_WITH_DATA = 1000;

	private static final int PHOTO_PICKED_WITH_DATA = 1001;

	private static final String TAG = PublishActivity.class.getSimpleName();

	@Override
	protected int getLayoutId() {
		return R.layout.activity_publish;
	}

	@Override
	protected void readIntent() {
		mInfo = (ActivityInfo) getIntent().getSerializableExtra(IntentConsts.INFO_KEY);
	}

	@Override
	protected void initControls(Bundle savedInstanceState) {
		titleView.setTitle(R.string.activity);
		titleView.setOperateText(R.string.publish);
		if(mInfo != null) {
			subjectView.setText(mInfo.getActivityTitle());
			addressView.setText(mInfo.getActivityAddress());
			numView.setText(String.valueOf(mInfo.getActivityPeapleNum()));
			SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss");
			startDate = new Date(mInfo.getActivityStartTime());
			startTimeView.setText(format.format(startDate));
			endDate = new Date(mInfo.getActivityEndTime());
			endTimeView.setText(format.format(endDate));
			if(!TextUtils.isEmpty(mInfo.getActivityImage())) {
				imageView.setImageURI(Uri.parse(mInfo.getActivityImage()));
			}
			description = mInfo.getActivityDescription();
			transportInfo = mInfo.getActivityTransportInfo();
			cost = mInfo.getActivityCost();
			arrange = mInfo.getActivityArrange();
			equipment = mInfo.getActivityEquipment();
		}
	}

	@Override
	protected void setListeners() {
		titleView.setListener(this);
		startTimeView.setOnClickListener(this);
		endTimeView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 hh:mm");
		DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
				this, format.format(new Date()));
		if(v.getId() == R.id.tv_start_time) {
			startDate = dateTimePicKDialog.dateTimePicKDialog(startTimeView);
		} else if (v.getId() == R.id.tv_end_time) {
			endDate = dateTimePicKDialog.dateTimePicKDialog(endTimeView);
		}
	}

	private String getResult(Intent data) {
		if (data == null)
			return null;
		String result = data.getStringExtra("input_result");
		return result;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			String result = getResult(data);
			switch (requestCode) {
				case 0:
					if(result != null) {
						description = result;
					}
					break;
				case 1:
					if(result != null) {
						transportInfo = result;
					}
					break;
				case 2:
					if(result != null) {
						cost = result;
					}
					break;
				case 3:
					if(result != null) {
						arrange = result;
					}
					break;
				case 4:
					if(result != null) {
						equipment = result;
					}
					break;
				case CAMERA_WITH_DATA:
					doCropPhoto(mCurrentPhotoFile);
					break;
				case PHOTO_PICKED_WITH_DATA:
					Bundle extras = data.getExtras();
					if(extras != null){
						Bitmap avatarBitmap = (Bitmap) extras.get("data");
						imageView.setImageBitmap(avatarBitmap);
						if (Environment.getExternalStorageState().equals(
								Environment.MEDIA_MOUNTED)) {
							Utils.savePicture(Utils.getTempDir(), avatarBitmap,
									Consts.IMAGE_CACHE_FILE);
							mCurrentPhotoFile = new File(Utils.getTempDir() + Consts.IMAGE_CACHE_FILE);
						} else {
							Toaster.show(R.string.no_sdcard);
						}
					}
					break;
			}
		}
	}

	private void publishActivity() {
		String subject = subjectView.getText().toString();
		if(TextUtils.isEmpty(subject)) {
			Toaster.show(R.string.input_subject);
			return;
		}
		String address = addressView.getText().toString();
		if(TextUtils.isEmpty(address)) {
			Toaster.show(R.string.input_address);
			return;
		}
		String peopleNum = numView.getText().toString();
		if(TextUtils.isEmpty(peopleNum)) {
			Toaster.show(R.string.input_num);
			return;
		}

		if(startDate == null) {
			Toaster.show(R.string.select_start_time);
			return;
		}

		if(endDate == null) {
			Toaster.show(R.string.select_end_time);
			return;
		}

		if(mCurrentPhotoFile == null) {
			Toaster.show(R.string.upload_image_tip);
			return;
		}

		if(TextUtils.isEmpty(description)) {
			Toaster.show(R.string.input_activity_detail);
			return;
		}

		if(TextUtils.isEmpty(transportInfo)) {
			Toaster.show(R.string.input_path_intro);
			return;
		}

		if(TextUtils.isEmpty(cost)) {
			Toaster.show(R.string.input_activity_cost);
			return;
		}

		if(TextUtils.isEmpty(arrange)) {
			Toaster.show(R.string.input_activity_arrange);
			return;
		}

		if(TextUtils.isEmpty(equipment)) {
			Toaster.show(R.string.input_equipment);
			return;
		}

		if(Utils.checkNetworkConnected(this)) {
			onShowLoadingDialog();
			int activityId = -1;
			if(mInfo != null) {
				activityId = mInfo.getActivityId();
			}
			HttpManager.publishActivity(PartnerApplication.getInstance().getUserInfo().getToken(), activityId, subject, mCurrentPhotoFile,
					String.valueOf(startDate.getTime()), String.valueOf(endDate.getTime()), address, peopleNum,
					description, transportInfo, cost, arrange, equipment, new AsyncHttpCallback() {
				@Override
				public void onRequestResponse(Response response) {
					onDismissLoadingDialog();
					String responseBody = HttpUtils.getResponseData(response);
					if (!TextUtils.isEmpty(responseBody)) {
						Toaster.show(R.string.publish_success);
						onBackPressed();
					}
				}

				@Override
				public void onRequestFailure(Request request, IOException e) {
					onDismissLoadingDialog();
				}
			});
		}
	}

	@Override
	public void onTitleOperateClick() {
		super.onTitleOperateClick();
		publishActivity();
	}

	public void onAddPictureClick(View view) {
		createChooseDialog();
	}

	public void onDetailClick(View view) {
		IntentManager.startContentActivity(this, getString(R.string.activity_detail_intro), description, true, -1, 0);
	}

	public void onPathClick(View view) {
		IntentManager.startContentActivity(this, getString(R.string.path_intro), transportInfo, true, -1, 1);
	}

	public void onCostClick(View view) {
		IntentManager.startContentActivity(this, getString(R.string.activity_cost), cost, true, InputType.TYPE_CLASS_NUMBER, 2);
	}

	public void onTravelClick(View view) {
		IntentManager.startContentActivity(this, getString(R.string.travel_assign), arrange, true, -1, 3);
	}

	public void onEquipClick(View view) {
		IntentManager.startContentActivity(this, getString(R.string.equipment_require), equipment, true, -1, 4);
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
			mCurrentPhotoFile = new File(photoDir, Consts.IMAGE_CACHE_FILE);

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
		intent.putExtra("outputX", 300);
		intent.putExtra("outputY", 300);
		intent.putExtra("return-data", true);
		return intent;
	}

	public Intent getCropImageIntent(Uri photoUri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(photoUri, "image/*");
		intent.putExtra("crop", "true");
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
}