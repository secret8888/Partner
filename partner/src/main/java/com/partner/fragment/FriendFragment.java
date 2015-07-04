package com.partner.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.partner.R;
import com.partner.adapter.ActivityFragmentAdapter;
import com.partner.adapter.FriendFragmentAdapter;
import com.partner.common.annotation.ViewId;
import com.partner.common.util.Logcat;
import com.partner.fragment.base.BaseFragment;
import com.partner.view.CustomViewPager;

public class FriendFragment extends BaseFragment implements View.OnClickListener, ViewPager.OnPageChangeListener {

	@ViewId(R.id.tv_type_friend)
	private TextView friendView;

	@ViewId(R.id.tv_type_follow)
	private TextView followTypeView;

	@ViewId(R.id.pager_friend)
	private CustomViewPager friendPager;

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_friend;
	}

	@Override
	protected void readIntent() {

	}

	@Override
	protected void initControls(Bundle savedInstanceState) {
		friendPager.setAdapter(new FriendFragmentAdapter(getFragmentManager()));
	}

	@Override
	protected void setListeners() {
		friendView.setOnClickListener(this);
		followTypeView.setOnClickListener(this);
		friendPager.setOnPageChangeListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.tv_type_friend:
				friendPager.setCurrentItem(0);
				break;
			case R.id.tv_type_follow:
				friendPager.setCurrentItem(1);
				break;
			default:
				break;
		}
	}

	private void setOptionChecked(int position) {
		int normalColor = getResources().getColor(R.color.black);
		int selectedColor = getResources().getColor(R.color.text_blue);
		friendView.setTextColor(normalColor);
		followTypeView.setTextColor(normalColor);
		switch (position) {
			case 0:
				friendView.setTextColor(selectedColor);
				break;
			case 1:
				followTypeView.setTextColor(selectedColor);
				break;
		}
	}

	@Override
	public void onPageSelected(int i) {
		setOptionChecked(i);
	}

	@Override
	public void onPageScrolled(int i, float v, int i1) {}

	@Override
	public void onPageScrollStateChanged(int i) {}
}
