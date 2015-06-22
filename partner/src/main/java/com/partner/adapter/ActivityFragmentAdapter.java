package com.partner.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.partner.fragment.ActivityFragment;

public class ActivityFragmentAdapter extends FragmentPagerAdapter{

//	private ArrayList<ProjectInfo> projectInfos;
	
	public ActivityFragmentAdapter(FragmentManager fm) {
		super(fm);
//		this.projectInfos = projectInfos;
	}

	@Override
	public Fragment getItem(int position) {
		return ActivityFragment.newInstance();
	}

	@Override
	public int getCount() {
		return 3;
	}

}
