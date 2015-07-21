package com.partner.activity.info;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.partner.PartnerApplication;
import com.partner.R;
import com.partner.activity.base.BaseActivity;
import com.partner.adapter.RegistrationInfoAdapter;
import com.partner.common.annotation.ViewId;
import com.partner.common.constant.IntentConsts;
import com.partner.common.http.AsyncHttpCallback;
import com.partner.common.http.HttpManager;
import com.partner.common.util.Toaster;
import com.partner.common.util.Utils;
import com.partner.model.RegistrationInfo;
import com.partner.view.TitleView;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;

public class RegistrationEditActivity extends BaseActivity {

	@ViewId(R.id.view_title)
	private TitleView titleView;

	@ViewId(R.id.edit_name)
	private EditText nameEdit;

	@ViewId(R.id.edit_phone)
	private EditText phoneEdit;

	@ViewId(R.id.tv_delete)
	private TextView deleteView;

	private RegistrationInfo mInfo;

	private static final String TAG = RegistrationEditActivity.class.getSimpleName();

	@Override
	protected int getLayoutId() {
		return R.layout.activity_registration_edit;
	}

	@Override
	protected void readIntent() {
		mInfo = (RegistrationInfo) getIntent().getSerializableExtra(IntentConsts.REFISTRATION_KEY);
	}

	@Override
	protected void initControls(Bundle savedInstanceState) {
		if(mInfo == null) {
			titleView.setTitle(R.string.parent_add);
		} else {
			titleView.setTitle(R.string.parent_info_edit);
			deleteView.setVisibility(View.VISIBLE);
			nameEdit.setText(mInfo.getUserenrollInfoParent());
			phoneEdit.setText(mInfo.getUserenrollCellphone());
		}
		titleView.setOperateText(R.string.finish);
	}

	@Override
	protected void setListeners() {
		titleView.setListener(this);
	}

	public void onMessageClick(View view) {
		onBackPressed();
	}

	@Override
	public void onTitleOperateClick() {
		super.onTitleOperateClick();
		if (Utils.checkNetworkConnected(this)) {
			if(mInfo == null) {
				modifyRegistrationInfo(true);
			} else {
				modifyRegistrationInfo(false);
			}

		}
	}

	public void onDeleteClick(View view) {
		HttpManager.deleteRegistrationInfo(PartnerApplication.getInstance().getUserInfo().getToken(),
				mInfo.getUserenrollInfoId(), new AsyncHttpCallback() {
					@Override
					public void onRequestResponse(Response response) {
						Toaster.show(RegistrationEditActivity.this, R.string.delete_success);
						onBackPressed();
					}

					@Override
					public void onRequestFailure(Request request, IOException e) {
						onDismissLoadingDialog();
					}
				});
	}

	private void modifyRegistrationInfo(boolean isAdd) {
		String name = nameEdit.getText().toString();
		String phone = phoneEdit.getText().toString();

		if (TextUtils.isEmpty(name)) {
			Toaster.show(this, R.string.input_name_tip);
			return;
		}

		if (TextUtils.isEmpty(phone)) {
			Toaster.show(this, R.string.phone_not_null);
			return;
		}

		onShowLoadingDialog();
		if(isAdd) {
			HttpManager.addRegistrationInfo(PartnerApplication.getInstance().getUserInfo().getToken(),
					phone, name, new AsyncHttpCallback() {
				@Override
				public void onRequestResponse(Response response) {
					Toaster.show(RegistrationEditActivity.this, R.string.add_success);
					onBackPressed();
				}

				@Override
				public void onRequestFailure(Request request, IOException e) {
					onDismissLoadingDialog();
				}
			});
		} else {
			HttpManager.modifyRegistrationInfo(PartnerApplication.getInstance().getUserInfo().getToken(),
					mInfo.getUserenrollInfoId(), phone, name, new AsyncHttpCallback() {
				@Override
				public void onRequestResponse(Response response) {
					Toaster.show(RegistrationEditActivity.this, R.string.update_success);
					onBackPressed();
				}

				@Override
				public void onRequestFailure(Request request, IOException e) {
					onDismissLoadingDialog();
				}
			});
		}
	}
}