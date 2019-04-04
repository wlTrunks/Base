package com.inter.trunks.base;


import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;


public class PagerAdapter extends FragmentPagerAdapter {

    private List<Page> pages;

    public PagerAdapter(FragmentManager fm, List<Page> pages) {
        super(fm);
        this.pages = pages;
    }

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setPages(List<Page> pages) {
        this.pages = pages;
        notifyDataSetChanged();
    }


    public void deletePage(int page) {
        if (page < pages.size()) {
            pages.remove(page);
            notifyDataSetChanged();
        }
    }

    @Override
    public Fragment getItem(int position) {
        return pages.get(position).fragment;
    }

    @Override
    public int getCount() {
        return pages != null ? pages.size() : 0;
    }

    public static class Page {
        private int iconResId;
        private int titleResId;
        private Fragment fragment;

        public Page(@DrawableRes int iconResId, Fragment fragment) {
            this(iconResId, -1, fragment);
        }

        public Page(Fragment fragment, @StringRes int titleResId) {
            this(-1, titleResId, fragment);
        }

        public Page(@DrawableRes int iconResId, @StringRes int titleResId, Fragment fragment) {
            this.iconResId = iconResId;
            this.titleResId = titleResId;
            this.fragment = fragment;
        }

        public int getIconResId() {
            return iconResId;
        }

        public int getTitleResId() {
            return titleResId;
        }

        public Fragment getFragment() {
            return fragment;
        }

        public void setFragment(Fragment fragment) {
            this.fragment = fragment;
        }
    }
}
