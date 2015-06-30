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
import com.partner.common.http.AsyncHttpCallback;
import com.partner.common.http.HttpManager;
import com.partner.common.util.Toaster;
import com.partner.common.util.Utils;
import com.partner.view.TitleView;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;

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
		titleView.setOperateText(R.string.save);
	}

	@Override
	protected void setListeners() {
		titleView.setListener(this);
	}

	@Override
	public void onTitleOperateClick() {
		super.onTitleOperateClick();
		updateUserInfo();
	}

	public void onDeleteClick(View view) {
		updateView.getText().clear();
	}

	private void updateUserInfo() {
		onShowLoadingDialog();
		String userName = null;
		String nickName = null;
		switch (updateType) {
			case IntentConsts.USER_NAME_TYPE:
				userName = updateView.getText().toString();
				break;
			case IntentConsts.NICK_NAME_TYPE:
				nickName = updateView.getText().toString();
				break;
		}
		HttpManager.updateUserInfo(PartnerApplication.getInstance().getUserInfo().getToken(), userName, nickName, null, null, new AsyncHttpCallback() {
			@Override
			public void onRequestResponse(Response response) {
				Toaster.show(R.string.update_success);
				onDismissLoadingDialog();
				onBackPressed();
				String name = updateView.getText().toString();
				switch (updateType) {
					case IntentConsts.USER_NAME_TYPE:
						PartnerApplication.getInstance().getUserInfo().setUserName(name);
						break;
					case IntentConsts.NICK_NAME_TYPE:
						PartnerApplication.getInstance().getUserInfo().setNickName(name);
						break;
				}
			}

			@Override
			public void onRequestFailure(Request request, IOException e) {
				onDismissLoadingDialog();
			}
		});
	}

}