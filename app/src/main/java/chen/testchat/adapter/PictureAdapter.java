package chen.testchat.adapter;

import org.pointstone.cugappplat.baseadapter.BaseWrappedAdapter;
import org.pointstone.cugappplat.baseadapter.BaseWrappedViewHolder;

import java.util.List;

import chen.testchat.R;
import chen.testchat.bean.PictureBean;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/9      0:03
 * QQ:             1981367757
 */
public class PictureAdapter extends BaseWrappedAdapter<PictureBean, BaseWrappedViewHolder> {
        public PictureAdapter(List<PictureBean> data, int layoutId) {
                super(data, layoutId);
        }




        @Override
        protected void convert(BaseWrappedViewHolder holder, PictureBean data) {
                holder.setText(R.id.tv_fragment_picture_item_description, data.getDesc())
                        .setOnClickListener(R.id.iv_fragment_picture_item_picture)
                        .setOnClickListener(R.id.iv_fragment_picture_item_share)
                        .setImageUrl(R.id.iv_fragment_picture_item_picture, data.getUrl());
        }
}
