package com.partner.activity.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.partner.PartnerApplication;
import com.partner.R;
import com.partner.activity.base.BaseActivity;
import com.partner.adapter.ActivityAdapter;
import com.partner.common.annotation.ViewId;
import com.partner.common.constant.Consts;
import com.partner.common.constant.IntentConsts;
import com.partner.common.http.AsyncHttpCallback;
import com.partner.common.http.HttpManager;
import com.partner.common.util.HttpUtils;
import com.partner.common.util.IntentManager;
import com.partner.common.util.Utils;
import com.partner.model.ActivityList;
import com.partner.view.TitleView;
import com.partner.view.refresh.RefreshListView;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.youdao.yjson.YJson;

import java.io.IOException;

public class PublishedActivity extends BaseActivity implements AdapterView.OnItemClickListener {

	@ViewId(R.id.view_title)
	private TitleView titleView;

	@ViewId(R.id.list_content)
	private RefreshListView contentView;

	private ActivityAdapter adapter = null;

	private ActivityList activityList = null;

	private int friendId;

	private static final String TAG = PublishedActivity.class.getSimpleName();

	@Override
	protected int getLayoutId() {
		return R.layout.activity_published;
	}

	@Override
	protected void readIntent() {
		friendId = getIntent().getIntExtra(IntentConsts.ID_KEY, -1);
	}

	@Override
	protected void initControls(Bundle savedInstanceState) {
		if(friendId == -1) {
			titleView.setTitle(R.string.published_activity);
		} else {
			titleView.setTitle(R.string.all_institution_activity);
		}

		contentView.setRefreshTime(Utils.getTime());
		onShowLoadingDialog();
		getActivityList(0);
	}

	@Override
	protected void setListeners() {
		titleView.setListener(this);
		contentView.setOnItemClickListener(this);
		contentView.setListViewRefreshListener(new RefreshListView.ListViewRefreshListener() {
			@Override
			public void onRefresh() {
				getActivityList(0);
			}

			@Override
			public void onLoadMore() {
				getActivityList(activityList.getActivities().size());
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		IntentManager.startDetailActivity(this, activityList
				.getActivities().get(position - 1).getActivityId());
	}

	private void onMessageLoad() {
		contentView.stopRefresh();
		contentView.stopLoadMore();
		contentView.setRefreshTime(Utils.getTime());
	}

	private void getActivityList(final int start) {
		if (Utils.checkNetworkConnected(this)) {
			AsyncHttpCallback callback = new AsyncHttpCallback() {
				@Override
				public void onRequestResponse(final Response response) {
					onDismissLoadingDialog();
					onMessageLoad();
					new AsyncTask<Void, Void, String>() {
						@Override
						protected String doInBackground(Void... params) {
							return HttpUtils.getResponseData(response, false);
						}

						@Override
						protected void onPostExecute(String data) {
							if(TextUtils.isEmpty(data)) {
								return;
							}
							if(start == 0) {
								activityList = YJson.getObj(data, ActivityList.class);
								adapter = new ActivityAdapter(PublishedActivity.this, activityList.getActivities());
								contentView.setAdapter(adapter);
							} else {
								ActivityList list = YJson.getObj(data, ActivityList.class);
								activityList.getActivities().addAll(list.getActivities());
								adapter.notifyDataSetChanged();
							}
						}
					}.execute();
				}

				@Override
				public void onRequestFailure(Request request, IOException e) {
					onDismissLoadingDialog();
					onMessageLoad();
				}
			};
			if (friendId == -1) {
				HttpManager.getPublishedActivities(PartnerApplication.getInstance().getUserInfo().getToken(),
						start, Consts.PAGE_OFFSET, callback);
			} else {
				HttpManager.getActivitiesByOtherOrg(PartnerApplication.getInstance().getUserInfo().getToken(), friendId, "",
						start, Consts.PAGE_OFFSET, callback);
			}
		}
	}
}