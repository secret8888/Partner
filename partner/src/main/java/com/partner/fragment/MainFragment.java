package com.partner.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.partner.R;
import com.partner.adapter.ActivityFragmentAdapter;
import com.partner.common.annotation.ViewId;
import com.partner.fragment.base.BaseFragment;

public class MainFragment extends BaseFragment implements OnClickListener, ViewPager.OnPageChangeListener {

	@ViewId(R.id.tv_type_all)
	private TextView allTypeView;

	@ViewId(R.id.tv_type_activity)
	private TextView activityTypeView;

	@ViewId(R.id.tv_type_follow)
	private TextView followTypeView;

	@ViewId(R.id.pager_activity)
	private ViewPager activityPager;

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_main;
	}

	@Override
	protected void readIntent() {

	}

	@Override
	protected void initControls(Bundle savedInstanceState) {
		activityPager.setAdapter(new ActivityFragmentAdapter(getFragmentManager()));
//		showRefreshingView();
//		HttpManager.getRecommendedProjects(new CoinvsHandler(getActivity()) {
//
//			@Override
//			protected void handleSuccessMessage(Object object) {
//				hideRefreshingView();
//				HandleInfo handleInfo = (HandleInfo) object;
//				ArrayList<ProjectInfo> projectInfos = GsonUtils
//						.getProjectsFromJson(handleInfo.getData());
//				if (projectInfos != null) {
//					ProjectFragmentAdapter mAdapter = new ProjectFragmentAdapter(
//							getActivity().getSupportFragmentManager(),
//							projectInfos);
//					projectPager.setAdapter(mAdapter);
//					projectIndicator.setViewPager(projectPager);
//				}
//				getNews();
//			}
//
//			@Override
//			protected void handleError(int errorCode) {
//				hideRefreshingView();
//				getNews();
//			}
//		});
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
			activityPager.setCurrentItem(0);
			break;
		case R.id.tv_type_activity:
			activityPager.setCurrentItem(1);
			break;
		case R.id.tv_type_follow:
			activityPager.setCurrentItem(2);
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
		if(position == 0) {
			allTypeView.setTextColor(getResources().getColor(R.color.text_blue));
		} else {
			allTypeView.setTextColor(getResources().getColor(R.color.black));
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
