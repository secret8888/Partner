package com.partner.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.partner.PartnerApplication;
import com.partner.R;
import com.partner.adapter.ActivityFragmentAdapter;
import com.partner.common.annotation.ViewId;
import com.partner.common.constant.Consts;
import com.partner.common.util.Utils;
import com.partner.fragment.base.BaseFragment;
import com.partner.view.CustomViewPager;

public class MainFragment extends BaseFragment implements OnClickListener, ViewPager.OnPageChangeListener {

	@ViewId(R.id.tv_type_all)
	private TextView allTypeView;

	@ViewId(R.id.tv_type_activity)
	private TextView activityTypeView;

	@ViewId(R.id.tv_type_follow)
	private TextView followTypeView;

	@ViewId(R.id.pager_activity)
	private CustomViewPager activityPager;

	private boolean isBusiness;

	private boolean isLogin;

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_main;
	}

	@Override
	protected void readIntent() {

	}

	@Override
	protected void initControls(Bundle savedInstanceState) {
		isBusiness = PartnerApplication.getInstance().getUserInfo().getUserType() == Consts.ROLE_BUSINESS;
		isLogin = PartnerApplication.getInstance().isLogin();
		activityPager.setAdapter(new ActivityFragmentAdapter(getFragmentManager(), isBusiness));
		activityPager.setOffscreenPageLimit(0);
		if(isBusiness) {
			activityTypeView.setText(R.string.publish);
			followTypeView.setVisibility(View.GONE);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		isBusiness = PartnerApplication.getInstance().getUserInfo().getUserType() == Consts.ROLE_BUSINESS;
		if(!isLogin && isBusiness) {
			isLogin = true;
			activityPager.setAdapter(new ActivityFragmentAdapter(getFragmentManager(), isBusiness));
			activityPager.setOffscreenPageLimit(0);
			if(isBusiness) {
				activityTypeView.setText(R.string.publish);
				followTypeView.setVisibility(View.GONE);
			}
		}
	}

	@Override
	protected void setListeners() {
		allTypeView.setOnClickListener(this);
		activityTypeView.setOnClickListener(this);
		followTypeView.setOnClickListener(this);
		activityPager.setOnPageChangeListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_type_all:
			activityPager.setCurrentItem(0, false);
			break;
		case R.id.tv_type_activity:
			if(!Utils.checkLogin(getActivity())) {
				return;
			}
			activityPager.setCurrentItem(1, false);
			break;
		case R.id.tv_type_follow:
			if(!Utils.checkLogin(getActivity())) {
				return;
			}
			activityPager.setCurrentItem(2, false);
			break;
		default:
			break;
		}
	}

	private void setOptionChecked(int position) {
		int normalColor = getResources().getColor(R.color.black);
		int selectedColor = getResources().getColor(R.color.text_blue);
		allTypeView.setTextColor(normalColor);
		activityTypeView.setTextColor(normalColor);
		followTypeView.setTextColor(normalColor);
		switch (position) {
			case 0:
				allTypeView.setTextColor(selectedColor);
				break;
			case 1:
				activityTypeView.setTextColor(selectedColor);
				break;
			case 2:
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
