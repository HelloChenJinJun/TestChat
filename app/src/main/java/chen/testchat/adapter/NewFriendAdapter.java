package chen.testchat.adapter;

import org.pointstone.cugappplat.baseadapter.BaseSwipeWrappedAdapter;
import org.pointstone.cugappplat.baseadapter.BaseWrappedViewHolder;

import java.util.List;

import chen.testchat.R;
import chen.testchat.base.Constant;
import chen.testchat.bean.InvitationMsg;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/10/15      20:30
 * QQ:             1981367757
 */

public class NewFriendAdapter extends BaseSwipeWrappedAdapter<InvitationMsg, BaseWrappedViewHolder> {
        public NewFriendAdapter(List<InvitationMsg> data, int layoutId) {
                super(data, layoutId);
        }

        @Override
        protected void convert(BaseWrappedViewHolder holder, InvitationMsg data, boolean isSwipeItem) {
                holder.setImageUrl(R.id.iv_new_friend_item_avatar, data.getAvatar())
                        .setText(R.id.tv_new_friend_item_name, data.getName());
                if (data.getReadStatus().equals(Constant.RECEIVE_UNREAD)) {
                        holder.setVisible(R.id.tv_new_friend_item_agree, false);
                        holder.setVisible(R.id.btn_new_friend_item_agree, true);
                } else if (data.getReadStatus().equals(Constant.READ_STATUS_READED)) {
                        holder.setVisible(R.id.btn_new_friend_item_agree, false);
                        holder.setVisible(R.id.tv_new_friend_item_agree, true);
                }
                holder.setOnClickListener(R.id.btn_new_friend_item_agree);
        }
}
