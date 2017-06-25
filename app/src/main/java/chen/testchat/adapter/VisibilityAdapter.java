package chen.testchat.adapter;

import org.pointstone.cugappplat.baseadapter.BaseSwipeWrappedAdapter;
import org.pointstone.cugappplat.baseadapter.BaseWrappedViewHolder;

import java.util.List;

import chen.testchat.R;
import chen.testchat.bean.User;
import chen.testchat.manager.UserCacheManager;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/3/4      21:06
 * QQ:             1981367757
 */
public class VisibilityAdapter extends BaseSwipeWrappedAdapter<String, BaseWrappedViewHolder> {
        public VisibilityAdapter(List<String> data, int layoutId) {
                super(data, layoutId);
        }

        @Override
        protected void convert(BaseWrappedViewHolder holder, String data, boolean isSwipeItem) {
                User user = UserCacheManager.getInstance().getUser(data);
                if (user != null) {
                        holder.setText(R.id.tv_edit_share_message_visibility_item_name, user.getNick())
                                .setText(R.id.tv_edit_share_message_visibility_item_signature, user.getSignature())
                                .setImageUrl(R.id.riv_edit_share_message_visibility_item_avatar, user.getAvatar());
                }
        }
}
