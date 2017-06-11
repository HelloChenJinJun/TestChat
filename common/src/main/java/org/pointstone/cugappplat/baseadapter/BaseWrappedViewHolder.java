package org.pointstone.cugappplat.baseadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import org.pointstone.cugappplat.util.LogUtil;

import java.io.File;
import java.util.HashSet;
import java.util.Set;


/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/3/25      14:38
 * QQ:             1981367757
 */

public class BaseWrappedViewHolder extends RecyclerView.ViewHolder {
        private Set<Integer> mClickableItemIds;
        private Set<Integer> mNestIds;
        private Set<Integer> mLongClickableItemIds;
        private SparseArray<View> views;
        public View itemView;

        public BaseWrappedViewHolder(View itemView) {
                super(itemView);
                this.itemView = itemView;
                mClickableItemIds = new HashSet<>();
                mNestIds = new HashSet<>();
                mLongClickableItemIds = new HashSet<>();
                views = new SparseArray<>();
        }


        public Context getContext() {
                return itemView.getContext();
        }


        public Set<Integer> getClickableItemIds() {
                return mClickableItemIds;
        }

        public Set<Integer> getNestIds() {
                return mNestIds;
        }

        public Set<Integer> getLongClickableItemIds() {
                return mLongClickableItemIds;
        }

        public BaseWrappedViewHolder setVisible(int layoutId, boolean isVisible) {
                getView(layoutId).setVisibility(isVisible ? View.VISIBLE : View.GONE);
                return this;
        }


        /**
         * 这个设置不是直接给view主体设置事件监听，如果是自定义的ViewGroup,请使用
         * 针对view主体注册监听事件
         *
         * @param id id
         * @return
         */
        public BaseWrappedViewHolder setOnClickListener(int id) {
                mClickableItemIds.add(id);
                return this;
        }


        /**
         * 直接给view主题设置事件监听
         *
         * @param id
         * @param onClickListener
         * @return
         */
        public BaseWrappedViewHolder setOnClickListener(int id, View.OnClickListener onClickListener) {
                getView(id).setOnClickListener(onClickListener);
                return this;
        }

        public View getView(int id) {
                View view = views.get(id);
                if (view == null) {
                        view = itemView.findViewById(id);
                        views.put(id, view);
                }
                return view;
        }

        public BaseWrappedViewHolder setText(int id, CharSequence content) {
                ((TextView) getView(id)).setText(content);
                return this;
        }

        public BaseWrappedViewHolder setImageUrl(int id, String url) {
                if (getView(id) instanceof ImageView) {
                        Glide.with(itemView.getContext()).load(url).into((ImageView) getView(id));
                }

                return this;
        }


        public BaseWrappedViewHolder setImageUrl(int id, File file) {
                if (file.exists()) {
                        Glide.with(itemView.getContext()).load(file).into((ImageView) getView(id));
                }
                return this;
        }

        public BaseWrappedViewHolder setImageResource(int id, int resId) {
                if (getView(id) instanceof ImageView) {
                        ((ImageView) getView(id)).setImageResource(resId);
                }
                return this;
        }

        public BaseWrappedViewHolder setImageBg(final int id, String url) {
                if (getView(id) instanceof ImageView) {
                        Glide.with(itemView.getContext()).load(url).into(new SimpleTarget<GlideDrawable>() {
                                @Override
                                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                                        LogUtil.e("设置背景");
                                        getView(id).setBackground(resource);
                                }
                        });
                }
                return this;
        }
}
