package com.partner.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.partner.PartnerApplication;
import com.partner.R;
import com.partner.adapter.ActivityAdapter;
import com.partner.common.annotation.ViewId;
import com.partner.common.constant.Consts;
import com.partner.common.http.AsyncHttpCallback;
import com.partner.common.http.HttpManager;
import com.partner.common.util.HttpUtils;
import com.partner.common.util.IntentManager;
import com.partner.common.util.Toaster;
import com.partner.common.http.HttpManager;
import com.partner.common.util.Utils;
import com.partner.fragment.base.BaseFragment;
import com.partner.model.ActivityList;
import com.partner.view.refresh.RefreshListView;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.youdao.yjson.YJson;

import java.io.IOException;
import java.util.ArrayList;

public class ActivityListFragment extends BaseFragment implements OnItemClickListener{

	@ViewId(R.id.list_content)
	private RefreshListView contentView;

	private ActivityAdapter adapter = null;

	private ActivityList activityList = null;

	private boolean isAllActivities = true;

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_activity_list;
	}

	@Override
	protected void readIntent() {

	}

	@Override
	protected void initControls(Bundle savedInstanceState) {
		contentView.setRefreshTime(Utils.getTime());
		getActivityList(0);
	}

	@Override
	public void onResume() {
		super.onResume();
//		contentView.autoRefresh();
	}

	@Override
	protected void setListeners() {
		contentView.setOnItemClickListener(this);
		contentView.setListViewRefreshListener(new RefreshListView.ListViewRefreshListener() {
			@Override
			public void onRefresh() {
				onMessageLoad();
			}

			@Override
			public void onLoadMore() {
				onMessageLoad();
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		IntentManager.startDetailActivity(getActivity(), activityList
				.getActivities().get(position - 1).getActivityId());
	}

	public static ActivityListFragment newInstance(int position) {
		ActivityListFragment fragment = new ActivityListFragment();
		if(position == 0) {
			fragment.isAllActivities = true;
		} else {
			fragment.isAllActivities = false;
		}
		return fragment;
	}

	private void onMessageLoad() {
		contentView.stopRefresh();
		contentView.stopLoadMore();
		contentView.setRefreshTime(Utils.getTime());
	}

	private void getActivityList(final int start) {
		if (Utils.checkNetworkConnected(getActivity())) {
			onShowLoadingDialog();
			AsyncHttpCallback callback = new AsyncHttpCallback() {
				@Override
				public void onRequestResponse(Response response) {
					onDismissLoadingDialog();
					String data = HttpUtils.getResponseData(response);
					if(TextUtils.isEmpty(data)) {
						return;
					}
					if(start == 0) {
						activityList = YJson.getObj(data, ActivityList.class);
						adapter = new ActivityAdapter(getActivity(), activityList.getActivities());
						contentView.setAdapter(adapter);
					} else {
						ActivityList list = YJson.getObj(data, ActivityList.class);
						activityList.getActivities().addAll(list.getActivities());
						adapter.notifyDataSetChanged();
					}
				}

				@Override
				public void onRequestFailure(Request request, IOException e) {
					onDismissLoadingDialog();
				}
			};
			if(isAllActivities) {
				HttpManager.getActivities(start, Consts.PAGE_OFFSET, "", callback);
			} else {
				HttpManager.getJoinedActivities(PartnerApplication.getInstance().getUserInfo().getToken(),
						start, Consts.PAGE_OFFSET, "", callback);
			}
		}
	}

}
