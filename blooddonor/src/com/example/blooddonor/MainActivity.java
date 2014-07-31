package com.example.blooddonor;

import com.example.blooddonor.interfaces.ResponseInterface;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

public class MainActivity extends FragmentActivity implements TabListener,
		ResponseInterface {
	ViewPager pager = null;
	FragmentManager manager = null;
	ActionBar actionbar = null;
	ActionBar.Tab friendsTab;
	ActionBar.Tab meTab;
	ActionBar.Tab searchTab;
	ActionBar.Tab homeTab;
	SharedprefClass pref;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		manager = getSupportFragmentManager();
		pager = (ViewPager) findViewById(R.id.pager);
		actionbar = getActionBar();
		pref = new SharedprefClass(this);
		Drawable dg = getResources().getDrawable(R.drawable.action);
		pref.put("inMainActivity", "true");
		Intent serviceBackground = new Intent(this, Background.class);
		startService(serviceBackground);
		// Mentioning android bar that there will tabs added to you.
		actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionbar.setStackedBackgroundDrawable(dg);

		actionbar.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.actionbarbackground));
		pager.setAdapter(new Scrollpager(manager, this));

		pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub

				actionbar.setSelectedNavigationItem(arg0);
				if (arg0 == 0) {
					actionbar.setTitle("Friends");
					friendsTab.setIcon(R.drawable.friends);
					meTab.setIcon(R.drawable.profilenot);
					searchTab.setIcon(R.drawable.searchnot);
					homeTab.setIcon(R.drawable.homenot);
				} else if (arg0 == 1) {
					actionbar.setTitle("Profile");
					friendsTab.setIcon(R.drawable.friendsnot);
					meTab.setIcon(R.drawable.profile);
					searchTab.setIcon(R.drawable.searchnot);
					homeTab.setIcon(R.drawable.homenot);
				} else if (arg0 == 2) {
					actionbar.setTitle("Search");
					searchTab.setIcon(R.drawable.search);
					meTab.setIcon(R.drawable.profilenot);
					friendsTab.setIcon(R.drawable.friendsnot);
					homeTab.setIcon(R.drawable.homenot);
				} else if (arg0 == 3) {
					actionbar.setTitle("Home");
					homeTab.setIcon(R.drawable.home);
					friendsTab.setIcon(R.drawable.friendsnot);
					meTab.setIcon(R.drawable.profilenot);
					searchTab.setIcon(R.drawable.searchnot);
				}

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
		// Adding tabs
		friendsTab = actionbar.newTab();
		// friendsTab.setText("Friends");

		friendsTab.setIcon(R.drawable.friendsnot);
		friendsTab.setTabListener(this);

		meTab = actionbar.newTab();
		// meTab.setText("Me");
		meTab.setIcon(R.drawable.profilenot);
		meTab.setTabListener(this);

		searchTab = actionbar.newTab();
		// searchTab.setText("Search");
		searchTab.setIcon(R.drawable.searchnot);
		searchTab.setTabListener(this);

		homeTab = actionbar.newTab();
		// homeTab.setText("Home");
		homeTab.setIcon(R.drawable.homenot);
		homeTab.setTabListener(this);

		actionbar.addTab(friendsTab);
		actionbar.addTab(meTab);
		actionbar.addTab(searchTab);
		actionbar.addTab(homeTab);
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

		pager.setCurrentItem(tab.getPosition());

	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getResponse(Context context, String json) {
		// TODO Auto-generated method stub

	}

}

class Scrollpager extends FragmentPagerAdapter {
	Context context;

	public Scrollpager(FragmentManager fm, Context context) {
		super(fm);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	@Override
	public Fragment getItem(int i) {
		// TODO Auto-generated method stub
		Fragment fragment = null;
		if (i == 0) {
			fragment = new Friends(context);
		} else if (i == 1) {
			fragment = new Me();
		} else if (i == 2) {
			fragment = new Search();
		} else if (i == 3) {
			fragment = new Home(context);
		}
		return fragment;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 4;
	}

}
