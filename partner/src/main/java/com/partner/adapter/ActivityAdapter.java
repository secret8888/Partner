package com.partner.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.partner.R;
import com.partner.model.ActivityInfo;

public class ActivityAdapter extends BaseAdapter {

	private Context context;

	private ArrayList<ActivityInfo> mItems;

	public ActivityAdapter(Context context, ArrayList<ActivityInfo> items) {
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
			convertView = View.inflate(context, R.layout.adapter_activity, null);

			holder.activityIntroView = (ImageView) convertView
					.findViewById(R.id.im_activity_intro);
			holder.monthView = (TextView) convertView
					.findViewById(R.id.tv_month);
			holder.dateView = (TextView) convertView
					.findViewById(R.id.tv_date);
			holder.activityNameView = (TextView) convertView
					.findViewById(R.id.tv_activity_name);
			holder.signNumView = (TextView) convertView
					.findViewById(R.id.tv_sign_num);
			holder.viewNumView = (TextView) convertView
					.findViewById(R.id.tv_view_num);
			holder.locationView = (TextView) convertView
					.findViewById(R.id.tv_location);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		ActivityInfo info = mItems.get(position);
		if(!TextUtils.isEmpty(info.getActivityImage())) {
			holder.activityIntroView.setImageURI(Uri.parse(info.getActivityImage()));
		}
		long createTime = info.getCreateTime();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM");
		Date date = new Date(createTime);
		holder.monthView.setText(dateFormat.format(date));
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		holder.monthView.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
		holder.activityNameView.setText(info.getActivityTitle());
		return convertView;
	}

	private static class ViewHolder {
		private ImageView activityIntroView;
		private TextView monthView;
		private TextView dateView;
		private TextView activityNameView;
		private TextView signNumView;
		private TextView viewNumView;
		private TextView locationView;
	}
}