package chen.testchat.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/12/31      17:36
 * QQ:             1981367757
 */

public class CustomGridView extends GridView {
        public CustomGridView(Context context) {
                this(context, null);
        }

        public CustomGridView(Context context, AttributeSet attrs) {
                this(context, attrs, 0);
        }

        public CustomGridView(Context context, AttributeSet attrs, int defStyleAttr) {
                super(context, attrs, defStyleAttr);
        }


        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                int expandedHeight = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
                super.onMeasure(widthMeasureSpec, expandedHeight);
        }
}
