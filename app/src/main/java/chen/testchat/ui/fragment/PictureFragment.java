package chen.testchat.ui.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import org.pointstone.cugappplat.baseadapter.BaseWrappedViewHolder;
import org.pointstone.cugappplat.baseadapter.baseloadview.OnLoadMoreDataListener;

import java.util.ArrayList;
import java.util.List;

import chen.testchat.R;
import chen.testchat.adapter.PictureAdapter;
import chen.testchat.bean.ImageItem;
import chen.testchat.bean.PictureBean;
import chen.testchat.db.ChatDB;
import chen.testchat.listener.OnBaseItemChildClickListener;
import chen.testchat.mvp.PictureInfoTask.PictureContacts;
import chen.testchat.mvp.PictureInfoTask.PictureModel;
import chen.testchat.mvp.PictureInfoTask.PicturePresenter;
import chen.testchat.ui.BasePreViewActivity;
import chen.testchat.ui.EditShareMessageActivity;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/9      0:02
 * QQ:             1981367757
 */

public class PictureFragment extends org.pointstone.cugappplat.base.basemvp.BaseFragment implements SwipeRefreshLayout.OnRefreshListener, PictureContacts.View {
        private SwipeRefreshLayout refresh;
        private RecyclerView display;
        private PictureAdapter mAdapter;
        private StaggeredGridLayoutManager mStaggeredGridLayoutManager;
        private PicturePresenter mHappyPresenter;
        private int currentPage = 1;
        private PictureModel mPictureModel;
//        private int visibleCount;
//        private int[] firstVisiblePosition;
//        private int itemCount;
//        private boolean isLoading = false;


        @Override
        protected boolean isNeedHeadLayout() {
                return false;
        }

        @Override
        protected boolean isNeedEmptyLayout() {
                return true;
        }

        @Override
        protected int getContentLayout() {
                return R.layout.happy_layout;
        }

        @Override
        public void initView() {
                refresh = (SwipeRefreshLayout) findViewById(R.id.refresh_happy_fragment);
                display = (RecyclerView) findViewById(R.id.rcv_happy_fragment_display);
                refresh.setOnRefreshListener(this);
//                display.addOnScrollListener(new RecyclerView.OnScrollListener() {
//                        @Override
//                        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                                if (dy > 0) {
////                                        向下滚动
//                                        visibleCount = mStaggeredGridLayoutManager.getChildCount();
//                                        firstVisiblePosition = mStaggeredGridLayoutManager.findFirstVisibleItemPositions(null);
//                                        itemCount = mStaggeredGridLayoutManager.getItemCount();
//                                        minFirstVisiblePosition = firstVisiblePosition[0];
//                                        for (int i = 0; i < firstVisiblePosition.length - 1; i++) {
//                                                if (firstVisiblePosition[i + 1] < minFirstVisiblePosition) {
//                                                        minFirstVisiblePosition = firstVisiblePosition[i + 1];
//                                                }
//                                        }
//                                        if (!isLoading && minFirstVisiblePosition + visibleCount >= itemCount) {
//                                                isLoading = true;
//                                                onLoadMoreData(currentPage);
//                                        }
//                                }
//                        }
//                });
        }

//        private int minFirstVisiblePosition;

        private void loadMoreData(int currentPage) {
                mHappyPresenter.getPictureInfo(currentPage);
        }

        @Override
        public void initData() {
                display.setLayoutManager(mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                display.setItemAnimator(new DefaultItemAnimator());
                mHappyPresenter = new PicturePresenter();
                mPictureModel=new PictureModel();
                mHappyPresenter.setViewAndModel(this,mPictureModel);
                mAdapter = new PictureAdapter(null, R.layout.fragment_picture_item_layout);
                display.addOnItemTouchListener(new OnBaseItemChildClickListener() {
                        @Override
                        protected void onItemChildClick(BaseWrappedViewHolder baseWrappedViewHolder, int id, View view, int position) {
                                PictureBean bean = mAdapter.getData(position);
                                if (id == R.id.iv_fragment_picture_item_share) {

                                        Intent intent = new Intent(getActivity(), EditShareMessageActivity.class);
                                        intent.setAction(Intent.ACTION_SEND);
                                        intent.putExtra(Intent.EXTRA_TEXT, bean.getUrl());
                                        intent.putExtra("share_info", bean);
                                        intent.putExtra("type", "picture");
                                        intent.putExtra("destination", "url");
                                        intent.setType("text/plain");
                                        startActivity(intent);

                                } else if (id == R.id.iv_fragment_picture_item_picture) {
                                        ChatDB.create().updatePictureReaded(bean.getUrl(), 1);
                                        List<PictureBean> data = mAdapter.getAllData();
                                        List<ImageItem> itemList = new ArrayList<>();
                                        for (PictureBean item :
                                                data) {
                                                ImageItem imageItem = new ImageItem();
                                                imageItem.setPath(item.getUrl());
                                                itemList.add(imageItem);
                                        }
                                        BasePreViewActivity.startBasePreview(getActivity(), itemList, position);
                                }
                        }
                });
                mAdapter.setOnLoadMoreDataListener(new OnLoadMoreDataListener() {
                        @Override
                        public void onLoadMoreData() {
                                loadMoreData(currentPage);
                        }
                }, display);
                display.setAdapter(mAdapter);
        }

        @Override
        protected void updateView() {
                onRefresh();
        }


        @Override
        public void hideLoading() {

                if (refresh.isRefreshing()) {
                        refresh.setRefreshing(false);
                }
//                isLoading = false;
                super.hideLoading();
        }


        @Override
        public void onRefresh() {
                mAdapter.clearData();
                currentPage = 1;
                loadMoreData(currentPage);
        }


        @Override
        public void onDestroyView() {
                super.onDestroyView();
                mHappyPresenter.onDestroy();
        }


        @Override
        public void onUpdatePictureInfo(List<PictureBean> data) {
                currentPage++;
                mAdapter.addData(data);
        }
}
