package com.partner.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.partner.R;
import com.partner.adapter.ActivityAdapter;
import com.partner.common.annotation.ViewId;
import com.partner.common.util.Utils;
import com.partner.fragment.base.BaseFragment;
import com.partner.view.refresh.RefreshListView;

import java.util.ArrayList;

public class ActivityFragment extends BaseFragment implements OnItemClickListener{

	@ViewId(R.id.list_content)
	private RefreshListView contentView;

//	private ProjectAdapter projectAdapter = null;
//
//	private ArrayList<ProjectInfo> projectInfos = null;
	
	@Override
	protected int getLayoutId() {
		return R.layout.fragment_activity;
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

	public static ActivityFragment newInstance() {
		ActivityFragment fragment = new ActivityFragment();
		return fragment;
	}

	private void onMessageLoad() {
		contentView.stopRefresh();
		contentView.stopLoadMore();
		contentView.setRefreshTime(Utils.getTime());
	}

//	private void loadData(final int start) {
//		HttpManager.getProjectList(new CoinvsHandler(getActivity()) {
//
//			@Override
//			protected void handleSuccessMessage(Object object) {
//				hideRefreshingView();
//				HandleInfo handleInfo = (HandleInfo) object;
//				ArrayList<ProjectInfo> localInfos = GsonUtils
//						.getProjectsFromJson(handleInfo.getData());
//
//				if(start == 0) {
//					allProjectView.onRefreshComplete();
//					if(localInfos == null) {
//						return;
//					}
//					projectInfos = new ArrayList<ProjectInfo>();
//					projectInfos.addAll(localInfos);
//					projectAdapter = new ProjectAdapter(getActivity(), projectInfos);
//					allProjectView.setAdapter(projectAdapter);
//				} else {
//					allProjectView.onLoadMoreComplete();
//					if(localInfos == null) {
//						return;
//					}
//					projectInfos.addAll(localInfos);
//					projectAdapter.notifyDataSetChanged();
//				}
//			}
//
//			@Override
//			protected void handleError(int errorCode) {
//				hideRefreshingView();
//				if(start == 0) {
//					allProjectView.onRefreshComplete();
//				} else {
//					allProjectView.onLoadMoreComplete();
//				}
//			}
//		}, String.valueOf(start), String.valueOf(Consts.PAGE_OFFSET));
//	}

}
