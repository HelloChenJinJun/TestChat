package chen.testchat.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;

import chen.testchat.R;
import chen.testchat.util.LogUtil;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/12/3      20:02
 * QQ:             1981367757
 */

public class MultiFuncRecyclerView extends FrameLayout {


        private RecyclerView display;
        private SwipeRefreshLayout mSwipeRefreshLayout;
        private ViewStub emptyStub;
        private ViewStub moreLoadStub;
        private View emptyView;
        private View moreLoadView;


        public RecyclerView getRecyclerView() {
                return display;
        }

        /**
         * 当滚动时，item剩余的个数小于或等于LEFT_LOAD_COUNT时，自动进行加载
         */
        private int LEFT_LOAD_COUNT = 5;


        private int mainLayoutId;
        private int moreLoadLayoutId;
        private int emptyLayoutId;
        private boolean isLoading = false;
        private RecyclerView.OnScrollListener mInnerScrollListener;

        public MultiFuncRecyclerView(Context context) {
                this(context, null);
        }

        public MultiFuncRecyclerView(Context context, AttributeSet attrs) {
                this(context, attrs, 0);
        }

        public MultiFuncRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
                super(context, attrs, defStyleAttr);
                initAttrs(attrs);
                initView();
        }

        private void initView() {
                View mainLayoutView = LayoutInflater.from(getContext()).inflate(mainLayoutId, this);
                mSwipeRefreshLayout = (SwipeRefreshLayout) mainLayoutView.findViewById(R.id.refresh_share_fragment_layout);
                emptyStub = (ViewStub) mainLayoutView.findViewById(R.id.vs_share_fragment_empty_layout);
                moreLoadStub = (ViewStub) mainLayoutView.findViewById(R.id.vs_share_fragment_more_load);
                if (emptyLayoutId != 0) {
                        emptyStub.setLayoutResource(emptyLayoutId);
                        emptyView = emptyStub.inflate();
                        emptyView.setVisibility(GONE);
                }
                if (moreLoadLayoutId != 0) {
                        moreLoadStub.setLayoutResource(moreLoadLayoutId);
                        moreLoadView = moreLoadStub.inflate();
                        moreLoadView.setVisibility(GONE);
                }
                display = (RecyclerView) mainLayoutView.findViewById(R.id.rcv_share_fragment_display);
                display.setClipToPadding(true);
                display.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                super.onScrolled(recyclerView, dx, dy);
                                processLoadMoreData();
                        }


                        @Override
                        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                                super.onScrollStateChanged(recyclerView, newState);
                                if (mInnerScrollListener != null) {
                                        mInnerScrollListener.onScrollStateChanged(recyclerView, newState);
                                }
                        }
                });
        }

        private void processLoadMoreData() {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) display.getLayoutManager();
                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                int totalCount = linearLayoutManager.getItemCount();
                int visibleCount = linearLayoutManager.getChildCount();
//                这里分两种情况，当缓慢滑动的时候，边滑动边加载，没有接触到底部。当如果快速滑动的时候，还没加载完就滑动到底部
                if ((firstVisibleItemPosition + visibleCount >= totalCount) && !isLoading && totalCount != 1) {
                        LogUtil.e("底部加载拉");
                        if (mOnMoreDataLoadListener != null) {
                                isLoading = true;
                                moreLoadView.setVisibility(VISIBLE);
                                mOnMoreDataLoadListener.onMoreDataLoad(totalCount, firstVisibleItemPosition);
                        }
                }
        }


        public void setOnTouchListener(OnTouchListener listener) {
                this.display.setOnTouchListener(listener);
        }


        public void setAdapter(final RecyclerView.Adapter adapter) {
                if (adapter != null) {
                        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                                @Override
                                public void onChanged() {
                                        LogUtil.e("数据改变了 ");
//                                        当数据插入完成后
                                        isLoading = false;
                                        if (moreLoadView.getVisibility() == VISIBLE) {
                                                moreLoadView.setVisibility(GONE);
                                        }
                                        if (adapter.getItemCount() == 0 && emptyView != null) {
                                                LogUtil.e("数据为空时，显示空view");
                                                emptyView.setVisibility(VISIBLE);
                                        } else if (emptyView != null) {
                                                emptyView.setVisibility(GONE);
                                        }
                                }
                        });
                        display.setAdapter(adapter);
                }
        }

        public SwipeRefreshLayout getSwipeRefreshLayout() {
                return mSwipeRefreshLayout;
        }

        public void setLayoutManager(RecyclerView.LayoutManager manager) {
                if (manager != null) {
                        display.setLayoutManager(manager);
                }
        }

        public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
                if (itemDecoration != null) {
                        display.addItemDecoration(itemDecoration);
                }
        }


        public void setHasFixedSize(boolean hasFixedSize) {
                display.setHasFixedSize(hasFixedSize);
        }

        public void setItemAnimator(RecyclerView.ItemAnimator itemAnimator) {
                if (itemAnimator != null) {
                        display.setItemAnimator(itemAnimator);
                }
        }


        public View getEmptyView() {
                return emptyView;
        }

        public View getMoreLoadView() {
                return moreLoadView;
        }

        private OnMoreDataLoadListener mOnMoreDataLoadListener;

        public void setOnMoreDataLoadListener(OnMoreDataLoadListener onMoreDataLoadListener) {
                mOnMoreDataLoadListener = onMoreDataLoadListener;
        }


        public void setLeftLoadCount(int leftLoadCount) {
                LEFT_LOAD_COUNT = leftLoadCount;
        }

        public void setRefreshing(boolean refresh) {
                if (mSwipeRefreshLayout != null) {
                        mSwipeRefreshLayout.setRefreshing(refresh);
                }
        }

        public void setRefreshListener(SwipeRefreshLayout.OnRefreshListener onRefreshListener) {
                if (onRefreshListener != null) {
                        mSwipeRefreshLayout.setOnRefreshListener(onRefreshListener);
                }
        }

        public void addOnScrollListener(RecyclerView.OnScrollListener onScrollListener) {
                mInnerScrollListener = onScrollListener;
        }

        public interface OnMoreDataLoadListener {
                void onMoreDataLoad(int totalCount, int lastVisiblePosition);
        }

        private void initAttrs(AttributeSet attrs) {
                TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.MultiFuncRecyclerView);
                mainLayoutId = typedArray.getResourceId(R.styleable.MultiFuncRecyclerView_mainLayout, R.layout.mulit_recyclerview_main_layout);
                emptyLayoutId = typedArray.getResourceId(R.styleable.MultiFuncRecyclerView_emptyLayout, 0);
                moreLoadLayoutId = typedArray.getResourceId(R.styleable.MultiFuncRecyclerView_moreLoadProgressLayout, 0);
                typedArray.recycle();
        }
}
