package chen.testchat.adapter;

import org.pointstone.cugappplat.baseadapter.BaseWrappedAdapter;
import org.pointstone.cugappplat.baseadapter.BaseWrappedViewHolder;

import java.util.List;

import chen.testchat.R;
import chen.testchat.bean.HappyContentBean;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/8      20:27
 * QQ:             1981367757
 */
public class HappyContentAdapter extends BaseWrappedAdapter<HappyContentBean, BaseWrappedViewHolder> {



        public HappyContentAdapter(List<HappyContentBean> data, int layoutId) {
                super(data, layoutId);
        }

        @Override
        protected void convert(BaseWrappedViewHolder holder, HappyContentBean data) {
                holder.setOnClickListener(R.id.iv_fragment_happy_content_item_share)
                .setText(R.id.tv_fragment_happy_content_item_content, data.getContent())
                .setText(R.id.tv_fragment_happy_content_item_time, data.getUpdatetime());
        }
}
