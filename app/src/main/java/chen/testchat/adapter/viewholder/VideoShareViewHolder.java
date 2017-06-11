package chen.testchat.adapter.viewholder;

import android.view.View;
import android.view.ViewStub;

import java.util.ArrayList;
import java.util.List;

import chen.testchat.R;
import chen.testchat.adapter.OnShareMessageItemClickListener;
import chen.testchat.bean.SharedMessage;
import chen.testchat.util.CommonUtils;
import chen.testchat.view.ListImageView;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/3/27      19:04
 * QQ:             1981367757
 */

public class VideoShareViewHolder extends BaseShareMessageViewHolder {
        private ListImageView display;

        public VideoShareViewHolder(View itemView) {
                super(itemView);
                ViewStub viewStub = (ViewStub) itemView.findViewById(R.id.vs_share_fragment_item_main);
                viewStub.setLayoutResource(R.layout.share_fragment_item_image_layout);
                display = (ListImageView) viewStub.inflate().findViewById(R.id.liv_share_fragment_item_main_image_display);
        }


        @Override
        public void initCommonData(final SharedMessage shareMessage, final OnShareMessageItemClickListener listener) {
                super.initCommonData(shareMessage, listener);

        }

        public void bindData(final SharedMessage sharedMessage, final OnShareMessageItemClickListener listener) {
                List<String> list = new ArrayList<>();
                String wrappedUrl = CommonUtils.list2Content(sharedMessage.getImageList());
                list.add(wrappedUrl);
                display.bindData(list);
                display.setOnImageViewItemClickListener(new ListImageView.OnImageViewItemClickListener() {
                        @Override
                        public void onImageClick(View view, int position, String url) {
                                if (listener != null) {
                                        listener.onPhotoItemClick(view, sharedMessage.getObjectId(), position, url);
                                }
                        }
                });
        }
}
