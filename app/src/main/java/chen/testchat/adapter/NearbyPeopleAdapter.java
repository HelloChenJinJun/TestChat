package chen.testchat.adapter;

import org.pointstone.cugappplat.baseadapter.BaseWrappedAdapter;
import org.pointstone.cugappplat.baseadapter.BaseWrappedViewHolder;

import java.util.List;

import chen.testchat.R;
import chen.testchat.bean.User;
import chen.testchat.util.CommonUtils;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/11      12:48
 * QQ:             1981367757
 */
public class NearbyPeopleAdapter extends BaseWrappedAdapter<User, BaseWrappedViewHolder> {
        public NearbyPeopleAdapter(List<User> data, int layoutId) {
                super(data, layoutId);
        }

        @Override
        protected void convert(BaseWrappedViewHolder holder, User data) {
                holder.setImageUrl(R.id.riv_nearby_people_avatar, data.getAvatar())
                        .setText(R.id.tv_nearby_people_nick,data.getNick())
                        .setText(R.id.tv_nearby_people_distance, "距离" + CommonUtils.getDistance(data.getLocation().getLongitude(), data.getLocation().getLatitude()) + "米");
                if (data.getSignature() != null) {
                        holder.setVisible(R.id.tv_nearby_people_signature, true)
                                .setText(R.id.tv_nearby_people_signature, data.getSignature());
                } else {
                        holder.setVisible(R.id.tv_nearby_people_signature, false);
                }
        }
}
