package com.partner.activity.info;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.partner.PartnerApplication;
import com.partner.R;
import com.partner.activity.base.BaseActivity;
import com.partner.common.annotation.ViewId;
import com.partner.common.constant.Consts;
import com.partner.common.constant.IntentConsts;
import com.partner.common.http.AsyncHttpCallback;
import com.partner.common.http.HttpManager;
import com.partner.common.util.HttpUtils;
import com.partner.common.util.Toaster;
import com.partner.common.util.Utils;
import com.partner.model.FriendInfo;
import com.partner.model.FriendList;
import com.partner.model.MessageInfo;
import com.partner.model.UserInfo;
import com.partner.view.TitleView;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.youdao.yjson.YJson;

import java.io.IOException;

public class MessageDetailActivity extends BaseActivity {

	@ViewId(R.id.view_title)
	private TitleView titleView;

	@ViewId(R.id.tv_content)
	private TextView contentView;

	private MessageInfo messageInfo;

	private static final String TAG = MessageDetailActivity.class.getSimpleName();

	@Override
	protected int getLayoutId() {
		return R.layout.activity_message_detail;
	}

	@Override
	protected void readIntent() {
		messageInfo = (MessageInfo)getIntent().getSerializableExtra(IntentConsts.INFO_KEY);
	}

	@Override
	protected void initControls(Bundle savedInstanceState) {
		titleView.setTitle(R.string.message_detail);
		contentView.setText(messageInfo.getMessageContent().getContent());
	}

	@Override
	protected void setListeners() {
		titleView.setListener(this);
	}
}