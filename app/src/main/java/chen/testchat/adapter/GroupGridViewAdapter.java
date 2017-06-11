package chen.testchat.adapter;

import org.pointstone.cugappplat.baseadapter.BaseWrappedAdapter;
import org.pointstone.cugappplat.baseadapter.BaseWrappedViewHolder;

import java.util.List;

import chen.testchat.R;
import chen.testchat.bean.GroupNumberInfo;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/1      16:52
 * QQ:             1981367757
 */
public class GroupGridViewAdapter extends BaseWrappedAdapter<GroupNumberInfo, BaseWrappedViewHolder> {



        public GroupGridViewAdapter(List<GroupNumberInfo> data, int layoutId) {
                super(data, layoutId);
        }


        @Override
        public void addData(List<GroupNumberInfo> newData) {
                super.addData(newData);
        }

        @Override
        protected void convert(BaseWrappedViewHolder holder, GroupNumberInfo data) {
                holder.setText(R.id.tv_group_info_avatar_item_layout_name, data.getGroupNick())
                        .setOnClickListener(R.id.riv_group_info_avatar_item_layout_avatar);
                if (data.getUser() != null) {
                        holder.setImageUrl(R.id.riv_group_info_avatar_item_layout_avatar, data.getUser().getAvatar());
                }
        }
}
