package chen.testchat.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import chen.testchat.R;
import chen.testchat.util.CommonUtils;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/12/6      10:53
 * QQ:             1981367757
 */

public class CommentBaseDialog extends Dialog implements View.OnClickListener {
        private boolean isDeleteVisible = true;
        private Context mContext;

        public CommentBaseDialog(Context context) {
                this(context, 0);
                this.mContext = context;
        }

        public CommentBaseDialog(Context context, int themeResId) {
                super(context, themeResId);

        }

        public boolean isDeleteVisible() {
                return isDeleteVisible;
        }

        public void setDeleteVisible(boolean deleteVisible) {
                isDeleteVisible = deleteVisible;
        }

        public interface OnCommentDialogItemClickListener {
                void onItemDeleteClick(View view);

                void onItemCopyClick(View view);
        }


        private OnCommentDialogItemClickListener mOnCommentDialogItemClickListener;

        public void setOnCommentDialogItemClickListener(OnCommentDialogItemClickListener onCommentDialogItemClickListener) {
                mOnCommentDialogItemClickListener = onCommentDialogItemClickListener;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.comment_dialog_layout);
                setCanceledOnTouchOutside(true);
                initLayoutParams();
                initView();
        }

        private void initView() {
                TextView copy = (TextView) findViewById(R.id.tv_comment_dialog_copy);
                TextView delete = (TextView) findViewById(R.id.tv_comment_dialog_delete);
                View view = findViewById(R.id.view_comment_dialog_divider);
                if (isDeleteVisible()) {
                        view.setVisibility(View.VISIBLE);
                        delete.setVisibility(View.VISIBLE);
                } else {
                        view.setVisibility(View.GONE);
                        delete.setVisibility(View.GONE);
                }
                copy.setOnClickListener(this);
                delete.setOnClickListener(this);
        }

        private void initLayoutParams() {
                DisplayMetrics displayMetrics = CommonUtils.getScreenPix(mContext);
                int screenWidth = displayMetrics.widthPixels;
                WindowManager.LayoutParams layoutParams;
                if (getWindow() != null) {
                        layoutParams = getWindow().getAttributes();
                        layoutParams.width = (int) (screenWidth * (0.65));
                        getWindow().setGravity(Gravity.CENTER);
                        getWindow().setAttributes(layoutParams);
                }

        }


        @Override
        public void onClick(View v) {
                switch (v.getId()) {
                        case R.id.tv_comment_dialog_delete:
                                if (mOnCommentDialogItemClickListener != null) {
                                        mOnCommentDialogItemClickListener.onItemDeleteClick(v);
                                }
                                break;
                        case R.id.tv_comment_dialog_copy:
                                if (mOnCommentDialogItemClickListener != null) {
                                        mOnCommentDialogItemClickListener.onItemCopyClick(v);
                                }
                                break;

                }
        }
}
