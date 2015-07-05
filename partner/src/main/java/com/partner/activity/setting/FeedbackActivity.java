package com.partner.activity.setting;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.partner.R;
import com.partner.activity.base.BaseActivity;
import com.partner.common.annotation.ViewId;
import com.partner.common.util.Toaster;
import com.partner.view.TitleView;
import com.umeng.fb.FeedbackAgent;
import com.umeng.fb.SyncListener;
import com.umeng.fb.model.Conversation;
import com.umeng.fb.model.Reply;

import java.util.List;

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

	@Override
	public void onTitleOperateClick() {
		super.onTitleOperateClick();
		String content = contentText.getText().toString();
		if(!TextUtils.isEmpty(content)) {
			onShowLoadingDialog();
			Conversation conversation = new FeedbackAgent(this).getDefaultConversation();
			conversation.addUserReply(content);
			conversation.sync(new SyncListener() {
				@Override
				public void onReceiveDevReply(List<Reply> list) {
					onDismissLoadingDialog();
				}

				@Override
				public void onSendUserReply(List<Reply> list) {
					onDismissLoadingDialog();
					Toaster.show(R.string.feedback_success);
					onBackPressed();
				}
			});
		} else {
			Toaster.show(R.string.input_feedback_tip);
		}
	}
}