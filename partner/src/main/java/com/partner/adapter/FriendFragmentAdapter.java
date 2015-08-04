package com.partner.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.partner.fragment.ActivityListFragment;
import com.partner.fragment.FriendListFragment;

public class FriendFragmentAdapter extends FragmentPagerAdapter{

	private boolean isBusiness;

	public FriendFragmentAdapter(FragmentManager fm, boolean isBusiness) {
		super(fm);
		this.isBusiness = isBusiness;
	}

	@Override
	public Fragment getItem(int position) {
		return FriendListFragment.newInstance(position);
	}

	@Override
	public int getCount() {
		return isBusiness? 1: 2;
	}

}
