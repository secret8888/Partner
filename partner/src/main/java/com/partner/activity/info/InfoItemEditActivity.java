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
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.partner.PartnerApplication;
import com.partner.R;
import com.partner.activity.base.BaseActivity;
import com.partner.common.annotation.ViewId;
import com.partner.common.constant.Consts;
import com.partner.common.constant.IntentConsts;
import com.partner.common.util.Toaster;
import com.partner.common.util.Utils;
import com.partner.view.TitleView;

import java.io.File;

public class InfoItemEditActivity extends BaseActivity {

	@ViewId(R.id.view_title)
	private TitleView titleView;

	@ViewId(R.id.edit_update)
	private EditText updateView;

	private int updateType;

	private static final String TAG = InfoItemEditActivity.class.getSimpleName();

	@Override
	protected int getLayoutId() {
		return R.layout.activity_item_edit;
	}

	@Override
	protected void readIntent() {
		updateType = getIntent().getIntExtra(IntentConsts.UPDATE_ITEM_KEY, IntentConsts.USER_NAME_TYPE);
	}

	@Override
	protected void initControls(Bundle savedInstanceState) {
		if(updateType == IntentConsts.USER_NAME_TYPE) {
			titleView.setTitle(R.string.name);
			updateView.setText(PartnerApplication.getInstance().getUserInfo().getUserName());
		} else {
			titleView.setTitle(R.string.nickname);
			updateView.setText(PartnerApplication.getInstance().getUserInfo().getNickName());
		}
	}

	@Override
	protected void setListeners() {
		titleView.setListener(this);
	}

	public void onDeleteClick(View view) {
		updateView.getText().clear();
	}

}