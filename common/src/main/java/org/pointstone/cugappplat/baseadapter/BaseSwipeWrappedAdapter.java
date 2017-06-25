package org.pointstone.cugappplat.baseadapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.common.R;
import org.pointstone.cugappplat.base.cusotomview.swipeview.OnSwipeMenuItemClickListener;
import org.pointstone.cugappplat.base.cusotomview.swipeview.SwipeMenu;
import org.pointstone.cugappplat.base.cusotomview.swipeview.SwipeMenuCreator;
import org.pointstone.cugappplat.base.cusotomview.swipeview.SwipeMenuLayout;
import org.pointstone.cugappplat.base.cusotomview.swipeview.SwipeMenuRecyclerView;
import org.pointstone.cugappplat.base.cusotomview.swipeview.SwipeMenuView;
import org.pointstone.cugappplat.util.LogUtil;

import java.util.List;



/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/5/23      8:39
 * QQ:             1981367757
 */

public abstract class BaseSwipeWrappedAdapter<T, K extends BaseWrappedViewHolder> extends BaseWrappedAdapter<T, K> {

        /**
         * Swipe menu creator。
         */
        private SwipeMenuCreator mSwipeMenuCreator;

        /**
         * Swipe menu click listener。
         */
        private OnSwipeMenuItemClickListener mSwipeMenuItemClickListener;

        /**
         * Set to create menu listener.
         *
         * @param swipeMenuCreator listener.
         */
        public void setSwipeMenuCreator(SwipeMenuCreator swipeMenuCreator) {
                this.mSwipeMenuCreator = swipeMenuCreator;
        }

        /**
         * Set to click menu listener.
         *
         * @param swipeMenuItemClickListener listener.
         */
        public void setSwipeMenuItemClickListener(OnSwipeMenuItemClickListener swipeMenuItemClickListener) {
                this.mSwipeMenuItemClickListener = swipeMenuItemClickListener;
        }

        public BaseSwipeWrappedAdapter(List<T> data, int layoutId) {
                super(data, layoutId);
        }


        @Override
        protected void convert(K holder, T data) {
                if (holder.itemView instanceof SwipeMenuLayout) {
                        LogUtil.e("这里呢11SwipeMenuLayout");
                        SwipeMenuLayout swipeMenuLayout = (SwipeMenuLayout) holder.itemView;
                        int childCount = swipeMenuLayout.getChildCount();
                        for (int i = 0; i < childCount; i++) {
                                View childView = swipeMenuLayout.getChildAt(i);
                                if (childView instanceof SwipeMenuView) {
                                        ((SwipeMenuView) childView).bindAdapterViewHolder(holder);
                                }
                        }
                        convert(holder, data, true);
                } else {
                        convert(holder, data, false);
                }
        }


        protected abstract void convert(K holder, T data, boolean isSwipeItem);


        @Override
        public K onCreateViewHolder(ViewGroup parent, int viewType) {
                LogUtil.e("2222222111111");
                K viewHolder = super.onCreateViewHolder(parent, viewType);
                View contentView = viewHolder.itemView;

                if (mSwipeMenuCreator != null && (viewType != HEADER_VIEW && viewType != FOOTER_VIEW && viewType != EMPTY_VIEW)) {
                        LogUtil.e("这里有没有执行mSwipeMenuCreator");
                        SwipeMenuLayout swipeMenuLayout = (SwipeMenuLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.yanzhenjie_item_default, parent, false);
                        SwipeMenu swipeLeftMenu = new SwipeMenu(swipeMenuLayout, viewType);
                        SwipeMenu swipeRightMenu = new SwipeMenu(swipeMenuLayout, viewType);

                        mSwipeMenuCreator.onCreateMenu(swipeLeftMenu, swipeRightMenu, viewType);

                        int leftMenuCount = swipeLeftMenu.getMenuItems().size();
                        if (leftMenuCount > 0) {
                                SwipeMenuView swipeLeftMenuView = (SwipeMenuView) swipeMenuLayout.findViewById(R.id.swipe_left);
                                swipeLeftMenuView.setOrientation(swipeLeftMenu.getOrientation());
                                swipeLeftMenuView.bindMenu(swipeLeftMenu, SwipeMenuRecyclerView.LEFT_DIRECTION);
                                swipeLeftMenuView.bindMenuItemClickListener(mSwipeMenuItemClickListener, swipeMenuLayout);
                        }

                        int rightMenuCount = swipeRightMenu.getMenuItems().size();
                        if (rightMenuCount > 0) {
                                LogUtil.e("rightMenuCount");
                                SwipeMenuView swipeRightMenuView = (SwipeMenuView) swipeMenuLayout.findViewById(R.id.swipe_right);
                                swipeRightMenuView.setOrientation(swipeRightMenu.getOrientation());
                                swipeRightMenuView.bindMenu(swipeRightMenu, SwipeMenuRecyclerView.RIGHT_DIRECTION);
                                swipeRightMenuView.bindMenuItemClickListener(mSwipeMenuItemClickListener, swipeMenuLayout);
                        }

                        if (leftMenuCount > 0 || rightMenuCount > 0) {
                                LogUtil.e("大于0");
                                ViewGroup viewGroup = (ViewGroup) swipeMenuLayout.findViewById(R.id.swipe_content);
                                viewGroup.addView(contentView);
//                                contentView = swipeMenuLayout;
                                return createBaseViewHolder(swipeMenuLayout);
                        }
                }
                return viewHolder;
        }
}
