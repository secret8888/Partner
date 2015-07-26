package com.youdao.sharesdk.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.youdao.sharesdk.R;

public class AlertAdapter extends BaseAdapter {
	private List<HashMap<String, Object>> items;
	
	private Context context;
	
	public AlertAdapter(Context context, List<HashMap<String, Object>> items) {
		this.context = context;
		this.items = items;
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
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
			convertView = View.inflate(context, R.layout.adapter_alert_item, null);

			holder.iconView = (ImageView) convertView.findViewById(R.id.im_icon);
			holder.nameView = (TextView) convertView.findViewById(R.id.tv_name);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		HashMap<String, Object> item = items.get(position);
		holder.iconView.setBackgroundResource((Integer)item.get("icon"));
		holder.nameView.setText((String)item.get("name"));
		
		return convertView;
	}

	private class ViewHolder {
		private ImageView iconView = null;
		
		private TextView nameView = null;
	}
}