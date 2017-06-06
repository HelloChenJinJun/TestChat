package chen.testchat.adapter;

import org.pointstone.cugappplat.baseadapter.BaseWrappedAdapter;
import org.pointstone.cugappplat.baseadapter.BaseWrappedViewHolder;

import java.util.List;

import chen.testchat.R;
import chen.testchat.bean.HappyBean;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/7      18:49
 * QQ:             1981367757
 */
public class HappyAdapter extends BaseWrappedAdapter<HappyBean, BaseWrappedViewHolder> {


        public HappyAdapter(List<HappyBean> data, int layoutId) {
                super(data, layoutId);
        }

        @Override
        protected void convert(BaseWrappedViewHolder holder, HappyBean data) {
                holder.setText(R.id.tv_fragment_happy_item_content, data.getContent())
                        .setText(R.id.tv_fragment_happy_item_time, data.getUpdatetime())
                        .setOnClickListener(R.id.iv_fragment_happy_item_picture)
                        .setOnClickListener(R.id.iv_fragment_happy_item_share)
                        .setImageUrl(R.id.iv_fragment_happy_item_picture, data.getUrl());
        }

}
