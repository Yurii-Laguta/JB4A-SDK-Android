package com.exacttarget.practicefield.scrollpages;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ScrollPagesAdapter extends FragmentPagerAdapter {
	private String[] mPages = new String[] { "" };
	private boolean mShowSendMessage = false;

	public ScrollPagesAdapter(FragmentManager fm, String[] inPages, boolean showSendMessage) {
		super(fm);
		setPages(inPages);
		mShowSendMessage = showSendMessage;
	}

	@Override
	public Fragment getItem(int position) {
		return ScrollPagesFragment.newInstance(mPages[position % mPages.length], mShowSendMessage);
	}

	@Override
	public int getCount() {
		return mPages.length;
	}

	public void setPages(String[] inPages) {
		mPages = inPages.clone();
	}
}
