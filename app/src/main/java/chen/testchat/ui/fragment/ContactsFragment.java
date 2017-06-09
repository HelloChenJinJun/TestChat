package chen.testchat.ui.fragment;

import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AlphabetIndexer;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import org.pointstone.cugappplat.baseadapter.BaseWrappedViewHolder;

import chen.testchat.R;
import chen.testchat.adapter.ContactsAdapter;
import chen.testchat.bean.User;
import chen.testchat.db.ChatDB;
import chen.testchat.listener.OnBaseItemClickListener;
import chen.testchat.manager.UserCacheManager;
import chen.testchat.ui.BlackListActivity;
import chen.testchat.ui.GroupListActivity;
import chen.testchat.ui.MainActivity;
import chen.testchat.ui.NearbyPeopleActivity;
import chen.testchat.ui.UserInfoActivity;
import chen.testchat.util.LogUtil;
import chen.testchat.view.ListViewDecoration;
import chen.testchat.view.MyLetterView;


/**
 * 项目名称:    HappyChat
 * 创建人:        陈锦军
 * 创建时间:    2016/9/15      15:25
 * QQ:             1981367757
 */
public class ContactsFragment extends org.pointstone.cugappplat.base.basemvp.BaseFragment implements MyLetterView.MyLetterChangeListener, View.OnClickListener {
        MyLetterView mMyLetterView;
        ContactsAdapter adapter;
        SectionIndexer mIndexer;
        TextView middle;
        String alphabet = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        private RecyclerView display;
        private LinearLayoutManager mLinearLayoutManager;


        @Override
        protected boolean isNeedHeadLayout() {
                return false;
        }

        @Override
        protected boolean isNeedEmptyLayout() {
                return false;
        }

        @Override
        protected int getContentLayout() {
                return R.layout.fragment_contacts;
        }

        @Override
        public void initView() {
                display = (RecyclerView) findViewById(R.id.rcv_fragment_contacts_display);
                mMyLetterView = (MyLetterView) findViewById(R.id.ml_fragment_contacts_letter);
                middle = (TextView) findViewById(R.id.tv_fragment_contacts_middle);
                LinearLayout groupList = (LinearLayout) findViewById(R.id.ll_fragment_contacts_group);
                LinearLayout blackList = (LinearLayout) findViewById(R.id.ll_fragment_contacts_black);
                LinearLayout nearby = (LinearLayout) findViewById(R.id.ll_fragment_contacts_nearby);
                mMyLetterView.setListener(this);
                groupList.setOnClickListener(this);
                blackList.setOnClickListener(this);
                nearby.setOnClickListener(this);
        }





        @Override
        public void initData() {
                LogUtil.e("这里设置display的adapter121");
                mMyLetterView.setTextView(middle);
                adapter = new ContactsAdapter(UserCacheManager.getInstance().getAllContacts(), R.layout.fragment_contacts_list_item);
                LogUtil.e("初始化的所有好友信息");
                if (adapter.getAllData() != null && adapter.getAllData().size() > 0) {
                        for (User user :
                                adapter.getAllData()) {
                                LogUtil.e(user);
                        }
                }
                mIndexer = new AlphabetIndexer(ChatDB.create().getSortedKeyCursor(), 0, alphabet);

                adapter.setSectionIndexer(mIndexer);
                mLinearLayoutManager = new LinearLayoutManager(getActivity());
                display.setLayoutManager(mLinearLayoutManager);
                display.addItemDecoration(new ListViewDecoration(getActivity()));
                display.setItemAnimator(new DefaultItemAnimator());
                display.addOnItemTouchListener(new OnBaseItemClickListener() {
                        @Override
                        protected void onItemClick(BaseWrappedViewHolder baseWrappedViewHolder, int id, View view, int position) {
                                User user = adapter.getData(position);
                                Intent intent = new Intent(getActivity(), UserInfoActivity.class);
                                intent.putExtra("uid", user.getObjectId());
                                startActivity(intent);
                        }
                });
                display.setAdapter(adapter);
        }

        @Override
        protected void updateView() {

        }


        @Override
        public void onResume() {
                super.onResume();
                onHiddenChanged(false);
        }

        @Override
        public void onHiddenChanged(boolean hidden) {
                super.onHiddenChanged(hidden);
                if (!hidden) {
                        ((MainActivity) getActivity()).initActionBar("好友");
                }
        }

        @Override
        public void onLetterChanged(String s) {
                int index = alphabet.indexOf(s);
                mLinearLayoutManager.scrollToPosition(mIndexer.getPositionForSection(index));
        }


        public void updateContactsData(String belongId) {
                mIndexer = new AlphabetIndexer(ChatDB.create().getSortedKeyCursor(), 0, alphabet);
                adapter.setSectionIndexer(mIndexer);
                User user=UserCacheManager.getInstance().getUser(belongId);
                LogUtil.e("这里添加的好友星星如下");
                LogUtil.e(user);
                adapter.addData(user);
        }

        @Override
        public void onClick(View view) {
                switch (view.getId()) {
                        case R.id.ll_fragment_contacts_group:
                                GroupListActivity.start(getActivity());
                                break;
                        case R.id.ll_fragment_contacts_black:
                                BlackListActivity.start(getActivity());
                                break;
                        case R.id.ll_fragment_contacts_nearby:
                                NearbyPeopleActivity.start(getActivity());
                                break;


                }
        }
}
