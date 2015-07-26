package com.youdao.sharesdk.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youdao.sharesdk.R;
import com.youdao.sharesdk.adapter.AlertAdapter;
import com.youdao.sharesdk.listener.OnSharePlatformListener;

public class ShareAlert {

	/**
	 * ShareAlert instance, there are 8 platforms(yixin, yixin timeline, weixin,
	 * weixin timeline, weibo, qq, qzone and public share). when you click one
	 * of thems, the callback will be called.
	 * 
	 * @author yuym
	 */
	private static ShareAlert shareAlert = null;

	/**
	 * share platform icons
	 */
	private int[] platformIcons = null;

	//R.drawable.sns_yixin_icon, R.drawable.sns_yixin_timeline_icon, 
	private ShareAlert() {
		platformIcons = new int[] { R.drawable.sns_weixin_icon,
				R.drawable.sns_weixin_timeline_icon, R.drawable.sns_sina_icon,
				R.drawable.sns_qqfriends_icon, R.drawable.sns_qzone_icon,
				R.drawable.sns_others_icon };
	}

	public static ShareAlert getInstance() {
		if (shareAlert == null) {
			shareAlert = new ShareAlert();
		}

		return shareAlert;
	}

	/**
	 * show default dialog alert. the method "onSharePlatformClick" will be
	 * called when icon is clicked.
	 * 
	 * @param context
	 * @param platformListener
	 *            callback of platform icon click
	 * @see the class ShareConsts
	 * @return
	 */
	public Dialog showAlert(final Context context,
			final OnSharePlatformListener platformListener) {
		final Dialog shareDialog = new Dialog(context,
				R.style.MMTheme_DataSheet);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.alert_dialog_menu_layout, (ViewGroup) null, false);
		layout.setMinimumWidth(10000);
		GridView gridView = (GridView) layout.findViewById(R.id.grid_content);
		TextView cancelView = (TextView) layout.findViewById(R.id.tv_cancel);
		AlertAdapter adapter = new AlertAdapter(context, getData(context));
		gridView.setAdapter(adapter);
		// set a large value put it in bottom
		WindowManager.LayoutParams layoutParams = shareDialog.getWindow()
				.getAttributes();
		layoutParams.x = 0;
		layoutParams.y = -1000;
		layoutParams.gravity = Gravity.BOTTOM;
		shareDialog.onWindowAttributesChanged(layoutParams);
		shareDialog.setCanceledOnTouchOutside(true);
		shareDialog.setContentView(layout);
		shareDialog.show();

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				shareDialog.dismiss();
				platformListener.onSharePlatformClick(position);
			}
		});

		cancelView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				shareDialog.dismiss();
			}
		});
		return shareDialog;
	}

	/**
	 * get platform data
	 * 
	 * @param context
	 * @return
	 */
	private List<HashMap<String, Object>> getData(Context context) {
		ArrayList<HashMap<String, Object>> dataList = new ArrayList<HashMap<String, Object>>();
		String sharePlatforms[] = context.getResources().getStringArray(
				R.array.share_platforms);
		for (int i = 0; i < sharePlatforms.length; i++) {
			HashMap<String, Object> platforMap = new HashMap<String, Object>();
			platforMap.put("icon", platformIcons[i]);
			platforMap.put("name", sharePlatforms[i]);
			dataList.add(platforMap);
		}
		return dataList;
	}
}
