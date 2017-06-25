package chen.testchat.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.pointstone.cugappplat.base.cusotomview.ToolBarOption;
import org.pointstone.cugappplat.baseadapter.BaseWrappedViewHolder;

import chen.testchat.R;
import chen.testchat.adapter.GroupListAdapter;
import chen.testchat.bean.GroupTableMessage;
import chen.testchat.listener.OnBaseItemClickListener;
import chen.testchat.manager.MessageCacheManager;
import chen.testchat.manager.UserManager;
import chen.testchat.view.ListViewDecoration;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/10      10:03
 * QQ:             1981367757
 */
public class GroupListActivity extends SlideBaseActivity {
        private RecyclerView display;
        private GroupListAdapter groupListAdapter;



        @Override
        protected boolean isNeedHeadLayout() {
                return true;
        }

        @Override
        protected boolean isNeedEmptyLayout() {
                return true;
        }

        @Override
        protected int getContentLayout() {
                return R.layout.activity_group_list;
        }


        @Override
        public void initView() {
                display = (RecyclerView) findViewById(R.id.rcv_group_list_display);
        }

        @Override
        public void initData() {
                display.setLayoutManager(new LinearLayoutManager(this));
                display.setItemAnimator(new DefaultItemAnimator());
                display.setHasFixedSize(true);
                display.addItemDecoration(new ListViewDecoration(this));
                groupListAdapter = new GroupListAdapter(MessageCacheManager.getInstance().getAllGroupTableMessage(), R.layout.group_list_item_layout);
                display.addOnItemTouchListener(new OnBaseItemClickListener() {
                        @Override
                        protected void onItemClick(BaseWrappedViewHolder baseWrappedViewHolder, int id, View view, int position) {
                                GroupTableMessage message = groupListAdapter.getData(position);
                                Intent intent = new Intent(GroupListActivity.this, ChatActivity.class);
                                intent.putExtra("groupId", message.getGroupId());
                                intent.putExtra("from", "group");
                                startActivity(intent);
                        }
                });
                display.setAdapter(groupListAdapter);
                initActionBar();
        }

        private void initActionBar() {
                ToolBarOption toolBarOption = new ToolBarOption();
                toolBarOption.setAvatar(UserManager.getInstance().getCurrentUser().getAvatar());
                toolBarOption.setTitle("群列表");
                toolBarOption.setNeedNavigation(true);
                setToolBar(toolBarOption);
        }

        public static void start(Activity activity) {
                Intent intent = new Intent(activity, GroupListActivity.class);
                activity.startActivity(intent);
        }

}
