package chen.testchat.ui;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.pointstone.cugappplat.base.cusotomview.ToolBarOption;

import chen.testchat.R;
import chen.testchat.bean.HappyBean;
import chen.testchat.bean.HappyContentBean;
import chen.testchat.manager.UserCacheManager;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/13      22:45
 * QQ:             1981367757
 */

public class HappyContentDisplayActivity extends SlideBaseActivity {
        private TextView content;
        private ImageView display;
        private HappyBean mHappyBean;


        @Override
        protected boolean isNeedHeadLayout() {
                return true;
        }

        @Override
        protected boolean isNeedEmptyLayout() {
                return false;
        }

        @Override
        protected int getContentLayout() {
                return R.layout.happy_content_layout;
        }

        @Override
        public void initView() {
                content = (TextView) findViewById(R.id.tv_happy_content_text_display);
                display = (ImageView) findViewById(R.id.iv_happy_content_image_display);
                display.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                ImageDisplayActivity.start(HappyContentDisplayActivity.this, display, mHappyBean.getUrl());
                        }
                });
        }


        @Override
        public void initData() {
                if (getIntent().getStringExtra("destination").equals("happy_content")) {
                        HappyContentBean happyContentBean = (HappyContentBean) getIntent().getSerializableExtra("share_info");
                        content.setText(happyContentBean.getContent());
                } else if (getIntent().getStringExtra("destination").equals("happy")) {
                        mHappyBean = (HappyBean) getIntent().getSerializableExtra("share_info");
                        content.setText(mHappyBean.getContent());
                        Glide.with(this).load(mHappyBean.getUrl())
                                .into(display);
                }
                initActionBar();
        }

        private void initActionBar() {
                ToolBarOption toolBarOption = new ToolBarOption();
                toolBarOption.setNeedNavigation(true);
                toolBarOption.setAvatar(UserCacheManager.getInstance().getUser().getAvatar());
                toolBarOption.setTitle("内容");
                setToolBar(toolBarOption);
        }
}
