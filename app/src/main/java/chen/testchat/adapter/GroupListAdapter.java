package chen.testchat.adapter;

import org.pointstone.cugappplat.baseadapter.BaseWrappedAdapter;
import org.pointstone.cugappplat.baseadapter.BaseWrappedViewHolder;

import java.util.List;

import chen.testchat.R;
import chen.testchat.bean.GroupTableMessage;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/10      10:09
 * QQ:             1981367757
 */
public class GroupListAdapter extends BaseWrappedAdapter<GroupTableMessage, BaseWrappedViewHolder> {

        public GroupListAdapter(List<GroupTableMessage> data, int layoutId) {
                super(data, layoutId);
        }

        @Override
        protected void convert(BaseWrappedViewHolder holder, GroupTableMessage data) {
                holder.setImageUrl(R.id.riv_group_list_item_avatar, data.getGroupAvatar())
                        .setText(R.id.tv_group_list_item_nick, data.getGroupName());
        }
}
