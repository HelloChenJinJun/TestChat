package chen.testchat.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;

import org.pointstone.cugappplat.base.cusotomview.ToolBarOption;
import org.pointstone.cugappplat.baseadapter.BaseWrappedViewHolder;
import org.pointstone.cugappplat.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import chen.testchat.R;
import chen.testchat.adapter.NearbyPeopleAdapter;
import chen.testchat.bean.User;
import chen.testchat.listener.OnBaseItemClickListener;
import chen.testchat.manager.UserManager;
import chen.testchat.mvp.NearByPeopleTask.NearbyPeopleContacts;
import chen.testchat.mvp.NearByPeopleTask.NearbyPeopleModel;
import chen.testchat.mvp.NearByPeopleTask.NearbyPeoplePresenter;
import chen.testchat.util.LogUtil;
import chen.testchat.view.ListViewDecoration;
import cn.bmob.v3.datatype.BmobGeoPoint;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/11      12:42
 * QQ:             1981367757
 */

public class NearbyPeopleActivity extends SlideBaseActivity implements SwipeRefreshLayout.OnRefreshListener, NearbyPeopleContacts.View {
    private SwipeRefreshLayout refresh;
    private RecyclerView display;
    private NearbyPeopleAdapter mNearbyPeopleAdapter;
    private NearbyPeoplePresenter mNearbyPeoplePresenter;
    private int currentPosition = 0;
    private List<User> data = new ArrayList<>();
    private double longitude = 0;
    private double latitude = 0;


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
        return R.layout.activity_nearby_people;
    }


    @Override
    public void initView() {
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh_nearby_people_refresh);
        display = (RecyclerView) findViewById(R.id.rcv_nearby_people_display);
        refresh.setOnRefreshListener(this);
    }


    @Override
    public void initData() {
        mNearbyPeoplePresenter = new NearbyPeoplePresenter();
        mNearbyPeoplePresenter.setViewAndModel(this, new NearbyPeopleModel());
        BmobGeoPoint bmobGeoPoint = UserManager.getInstance().getCurrentUser().getLocation();
        longitude = bmobGeoPoint.getLongitude();
        latitude = bmobGeoPoint.getLatitude();
        display.setLayoutManager(new LinearLayoutManager(this));
//                display.setHasFixedSize(true);
        display.setItemAnimator(new DefaultItemAnimator());
        display.addItemDecoration(new ListViewDecoration(this));
        mNearbyPeopleAdapter = new NearbyPeopleAdapter(data, R.layout.nearby_people_item_layout);
        display.addOnItemTouchListener(new OnBaseItemClickListener() {
            @Override
            protected void onItemClick(BaseWrappedViewHolder baseWrappedViewHolder, int id, View view, int position) {
                User user = mNearbyPeopleAdapter.getData(position);
                Intent intent = new Intent(NearbyPeopleActivity.this, UserInfoActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("uid", user.getObjectId());
                startActivity(intent);
            }
        });
        display.setAdapter(mNearbyPeopleAdapter);
        display.post(new Runnable() {
            @Override
            public void run() {
                loadData(false);
            }
        });
        initActionBar();
    }

    private void initActionBar() {
        ToolBarOption toolBarOption = new ToolBarOption();
        toolBarOption.setAvatar(UserManager.getInstance().getCurrentUser().getAvatar());
        toolBarOption.setTitle("附近的人");
        toolBarOption.setNeedNavigation(true);
        toolBarOption.setRightResId(R.drawable.ic_list_blue_grey_900_24dp);
        toolBarOption.setRightListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<String> list = new ArrayList<>();
                list.add("查看全部");
                list.add("只查看女生");
                list.add("只查看男生");
                showChooseDialog("搜索条件", list, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        dismissBaseDialog();
                        if (currentPosition != position) {
                            currentPosition = position;
                            loadData(true);
                        }
                    }
                });
            }
        });
        setToolBar(toolBarOption);
    }

    private void loadData(boolean isRefresh) {
        if (isRefresh) {
            if (data.size() > 0) {
                data.clear();
            }
        }
        if (longitude != 0 || latitude != 0) {
            LogUtil.e("已经定位过了，无需再定位");
            boolean result = false;
            boolean isAll = false;
            if (currentPosition == 1) {
                result = false;
            } else if (currentPosition == 2) {
                result = true;
            } else {
                isAll = true;
            }
            mNearbyPeoplePresenter.queryNearbyPeople(longitude, latitude, isAll, result);
        }
    }

    @Override
    public void onRefresh() {
        loadData(true);
    }

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, NearbyPeopleActivity.class);
        activity.startActivity(intent);
    }


    @Override
    public void updateNearbyPeople(List<User> list) {
        if (refresh.isRefreshing()) {
            refresh.setRefreshing(false);
        }
        dismissLoadDialog();
        if (list != null && list.size() > 0) {
            data.addAll(list);
            mNearbyPeopleAdapter.notifyDataSetChanged();
        } else {
            data.clear();
            mNearbyPeopleAdapter.notifyDataSetChanged();
            LogUtil.e("查询得到的附近人为空");
            ToastUtils.showShortToast("查询得到的附近人为空");
        }
    }

    @Override
    public void hideLoading() {
        if (refresh.isRefreshing()) {
            refresh.setRefreshing(false);
        }
        super.hideLoading();
    }
}
