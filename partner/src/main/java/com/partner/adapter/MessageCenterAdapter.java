package com.partner.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.partner.R;
import com.partner.activity.activity.ActivitySignActivity;
import com.partner.model.MessageInfo;
import com.partner.model.RegistrationInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MessageCenterAdapter extends BaseAdapter {

	private Context context;

	private ArrayList<MessageInfo> mItems;

	public MessageCenterAdapter(Context context, ArrayList<MessageInfo> items) {
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
			convertView = View.inflate(context, R.layout.adapter_message_center, null);

			holder.avatarView = (SimpleDraweeView) convertView
					.findViewById(R.id.im_avatar);
			holder.nameView = (TextView) convertView
					.findViewById(R.id.tv_name);
			holder.timeView = (TextView) convertView
					.findViewById(R.id.tv_time);
			holder.contentView = (TextView) convertView
					.findViewById(R.id.tv_content);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final MessageInfo info = mItems.get(position);
		holder.nameView.setText(info.getMessageFromUserName());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		holder.timeView.setText(format.format(new Date(info.getLastModTime())));
		holder.contentView.setText(info.getMessageContent().getContent());

		String avatarUrl = info.getMessageFromUserHeadImage();
		if(!TextUtils.isEmpty(avatarUrl)) {
			holder.avatarView.setImageURI(Uri.parse(avatarUrl));
		}
		return convertView;
	}

	private static class ViewHolder {
		private SimpleDraweeView avatarView;
		private TextView nameView;
		private TextView timeView;
		private TextView contentView;
	}
}