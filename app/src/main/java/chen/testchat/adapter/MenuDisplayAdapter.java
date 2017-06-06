package chen.testchat.adapter;

import android.widget.TextView;

import org.pointstone.cugappplat.baseadapter.BaseWrappedAdapter;
import org.pointstone.cugappplat.baseadapter.BaseWrappedViewHolder;

import java.util.List;

import chen.testchat.R;
import chen.testchat.db.ChatDB;
import chen.testchat.util.LogUtil;
import chen.testchat.util.PixelUtil;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/11/5      15:27
 * QQ:             1981367757
 */
public class MenuDisplayAdapter extends BaseWrappedAdapter<String, BaseWrappedViewHolder> {

        public MenuDisplayAdapter(List<String> data, int layoutId) {
                super(data, layoutId);
        }

        @Override
        protected void convert(BaseWrappedViewHolder holder, String data) {
                holder.setText(R.id.tv_menu_item, data);
                int position = holder.getAdapterPosition();
                LogUtil.e("position22" + position);
                int size;
                if (position == 0) {
                        ((TextView) holder.getView(R.id.tv_menu_item)).setCompoundDrawablesWithIntrinsicBounds(holder.itemView.getContext().getResources().getDrawable(R.drawable.ic_chat_blue_grey_900_24dp), null, null, null);
                        if ((size = ChatDB.create().hasUnReadChatMessage()) > 0) {
                                holder.setVisible(R.id.tv_menu_item_tips, true)
                                        .setText(R.id.tv_menu_item_tips, size + "");
                        } else {
                                holder.setVisible(R.id.tv_menu_item_tips, false);
                        }

                } else if (position == 1) {
                        ((TextView) holder.getView(R.id.tv_menu_item)).setCompoundDrawablesWithIntrinsicBounds(holder.itemView.getContext().getResources().getDrawable(R.drawable.ic_people_blue_grey_900_24dp), null, null, null);
                } else if (position == 2) {
                        if ((size = ChatDB.create().hasUnReadInvitation()) > 0) {
                                holder.setVisible(R.id.tv_menu_item_tips, true)
                                        .setText(R.id.tv_menu_item_tips, size + "");
                        } else {
                                holder.setVisible(R.id.tv_menu_item_tips, false);
                        }
                        ((TextView) holder.getView(R.id.tv_menu_item)).setCompoundDrawablesWithIntrinsicBounds(holder.itemView.getContext().getResources().getDrawable(R.drawable.ic_insert_invitation_blue_grey_900_24dp), null, null, null);
                } else if (position == 3) {
                        ((TextView) holder.getView(R.id.tv_menu_item)).setCompoundDrawablesWithIntrinsicBounds(holder.itemView.getContext().getResources().getDrawable(R.drawable.ic_fiber_new_blue_grey_900_24dp), null, null, null);
                }
                ((TextView) holder.getView(R.id.tv_menu_item)).setCompoundDrawablePadding(PixelUtil.todp(10));
        }
}
