package com.partner.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

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
	private ListView contentView;

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
	}

	@Override
	public void onResume() {
		super.onResume();
		if(friendList == null || ((MainActivity)getActivity()).isFriendNeedRefresh()) {
			onShowLoadingDialog();
			getFollowedInstitutions();
		}
	}

	@Override
	protected void setListeners() {
		contentView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		IntentManager.startInfoEditActivity(this, friendList.getOrgs().get(position), 0);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 0 && resultCode == Activity.RESULT_OK) {
			onShowLoadingDialog();
			getFollowedInstitutions();
		}
	}

	public static InstitutionListFragment newInstance() {
		InstitutionListFragment fragment = new InstitutionListFragment();
		return fragment;
	}

	public void getFollowedInstitutions() {
		if(Utils.isNetworkConnected(getActivity())) {
			HttpManager.getFollowedInstitutions(PartnerApplication.getInstance().getUserInfo().getToken(), new AsyncHttpCallback() {
				@Override
				public void onRequestResponse(Response response) {
					super.onRequestResponse(response);
					onDismissLoadingDialog();
					String result = HttpUtils.getResponseData(response);
					if (!TextUtils.isEmpty(result)) {
						friendList = YJson.getObj(result, FriendList.class);
						InstitutionAdapter adapter = new InstitutionAdapter(getActivity(), friendList.getOrgs());
						contentView.setAdapter(adapter);
					}
				}

				@Override
				public void onRequestFailure(Request request, IOException e) {
					super.onRequestFailure(request, e);
					onDismissLoadingDialog();
				}
			});
		}
	}

}
