package com.partner.activity.info.setting;

import android.os.Bundle;
import android.text.Editable;
import android.widget.EditText;

import com.partner.R;
import com.partner.activity.base.BaseActivity;
import com.partner.common.annotation.ViewId;
import com.partner.view.TitleView;

public class FeedbackActivity extends BaseActivity {

	@ViewId(R.id.view_title)
	private TitleView titleView;

	@ViewId(R.id.edit_content)
	private EditText contentText;

	private static final String TAG = FeedbackActivity.class.getSimpleName();

	@Override
	protected int getLayoutId() {
		return R.layout.activity_feedback;
	}

	@Override
	protected void readIntent() {
	}

	@Override
	protected void initControls(Bundle savedInstanceState) {
		titleView.setTitle(R.string.setting);
		titleView.setOperateText(R.string.submit);
	}

	@Override
	protected void setListeners() {
		titleView.setListener(this);
	}
}