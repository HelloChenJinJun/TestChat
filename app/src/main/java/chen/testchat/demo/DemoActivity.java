package chen.testchat.demo;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.pointstone.cugappplat.baseadapter.baseloadview.OnLoadMoreDataListener;

import java.util.ArrayList;
import java.util.List;

import chen.testchat.R;
import chen.testchat.util.LogUtil;
import chen.testchat.view.ListViewDecoration;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/5/25      17:01
 * QQ:             1981367757
 */

public class DemoActivity extends org.pointstone.cugappplat.base.basemvp.BaseActivity {

        private DemoAdapter mDemoAdapter;
        private RecyclerView display;
        private SwipeRefreshLayout refresh;

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
                return R.layout.activity_demo;
        }

        @Override
        public void initView() {
                display = (RecyclerView) findViewById(R.id.demo_display);
                refresh = (SwipeRefreshLayout) findViewById(R.id.demo_refresh);
        }


        private boolean flag = false;


        @Override
        public void initData() {
                display.setLayoutManager(new LinearLayoutManager(this));
                display.setItemAnimator(new DefaultItemAnimator());
                display.addItemDecoration(new ListViewDecoration(this));
                mDemoAdapter = new DemoAdapter(getData(), 0);
                LogUtil.e("这里1111233333333");
                mDemoAdapter.setOnLoadMoreDataListener(new OnLoadMoreDataListener() {
                        @Override
                        public void onLoadMoreData() {
                                LogUtil.e("数据要求加载拉123456789");
                                if (!flag) {
                                        flag = true;
                                        refresh.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                        mDemoAdapter.addData(getData());
                                                        mDemoAdapter.notifyLoadingEnd();
                                                }
                                        }, 1000);
                                } else {
                                        mDemoAdapter.notifyLoadingFailed();
                                }
                        }
                }, display);
                display.setAdapter(mDemoAdapter);
        }

        private List<Demo> getData() {
                List<Demo> list = new ArrayList<>();
                Demo demo;
                for (int i = 0; i < 10; i++) {
                        demo = new Demo();
                        demo.setData("内容" + i);
                        if (i == 5) {
                                demo.setDemoType(Demo.TYPE_SPECIAL);
                        } else {
                                demo.setDemoType(Demo.TYPE_NORMAL);
                        }
                        list.add(demo);
                }

                return list;
        }
}
