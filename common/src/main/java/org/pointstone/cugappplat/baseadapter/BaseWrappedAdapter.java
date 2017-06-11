package org.pointstone.cugappplat.baseadapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import org.pointstone.cugappplat.baseadapter.baseloadview.LoadMoreView;
import org.pointstone.cugappplat.baseadapter.baseloadview.OnLoadMoreDataListener;
import org.pointstone.cugappplat.baseadapter.baseloadview.SimpleLoadMoreView;
import org.pointstone.cugappplat.util.LogUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/3/25      13:49
 * QQ:             1981367757
 */

public abstract class BaseWrappedAdapter<T, K extends BaseWrappedViewHolder> extends RecyclerView.Adapter<K> {
        public static final int HEADER_VIEW = 20;
        public static final int FOOTER_VIEW = 21;
        public static final int EMPTY_VIEW = 22;
        public static final int LOADING_VIEW = 23;
        protected List<T> data;
        private int layoutId;
        //        当展示空布局的时候是否展示头布局
//        当展示空布局的时候是否展示尾布局
        private boolean isShowEmptyAndHeader;
        private boolean isShowEmptyAndFooter;
        private LinearLayout headerLayout;
        private LinearLayout footerLayout;

        /**
         * 当滚动到距离底部的个数<=autoLoadItemCount, 就触发加载更多事件
         */
        private int autoLoadItemCount = 1;

        public void setAutoLoadItemCount(int autoLoadItemCount) {
                this.autoLoadItemCount = autoLoadItemCount;
        }


        public void setHeaderView(View view) {
                setHeaderView(view, 0);
        }


        public void setHeaderView(View view, int index) {
                setHeaderView(view, index, LinearLayout.VERTICAL);
        }


        public int setHeaderView(View view, int index, int orientation) {
                if (headerLayout != null && 0 <= index && index < headerLayout.getChildCount()) {
                        headerLayout.removeViewAt(index);
                        headerLayout.addView(view, index);
                        return index;
                }
                if (headerLayout == null) {
                        headerLayout = new LinearLayout(view.getContext());
                        if (orientation == LinearLayout.VERTICAL) {
                                headerLayout.setOrientation(LinearLayout.VERTICAL);
                                headerLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        } else {
                                headerLayout.setOrientation(LinearLayout.HORIZONTAL);
                                headerLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
                        }
                }
                int childCount = headerLayout.getChildCount();
//                判断是否越界
                if (index < 0 || index > childCount) {
                        index = childCount;
                }
                headerLayout.addView(view, index);
//                判断是否是第一次设置
                if (headerLayout.getChildCount() == 1) {
//                        获取该头部布局的位置,因为要考虑到空布局,当空布局和头部布局不能同时存在的时候，就返回-1
                        int position = getHeaderPosition();
                        if (position != -1) {
                                notifyItemInserted(position);
                        }
                }
                return index;
        }

        /**
         * 获取头部的位置
         *
         * @return
         */
        private int getHeaderPosition() {
                if (getEmptyViewCount() == 1 && !isShowEmptyAndHeader) {
                        return -1;
                } else {
                        return 0;
                }
        }


        public void setFooterView(View view) {
                setFooterView(view, 0);
        }

        public void addData(int position, List<T> newData) {
                if (mLoadMoreView.getCurrentLoadingStatus() == LoadMoreView.STATUS_LOADING) {
                        notifyLoadingFinished();
                }
                data.addAll(position, newData);
                notifyItemRangeInserted(position + getHeaderViewCount(), newData.size());
                LogUtil.e("position:::" + position);
                compatibilityDataSizeChanged(newData.size());
        }

        public void addData(T newData) {
                addData(data.size(), newData);
        }


        public void addData(int position, T newData) {
                data.add(position, newData);
                notifyItemInserted(position + getHeaderViewCount());
                compatibilityDataSizeChanged(1);
        }

        public void addData(List<T> newData) {
                LogUtil.e("data.size()" + newData.size());
                addData(data.size(), newData);
        }

        private void compatibilityDataSizeChanged(int size) {
                int dataSize = data == null ? 0 : data.size();
                if (dataSize == size) {
                        LogUtil.e("兼容通知数据改变");
//                        证明是空布局插入数据过来的，所以这里要通知空布局的改变和加载布局的改变
                        notifyDataSetChanged();
//                        LogUtil.e("再通知改变");
//                        notifyDataSetChanged();
                }
        }


        public void setFooterView(View view, int index) {
                setFooterView(view, index, LinearLayout.VERTICAL);
        }


        public int setFooterView(View view, int index, int orientation) {
                if (footerLayout != null && 0 <= index && index < footerLayout.getChildCount()) {
                        footerLayout.removeViewAt(index);
                        footerLayout.addView(view, index);
                        return index;
                }
                if (footerLayout == null) {
                        footerLayout = new LinearLayout(view.getContext());
                        if (orientation == LinearLayout.VERTICAL) {
                                footerLayout.setOrientation(LinearLayout.VERTICAL);
                                footerLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        } else {
                                footerLayout.setOrientation(LinearLayout.HORIZONTAL);
                                footerLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
                        }
                }
                int childCount = footerLayout.getChildCount();
//                判断是否越界
                if (index < 0 || index > childCount) {
                        index = childCount;
                }
                footerLayout.addView(view, index);
//                判断是否是第一次设置
                if (footerLayout.getChildCount() == 1) {
                        int position = getFooterPosition();
                        if (position != -1) {
                                notifyItemInserted(position);
                        }
                }
                return index;
        }

        /**
         * 获取尾部的位置
         *
         * @return
         */
        private int getFooterPosition() {
                if (getEmptyViewCount() == 1) {
                        int position = 1;
                        if (isShowEmptyAndHeader) {
                                position++;
                        }
                        if (isShowEmptyAndFooter) {
                                return position;
                        }
                } else {
                        return getHeaderViewCount() + data.size();
                }
                return -1;
        }


        public BaseWrappedAdapter(List<T> data, int layoutId) {
                this.data = data == null ? new ArrayList<T>() : data;
                if (layoutId != 0)
                        this.layoutId = layoutId;
        }


        protected LayoutInflater mLayoutInflater;

        @Override
        public K onCreateViewHolder(ViewGroup parent, int viewType) {
                LogUtil.e("onCreateViewHolder");
                if (mLayoutInflater == null) {
                        mLayoutInflater = LayoutInflater.from(parent.getContext());
                }
                return createBaseViewHolder(getLayoutFromViewType(parent, viewType));
        }

        protected K createBaseViewHolder(View view) {
                LogUtil.e("createBaseViewHolder");
                Class adapterClass = getClass();
                Class resultClass = null;
                while (resultClass == null && adapterClass != null) {
                        resultClass = getInstancedGenericKClass(adapterClass);
                        adapterClass = adapterClass.getSuperclass();
                }
                K k = createGenericKInstance(resultClass, view);
                return k != null ? k : (K) new BaseWrappedViewHolder(view);
        }

        private K createGenericKInstance(Class aClass, View view) {
                try {
//                构造函数
                        Constructor constructor;
//                获取修饰符
                        String modifier = Modifier.toString(aClass.getModifiers());
                        String name = aClass.getName();

//                内部类并且不是静态类
                        if (name.contains("$") && !modifier.contains("static")) {
                                constructor = aClass.getDeclaredConstructor(getClass(), View.class);
                                return (K) constructor.newInstance(this, view);
                        }
                        constructor = aClass.getDeclaredConstructor(View.class);
                        return (K) constructor.newInstance(view);
                } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                } catch (IllegalAccessException e) {
                        e.printStackTrace();
                } catch (InstantiationException e) {
                        e.printStackTrace();
                } catch (InvocationTargetException e) {
                        e.printStackTrace();
                }
                return null;
        }

        private Class getInstancedGenericKClass(Class adapterClass) {
                Type type = adapterClass.getGenericSuperclass();
                if (type instanceof ParameterizedType) {
                        Type[] types = ((ParameterizedType) type).getActualTypeArguments();
                        for (Type type1 :
                                types) {
                                if (type1 instanceof Class) {
                                        Class class1 = (Class) type1;
                                        if (BaseWrappedViewHolder.class.isAssignableFrom(class1)) {
                                                return class1;
                                        }
                                }
                        }
                }
                return null;
        }

        private View getLayoutFromViewType(ViewGroup parent, int viewType) {
                switch (viewType) {
                        case LOADING_VIEW:
                                return mLayoutInflater.inflate(mLoadMoreView.getLayoutId(), parent, false);
                        case EMPTY_VIEW:
                                return emptyLayout;
                        case HEADER_VIEW:
                                return headerLayout;
                        case FOOTER_VIEW:
                                return footerLayout;
                        default:
                                return mLayoutInflater.inflate(layoutId, parent, false);
                }
        }

        @Override
        public void onBindViewHolder(K holder, int position) {
                int viewType = getItemViewType(position);
                LogUtil.e("viewType:" + viewType);
                switch (viewType) {
                        case 0:
                                convert(holder, data.get(position - getHeaderViewCount()));
                                break;
                        case LOADING_VIEW:
                                LogUtil.e("绑定加载view");
                                mLoadMoreView.convert(holder);
                                break;
                        case HEADER_VIEW:

                                break;
                        case FOOTER_VIEW:
                                break;
                        case EMPTY_VIEW:
                                break;
                        default:
                                convert(holder, data.get(position - getHeaderViewCount()));
                }
        }


        protected abstract void convert(K holder, T data);


        @Override
        public int getItemViewType(int position) {
                LogUtil.e("getItemViewType");
//                首先判断两种状态，空布局和非空布局
                if (getEmptyViewCount() == 1) {
//                        空布局的情况下，判断是否存在HeaderView或FooterView
                        boolean hasHeader = isShowEmptyAndHeader && getHeaderViewCount() == 1;
                        switch (position) {
                                case 0:
                                        if (hasHeader) {
                                                return HEADER_VIEW;
                                        } else {
                                                return EMPTY_VIEW;
                                        }
                                case 1:
                                        if (hasHeader) {
                                                return EMPTY_VIEW;
                                        } else {
                                                return FOOTER_VIEW;
                                        }
//                                        当存在三个item的时候，就可以判断出isShowEmptyFooter为true
                                case 2:
                                        return FOOTER_VIEW;
                                default:
                                        return HEADER_VIEW;
                        }
                } else {
//                        这里触发加载更多
                        onLoadMoreData(position);
                        //                                非空布局的情况下,四种情况，头布据，尾布局，更过加载布局，默认布局
                        if (position < getHeaderViewCount()) {
//                                        证明有头部布局
                                LogUtil.e("证明有头部布局");
                                return HEADER_VIEW;
                        } else {
                                int realPosition = position - getHeaderViewCount();
                                if (realPosition < data.size()) {
                                        return getDefaultItemViewType(realPosition);
                                } else {
                                        int realPosition1 = realPosition - data.size();
                                        if (realPosition1 < getFooterViewCount()) {
                                                LogUtil.e("有底部布局");
                                                return FOOTER_VIEW;
                                        } else {
                                                LogUtil.e("有加载布局");
                                                return LOADING_VIEW;
                                        }
                                }
                        }
                }
        }

        /**
         * 子类可以重写
         *
         * @param position
         * @return
         */
        protected int getDefaultItemViewType(int position) {
                return 0;
        }

        private void onLoadMoreData(int position) {
                if (getMoreViewCount() == 1 && position >= getItemCount() - autoLoadItemCount) {
                        if (mLoadMoreView.getCurrentLoadingStatus() == LoadMoreView.STATUS_DEFAULT) {
                                mLoadMoreView.setCurrentLoadingStatus(LoadMoreView.STATUS_LOADING);
                                if (mOnLoadMoreDataListener != null) {
                                        if (mRecyclerView != null) {
//                                                这里是为了防止crash
                                                mRecyclerView.post(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                                LogUtil.e("adapter加载数据");
                                                                mOnLoadMoreDataListener.onLoadMoreData();
                                                        }
                                                });
                                        } else {
                                                mOnLoadMoreDataListener.onLoadMoreData();
                                        }
                                }
                        }
                }
        }


        public void notifyLoadingEnd() {
                LogUtil.e("abc");
                if (mRecyclerView != null) {
                        mRecyclerView.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                        mLoadMoreView.setCurrentLoadingStatus(LoadMoreView.STATUS_DEFAULT);
                                        setEnableLoadMore(false);
                                }
                        }, 1000);
                } else {
                        setEnableLoadMore(false);
                }
//                mLoadMoreView.setCurrentLoadingStatus(LoadMoreView.STATUS_END);
        }


        public void notifyLoadingFailed() {
                LogUtil.e("abc");
                if (mRecyclerView != null) {
                        mRecyclerView.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                        setEnableLoadMore(false);
                                }
                        }, 1000);
                } else {
                        setEnableLoadMore(false);
                }
        }


        public void notifyLoadingFinished() {
                if (mRecyclerView != null) {
                        mRecyclerView.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                        mLoadMoreView.setCurrentLoadingStatus(LoadMoreView.STATUS_END);
                                        LogUtil.e("通知完成");
                                        notifyItemChanged(getItemCount() - 1);
                                }
                        }, 1000);
                }
        }


        @Override
        public int getItemCount() {
//                这里需要判断两种情况，一种为空布局，另一种非空布局
                int count;
                if (getEmptyViewCount() != 0) {
                        count = 1;
                        if (isShowEmptyAndHeader && getHeaderViewCount() == 1) {
                                count++;
                        }
                        if (isShowEmptyAndFooter && getFooterViewCount() == 1) {
                                count++;
                        }
                } else {
//                        总共个数为item个数和头部和尾部个数和更多加载view个数
                        count = data.size() + getHeaderViewCount() + getFooterViewCount() + getMoreViewCount();
                }
                LogUtil.e("getItemCount" + count);
                return count;
        }


        private LoadMoreView mLoadMoreView = new SimpleLoadMoreView();
        private boolean enableLoadMore = false;
        private OnLoadMoreDataListener mOnLoadMoreDataListener;
        private RecyclerView mRecyclerView;


        /**
         * 设置加载viewenable
         * false表示使正在加载的view移除
         * true表示添加一个加载的view
         *
         * @param enableLoadMore
         */
        public void setEnableLoadMore(boolean enableLoadMore) {
                int oldLoadMoreCount = getMoreViewCount();
                this.enableLoadMore = enableLoadMore;
                int newLoadMoreCount = getMoreViewCount();
                if (oldLoadMoreCount == 1 && newLoadMoreCount == 0) {
                        notifyItemRemoved(getHeaderViewCount() + data.size() + getFooterViewCount());
                } else if (oldLoadMoreCount == 0 && newLoadMoreCount == 1) {
                        notifyItemInserted(getHeaderViewCount() + data.size() + getFooterViewCount());
                }
        }

        /**
         * 设置加载更多监听
         * 添加了一个recyclerView参数是为了能够通过post方法提交加载更多方法,不容易crash
         *
         * @param onLoadMoreDataListener
         * @param recyclerView
         */
        public void setOnLoadMoreDataListener(OnLoadMoreDataListener onLoadMoreDataListener, RecyclerView recyclerView) {
                setEnableLoadMore(true);
                mOnLoadMoreDataListener = onLoadMoreDataListener;
                mRecyclerView = recyclerView;
                mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                processLoadMoreData();
                        }
                });
        }

        private void processLoadMoreData() {
                int lastVisiblePosition = 0;
                RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
                if (layoutManager instanceof GridLayoutManager) {
                        lastVisiblePosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                } else if (layoutManager instanceof LinearLayoutManager) {
                        lastVisiblePosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                } else {
                        int[] result = ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(null);
                        int max = result[0];
                        for (int i = 1; i < result.length; i++) {
                                if (result[i] > max) {
                                        max = result[i];
                                }
                        }
                        lastVisiblePosition = max;
                }
                if (data.size() > 0) {
                        LogUtil.e("底部加载数据大小大于0");
                        int size = data.size();
                        LogUtil.e("loadStatus" + mLoadMoreView.getCurrentLoadingStatus());
                        if (lastVisiblePosition + 2 >= size && mLoadMoreView.getCurrentLoadingStatus() != LoadMoreView.STATUS_LOADING) {
                                LogUtil.e("底部加载拉11");
                                if (mOnLoadMoreDataListener != null && !isLoadViewEnable()) {
                                        setEnableLoadMore(true);
                                        mOnLoadMoreDataListener.onLoadMoreData();
                                }
                        }
                }
        }

        private int getMoreViewCount() {
                if (mLoadMoreView == null || !enableLoadMore || mOnLoadMoreDataListener == null || data.size() == 0)
                        return 0;
                return 1;
        }

        private int getFooterViewCount() {
                return 0;
        }

        private FrameLayout emptyLayout;


        public void setEmptyView(View view) {
                boolean shouldInsert = false;
                if (emptyLayout == null) {
                        emptyLayout = new FrameLayout(view.getContext());
//                        与子view的布局一致
                        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        ViewGroup.LayoutParams childLayoutParams = view.getLayoutParams();
                        if (childLayoutParams != null) {
                                layoutParams.width = childLayoutParams.width;
                                layoutParams.height = childLayoutParams.height;
                        }
                        emptyLayout.setLayoutParams(layoutParams);
//                        第一次创建空布局，设置标志,便于通知改变
                        shouldInsert = true;
                }
                emptyLayout.removeAllViews();
                emptyLayout.addView(view);
                if (shouldInsert) {
                        int position = 0;
                        if (isShowEmptyAndHeader && getHeaderViewCount() == 1) {
                                position++;
                        }
                        notifyItemInserted(position);
                }
        }


        public void setEmptyView(Context context, int layoutId) {
                View emptyView = LayoutInflater.from(context).inflate(layoutId, null);
                setEmptyView(emptyView);
        }


        private int getEmptyViewCount() {
                if (emptyLayout == null || emptyLayout.getChildCount() == 0) {
                        return 0;
                }
                if (data.size() > 0) {
                        return 0;
                }
                return 1;
        }


        public void setShowEmptyAndFooter(boolean showEmptyAndFooter) {
                isShowEmptyAndFooter = showEmptyAndFooter;
        }

        public void setShowEmptyAndHeader(boolean showEmptyAndHeader) {
                isShowEmptyAndHeader = showEmptyAndHeader;
        }

        /**
         * 获取头部view的个数
         *
         * @return
         */
        public int getHeaderViewCount() {
                if (headerLayout == null || headerLayout.getChildCount() == 0)
                        return 0;
                return 1;
        }


        public void removeHeaderView(View view) {
                if (getHeaderViewCount() == 0) return;
                headerLayout.removeView(view);
//                移除后再判断
                if (headerLayout.getChildCount() == 0) {
//                        通知移除
                        int position = getHeaderPosition();
                        if (position != -1) {
                                notifyItemRemoved(position);
                        }
                }
        }

        public void removeFooterView(View view) {
                if (getFooterViewCount() == 0) return;
                footerLayout.removeView(view);
//                移除后再判断
                if (footerLayout.getChildCount() == 0) {
//                        通知移除
                        int position = getFooterPosition();
                        if (position != -1) {
                                notifyItemRemoved(position);
                        }
                }
        }


        public void clearData() {
                data.clear();
        }

        public T getData(int position) {
//                int realPosition = position - getHeaderViewCount();
//                LogUtil.e("获取数据的位置" + position);
                if (position > -1 && position < data.size()) {
                        return data.get(position);
                }
                return null;
        }

        public List<T> getAllData() {
                return data;
        }

        public void removeData(int position) {
                if (position>=0 && position < data.size()) {
                        data.remove(position);
                        notifyItemRemoved(position);
                }
        }

        public boolean isLoadViewEnable() {
                return enableLoadMore;
        }
}
