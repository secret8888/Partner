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
import com.partner.adapter.FriendAdapter;
import com.partner.adapter.InstitutionAdapter;
import com.partner.common.annotation.ViewId;
import com.partner.common.http.AsyncHttpCallback;
import com.partner.common.http.HttpManager;
import com.partner.common.util.HttpUtils;
import com.partner.common.util.IntentManager;
import com.partner.common.util.Utils;
import com.partner.fragment.base.BaseFragment;
import com.partner.model.FriendList;
import com.partner.view.refresh.RefreshListView;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.youdao.yjson.YJson;

import java.io.IOException;

public class InstitutionListFragment extends BaseFragment implements OnItemClickListener{

	@ViewId(R.id.list_content)
	private RefreshListView contentView;

	private FriendList friendList;

	private int type = 0;
	@Override
	protected int getLayoutId() {
		return R.layout.fragment_institution_list;
	}

	@Override
	protected void readIntent() {

	}

	@Override
	protected void initControls(Bundle savedInstanceState) {
		contentView.setRefreshTime(Utils.getTime());
	}

	@Override
	public void onResume() {
		super.onResume();
		if(friendList == null || ((MainActivity)getActivity()).isFriendNeedRefresh()) {
//			onShowLoadingDialog();
			getFriendsList();
		}
	}

	@Override
	protected void setListeners() {
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
//		IntentManager.startInfoEditActivity(this, friendList.getFriends().get(position - 1), 0);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 0 && resultCode == Activity.RESULT_OK) {
			contentView.autoRefresh();
		}
	}

	public static InstitutionListFragment newInstance() {
		InstitutionListFragment fragment = new InstitutionListFragment();
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
//			HttpManager.getFriendsList(PartnerApplication.getInstance().getUserInfo().getToken(), type, new AsyncHttpCallback() {
//				@Override
//				public void onRequestResponse(Response response) {
//					super.onRequestResponse(response);
//					onMessageLoad();
//					String result = HttpUtils.getResponseData(response);
//					if(!TextUtils.isEmpty(result)) {
//						friendList = YJson.getObj(result, FriendList.class);
//						InstitutionAdapter adapter = new InstitutionAdapter(getActivity(), friendList.getFriends());
//						contentView.setAdapter(adapter);
//					}
//				}
//
//				@Override
//				public void onRequestFailure(Request request, IOException e) {
//					super.onRequestFailure(request, e);
//					onMessageLoad();
//				}
//			});
		} else {
			onMessageLoad();
		}
	}

}
