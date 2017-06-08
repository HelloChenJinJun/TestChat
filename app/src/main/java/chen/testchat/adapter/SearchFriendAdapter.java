package chen.testchat.adapter;

import org.pointstone.cugappplat.baseadapter.BaseWrappedAdapter;
import org.pointstone.cugappplat.baseadapter.BaseWrappedViewHolder;

import java.util.List;

import chen.testchat.R;
import chen.testchat.bean.User;
import chen.testchat.util.LogUtil;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/3/25      23:54
 * QQ:             1981367757
 */

public class SearchFriendAdapter extends BaseWrappedAdapter<User, BaseWrappedViewHolder> {
        public SearchFriendAdapter(List<User> data, int layoutId) {
                super(data, layoutId);
        }

        @Override
        protected void convert(BaseWrappedViewHolder holder, User data) {
                LogUtil.e("convert转换");
                holder.setImageUrl(R.id.iv_search_friend_item_avatar, data.getAvatar())
                        .setText(R.id.tv_search_friend_item_name, data.getNick())
                        .setOnClickListener(R.id.btn_search_friend_item_look);
        }
}
