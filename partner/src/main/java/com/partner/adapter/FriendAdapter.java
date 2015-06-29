package com.partner.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.partner.R;

import java.util.ArrayList;

public class FriendAdapter extends BaseAdapter {

	private Context context;

	private ArrayList<String> mItems;

	public FriendAdapter(Context context, ArrayList<String> projectInfos) {
		this.context = context;
		this.mItems = projectInfos;
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

//			holder.projectCoverView = (ImageView) convertView
//					.findViewById(R.id.im_project_cover);
//			holder.projectNameView = (TextView) convertView
//					.findViewById(R.id.tv_project_name);
//			holder.projectDescView = (TextView) convertView
//					.findViewById(R.id.tv_project_desc);
//			holder.investAmountView = (TextView) convertView
//					.findViewById(R.id.tv_invest_amount);
//			holder.investStatusView = (TextView) convertView
//					.findViewById(R.id.tv_invest_status);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		return convertView;
	}

	private class ViewHolder {
		private ImageView projectCoverView = null;
		private TextView projectNameView = null;
		private TextView projectDescView = null;
		private TextView investAmountView = null;
		private TextView investStatusView = null;
	}
}