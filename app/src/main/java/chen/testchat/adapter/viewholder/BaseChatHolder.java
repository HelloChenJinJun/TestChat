package chen.testchat.adapter.viewholder;

import android.view.View;

import org.pointstone.cugappplat.baseadapter.BaseWrappedViewHolder;

import chen.testchat.adapter.ChatMessageAdapter;
import chen.testchat.bean.BaseMessage;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/5/24      18:14
 * QQ:             1981367757
 */

public abstract class BaseChatHolder extends BaseWrappedViewHolder {
        public BaseChatHolder(View itemView) {
                super(itemView);
        }

        public abstract void bindData(BaseMessage baseMessage, ChatMessageAdapter.OnItemClickListener listener,boolean isShowTime);
}
