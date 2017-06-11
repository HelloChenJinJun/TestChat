package org.pointstone.cugappplat.baseadapter.baseloadview;


import org.common.R;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/3/25      20:54
 * QQ:             1981367757
 */

public class SimpleLoadMoreView extends LoadMoreView {
        @Override
        public int getLayoutId() {
                return R.layout.base_load_view_layout;
        }

        @Override
        public int getLoadingLayoutId() {
                return R.id.ll_base_load_view_loading;
        }

        @Override
        public int getFailedLayoutId() {
                return R.id.fl_base_load_view_failed;
        }

        @Override
        public int getEndLayoutId() {
                return R.id.fl_base_load_view_end;
        }


//        private TextView mTextView;
//
//        public void setText(String message, Context context) {
//                if (getCurrentLoadingStatus() == STATUS_END) {
//                        if (mTextView == null) {
//                                mTextView = ((TextView) LayoutInflater.from(context).inflate(getEndLayoutId(), null).findViewById(R.id.tv_base_load_view_end_content));
//                        }
//                        mTextView.setText(message);
//                }
//        }
}
