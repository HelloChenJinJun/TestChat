package chen.testchat.adapter.viewholder;

import android.view.View;
import android.view.ViewStub;

import chen.testchat.R;
import chen.testchat.adapter.OnShareMessageItemClickListener;
import chen.testchat.bean.SharedMessage;
import chen.testchat.util.PixelUtil;
import chen.testchat.view.ListImageView;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/3/27      18:59
 * QQ:             1981367757
 */

public class ImageShareViewHolder extends BaseShareMessageViewHolder {
        private ListImageView display;

        public  ImageShareViewHolder(View itemView) {
                super(itemView);
                ViewStub viewStub = (ViewStub) itemView.findViewById(R.id.vs_share_fragment_item_main);
                viewStub.setLayoutResource(R.layout.share_fragment_item_image_layout);
                display = (ListImageView) viewStub.inflate().findViewById(R.id.liv_share_fragment_item_main_image_display);
        }




        public void bindData(final SharedMessage shareMessage, final OnShareMessageItemClickListener listener) {
                display.setImagePadding(PixelUtil.todp(3));
                display.bindData(shareMessage.getImageList());
                display.setOnImageViewItemClickListener(new ListImageView.OnImageViewItemClickListener() {
                        @Override
                        public void onImageClick(View view, int position, String url) {
                                if (listener != null) {
                                        listener.onPhotoItemClick(view, shareMessage.getObjectId(), position, url);
                                }
                        }
                });

        }
}
