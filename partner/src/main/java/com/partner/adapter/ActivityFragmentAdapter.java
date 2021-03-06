package com.partner.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.partner.fragment.ActivityListFragment;
import com.partner.fragment.InstitutionListFragment;

public class ActivityFragmentAdapter extends FragmentPagerAdapter{

	private boolean isBusiness;

	public ActivityFragmentAdapter(FragmentManager fm, boolean isBusiness) {
		super(fm);
		this.isBusiness = isBusiness;
	}

	@Override
	public Fragment getItem(int position) {
		if(position == 2) {
			return InstitutionListFragment.newInstance();
		} else {
			return ActivityListFragment.newInstance(position);
		}
	}

	@Override
	public int getCount() {
		return isBusiness? 2: 3;
	}

}
