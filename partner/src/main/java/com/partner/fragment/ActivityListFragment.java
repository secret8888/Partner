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
import com.partner.common.http.AsyncHttpCallback;
import com.partner.common.http.HttpManager;
import com.partner.common.util.HttpUtils;
import com.partner.common.util.Toaster;
import com.partner.common.util.Utils;
import com.partner.fragment.base.BaseFragment;
import com.partner.view.refresh.RefreshListView;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;

public class ActivityListFragment extends BaseFragment implements OnItemClickListener{

	@ViewId(R.id.list_content)
	private RefreshListView contentView;

//	private ProjectAdapter projectAdapter = null;
//
//	private ArrayList<ProjectInfo> projectInfos = null;
	
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
//		loadData(0);
		ArrayList list = new ArrayList();
		for(int i = 0; i < 100; i ++) {
			list.add("");
		}
		ActivityAdapter adapter = new ActivityAdapter(getActivity(), list);
		contentView.setAdapter(adapter);
	}

	@Override
	public void onResume() {
		super.onResume();
		getActivityList();
//		contentView.autoRefresh();
	}

	@Override
	protected void setListeners() {
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
	}

	public static ActivityListFragment newInstance() {
		ActivityListFragment fragment = new ActivityListFragment();
		return fragment;
	}

	private void onMessageLoad() {
		contentView.stopRefresh();
		contentView.stopLoadMore();
		contentView.setRefreshTime(Utils.getTime());
	}

	private void getActivityList() {
		if (Utils.checkMetworkConnected(getActivity())) {
			onShowLoadingDialog();
			HttpManager.getAllActivities(PartnerApplication.getInstance().getUserInfo().getToken(), new AsyncHttpCallback() {
				@Override
				public void onRequestResponse(Response response) {
					HttpUtils.getResponseData(response);
				}

				@Override
				public void onRequestFailure(Request request, IOException e) {
					onDismissLoadingDialog();
				}
			});
		}
	}

}
