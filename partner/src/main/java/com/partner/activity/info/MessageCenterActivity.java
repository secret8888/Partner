package com.partner.activity.info;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.partner.PartnerApplication;
import com.partner.R;
import com.partner.activity.base.BaseActivity;
import com.partner.adapter.ActivityAdapter;
import com.partner.adapter.MessageCenterAdapter;
import com.partner.adapter.RegistrationInfoAdapter;
import com.partner.common.annotation.ViewId;
import com.partner.common.constant.IntentConsts;
import com.partner.common.http.AsyncHttpCallback;
import com.partner.common.http.HttpManager;
import com.partner.common.util.HttpUtils;
import com.partner.common.util.IntentManager;
import com.partner.common.util.Utils;
import com.partner.model.ActivityList;
import com.partner.model.MessageList;
import com.partner.model.RegistrationList;
import com.partner.view.TitleView;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.youdao.yjson.YJson;

import java.io.IOException;

public class MessageCenterActivity extends BaseActivity implements AdapterView.OnItemClickListener {

	@ViewId(R.id.view_title)
	private TitleView titleView;

	@ViewId(R.id.list_content)
	private ListView contentView;

	private MessageList messageList;

	private int activityId;

	private static final String TAG = MessageCenterActivity.class.getSimpleName();

	@Override
	protected int getLayoutId() {
		return R.layout.activity_message_center;
	}

	@Override
	protected void readIntent() {
		activityId = getIntent().getIntExtra(IntentConsts.ID_KEY, -1);
	}

	@Override
	protected void initControls(Bundle savedInstanceState) {
		if(activityId == -1) {
			titleView.setTitle(R.string.message_center);
		} else {
			titleView.setTitle(R.string.activity_message_list);
		}
		getMessageList();
	}

	@Override
	protected void setListeners() {
		titleView.setListener(this);
		contentView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		if(messageList.getMessages().get(position).getMessageType() == 4) {
			IntentManager.startMarkActivity(this, messageList.getMessages().get(position));
		} else {
			IntentManager.startMessageDetailActivity(this, messageList.getMessages().get(position));
		}
	}

	private void getMessageList() {
		if (Utils.checkNetworkConnected(this)) {
			onShowLoadingDialog();
			AsyncHttpCallback callback = new AsyncHttpCallback() {
				@Override
				public void onRequestResponse(Response response) {
					handleMessageResult(response);
				}

				@Override
				public void onRequestFailure(Request request, IOException e) {
					onDismissLoadingDialog();
				}
			};
			if(activityId == -1) {
				HttpManager.getMessageList(PartnerApplication.getInstance().getUserInfo().getToken(), callback);
			} else {
				HttpManager.getMessagesByActivity(PartnerApplication.getInstance().getUserInfo().getToken(),
						activityId, callback);
			}
		}
	}

	private void handleMessageResult(final Response response) {
		onDismissLoadingDialog();
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				return HttpUtils.getResponseData(response, false);
			}

			@Override
			protected void onPostExecute(String result) {
				if(TextUtils.isEmpty(result)) {
					return;
				}
				messageList = YJson.getObj(result, MessageList.class);
				if(messageList == null || messageList.getMessages() == null) {
					return;
				}
				MessageCenterAdapter adapter = new MessageCenterAdapter(MessageCenterActivity.this, messageList.getMessages());
				contentView.setAdapter(adapter);
			}
		}.execute();
	}
}