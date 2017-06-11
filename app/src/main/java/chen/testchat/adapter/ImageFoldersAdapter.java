package chen.testchat.adapter;

import org.pointstone.cugappplat.baseadapter.BaseWrappedAdapter;
import org.pointstone.cugappplat.baseadapter.BaseWrappedViewHolder;

import java.util.List;

import chen.testchat.R;
import chen.testchat.bean.ImageFolder;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/5/23      23:18
 * QQ:             1981367757
 */

public class ImageFoldersAdapter extends BaseWrappedAdapter<ImageFolder, BaseWrappedViewHolder> {

        private int currentSelectedPosition;


        public int getCurrentSelectedPosition() {
                return currentSelectedPosition;
        }

        public void setCurrentSelectedPosition(int currentSelectedPosition) {
                this.currentSelectedPosition = currentSelectedPosition;
        }

        public ImageFoldersAdapter(List<ImageFolder> data, int layoutId) {
                super(data, layoutId);
        }

        @Override
        protected void convert(BaseWrappedViewHolder holder, ImageFolder data) {
                if (getCurrentSelectedPosition() == holder.getAdapterPosition()) {
                        holder.setVisible(R.id.iv_image_folder_select, true);
                } else {
                        holder.setVisible(R.id.iv_image_folder_select, false);
                }
                holder.setText(R.id.tv_image_folder_name, data.getName())
                        .setImageUrl(R.id.iv_image_folder_cover, data.getDisplay().getPath())
                        .setText(R.id.tv_image_folder_count, data.getAllImages().size() + "");

        }
}
