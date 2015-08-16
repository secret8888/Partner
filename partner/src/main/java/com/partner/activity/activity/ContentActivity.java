package com.partner.activity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.partner.R;
import com.partner.activity.base.BaseActivity;
import com.partner.common.annotation.ViewId;
import com.partner.common.constant.IntentConsts;
import com.partner.common.util.Utils;
import com.partner.view.TitleView;

import org.w3c.dom.Text;

public class ContentActivity extends BaseActivity {

	@ViewId(R.id.view_title)
	private TitleView titleView;

	@ViewId(R.id.edit_content)
	private EditText contentView;

	private String title;

	private String content;

	private int inputType;

	private boolean isEditModel = false;

	private static final String TAG = ContentActivity.class.getSimpleName();

	@Override
	protected int getLayoutId() {
		return R.layout.activity_content;
	}

	@Override
	protected void readIntent() {
		title = getIntent().getStringExtra(IntentConsts.TITLE_KEY);
		content = getIntent().getStringExtra(IntentConsts.INFO_KEY);
		isEditModel = getIntent().getBooleanExtra(IntentConsts.EDIT_KEY, false);
		inputType = getIntent().getIntExtra(IntentConsts.TYPE_KEY, -1);
	}

	@Override
	protected void initControls(Bundle savedInstanceState) {
		titleView.setTitle(title);
		if(!TextUtils.isEmpty(content)) {
			contentView.setText(content);
			Utils.inputMethodShow(this, contentView);
		}
		if(!isEditModel) {
			contentView.setFocusable(false);
			contentView.setClickable(false);
		} else if(inputType != -1) {
			contentView.setInputType(inputType);
		}
	}

	@Override
	protected void setListeners() {
		titleView.setListener(this);
	}

	@Override
	public void onBackPressed() {
		String result = contentView.getText().toString();
		if(!TextUtils.isEmpty(result) && isEditModel) {
			Intent intent = new Intent();
			intent.putExtra("input_result", result);
			setResult(RESULT_OK, intent);
		}
		super.onBackPressed();
	}
}