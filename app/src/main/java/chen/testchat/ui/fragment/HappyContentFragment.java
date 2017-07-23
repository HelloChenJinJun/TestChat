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
import chen.testchat.adapter.HappyContentAdapter;
import chen.testchat.bean.HappyContentBean;
import chen.testchat.listener.OnBaseItemChildClickListener;
import chen.testchat.mvp.HappyContentInfoTask.HappyContentContacts;
import chen.testchat.mvp.HappyContentInfoTask.HappyContentModel;
import chen.testchat.mvp.HappyContentInfoTask.HappyContentPresenter;
import chen.testchat.ui.EditShareMessageActivity;
import chen.testchat.view.ListViewDecoration;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/8      20:26
 * QQ:             1981367757
 */

public class HappyContentFragment extends org.pointstone.cugappplat.base.basemvp.BaseFragment implements HappyContentContacts.View, SwipeRefreshLayout.OnRefreshListener {
        private RecyclerView display;
        private HappyContentAdapter mHappyAdapter;
        private List<HappyContentBean> data = new ArrayList<>();
        private HappyContentPresenter mHappyPresenter;
        private int currentPage = 1;
        private HappyContentModel mHappyContentModel;




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
                SwipeRefreshLayout refresh = (SwipeRefreshLayout) findViewById(R.id.refresh_happy_fragment);
                display = (RecyclerView) findViewById(R.id.rcv_happy_fragment_display);
                refresh.setOnRefreshListener(this);
        }



        private void loadMoreData(int currentPage) {
                mHappyPresenter.getHappyContentInfo(currentPage);
        }

        @Override
        public void initData() {
                display.setLayoutManager( new LinearLayoutManager(getActivity()));
                display.addItemDecoration(new ListViewDecoration(getActivity()));
//                display.setHasFixedSize(true);
                display.setItemAnimator(new DefaultItemAnimator());
                mHappyPresenter = new HappyContentPresenter();
                mHappyContentModel=new HappyContentModel();
                mHappyPresenter.setViewAndModel(this,mHappyContentModel);
                mHappyAdapter = new HappyContentAdapter(data, R.layout.fragment_happy_content_item_layout);
                display.addOnItemTouchListener(new OnBaseItemChildClickListener() {
                        @Override
                        protected void onItemChildClick(BaseWrappedViewHolder baseWrappedViewHolder, int id, View view, int position) {
                                if (id == R.id.iv_fragment_happy_content_item_share) {
                                        HappyContentBean bean = mHappyAdapter.getData(position);
                                        Intent intent = new Intent(getActivity(), EditShareMessageActivity.class);
                                        intent.setAction(Intent.ACTION_SEND);
                                        intent.putExtra(Intent.EXTRA_TEXT, bean.getContent());
                                        intent.putExtra("share_info", bean);
                                        intent.putExtra("type", "happy_content");
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
        }

        @Override
        protected void updateView() {
                loadMoreData(currentPage);
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

        @Override
        public void onUpdateHappyContentInfo(List<HappyContentBean> data) {
                currentPage++;
                this.data.addAll(data);
                mHappyAdapter.notifyDataSetChanged();
        }


}
