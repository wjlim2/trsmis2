package com.example.trsmis2.util;

import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by sy-02 on 2018-04-12.
 */

public abstract class RecyclerViewScrollListener extends RecyclerView.OnScrollListener {
    private static final String TAG = RecyclerViewScrollListener.class.getSimpleName();

    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private int visibleThreshold = 10; // 8 The minimum amount of items to have below your current scroll position before loading more.
    private static final int HIDE_THRESHOLD = 30; // 20
    private int scrolledDistance = 0;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    RecyclerViewPositionHelper mRecyclerViewHelper;
    private int currentPage = 1;
    private int totalDy = 0;

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        totalDy += dy; // 아이템 리스트의 y축값. 최상단에 있을 때 0, 최하단은 아이템 수에 따라 다름
        super.onScrolled(recyclerView, dx, dy);
        mRecyclerViewHelper = RecyclerViewPositionHelper.createHelper(recyclerView);
        visibleItemCount = recyclerView.getChildCount(); // 현재 뷰에 보이는 아이템의 수
        totalItemCount = mRecyclerViewHelper.getItemCount(); // 아이템 총량
        firstVisibleItem = mRecyclerViewHelper.findFirstVisibleItemPosition(); // 현재 뷰에 보이는 아이템 중 최상단에 있는 아이템의 포지션
        if (scrolledDistance > HIDE_THRESHOLD) {
            onHide();
            scrolledDistance = 0;
        } else if (scrolledDistance < -HIDE_THRESHOLD) {
            onShow();
            scrolledDistance = 0;
        }
        scrolledDistance += dy;

        if (loading) {
            if (totalItemCount > previousTotal) { // 현재 아이템 수가 이전 아이템수보다 많다 -> 아이템을 서버에서 가져왔다 -> 서버로부터 아이템을 로드하는 상태가 아니다.
                loading = false;
                previousTotal = totalItemCount;
            }
        }
        //totalItemCount - visibleItemCount = 보이지 않는 아이템 수, firstVisibleItem + visibleThreshold =
        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
            currentPage++;
            onLoadMore(currentPage);
            loading = true;
        }
    }

    public abstract void onHide();

    public abstract void onShow();

    public abstract void onLoadMore(int currentPage);
}