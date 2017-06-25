package org.pointstone.cugappplat.baseadapter;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.pointstone.cugappplat.baseadapter.baseitem.MultipleItem;
import org.pointstone.cugappplat.util.LogUtil;

import java.util.List;


/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/3/26      13:19
 * QQ:             1981367757
 */

public abstract class BaseMultipleWrappedAdapter<T extends MultipleItem, K extends BaseWrappedViewHolder> extends BaseWrappedAdapter<T, K> {
        protected SparseArray<Integer> layoutIds;


        public  SparseArray<Integer>  getLayoutIds() {
                if (layoutIds == null) {
                        layoutIds = getLayoutIdMap();
                }
                return layoutIds;
        }

       public BaseMultipleWrappedAdapter(List<T> data, int layoutId) {
                super(data, layoutId);
        }

        @Override
        protected int getDefaultItemViewType(int position) {
                if (layoutIds == null) {
                        layoutIds = getLayoutIdMap();
                }
                if (data.get(position) != null) {
                        return data.get(position).getItemViewType();
                }
                return super.getDefaultItemViewType(position);
        }


        protected abstract SparseArray<Integer> getLayoutIdMap();


        @Override
        public K onCreateViewHolder(ViewGroup parent, int viewType) {
                if (layoutIds == null) {
                        LogUtil.e("执行一次为空111122221111");
                        layoutIds = getLayoutIdMap();
                }
                /**
                 * 这里说明一下，当创建适配器的时候如果传入的默认布局为0，那么调用super.onCreateViewHolder(parent, viewType)
                 * 会出现加载不到布局，所以在多布局的情况下，会首先加载新添加的新item布局。
                 * 所以要确定新添加布局和默认布局不能同时为0
                 * */
                if (layoutIds.get(viewType) != null && layoutIds.get(viewType) != 0) {
                        return createBaseViewHolder(LayoutInflater.from(parent.getContext()).inflate(layoutIds.get(viewType), parent, false));
                }
                return super.onCreateViewHolder(parent, viewType);
        }
}
