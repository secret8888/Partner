package com.partner.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.partner.R;
import com.partner.activity.activity.ActivitySignActivity;
import com.partner.model.RegistrationInfo;

import java.util.ArrayList;

public class RegistrationInfoAdapter extends BaseAdapter {

	private Context context;

	private ArrayList<RegistrationInfo> mItems;

	private boolean isRegistrationAdd = false;

	public RegistrationInfoAdapter(Context context, ArrayList<RegistrationInfo> items) {
		this.context = context;
		this.mItems = items;
	}

	public RegistrationInfoAdapter(Context context, ArrayList<RegistrationInfo> items, boolean isAdd) {
		this(context, items);
		isRegistrationAdd = isAdd;
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
			convertView = View.inflate(context, isRegistrationAdd?
					R.layout.adapter_registration_add: R.layout.adapter_registration_info, null);

			holder.nameView = (TextView) convertView
					.findViewById(R.id.tv_name);
			holder.phoneView = (TextView) convertView
					.findViewById(R.id.tv_phone);
			holder.selectBox = (CheckBox) convertView
					.findViewById(R.id.cbox_select);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final RegistrationInfo info = mItems.get(position);
		holder.nameView.setText(info.getUserenrollInfoParent());
		holder.phoneView.setText(info.getUserenrollCellphone());

		holder.selectBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				info.setIsRegistrationAdd(isChecked);
				((ActivitySignActivity) context).onAddNumChanged();
			}
		});
		return convertView;
	}

	private static class ViewHolder {
		private TextView nameView;
		private TextView phoneView;
		private CheckBox selectBox;
	}
}