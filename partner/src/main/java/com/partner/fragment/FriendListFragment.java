package com.partner.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.partner.R;
import com.partner.adapter.ActivityAdapter;
import com.partner.adapter.FriendAdapter;
import com.partner.common.annotation.ViewId;
import com.partner.common.util.IntentManager;
import com.partner.common.util.Utils;
import com.partner.fragment.base.BaseFragment;
import com.partner.view.refresh.RefreshListView;

import java.util.ArrayList;

public class FriendListFragment extends BaseFragment implements OnItemClickListener{

	@ViewId(R.id.list_content)
	private RefreshListView contentView;

//	private ProjectAdapter projectAdapter = null;
//
//	private ArrayList<ProjectInfo> projectInfos = null;
	
	@Override
	protected int getLayoutId() {
		return R.layout.fragment_friend_list;
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
		FriendAdapter adapter = new FriendAdapter(getActivity(), list);
		contentView.setAdapter(adapter);
	}

	@Override
	public void onResume() {
		super.onResume();
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
		IntentManager.startInfoEditActivity(getActivity());
	}

	public static FriendListFragment newInstance() {
		FriendListFragment fragment = new FriendListFragment();
		return fragment;
	}

	private void onMessageLoad() {
		contentView.stopRefresh();
		contentView.stopLoadMore();
		contentView.setRefreshTime(Utils.getTime());
	}

}
