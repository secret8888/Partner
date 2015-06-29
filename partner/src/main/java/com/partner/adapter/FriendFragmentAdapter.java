package com.partner.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.partner.fragment.ActivityListFragment;
import com.partner.fragment.FriendListFragment;

public class FriendFragmentAdapter extends FragmentPagerAdapter{

//	private ArrayList<ProjectInfo> projectInfos;

	public FriendFragmentAdapter(FragmentManager fm) {
		super(fm);
//		this.projectInfos = projectInfos;
	}

	@Override
	public Fragment getItem(int position) {
		return FriendListFragment.newInstance();
	}

	@Override
	public int getCount() {
		return 2;
	}

}
