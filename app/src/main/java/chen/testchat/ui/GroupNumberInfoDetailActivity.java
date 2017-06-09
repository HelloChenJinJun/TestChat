package chen.testchat.ui;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.pointstone.cugappplat.base.cusotomview.RoundAngleImageView;
import org.pointstone.cugappplat.base.cusotomview.ToolBarOption;
import org.pointstone.cugappplat.util.ToastUtils;

import chen.testchat.R;
import chen.testchat.bean.GroupNumberInfo;
import chen.testchat.manager.MsgManager;
import chen.testchat.manager.UserManager;
import chen.testchat.util.CommonUtils;
import chen.testchat.util.LogUtil;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/1      23:32
 * QQ:             1981367757
 */
public class GroupNumberInfoDetailActivity extends SlideBaseActivity implements View.OnClickListener {
        private RoundAngleImageView avatar;
        private TextView name;
        private TextView type;
        private TextView groupNick;
        private Button exit;
        private String groupId;
        private GroupNumberInfo mGroupNumberInfo;


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
                return R.layout.activity_group_number_info_detail;
        }


        @Override
        public void initView() {
                avatar = (RoundAngleImageView) findViewById(R.id.riv_group_number_info_detail_avatar);
                name = (TextView) findViewById(R.id.tv_group_number_info_detail_nick);
                LinearLayout nickLayout = (LinearLayout) findViewById(R.id.ll_group_number_info_detail_nick);
                ((TextView) nickLayout.findViewById(R.id.tv_group_info_item_layout_title)).setText("群昵称");
                groupNick = (TextView) nickLayout.findViewById(R.id.tv_group_info_item_layout_value);
                LinearLayout typeLayout = (LinearLayout) findViewById(R.id.ll_group_number_info_detail_type);
                ((TextView) typeLayout.findViewById(R.id.tv_group_info_item_layout_title)).setText("身份");
                type = (TextView) typeLayout.findViewById(R.id.tv_group_info_item_layout_value);
                exit = (Button) findViewById(R.id.btn_group_number_info_detail_exit);
                exit.setOnClickListener(this);
        }


        @Override
        public void initData() {
                mGroupNumberInfo = (GroupNumberInfo) getIntent().getSerializableExtra("groupNumberInfo");
                Glide.with(this).load(mGroupNumberInfo.getUser().getAvatar()).error(R.mipmap.default_image).placeholder(R.drawable.location_default).into(avatar);
                name.setText(mGroupNumberInfo.getUser().getNick());
                groupNick.setText(mGroupNumberInfo.getGroupNick());
                groupId = getIntent().getStringExtra("groupId");
                if (getIntent().getBooleanExtra("isCreator", false)) {
                        type.setText("群主");
                        if (mGroupNumberInfo.getUser().getObjectId().equals(UserManager.getInstance().getCurrentUserObjectId())) {
                                exit.setVisibility(View.GONE);
                        }else {
                                exit.setVisibility(View.VISIBLE);
                        }
                } else {
                        type.setText("群成员");
                        exit.setVisibility(View.GONE);
                }

                ToolBarOption mToolBarOption = new ToolBarOption();

                mToolBarOption.setAvatar(null);
                mToolBarOption.setTitle("成员信息");
                mToolBarOption.setNeedNavigation(true);
                setToolBar(mToolBarOption);
        }

        @Override
        public void onClick(View v) {
                switch (v.getId()) {
                        case R.id.btn_group_number_info_detail_exit:
                                if (!CommonUtils.isNetWorkAvailable()) {
                                        ToastUtils.showShortToast("网络连接失败，请检查网络配置");
                                        return;
                                }
                                final String deleteId = mGroupNumberInfo.getUser().getObjectId();
                                MsgManager.getInstance().updateGroupMessage(groupId, "deleteNumber", deleteId, new UpdateListener() {
                                        @Override
                                        public void onSuccess() {
                                                LogUtil.e("1删除群成员成功");
                                        }

                                        @Override
                                        public void onFailure(int i, String s) {
                                                LogUtil.e("删除群成员失败" + s + i);
                                        }
                                });
                                break;
                }
        }
}
