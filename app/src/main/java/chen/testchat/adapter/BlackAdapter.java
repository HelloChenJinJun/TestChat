package chen.testchat.adapter;

import org.pointstone.cugappplat.baseadapter.BaseWrappedAdapter;
import org.pointstone.cugappplat.baseadapter.BaseWrappedViewHolder;

import java.util.List;

import chen.testchat.R;
import chen.testchat.bean.User;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/5/24      0:17
 * QQ:             1981367757
 */

public class BlackAdapter extends BaseWrappedAdapter<User, BaseWrappedViewHolder> {
        public BlackAdapter(List<User> data, int layoutId) {
                super(data, layoutId);
        }

        @Override
        protected void convert(BaseWrappedViewHolder holder, User data) {
                holder.setText(R.id.tv_black_list_item_nick, data.getNick())
                        .setImageUrl(R.id.riv_black_list_item_avatar, data.getAvatar());
        }
}
