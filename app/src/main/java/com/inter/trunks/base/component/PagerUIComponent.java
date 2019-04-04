package com.inter.trunks.base.component;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import com.inter.trunks.base.PagerAdapter;
import com.inter.trunks.base.R;

import java.util.List;

/**
 * Компонет ViewPager R.id.pager и TabLayout R.id.slidingTabs
 */
public class PagerUIComponent extends UIComponent {
    private TabLayout tabLayout;
    private ViewPager pager;
    private PagerAdapter adapter;
    private List<PagerAdapter.Page> pageList;

    public PagerUIComponent(FragmentManager manager) {
        adapter = new PagerAdapter(manager);
    }


    @Override
    public void onViewCreated(View layout) {
        super.onViewCreated(layout);
        tabLayout = layout.findViewById(R.id.slidingTabs);
        pager = layout.findViewById(R.id.pager);
        if (pager == null) throw new RuntimeException("Setup ViewPager on layout");
        pager.setAdapter(adapter);
        if (tabLayout != null) {
            tabLayout.setupWithViewPager(pager);
        }
    }

    public void setPages(List<PagerAdapter.Page> pageList) {
        this.pageList = pageList;
        adapter.setPages(pageList);
        setTabLayoutTitleIcon();
    }

    private void setTabLayoutTitleIcon() {
        if (tabLayout != null) {
            for (int i = 0; i < pageList.size(); i++) {
                int resIcon = pageList.get(i).getIconResId();
                int resTitle = pageList.get(i).getTitleResId();
                TabLayout.Tab tab = tabLayout.getTabAt(i);
                if (tab != null) {
                    if (resIcon != -1) {
                        tab.setIcon(pageList.get(i).getIconResId());
                    }
                    if (resTitle != -1) {
                        tab.setText(resTitle);
                    }
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        pager.setAdapter(null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (pageList != null) {
            for (PagerAdapter.Page page : pageList) {
                page.setFragment(null);
            }
            this.pageList.clear();
        }
    }
}
