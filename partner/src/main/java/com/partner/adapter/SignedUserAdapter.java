package com.partner.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.partner.PartnerApplication;
import com.partner.R;
import com.partner.activity.base.BaseActivity;
import com.partner.common.constant.Consts;
import com.partner.common.http.AsyncHttpCallback;
import com.partner.common.http.HttpManager;
import com.partner.common.util.Utils;
import com.partner.model.FriendInfo;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;

public class SignedUserAdapter extends BaseAdapter {

	private Context context;

	private ArrayList<FriendInfo> mItems;

	private boolean isBusiness;

	public SignedUserAdapter(Context context, ArrayList<FriendInfo> items) {
		isBusiness = PartnerApplication.getInstance().getUserInfo().getUserType() == Consts.ROLE_BUSINESS;
		this.context = context;
		this.mItems = items;
	}

	@Override
	public int getCount() {
		return mItems.size();
	}

	@Override
	public Object getItem(int position) {
		return mItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.adapter_signed_user, null);

			holder.avatarView = (SimpleDraweeView) convertView.findViewById(R.id.im_avatar);
			holder.nameView = (TextView) convertView.findViewById(R.id.tv_name);
			holder.followView = (TextView) convertView.findViewById(R.id.tv_follow);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final FriendInfo info = mItems.get(position);
		if(!TextUtils.isEmpty(info.getHeadimage())) {
			Uri uri = Uri.parse(info.getHeadimage());
			holder.avatarView.setImageURI(uri);
		}
		holder.nameView.setText(info.getNickname());
		if(isBusiness) {
			holder.followView.setVisibility(View.GONE);
		} else {
			if(info.isfriend()) {
				holder.followView.setText(R.string.cancel_follow);
			} else {
				holder.followView.setText(R.string.follow);
			}
			holder.followView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					operaFriend(info);
				}
			});
		}
		return convertView;
	}

	private void operaFriend(final FriendInfo mInfo) {
		final AsyncHttpCallback callback = new AsyncHttpCallback() {
			@Override
			public void onRequestResponse(Response response) {
				((BaseActivity)context).onDismissLoadingDialog();
				if(mInfo.isfriend()) {
					mInfo.setIsfriend(false);
				} else {
					mInfo.setIsfriend(true);
				}
				notifyDataSetChanged();
			}

			@Override
			public void onRequestFailure(Request request, IOException e) {
				((BaseActivity)context).onDismissLoadingDialog();
			}
		};
		if(Utils.checkNetworkConnected(context)) {
			((BaseActivity)context).onShowLoadingDialog();
			if(mInfo.isfriend()) {
				HttpManager.deleteFriend(PartnerApplication.getInstance().getUserInfo().getToken(),
						mInfo.getFriendId(), callback);
			} else {
				HttpManager.followFriend(PartnerApplication.getInstance().getUserInfo().getToken(),
						mInfo.getUsertoken(), callback);
			}
		}
	}

	private static class ViewHolder {
		private SimpleDraweeView avatarView;
		private TextView nameView;
		private TextView followView;
	}
}