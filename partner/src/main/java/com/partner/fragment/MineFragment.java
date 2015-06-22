package com.partner.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.partner.R;
import com.partner.fragment.base.BaseFragment;

public class MineFragment extends BaseFragment implements OnClickListener {

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_mine;
	}

	@Override
	protected void readIntent() {

	}

	@Override
	protected void initControls(Bundle savedInstanceState) {
		
	}

	@Override
	protected void setListeners() {
	}

	@Override
	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.tv_setting:
//			ActivityUtils.startSetingActivity(this, 1);
//			break;
//		case R.id.btn_all_investment:
//			ActivityUtils.startInvestmentRecordActivity(getActivity(),
//					accountInfo);
//			break;
//		case R.id.btn_all_rewardment:
//			ActivityUtils.startRewardRecordActivity(getActivity(), accountInfo);
//			break;
//		case R.id.lv_invest_one:
//			startInvestActivity(accountInfo.getIvlist().get(0));
//			break;
//		case R.id.lv_invest_two:
//			if(accountInfo.getIvlist().size() > 1) {
//				startInvestActivity(accountInfo.getIvlist().get(1));
//			}
//			break;
//		default:
//			break;
//		}
	}

}
