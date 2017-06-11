package chen.testchat.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.pointstone.cugappplat.base.basemvp.BaseActivity;
import org.pointstone.cugappplat.baseadapter.baseloadview.OnLoadMoreDataListener;
import org.pointstone.cugappplat.util.ToastUtils;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import chen.testchat.CustomApplication;
import chen.testchat.R;
import chen.testchat.adapter.OnShareMessageItemClickListener;
import chen.testchat.adapter.ShareMultipleLayoutAdapter;
import chen.testchat.base.CommonImageLoader;
import chen.testchat.base.Constant;
import chen.testchat.bean.ImageItem;
import chen.testchat.bean.SharedMessage;
import chen.testchat.bean.User;
import chen.testchat.listener.OnShareMessageReceivedListener;
import chen.testchat.manager.MessageCacheManager;
import chen.testchat.manager.UserManager;
import chen.testchat.mvp.ShareMessageTask.ShareMessageContacts;
import chen.testchat.mvp.ShareMessageTask.ShareMessageModel;
import chen.testchat.mvp.ShareMessageTask.ShareMessagePresenter;
import chen.testchat.service.GroupMessageService;
import chen.testchat.ui.BasePreViewActivity;
import chen.testchat.ui.EditShareMessageActivity;
import chen.testchat.ui.HappyContentDisplayActivity;
import chen.testchat.ui.ImageDisplayActivity;
import chen.testchat.ui.MainActivity;
import chen.testchat.ui.SelectedPictureActivity;
import chen.testchat.ui.UserDetailActivity;
import chen.testchat.ui.VideoPlayActivity;
import chen.testchat.ui.WallPaperActivity;
import chen.testchat.ui.WeiXinNewsActivity;
import chen.testchat.util.CommonUtils;
import chen.testchat.util.LogUtil;
import chen.testchat.util.PhotoUtil;
import chen.testchat.view.CommentPopupWindow;
import chen.testchat.view.fab.FloatingActionButton;
import chen.testchat.view.fab.FloatingActionsMenu;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/11/27      16:47
 * QQ:             1981367757
 */

public class ShareMessageFragment extends org.pointstone.cugappplat.base.basemvp.BaseFragment implements ShareMessageContacts.View, View.OnClickListener, OnShareMessageReceivedListener, AdapterView.OnItemClickListener, OnShareMessageItemClickListener {


        private RecyclerView display;
        private SwipeRefreshLayout refresh;
        private LinearLayoutManager mLinearLayoutManager;
        private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener;
        private ShareMultipleLayoutAdapter mAdapter;
        private LinearLayout bottomInput;
        private ImageView send;
        private EditText input;
        private RelativeLayout container;
        private ShareMessagePresenter presenter;
        private int screenHeight;
        private String simpleDate = "0000-00-00 01:00:00";
        private FloatingActionsMenu mMenu;
        private FloatingActionButton video;
        private FloatingActionButton normal;
        private FloatingActionButton image;
        //        private File photoFile;
        private View headerView;


        @Override
        public void onResume() {
                super.onResume();
                onHiddenChanged(false);
        }


        @Override
        public void onHiddenChanged(boolean hidden) {
                super.onHiddenChanged(hidden);
                if (!hidden) {
                        ((MainActivity) getActivity()).initActionBar("朋友圈");
                }
        }


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
                return R.layout.fragment_share_message;
        }

        @Override
        public void initView() {
                display = (RecyclerView) findViewById(R.id.rcv_share_fragment_display);
                refresh = (SwipeRefreshLayout) findViewById(R.id.refresh_share_fragment_refresh);
                bottomInput = (LinearLayout) findViewById(R.id.ll_share_fragment_bottom);
                send = (ImageView) findViewById(R.id.iv_share_fragment_send);
                container = (RelativeLayout) findViewById(R.id.rl_share_fragment_container);
                mMenu = (FloatingActionsMenu) findViewById(R.id.fam_share_message_menu);
                normal = (FloatingActionButton) findViewById(R.id.fab_share_message_normal);
                video = (FloatingActionButton) findViewById(R.id.fab_share_message_video);
                input = (EditText) findViewById(R.id.et_share_fragment_input);
                image = (FloatingActionButton) findViewById(R.id.fab_share_message_image);
                container.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                                Rect rect = new Rect();
                                container.getWindowVisibleDisplayFrame(rect);
                                screenHeight = container.getRootView().getHeight();
                                int keyBoardHeight = screenHeight - rect.bottom;
                                if (keyBoardHeight != mKeyBoardHeight) {
                                        if (keyBoardHeight > mKeyBoardHeight) {
                                                bottomInput.setVisibility(View.VISIBLE);
                                                mMenu.setVisibility(View.GONE);
                                                mKeyBoardHeight = keyBoardHeight;
                                                mLinearLayoutManager.scrollToPositionWithOffset(currentPosition, getListOffset());
                                        } else {
                                                mKeyBoardHeight = keyBoardHeight;
                                                mMenu.setVisibility(View.VISIBLE);
                                                bottomInput.setVisibility(View.GONE);
                                        }
                                }
                        }
                });
                send.setOnClickListener(this);
                normal.setOnClickListener(this);
                video.setOnClickListener(this);
                image.setOnClickListener(this);
                display.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                                if (mCommentPopupWindow != null && mCommentPopupWindow.isShowing()) {
                                        mCommentPopupWindow.dismiss();
                                }
                                if (mMenu.isExpanded()) {
                                        mMenu.collapse();
                                }
                                LogUtil.e("container:onTouch");
//                                这里进行点击关闭编辑框
                                if (bottomInput.getVisibility() == View.VISIBLE) {
                                        LogUtil.e("触摸界面点击关闭输入法");
                                        dealBottomView(false);
                                        return true;
                                }
                                return false;
                        }
                });
                refresh.setOnRefreshListener(mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                                //                                数据量比较小，进行延时,2秒后再进行加载
                                new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                                SharedMessage shareMessage = mAdapter.getData(0);
                                                LogUtil.e("下拉加载的前一个说说消息为1");
                                                if (shareMessage == null) {
                                                        LogUtil.e("首次加载数据");
                                                        loadData(true, "0000-00-00 01:00:00");
                                                } else {
                                                        LogUtil.e(shareMessage);
                                                        loadData(true, shareMessage.getCreatedAt());
                                                }
                                        }
                                }, 2000);
                        }
                });
                display.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                                super.onScrollStateChanged(recyclerView, newState);
//                                这里进行重发，如果在滚动的时候就不进行数据的加载和视图的显示
                                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                                        LogUtil.e("休闲的时候进行加载");
                                        Glide.with(getActivity()).resumeRequests();
                                } else {
                                        LogUtil.e("滚动的时候不进行加载");
                                        Glide.with(getActivity()).pauseRequests();
                                }
                        }
                });
        }


        private int getListOffset() {
                int offset = 0;
                if (currentCommentPosition == -1) {
                        ToastUtils.showShortToast("点击评论");
                        LogUtil.e("点击评论111");
                        int firstVisiblePosition = mLinearLayoutManager.findFirstVisibleItemPosition();
                        View view = mLinearLayoutManager.getChildAt(currentPosition - firstVisiblePosition);
                        offset += view.getHeight();
                }
                offset += mKeyBoardHeight;
                offset += bottomInput.getHeight();
                offset += ((BaseActivity) getActivity()).getStatusHeight();
                if (replyUid != null) {
                        offset += commentItemOffset;
                }
                return screenHeight - offset;
        }


        private int mKeyBoardHeight = 0;


        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
                if (resultCode == Activity.RESULT_OK) {
                        switch (requestCode) {
                                case Constant.REQUEST_CODE_EDIT_SHARE_MESSAGE:
                                        if (data != null) {
                                                SharedMessage sharedMessage = (SharedMessage) data.getSerializableExtra(Constant.RESULT_CODE_SHARE_MESSAGE);
                                                LogUtil.e(sharedMessage);
                                                updateShareMessageAdded(sharedMessage);
                                                ((MainActivity) getActivity()).notifySharedMessageChanged(sharedMessage.getObjectId(), true);
                                        }
                                        break;
                                case Constant.REQUEST_CODE_SELECT_TITLE_WALLPAPER:
                                        LogUtil.e("改变背景成功后重绘11111");
                                        updateHeaderView();
                                        break;
                                case Constant.REQUEST_CODE_SELECT_PICTURE:
//                                        这里进入编辑图片界面
                                        if (CommonImageLoader.getInstance().getSelectedImages().get(0) != null) {
                                                String path = CommonImageLoader.getInstance().getSelectedImages().get(0).getPath();
                                                cropPhoto(path);
                                        }
                                        break;
                                case Constant.REQUEST_CODE_CROP:
                                        LogUtil.e("裁剪完成");
                                        try {
                                                showLoading("正在上传背景，请稍候........");
                                                final BmobFile bmobFile = new BmobFile(new File(new URI(PhotoUtil.buildUri(getActivity()).toString())));
                                                bmobFile.uploadblock(CustomApplication.getInstance(), new UploadFileListener() {
                                                        @Override
                                                        public void onSuccess() {
                                                                UserManager.getInstance().updateUserInfo("titleWallPaper", bmobFile.getFileUrl(CustomApplication.getInstance()), new UpdateListener() {
                                                                        @Override
                                                                        public void onSuccess() {
                                                                                hideLoading();
                                                                                LogUtil.e("更新用户背景成功");
                                                                                UserManager.getInstance().getCurrentUser().setTitleWallPaper(bmobFile.getFileUrl(CustomApplication.getInstance()));
                                                                                updateHeaderView();
                                                                        }

                                                                        @Override
                                                                        public void onFailure(int i, String s) {
                                                                                hideLoading();
                                                                                LogUtil.e("更新用户背景失败" + s + i);
                                                                        }
                                                                });
                                                        }

                                                        @Override
                                                        public void onFailure(int i, String s) {
                                                                hideLoading();
                                                                LogUtil.e("加载失败");
                                                        }
                                                });
                                        } catch (URISyntaxException e) {
                                                e.printStackTrace();
                                        }
                                        break;
                        }
                }
        }

        private void updateHeaderView() {
                Glide.with(this).load(UserManager.getInstance().getCurrentUser().getTitleWallPaper())
                        .into((ImageView) headerView.findViewById(R.id.iv_share_fragment_item_header_background));
        }

        private void cropPhoto(String path) {
                Uri uri = Uri.fromFile(new File(path));
                Intent cropIntent = new Intent("com.android.camera.action.CROP");
                cropIntent.setDataAndType(uri, "image/*");
                cropIntent.putExtra("crop", "true");
                cropIntent.putExtra("aspectX", 1);
                cropIntent.putExtra("aspectY", 1);
                cropIntent.putExtra("outputX", 400);
                cropIntent.putExtra("outputY", 200);
                cropIntent.putExtra("return-data", false);
                cropIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                Uri cropUri = PhotoUtil.buildUri(getActivity());
                cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, cropUri);
                if (cropIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivityForResult(cropIntent, Constant.REQUEST_CODE_CROP);
                }
        }

//        private void updateInfo(String localBgImagePath) {
//                final BmobFile bmobFile = new BmobFile(new File(localBgImagePath));
//                bmobFile.uploadblock(CustomApplication.getInstance(), new UploadFileListener() {
//                        @Override
//                        public void onSuccess() {
//                                LogUtil.e("上传背景图片成功");
//                                UserManager.getInstance().updateUserInfo("titleWallPaper", bmobFile.getFileUrl(CustomApplication.getInstance()), new UpdateListener() {
//                                        @Override
//                                        public void onSuccess() {
//                                                LogUtil.e("更新背景图片成功");
//                                                hideLoading();
//                                                UserCacheManager.getInstance().getUser().setTitleWallPaper(bmobFile.getFileUrl(CustomApplication.getInstance()));
//                                                mAdapter.notifyDataSetChanged();
//                                        }
//
//                                        @Override
//                                        public void onFailure(int i, String s) {
//                                                hideLoading();
//                                                LogUtil.e("更新背景图片失败");
//                                        }
//                                });
//
//                        }
//
//                        @Override
//                        public void onFailure(int i, String s) {
//                                hideLoading();
//                                ToastUtils.showShortToast("上传背景图片失败" + s + i);
//                                LogUtil.e("上传背景图片失败" + s + i);
//                        }
//                });
//        }

        private void loadData(boolean isPullRefresh, String time) {
                LogUtil.e("加载时间是：" + time);
                presenter.loadAllShareMessages(isPullRefresh, time);
        }

        @Override
        public void initData() {
                presenter = new ShareMessagePresenter();
                presenter.setViewAndModel(this, new ShareMessageModel());
                mAdapter = new ShareMultipleLayoutAdapter();
                View headerView = getHeaderView();
                mAdapter.setHeaderView(headerView, 0);
                mAdapter.setOnShareMessageItemClickListener(this);
                display.setLayoutManager(mLinearLayoutManager = new LinearLayoutManager(getActivity()));
//                display.setHasFixedSize(true);
                display.setItemAnimator(new DefaultItemAnimator());
                display.setAdapter(mAdapter);
                GroupMessageService.registerListener(this);
                mMenu.attachToRecyclerView(display);
                mAdapter.setOnLoadMoreDataListener(new OnLoadMoreDataListener() {
                        @Override
                        public void onLoadMoreData() {
                                int size = mAdapter.getAllData().size();
                                if (mAdapter != null && size > 0) {
                                        loadData(false, mAdapter.getData(size - 1).getCreatedAt());
                                } else {
                                        LogUtil.e("获取更多的时候data为空");
                                }
                        }
                }, display);

                refresh.post(new Runnable() {
                        @Override
                        public void run() {
                                refresh.setRefreshing(true);
                                mOnRefreshListener.onRefresh();
                        }
                });
        }

        @Override
        protected void updateView() {
                long deltaTime = CustomApplication.getInstance().getSharedPreferencesUtil().getDeltaTime();
                LogUtil.e("这里通过缓存的时间差值来计算出服务器上的时间");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                long realServerTime = System.currentTimeMillis() - deltaTime;
                simpleDate = simpleDateFormat.format(new Date(realServerTime));
                LogUtil.e("得到的时间格式为:" + simpleDate);
//                        display.setRefreshing(true);
                refresh.setRefreshing(true);
                mOnRefreshListener.onRefresh();
                LogUtil.e("说说界面隐藏了，这里取消监听 ");
                if (mAdapter.getAllData().size() > 0) {
                        for (SharedMessage sharedMessage :
                                mAdapter.getAllData()) {
                                ((MainActivity) getActivity()).notifySharedMessageChanged(sharedMessage.getObjectId(), false);
                        }
                }
        }

        private View getHeaderView() {
                User currentUser = UserManager.getInstance().getCurrentUser();
                headerView = View.inflate(getContext(), R.layout.share_fragment_item_header_layout, null);
                ((TextView) headerView.findViewById(R.id.tv_share_fragment_item_header_name)).setText(currentUser.getNick());
                ImageView avatar = (ImageView) headerView.findViewById(R.id.iv_share_fragment_item_header_avatar);
                ImageView bg = (ImageView) headerView.findViewById(R.id.iv_share_fragment_item_header_background);
                Glide.with(this).load(currentUser.getAvatar()).into(avatar);
                Glide.with(this).load(currentUser.getTitleWallPaper()).into(bg);
                avatar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                enterUserDetailActivity(UserManager.getInstance().getCurrentUser().getObjectId());
                        }
                });
                bg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                LogUtil.e("点击背景图片");
                                List<String> list = new ArrayList<>();
                                list.add("相册");
                                list.add("系统");
                                showChooseDialog("挑选背景图", list, new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                hideBaseDialog();
                                                if (position == 0) {
                                                        CommonImageLoader.getInstance().initStanderConfig(1);
                                                        Intent intent = new Intent();
                                                        intent.setClass(getActivity(), SelectedPictureActivity.class);
                                                        startActivityForResult(intent, Constant.REQUEST_CODE_SELECT_PICTURE);
                                                } else {
                                                        Intent wallPaperIntent = new Intent(getActivity(), WallPaperActivity.class);
                                                        wallPaperIntent.putExtra("from", "title_wallpaper");
                                                        startActivityForResult(wallPaperIntent, Constant.REQUEST_CODE_SELECT_TITLE_WALLPAPER);
                                                }
                                        }
                                });
                        }
                });
                return headerView;
        }


        @Override
        public void onDestroy() {
                super.onDestroy();
                GroupMessageService.unRegisterListener(this);
        }

        @Override
        public void updateShareMessageAdded(SharedMessage shareMessage) {
                if (shareMessage != null) {
                        mAdapter.addData(0, shareMessage);
                        mAdapter.notifyDataSetChanged();
                }
        }

        @Override
        public void updateShareMessageDeleted(String id) {
                SharedMessage sharedMessage = mAdapter.getSharedMessageById(id);
                LogUtil.e("将要删除的说说消息数据格式");
                if (sharedMessage != null) {
                        LogUtil.e(sharedMessage);
                        mAdapter.getAllData().remove(sharedMessage);
                        mAdapter.notifyDataSetChanged();
//                这里通知删除
                        ((MainActivity) getActivity()).notifySharedMessageChanged(sharedMessage.getObjectId(), false);
                } else {
                        LogUtil.e("该说说已删除");
                }
        }


//        由于更新需要的点赞数据在数据库中获取，所以这里只需要notifyDataSetChanged下就行了

        @Override
        public void updateLikerAdd(String id) {
                if (!mAdapter.getSharedMessageById(id).getLikerList().contains(UserManager.getInstance().getCurrentUserObjectId())) {
                        LogUtil.e("还未点赞，这里添加点赞");
                        mAdapter.getSharedMessageById(id).getLikerList().add(UserManager.getInstance().getCurrentUserObjectId());
                } else {
                        LogUtil.e("已经点赞，这里添加点赞失败,可能的原因是因为实时已经检测到拉");
                }
                mAdapter.notifyDataSetChanged();
        }

        @Override
        public void updateLikerDeleted(String id) {
                if (mAdapter.getSharedMessageById(id).getLikerList().contains(UserManager.getInstance().getCurrentUserObjectId())) {
                        LogUtil.e("已有点赞，这里删除点赞");
                        mAdapter.getSharedMessageById(id).getLikerList().remove(UserManager.getInstance().getCurrentUserObjectId());
                } else {
                        LogUtil.e("没有点赞，这里删除点赞失败，可能的原因是因为实时已经检测到啦");
                }
                mAdapter.notifyDataSetChanged();
        }

        @Override
        public void updateCommentAdded(String id, String content, int position) {
                LogUtil.e("更新添加评论操作，这里就不更新了，因为在实时检测的时候已经更新拉");
                dealBottomView(false);
                mAdapter.notifyDataSetChanged();
        }

        @Override
        public void updateCommentDeleted(String id, String content, int position) {
                LogUtil.e("更新删除评论操作，这里就不更新了，因为在实时检测的时候已经更新啦啦啦");
                mAdapter.notifyDataSetChanged();
        }

        @Override
        public void updateAllShareMessages(List<SharedMessage> data, boolean isPullRefresh) {
                if (isPullRefresh) {
                        if (!CommonUtils.isNetWorkAvailable()) {
                                ToastUtils.showShortToast("网络连接失败，请检查网络配置，所以该处只加载数据库中的缓存数据，并不是最新的数据");
                        } else {
                                if (data != null && data.size() > 0) {
                                        LogUtil.e("有网络状态，证明这是从服务器上拉取得到的数据，所以要实时监听");
                                        if (mAdapter.getAllData().size() + data.size() > 10) {
                                                LogUtil.e("下拉加载得到的数据和原本的数据加起来大于10条，所以需要裁剪");
//                                                这里这么做是为了防止在一步监听说说的时候，后面突然又把数据给删除导致有监听不了的现象发生
                                                List<SharedMessage> list = new ArrayList<>(mAdapter.getAllData());
                                                if (list.size() > 0) {
//                                                        这里有两种情况，一种是进来时的加载，一种是向上滚动时的加载
                                                        LogUtil.e("这里把之前监听的说说给取消掉");
                                                        for (SharedMessage shareMessage :
                                                                list) {
                                                                ((MainActivity) getActivity()).notifySharedMessageChanged(shareMessage.getObjectId(), false);
                                                        }
                                                        LogUtil.e("之前的说说个数为" + list.size());
                                                }
                                                mAdapter.addData(0, data);
                                                LogUtil.e("这里裁剪掉多余的说说");
                                                for (int i = 10; i < mAdapter.getAllData().size(); i++) {
                                                        mAdapter.getAllData().remove(i);
                                                }
                                                for (int i = 0; i < mAdapter.getAllData().size(); i++) {
                                                        String id = mAdapter.getData(i).getObjectId();
                                                        LogUtil.e("剩余10条消息的ID：" + id);
                                                        ((MainActivity) getActivity()).notifySharedMessageChanged(id, true);
                                                }
                                        } else {
                                                LogUtil.e("展示的说说数据不多于10条");
                                                mAdapter.addData(0, data);
                                                for (SharedMessage sharedMessage :
                                                        mAdapter.getAllData()) {
                                                        ((MainActivity) getActivity()).notifySharedMessageChanged(sharedMessage.getObjectId(), true);
                                                }
                                        }
                                } else {
//                                        这里下拉刷新时没有数据，也要监听遗留下来的数据
                                        if (mAdapter.getAllData().size() > 0) {
                                                for (SharedMessage message : mAdapter.getAllData()) {
                                                        ((MainActivity) getActivity()).notifySharedMessageChanged(message.getObjectId(), true);
                                                }
                                        }
                                }
                        }
                        refresh.setRefreshing(false);
                        mAdapter.notifyDataSetChanged();
                } else {
//                        display.getMoreLoadView().setVisibility(View.GONE);
                        if (data == null || data.size() == 0) {
                                LogUtil.e("没有更多的数据可加载");
//                                这里可移除加载更多的监听器
//                                display.setOnMoreDataLoadListener(null);
                        } else {
                                LogUtil.e("下部从网络加载得到数据拉");
                                if (CommonUtils.isNetWorkAvailable()) {
                                        for (SharedMessage sharedMessage :
                                                data) {
                                                LogUtil.e(sharedMessage);
                                                ((MainActivity) getActivity()).notifySharedMessageChanged(sharedMessage.getObjectId(), true);
                                        }
                                } else {
                                        LogUtil.e("下部是从缓存中获取得到的数据");
//                                        这里即时没有网络也要通知其监听，虽然监听不了，但是一旦网络改变时会通知监听服务重新监听其内部缓存的说说ID，所以
//                                        这里把缓存的说说ID放到监听服务那里
                                        for (SharedMessage sharedMessage :
                                                data) {
                                                LogUtil.e(sharedMessage);
                                                ((MainActivity) getActivity()).notifySharedMessageChanged(sharedMessage.getObjectId(), true);
                                        }
                                }
                                mAdapter.addData(data);
                        }
                        mAdapter.notifyLoadingEnd();
                }

        }


        @Override
        public void hideLoading() {
                if (refresh.isRefreshing()) {
                        refresh.setRefreshing(false);
                }
                super.hideLoading();
        }


        @Override
        public void onImageAvatarClick(String uid) {
                LogUtil.e("点击用户头像回调");
                Intent intent = new Intent(getActivity(), UserDetailActivity.class);
                if (uid.equals(UserManager.getInstance().getCurrentUserObjectId())) {
                        intent.putExtra("from", "me");
                } else {
                        intent.putExtra("from", "other");
                }
                intent.putExtra("uid", uid);
                startActivity(intent);
        }

        @Override
        public void onNameClick(String uid) {
                LogUtil.e("点击用户姓名回调");
                Intent intent = new Intent(getActivity(), UserDetailActivity.class);
                if (uid.equals(UserManager.getInstance().getCurrentUserObjectId())) {
                        intent.putExtra("from", "me");
                } else {
                        intent.putExtra("from", "other");
                }
                intent.putExtra("uid", uid);
                startActivity(intent);
        }


        private CommentPopupWindow mCommentPopupWindow;
        private int currentPosition;
        private String currentId;

        @Override
        public void onCommentBtnClick(View view, String id, int shareMessagePosition, final boolean isLike) {
                LogUtil.e("点击评论按钮");
                LogUtil.e("是否已点赞" + isLike);
                LogUtil.e("位置" + shareMessagePosition);
                currentId = id;
                currentPosition = shareMessagePosition;
                if (mCommentPopupWindow == null) {
                        mCommentPopupWindow = new CommentPopupWindow(getContext());
                        mCommentPopupWindow.setOnCommentPopupItemClickListener(new CommentPopupWindow.OnCommentPopupItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position, boolean isLiker) {
                                        if (position == 0) {
                                                LogUtil.e("处理赞的操作");
                                                if (isLiker) {
                                                        LogUtil.e("这里取消点赞操作");
                                                        LogUtil.e("删除点赞是的ID" + currentId);
                                                        presenter.deleteLiker(currentId);
                                                } else {
                                                        LogUtil.e("这里添加点赞操作");
                                                        LogUtil.e("position为多少" + position);
                                                        LogUtil.e("添加点赞时的ID" + currentId);
                                                        presenter.addLiker(currentId);
                                                }
                                        } else if (position == 1) {
                                                LogUtil.e("处理评论的操作");
//                                                进行位移
                                                currentCommentPosition = -1;
                                                replyUid = null;
                                                commentItemOffset = 0;
                                                dealBottomView(true);
                                        }
                                }
                        });
                }
                if (isLike) {
                        mCommentPopupWindow.setLikerName("取消");
                } else {
                        mCommentPopupWindow.setLikerName("赞");
                }
                mCommentPopupWindow.showPopupWindow(view);
        }

        private int currentCommentPosition;
        private String replyUid;
        private int commentItemOffset = 0;

        @Override
        public void onCommentItemClick(View view, String id, int shareMessagePosition, int position, String replyUser) {
                LogUtil.e("位置" + shareMessagePosition);
                currentPosition = shareMessagePosition;
                currentCommentPosition = position;
                ViewParent viewParent = view.getParent();
                if (viewParent != null) {
                        ViewGroup parent = (ViewGroup) viewParent;
                        commentItemOffset += parent.getHeight() - view.getBottom();
                        if (parent.getParent() != null) {
                                ViewGroup rootParent = (ViewGroup) parent.getParent();
                                commentItemOffset += rootParent.getHeight() + parent.getBottom();
                        }
                }
                this.replyUid = replyUser;
                dealBottomView(true);
        }

        private void dealBottomView(boolean isShow) {
                if (isShow) {
                        bottomInput.setVisibility(View.VISIBLE);
                        CommonUtils.showSoftInput(getContext(), input);
                        input.requestFocus();
                } else {
                        bottomInput.setVisibility(View.GONE);
                        CommonUtils.hideSoftInput(getContext(), input);
                }
        }

        @Override
        public void onCommentItemLongClick(String id, int shareMessagePosition, int position) {
                currentPosition = shareMessagePosition;
                currentCommentPosition = position;
                showCommentDialog(id, position);
        }

        private void showCommentDialog(final String id, final int position) {
                List<String> list = new ArrayList<>();
                list.add("复制");
                if (CommonUtils.content2List(mAdapter.getSharedMessageById(id).getCommentMsgList().get(position)).get(0).equals(UserManager.getInstance().getCurrentUser().getObjectId())) {
                        list.add("删除");
                }
                ((org.pointstone.cugappplat.base.basemvp.BaseActivity) getActivity()).showChooseDialog("操作", list, new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position1, long id1) {
                                ((BaseActivity) getActivity()).dismissBaseDialog();
                                if (position1 == 0) {
                                        LogUtil.e("复制操作");
                                } else {
                                        LogUtil.e("删除操作");
                                        presenter.deleteComment(id, position);
                                }
                        }
                });
        }

        @Override
        public void onCommentItemNameClick(String uid) {
                enterUserDetailActivity(uid);
        }


        @Override
        public void onLikerTextViewClick(String uid) {
                enterUserDetailActivity(uid);
        }

        @Override
        public void onLinkViewClick(SharedMessage shareMessage) {

                Intent happyContentIntent = new Intent(getActivity(), HappyContentDisplayActivity.class);
                if (shareMessage.getUrlList().size() == 0) {
//                     笑话
                        happyContentIntent.putExtra("content",shareMessage.getUrlTitle());
                        startActivity(happyContentIntent);
                } else if (shareMessage.getUrlList().size() == 1) {
//                        趣图
                        if (shareMessage.getUrlTitle() == null) {
//                                美女图片
                                List<ImageItem> list=new ArrayList<>();
                                ImageItem imageItem=new ImageItem();
                                imageItem.setPath(shareMessage.getUrlList().get(0));
                                list.add(imageItem);
                                BasePreViewActivity.startBasePreview(getActivity(),list,0);
                        }else {
                                happyContentIntent.putExtra("content",shareMessage.getUrlTitle());
                                happyContentIntent.putExtra("url",shareMessage.getUrlList().get(0));
                                startActivity(happyContentIntent);
                        }
                }else {
//                        微信精选
                        WeiXinNewsActivity.start(getActivity(), shareMessage.getUrlList().get(0), shareMessage.getUrlList().get(1));
                }
        }

        private void enterUserDetailActivity(String uid) {
                Intent intent = new Intent(getActivity(), UserDetailActivity.class);
                intent.putExtra("uid", uid);
                if (!uid.equals(UserManager.getInstance().getCurrentUserObjectId())) {
                        intent.putExtra("from", "other");
                } else {
                        intent.putExtra("from", "me");
                }
                startActivity(intent);
        }


        @Override
        public void onDeleteShareMessageClick(final String id) {
                LogUtil.e("删除");
                ((BaseActivity) (getActivity())).showBaseDialog("提示", "确定要删除该说说消息吗?", "取消", "确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                ((BaseActivity) getActivity()).cancelBaseDialog();
                        }
                }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                ((BaseActivity) getActivity()).dismissBaseDialog();
                                presenter.deleteShareMessage(id);
                        }
                });
        }

        @Override
        public void onPhotoItemClick(final View view, final String id, final int position, final String url) {
                LogUtil.e("点击图片");
                List<String> cache = MessageCacheManager.getInstance().getShareMessageCache(id);
                if (!url.contains("$")) {
                        List<ImageItem> list=new ArrayList<>();
                        if (cache != null) {
                                for (String cacheId :
                                        cache) {
                                        ImageItem imageItem = new ImageItem();
                                        imageItem.setPath(cacheId);
                                        list.add(imageItem);
                                }
                        } else {
                                SharedMessage message=mAdapter.getSharedMessageById(id);
                                for (String pId :
                                        message.getImageList()) {
                                        ImageItem imageItem = new ImageItem();
                                        imageItem.setPath(pId);
                                        list.add(imageItem);
                                }
                        }
                        BasePreViewActivity.startBasePreview(getActivity(),list,position);
                } else {
                        if (cache == null || cache.size() == 0) {
                                LogUtil.e("这是视频和封面混合的URL列表");
                                List<String> urlList = CommonUtils.content2List(url);
                                if (urlList != null && urlList.size() > 0) {
                                        String videoUrl = null;
                                        String imageUrl = null;
                                        LogUtil.e("视频URL列表");
                                        for (String str : urlList
                                                ) {
                                                if (str.contains(".jpg") || str.contains(".png") || str.contains(".jpeg")) {
                                                        LogUtil.e("封面图片URL" + str);
                                                        imageUrl = str;
                                                } else {
                                                        LogUtil.e("视频URL" + str);
                                                        videoUrl = str;
                                                }
                                        }
                                        if (videoUrl != null) {
                                                Intent videoIntent = new Intent(getContext(), ImageDisplayActivity.class);
                                                videoIntent.putExtra("name", "photo");
                                                videoIntent.putExtra("url", imageUrl);
                                                videoIntent.putExtra("videoUrl", videoUrl);
                                                videoIntent.putExtra("id", id);
                                                startActivity(videoIntent, ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), view, "photo").toBundle());
                                        }
                                }
                        } else {
                                LogUtil.e("有缓存证明是自己的视频说说，这里就是用缓存");
                                String videoUrl = cache.get(1);
                                Intent intent = new Intent(getActivity(), VideoPlayActivity.class);
                                intent.putExtra("path", videoUrl);
                                startActivity(intent);
                        }
                }
        }


        @Override
        public void onClick(View v) {
                switch (v.getId()) {
                        case R.id.iv_share_fragment_send:
                                String content = input.getText().toString().trim();
                                if (content.equals("") || content.contains("$") || content.contains("&")) {
                                        LogUtil.e("评论内容不能为空或包含特殊符号，比如$或者&");
                                } else {
                                        String wrappedContent;
                                        if (replyUid != null) {
                                                wrappedContent = UserManager.getInstance().getCurrentUserObjectId() + "$" + replyUid + "$" + content;
                                        } else {
                                                wrappedContent = UserManager.getInstance().getCurrentUserObjectId() + "$" + content;
                                        }
                                        presenter.addComment(mAdapter.getData(currentPosition - 1).getObjectId(), wrappedContent);
                                }
                                input.setText("");
                                break;
                        case R.id.fab_share_message_normal:
                                if (mMenu.isExpanded()) {
                                        mMenu.collapse();
                                }
                                Intent intent = new Intent(getActivity(), EditShareMessageActivity.class);
                                intent.putExtra("destination", "text");
                                startActivityForResult(intent, Constant.REQUEST_CODE_EDIT_SHARE_MESSAGE);
                                break;
                        case R.id.fab_share_message_video:
                                if (mMenu.isExpanded()) {
                                        mMenu.collapse();
                                }
                                Intent videoIntent = new Intent(getActivity(), EditShareMessageActivity.class);
                                videoIntent.putExtra("destination", "video");
                                startActivityForResult(videoIntent, Constant.REQUEST_CODE_EDIT_SHARE_MESSAGE);
                                break;
                        case R.id.fab_share_message_image:
                                if (mMenu.isExpanded()) {
                                        mMenu.collapse();
                                }
                                Intent imageIntent = new Intent(getActivity(), EditShareMessageActivity.class);
                                imageIntent.putExtra("destination", "image");
                                startActivityForResult(imageIntent, Constant.REQUEST_CODE_EDIT_SHARE_MESSAGE);
                                break;
                }
        }

        @Override
        public void onAddLiker(String id, String uid) {
        }

        @Override
        public void onDeleteLiker(String id, String uid) {

        }

        @Override
        public void onDeleteCommentMessage(String id, String content) {
        }

        @Override
        public void onAddCommentMessage(String id, String content) {
        }

        @Override
        public void onAddShareMessage(SharedMessage sharedMessage) {
                LogUtil.e("实时检测到说说消息到拉");
                LogUtil.e(sharedMessage);
                if (mAdapter.getAllData().contains(sharedMessage)) {
                        SharedMessage oldSharedMessage = mAdapter.getSharedMessageById(sharedMessage.getObjectId());
                        if (oldSharedMessage != null) {
                                int index = mAdapter.getAllData().indexOf(oldSharedMessage);
                                mAdapter.getAllData().set(index, sharedMessage);
                        }
                } else {
                        mAdapter.getAllData().add(0, sharedMessage);
                }
                mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onDeleteShareMessage(String id) {
                LogUtil.e("实时检测到删除说说消息到啦");
                LogUtil.e("消息为：" + id);
                SharedMessage sharedMessage = mAdapter.getSharedMessageById(id);
                LogUtil.e("将要移除的说说消息格式");
                if (sharedMessage != null) {
                        LogUtil.e(sharedMessage);
                        mAdapter.getAllData().remove(sharedMessage);
                        mAdapter.notifyDataSetChanged();
                        ((MainActivity) getActivity()).notifySharedMessageChanged(id, false);
                } else {
                        LogUtil.e("删除消息竟然为空，怎么回事?????");
                }
        }

        public void notifyUrlShareMessageCome(SharedMessage sharedMessage) {
                updateShareMessageAdded(sharedMessage);
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((MainActivity) getActivity()).dismissBaseDialog();
                switch (position) {
                        case 0:
                                CommonImageLoader.getInstance().initStanderConfig(1);
                                Intent intent = new Intent();
                                intent.setClass(getActivity(), SelectedPictureActivity.class);
                                startActivityForResult(intent, Constant.REQUEST_CODE_SELECT_PICTURE);
                                break;
                        case 1:
                                Intent wallPaperIntent = new Intent(getActivity(), WallPaperActivity.class);
                                wallPaperIntent.putExtra("from", "title_wallpaper");
                                startActivityForResult(wallPaperIntent, Constant.REQUEST_CODE_SELECT_TITLE_WALLPAPER);
                                break;
                        default:
                                break;
                }
        }


}
