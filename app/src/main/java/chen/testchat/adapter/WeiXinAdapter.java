package chen.testchat.adapter;

import android.widget.TextView;

import org.pointstone.cugappplat.baseadapter.BaseWrappedAdapter;
import org.pointstone.cugappplat.baseadapter.BaseWrappedViewHolder;

import java.util.List;

import chen.testchat.R;
import chen.testchat.bean.WinXinBean;
import chen.testchat.db.ChatDB;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/6      23:24
 * QQ:             1981367757
 */
public class WeiXinAdapter extends BaseWrappedAdapter<WinXinBean, BaseWrappedViewHolder> {


        public WeiXinAdapter(List<WinXinBean> data, int layoutId) {
                super(data, layoutId);
        }

        @Override
        protected void convert(BaseWrappedViewHolder holder, WinXinBean data) {
                holder.setImageUrl(R.id.iv_win_xin_fragment_item_picture, data.getPicUrl())
                        .setText(R.id.tv_wei_xin_fragment_layout_title, data.getTitle())
                        .setText(R.id.tv_wei_xin_fragment_item_description, data.getDescription())
                        .setText(R.id.tv_wei_xin_fragment_item_time, data.getCtime())
                        .setOnClickListener(R.id.btn_wei_xin_fragment_right);
                if (ChatDB.create().getWeixinInfoReadStatus(data.getUrl()) == 1) {
                        ((TextView) holder.getView(R.id.tv_wei_xin_fragment_layout_title)).setTextColor(holder.getContext().getResources().getColor(R.color.base_color_text_grey));
                } else {
                        ((TextView) holder.getView(R.id.tv_wei_xin_fragment_layout_title)).setTextColor(holder.getContext().getResources().getColor(R.color.base_color_text_black));
                }
        }
}
