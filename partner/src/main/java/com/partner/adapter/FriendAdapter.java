package com.partner.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.partner.R;
import com.partner.model.FriendInfo;

import java.util.ArrayList;

public class FriendAdapter extends BaseAdapter {

	private Context context;

	private ArrayList<FriendInfo> mItems;

	public FriendAdapter(Context context, ArrayList<FriendInfo> items) {
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
			convertView = View.inflate(context, R.layout.adapter_friend, null);

			holder.avatarView = (SimpleDraweeView) convertView.findViewById(R.id.im_avatar);
			holder.nameView = (TextView) convertView.findViewById(R.id.tv_name);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		FriendInfo info = mItems.get(position);
		holder.nameView.setText(info.getFriendRealName());
		return convertView;
	}

	private static class ViewHolder {
		private SimpleDraweeView avatarView;
		private TextView nameView;
	}
}