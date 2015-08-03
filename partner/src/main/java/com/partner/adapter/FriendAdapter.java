package com.partner.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.partner.R;
import com.partner.activity.activity.ActivitySignActivity;
import com.partner.model.FriendInfo;

import java.util.ArrayList;

public class FriendAdapter extends BaseAdapter {

	private Context context;

	private ArrayList<FriendInfo> mItems;

	private boolean isInvite = false;

	public FriendAdapter(Context context, ArrayList<FriendInfo> items) {
		this.context = context;
		this.mItems = items;
	}

	public FriendAdapter(Context context, ArrayList<FriendInfo> items, boolean isInvite) {
		this(context, items);
		this.isInvite = isInvite;
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
			holder.arrowView = (ImageView) convertView.findViewById(R.id.im_arrow);
			holder.selectBox = (CheckBox) convertView.findViewById(R.id.cbox_select);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final FriendInfo info = mItems.get(position);
		if(isInvite) {
			holder.selectBox.setVisibility(View.VISIBLE);
			holder.arrowView.setVisibility(View.GONE);
			holder.selectBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					info.setIsSelected(isChecked);
				}
			});
		} else {
			holder.selectBox.setVisibility(View.GONE);
			holder.arrowView.setVisibility(View.VISIBLE);
		}

		if(!TextUtils.isEmpty(info.getFriendHeadImage())) {
			Uri uri = Uri.parse(info.getFriendHeadImage());
			holder.avatarView.setImageURI(uri);
		}
		holder.nameView.setText(info.getFriendRealName());
		return convertView;
	}

	private static class ViewHolder {
		private SimpleDraweeView avatarView;
		private TextView nameView;
		private ImageView arrowView;
		private CheckBox selectBox;
	}
}