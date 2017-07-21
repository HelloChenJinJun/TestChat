package chen.testchat.ui.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.pointstone.cugappplat.baseadapter.BaseWrappedViewHolder;
import org.pointstone.cugappplat.baseadapter.baselistener.BaseItemClickListener;
import org.pointstone.cugappplat.baseadapter.baseloadview.OnLoadMoreDataListener;

import java.util.ArrayList;
import java.util.List;

import chen.testchat.R;
import chen.testchat.adapter.WeiXinAdapter;
import chen.testchat.bean.WinXinBean;
import chen.testchat.db.ChatDB;
import chen.testchat.mvp.WinXinInfoTask.WinXinInfoContacts;
import chen.testchat.mvp.WinXinInfoTask.WinXinInfoModel;
import chen.testchat.mvp.WinXinInfoTask.WinXinInfoPresenter;
import chen.testchat.ui.EditShareMessageActivity;
import chen.testchat.ui.WeiXinNewsActivity;
import chen.testchat.util.LogUtil;
import chen.testchat.view.ListViewDecoration;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/6      23:03
 * QQ:             1981367757
 */
public class WeiXinFragment extends org.pointstone.cugappplat.base.basemvp.BaseFragment implements WinXinInfoContacts.View {
//        private ProgressBar mProgressBar;
        private RecyclerView display;
        private SwipeRefreshLayout refresh;
//        private LinearLayoutManager mLinearLayoutManager;
        private WeiXinAdapter mAdapter;
        private List<WinXinBean> data = new ArrayList<>();
        private WinXinInfoPresenter mWinXinInfoPresenter;
        private WinXinInfoModel mWinXinInfoModel;
//        private int visibleCount;
//        private int itemCount;
//        private int firstVisiblePosition;
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
                return R.layout.wei_xin_layout;
        }

        @Override
        public void initView() {
                display = (RecyclerView) findViewById(R.id.rcv_wei_xin_display);
                refresh = (SwipeRefreshLayout) findViewById(R.id.swl_wei_xin_refresh);
//                mProgressBar = (ProgressBar) findViewById(R.id.pb_wei_xin_load);
                refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                                currentPage = 1;
                                data.clear();
//                                mWinXinAdapter.notifyDataSetChanged();
                                mWinXinInfoPresenter.getWinXinInfo(currentPage);
                        }
                });
//                display.addOnScrollListener(new RecyclerView.OnScrollListener() {
//                        @Override
//                        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                                if (dy > 0) {
////                                        向下滚动
//                                        visibleCount = mLinearLayoutManager.getChildCount();
//                                        firstVisiblePosition = mLinearLayoutManager.findFirstVisibleItemPosition();
//                                        itemCount = mLinearLayoutManager.getItemCount();
//                                        if (!isLoading && firstVisiblePosition + visibleCount >= itemCount) {
//                                                isLoading = true;
//                                                loadMoreData();
//                                        }
//                                }
//                        }
//                });
        }


        private int currentPage = 1;


        @Override
        public void initData() {
                display.setLayoutManager(new LinearLayoutManager(getActivity()));
                display.setItemAnimator(new DefaultItemAnimator());
                display.addItemDecoration(new ListViewDecoration(getActivity()));
                mAdapter = new WeiXinAdapter(data, R.layout.win_xin_fragment_item_layout);
                mWinXinInfoPresenter = new WinXinInfoPresenter();
                mWinXinInfoModel=new WinXinInfoModel();
                mWinXinInfoPresenter.setViewAndModel(this, mWinXinInfoModel);
                mAdapter.setOnLoadMoreDataListener(new OnLoadMoreDataListener() {
                        @Override
                        public void onLoadMoreData() {
                                loadMoreData();
                        }
                },display);
                display.addOnItemTouchListener(new BaseItemClickListener() {
                        @Override
                        protected void onItemLongClick(BaseWrappedViewHolder baseWrappedViewHolder, View view, int position) {
                        }

                        @Override
                        protected void onItemChildLongClick(BaseWrappedViewHolder baseWrappedViewHolder, int id, View view, int position) {

                        }

                        @Override
                        protected void onItemChildClick(BaseWrappedViewHolder baseWrappedViewHolder, int id, View view, int position) {
                               LogUtil.e("onItemChildClick");
                                if (id == R.id.btn_wei_xin_fragment_right) {
                                        final WinXinBean bean = mAdapter.getData(position);
                                        PopupMenu popupMenu = new PopupMenu(getActivity(), view);
                                        popupMenu.getMenuInflater().inflate(R.menu.wei_xin_fragment_item_menu, popupMenu.getMenu());
                                        if (ChatDB.create().getWeixinInfoReadStatus(bean.getUrl()) == 1) {
                                                popupMenu.getMenu().findItem(R.id.wei_xin_fragment_item_menu_read).setTitle("标记为未读状态");
                                        } else {
                                                popupMenu.getMenu().findItem(R.id.wei_xin_fragment_item_menu_read).setTitle("标记为已读状态");
                                        }
                                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                                @Override
                                                public boolean onMenuItemClick(MenuItem item) {
                                                        switch (item.getItemId()) {
                                                                case R.id.wei_xin_fragment_item_menu_share:
                                                                        Intent intent = new Intent(getActivity(), EditShareMessageActivity.class);
                                                                        intent.putExtra(Intent.EXTRA_TEXT, bean.getTitle() + "," + bean.getUrl());
                                                                        intent.putExtra("share_info", bean);
                                                                        intent.putExtra("type", "wei_xin");
                                                                        intent.putExtra("destination", "url");
                                                                        intent.setType("text/plain");
                                                                        startActivity(intent);
                                                                        break;
                                                                case R.id.wei_xin_fragment_item_menu_read:
                                                                        if (item.getTitle().equals("标记为未读状态")) {
                                                                                ChatDB.create().saveWeiXinInfoReadStatus(bean.getUrl(), 0);
                                                                        } else {
                                                                                ChatDB.create().saveWeiXinInfoReadStatus(bean.getUrl(), 1);
                                                                        }
                                                                        mAdapter.notifyDataSetChanged();
                                                                        break;
                                                        }
                                                        return true;
                                                }
                                        });
                                        popupMenu.show();
                                }

                        }

                        @Override
                        protected void onItemClick(BaseWrappedViewHolder baseWrappedViewHolder, int id, View view, int position) {
                                ((TextView) baseWrappedViewHolder.getView(R.id.tv_wei_xin_fragment_layout_title)).setTextColor(getResources().getColor(R.color.base_color_text_grey));
                                WinXinBean bean = mAdapter.getData(position);
                                ChatDB.create().saveWeiXinInfoReadStatus(bean.getUrl(), 1);
                                Intent intent = new Intent(getActivity(), WeiXinNewsActivity.class);
                                intent.putExtra("bean", bean);
                                startActivity(intent);
                        }
                });
                display.setAdapter(mAdapter);
//                refresh.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                                loadMoreData();
//                        }
//                }, 200);
        }

        @Override
        protected void updateView() {
                loadMoreData();
        }

        private void loadMoreData() {
                mWinXinInfoPresenter.getWinXinInfo(currentPage);
        }

        @Override
        public void updateData(List<WinXinBean> data) {
                currentPage++;
                if (data != null && data.size() > 0) {
                        this.data.addAll(data);
                } else {
                        LogUtil.e("加载的数据为空");
                }
                mAdapter.notifyDataSetChanged();
        }


        @Override
        public void hideLoading() {
//                isLoading = false;
                if (refresh.isRefreshing()) {
                        refresh.setRefreshing(false);
                }
                super.hideLoading();
        }


        @Override
        public void onDestroyView() {
                super.onDestroyView();
                mWinXinInfoPresenter.onDestroy();
        }


}
