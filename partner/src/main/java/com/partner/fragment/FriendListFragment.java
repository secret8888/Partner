package com.partner.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.partner.PartnerApplication;
import com.partner.R;
import com.partner.activity.MainActivity;
import com.partner.adapter.ActivityAdapter;
import com.partner.adapter.FriendAdapter;
import com.partner.common.annotation.ViewId;
import com.partner.common.constant.Consts;
import com.partner.common.http.AsyncHttpCallback;
import com.partner.common.http.HttpManager;
import com.partner.common.util.HttpUtils;
import com.partner.common.util.IntentManager;
import com.partner.common.util.Utils;
import com.partner.fragment.base.BaseFragment;
import com.partner.model.FriendInfo;
import com.partner.model.FriendList;
import com.partner.view.refresh.RefreshListView;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.youdao.yjson.YJson;

import java.io.IOException;
import java.util.ArrayList;

public class FriendListFragment extends BaseFragment implements OnItemClickListener{

	@ViewId(R.id.list_content)
	private RefreshListView contentView;

	private FriendList friendList;

	private int type = 0;

	private boolean isBusiness;

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_friend_list;
	}

	@Override
	protected void readIntent() {

	}

	@Override
	protected void initControls(Bundle savedInstanceState) {
		isBusiness = PartnerApplication.getInstance().getUserInfo().getUserType() == Consts.ROLE_BUSINESS;
		contentView.setRefreshTime(Utils.getTime());
	}

	@Override
	public void onResume() {
		super.onResume();
		if(friendList == null || ((MainActivity)getActivity()).isFriendNeedRefresh()) {
			onShowLoadingDialog();
			((MainActivity)getActivity()).setIsFriendNeedRefresh(false);
			getFriendsList();
		}
	}

	@Override
	protected void setListeners() {
		contentView.setPullRefreshEnable(false);
		contentView.setPullLoadEnable(false);
		contentView.setOnItemClickListener(this);
		contentView.setListViewRefreshListener(new RefreshListView.ListViewRefreshListener() {
			@Override
			public void onRefresh() {
				getFriendsList();
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
		if(!isBusiness) {
			IntentManager.startInfoEditActivity(this, friendList.getFriends().get(position - 1), 0);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 0 && resultCode == Activity.RESULT_OK) {
			contentView.autoRefresh();
		}
	}

	public static FriendListFragment newInstance(int type) {
		FriendListFragment fragment = new FriendListFragment();
		if(type == 0) {
			fragment.type = 1;
		} else {
			fragment.type = 0;
		}
		return fragment;
	}

	private void onMessageLoad() {
		contentView.stopRefresh();
		contentView.stopLoadMore();
		contentView.setRefreshTime(Utils.getTime());
		onDismissLoadingDialog();
	}

	public void getFriendsList() {
		if(Utils.isNetworkConnected(getActivity())) {
			AsyncHttpCallback callback = new AsyncHttpCallback() {
				@Override
				public void onRequestResponse(Response response) {
					super.onRequestResponse(response);
					onMessageLoad();
					String result = HttpUtils.getResponseData(response);
					if(!TextUtils.isEmpty(result)) {
						friendList = YJson.getObj(result, FriendList.class);
						FriendAdapter adapter = new FriendAdapter(getActivity(), friendList.getFriends());
						contentView.setAdapter(adapter);
					}
				}

				@Override
				public void onRequestFailure(Request request, IOException e) {
					super.onRequestFailure(request, e);
					onMessageLoad();
				}
			};

			if(isBusiness) {
				HttpManager.getFans(PartnerApplication.getInstance().getUserInfo().getToken(), callback);
			} else {
				HttpManager.getFriendsList(PartnerApplication.getInstance().getUserInfo().getToken(), type, callback);
			}
		} else {
			onMessageLoad();
		}
	}

}
