package chen.testchat.ui.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.pointstone.cugappplat.baseadapter.BaseWrappedViewHolder;
import org.pointstone.cugappplat.baseadapter.baseloadview.OnLoadMoreDataListener;

import java.util.ArrayList;
import java.util.List;

import chen.testchat.R;
import chen.testchat.adapter.HappyAdapter;
import chen.testchat.bean.HappyBean;
import chen.testchat.bean.ImageItem;
import chen.testchat.db.ChatDB;
import chen.testchat.listener.OnBaseItemChildClickListener;
import chen.testchat.mvp.HappyInfoTask.HappyContacts;
import chen.testchat.mvp.HappyInfoTask.HappyInfoModel;
import chen.testchat.mvp.HappyInfoTask.HappyPresenter;
import chen.testchat.ui.BasePreViewActivity;
import chen.testchat.ui.EditShareMessageActivity;
import chen.testchat.view.ListViewDecoration;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/7      18:26
 * QQ:             1981367757
 */
public class HappyFragment extends org.pointstone.cugappplat.base.basemvp.BaseFragment implements HappyContacts.View, SwipeRefreshLayout.OnRefreshListener {
        private SwipeRefreshLayout refresh;
        private RecyclerView display;
        private HappyAdapter mHappyAdapter;
        private List<HappyBean> data = new ArrayList<>();
        private HappyPresenter mHappyPresenter;
        private int currentPage = 1;
        private HappyInfoModel mHappyInfoModel;


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
        }



        private void loadMoreData(int currentPage) {
                mHappyPresenter.getHappyInfo(currentPage);
        }

        @Override
        public void initData() {
                display.setLayoutManager(new LinearLayoutManager(getActivity()));
                display.addItemDecoration(new ListViewDecoration(getActivity()));
                display.setItemAnimator(new DefaultItemAnimator());
                mHappyPresenter = new HappyPresenter();
                mHappyInfoModel=new HappyInfoModel();
                mHappyPresenter.setViewAndModel(this,mHappyInfoModel);
                mHappyAdapter = new HappyAdapter(data, R.layout.fragment_happy_item_layout);
                display.addOnItemTouchListener(new OnBaseItemChildClickListener() {
                        @Override
                        protected void onItemChildClick(BaseWrappedViewHolder baseWrappedViewHolder, int id, View view, int position) {
                                HappyBean happyBean = mHappyAdapter.getData(position);

                                if (id == R.id.iv_fragment_happy_item_picture) {
                                        List<ImageItem> list=new ArrayList<>();
                                        for (HappyBean bean :
                                                mHappyAdapter.getAllData()) {
                                                ImageItem imageItem = new ImageItem();
                                                imageItem.setPath(bean.getUrl());
                                                list.add(imageItem);
                                        }
                                        ChatDB.create().updateHappyInfoReaded(happyBean.getUrl(), 1);
                                        BasePreViewActivity.startBasePreview(getActivity(),list,position);
//                                        ImageDisplayActivity.start(getActivity(), view, happyBean.getUrl());
                                } else if (id == R.id.iv_fragment_happy_item_share) {
                                        Intent intent = new Intent(getActivity(), EditShareMessageActivity.class);
                                        intent.setAction(Intent.ACTION_SEND);
                                        intent.putExtra(Intent.EXTRA_TEXT, happyBean.getContent() + " " + happyBean.getUrl());
                                        intent.putExtra("share_info", happyBean);
                                        intent.putExtra("type", "happy");
                                        intent.putExtra("destination", "url");
                                        intent.setType("text/plain");
                                        startActivity(intent);
                                }
                        }
                });
                mHappyAdapter.setOnLoadMoreDataListener(new OnLoadMoreDataListener() {
                        @Override
                        public void onLoadMoreData() {
                                loadMoreData(currentPage);
                        }
                },display);
                display.setAdapter(mHappyAdapter);
                refresh.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                                onRefresh();
                        }
                }, 200);
        }

        @Override
        protected void updateView() {

        }

        @Override
        public void onUpdateHappyInfo(List<HappyBean> data) {
                currentPage++;
                this.data.addAll(data);
                mHappyAdapter.notifyDataSetChanged();
        }






        @Override
        public void onRefresh() {
                data.clear();
                currentPage = 1;
                loadMoreData(currentPage);
        }


        @Override
        public void onDestroyView() {
                super.onDestroyView();
                mHappyPresenter.onDestroy();
        }
}
