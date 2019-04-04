package com.inter.trunks.base.component;

import android.support.annotation.IntDef;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Копонент пагинации
 * RecyclerView id default R.id.list
 * NestedScrollView id default R.id.nested_scroll
 */
public class PaginationUIComponent extends UIComponent {

    protected RecyclerView recyclerView;
    protected NestedScrollView nestedScrollView;
    private PaginationUIInterface paginationUIInterface;
    /**
     * Скроллинг нестедвью при загрузке новой страницы
     */
    private int nestedScrollViewFocus = FOCUS_NONE;


    public PaginationUIComponent(PaginationUIInterface paginationUIInterface) {
        if (paginationUIInterface == null) {
            paginationUIInterface = PaginationUIInterface.STUB;
        }
        this.paginationUIInterface = paginationUIInterface;
    }

    @Override
    public void onViewCreated(View layout) {
        super.onViewCreated(layout);
        recyclerView = layout.findViewById(R.id.list);
        nestedScrollView = layout.findViewById(R.id.nested_scroll);
        if (nestedScrollView != null) {
            ViewCompat.setNestedScrollingEnabled(recyclerView, false);
            nestedScrollView.setOnScrollChangeListener(getNestedScrollable());
        } else {
            if (recyclerView != null) recyclerView.addOnScrollListener(getScrollable());
        }
    }


    private void proceedPaging() {
        if (paginationUIInterface.checkPagingEnable()) {
            paginationUIInterface.onLoadingNextPage();
            proccedNestedScroll();
        }
    }

    public PaginationUIComponent setNestedScrollViewFocus(@NestedScrollFocusDirection int nestedScrollViewFocus) {
        this.nestedScrollViewFocus = nestedScrollViewFocus;
        return this;
    }

    private void proccedNestedScroll() {
        if (nestedScrollView != null) {
            if (nestedScrollViewFocus != FOCUS_NONE)
                nestedScrollView.post(() -> nestedScrollView.fullScroll(nestedScrollViewFocus));
        }
    }

    protected RecyclerView.OnScrollListener getScrollable() {
        RecyclerScrollableController.OnLastItemVisibleListener onLastItemVisibleListener = this::proceedPaging;
        return new RecyclerScrollableController(onLastItemVisibleListener);
    }


    protected NestedScrollView.OnScrollChangeListener getNestedScrollable() {
        return (NestedScrollView nestedScrollView1, int scrollX, int scrollY, int oldScrollX, int oldScrollY) -> {
            if (nestedScrollView1.getChildAt(nestedScrollView1.getChildCount() - 1) != null) {
                if ((scrollY >= (nestedScrollView1.getChildAt(nestedScrollView1.getChildCount() - 1).getMeasuredHeight() - nestedScrollView1.getMeasuredHeight())) &&
                        scrollY > oldScrollY) {
                    proceedPaging();
                }
            }
        };
    }

    @Retention(SOURCE)
    @IntDef({View.FOCUS_DOWN, View.FOCUS_UP, FOCUS_NONE})
    public @interface NestedScrollFocusDirection {
    }

    public static final int FOCUS_NONE = -1;

    public interface PaginationUIInterface {
        PaginationUIInterface STUB = new PaginationUIInterface() {
            @Override
            public void onLoadingNextPage() {
            }

            @Override
            public boolean checkPagingEnable() {
                return true;
            }
        };

        void onLoadingNextPage();

        boolean checkPagingEnable();
    }
}
