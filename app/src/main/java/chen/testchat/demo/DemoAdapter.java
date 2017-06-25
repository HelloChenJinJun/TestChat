package chen.testchat.demo;

import android.util.SparseArray;

import org.pointstone.cugappplat.baseadapter.BaseMultipleWrappedAdapter;
import org.pointstone.cugappplat.baseadapter.BaseWrappedViewHolder;

import java.util.List;

import chen.testchat.R;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/5/25      17:05
 * QQ:             1981367757
 */

public class DemoAdapter extends BaseMultipleWrappedAdapter<Demo, BaseWrappedViewHolder> {
        public DemoAdapter(List<Demo> data, int layoutId) {
                super(data, layoutId);
        }

        @Override
        protected SparseArray<Integer> getLayoutIdMap() {
                SparseArray<Integer> result = new SparseArray<>();
                result.put(Demo.TYPE_NORMAL, R.layout.demo_normal_layout);
                result.put(Demo.TYPE_SPECIAL, R.layout.demo_special_layout);
                return result;
        }

        @Override
        protected void convert(BaseWrappedViewHolder holder, Demo data) {
                int viewType = holder.getItemViewType();
                if (viewType == Demo.TYPE_SPECIAL) {
                        holder.setText(R.id.tv_special_content, data.getData());
                } else {
                        holder.setText(R.id.tv_normal_content, data.getData());
                }
        }
}
