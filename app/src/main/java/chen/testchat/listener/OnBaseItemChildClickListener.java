package chen.testchat.listener;

import android.view.View;

import org.pointstone.cugappplat.baseadapter.BaseWrappedViewHolder;
import org.pointstone.cugappplat.baseadapter.baselistener.BaseItemClickListener;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/5/23      9:45
 * QQ:             1981367757
 */

public  abstract class OnBaseItemChildClickListener extends BaseItemClickListener {
        @Override
        protected void onItemLongClick(BaseWrappedViewHolder baseWrappedViewHolder, View view, int position) {

        }

        @Override
        protected void onItemChildLongClick(BaseWrappedViewHolder baseWrappedViewHolder, int id, View view, int position) {

        }



        @Override
        protected void onItemClick(BaseWrappedViewHolder baseWrappedViewHolder, int id, View view, int position) {

        }
}
