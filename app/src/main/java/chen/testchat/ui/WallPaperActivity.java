package chen.testchat.ui;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.pointstone.cugappplat.base.cusotomview.ToolBarOption;
import org.pointstone.cugappplat.baseadapter.BaseWrappedViewHolder;

import java.util.List;

import chen.testchat.R;
import chen.testchat.adapter.WallPaperAdapter;
import chen.testchat.listener.OnBaseItemClickListener;
import chen.testchat.manager.MsgManager;
import chen.testchat.manager.UserCacheManager;
import chen.testchat.manager.UserManager;
import chen.testchat.util.LogUtil;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/2/26      20:59
 * QQ:             1981367757
 */

public class WallPaperActivity extends SlideBaseActivity {
        private RecyclerView display;
        private WallPaperAdapter adapter;
        private String selectedImage;
        private String from;
        private GridLayoutManager manager;
        private int prePosition = -1;



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
                return R.layout.activity_wallpaper;
        }


        @Override
        public void initView() {
                display = (RecyclerView) findViewById(R.id.rcv_wall_paper_display);
        }


        @Override
        public void initData() {
                from = getIntent().getStringExtra("from");
                showLoadDialog("1正在加载背景图片.........");
                if (from.equals("title_wallpaper")) {
                        MsgManager.getInstance().getAllDefaultTitleWallPaperFromServer(new FindListener<String>() {
                                @Override
                                public void onSuccess(List<String> list) {
                                        dismissLoadDialog();
                                        initAdapter(list);
                                }

                                @Override
                                public void onError(int i, String s) {
                                        dismissLoadDialog();
                                        LogUtil.e("加载背景图片信息失败" + s + i);
                                }
                        });
                } else {
                        MsgManager.getInstance().getAllDefaultWallPaperFromServer(new FindListener<String>() {
                                @Override
                                public void onSuccess(List<String> list) {
                                        dismissLoadDialog();
                                        initAdapter(list);
                                }

                                @Override
                                public void onError(int i, String s) {
                                        dismissLoadDialog();
                                        LogUtil.e("加载背景图片信息失败" + s + i);
                                }
                        });
                }
                initActionBar();
        }

        private void initAdapter(List<String> list) {
                display.setLayoutManager(manager = new GridLayoutManager(this, 3));
                display.setItemAnimator(new DefaultItemAnimator());
                adapter = new WallPaperAdapter(list, R.layout.wallpaper_item);
                int i = 0;
                for (String url :
                        list) {
                        if (url.equals(UserCacheManager.getInstance().getUser().getTitleWallPaper())) {
                                adapter.setSelectedPosition(i);
                                prePosition = i;
                        }
                        i++;
                }
                display.addOnItemTouchListener(new OnBaseItemClickListener() {
                        @Override
                        protected void onItemClick(BaseWrappedViewHolder baseWrappedViewHolder, int id, View view, int position) {
                                if (position != prePosition) {
                                        selectedImage = adapter.getData(position);
                                        baseWrappedViewHolder.setImageBg(R.id.iv_wallpaper_item_display, selectedImage)
                                                .setImageResource(R.id.iv_wallpaper_item_display, R.drawable.change_background_picture_btn);
//                                        adapter.setSelectedPosition(position);
//                                        adapter.notifyDataSetChanged();
                                        if (prePosition != -1) {
                                                LogUtil.e("清除图片");
                                                ((BaseWrappedViewHolder) display.findViewHolderForAdapterPosition(prePosition)).setImageResource(R.id.iv_wallpaper_item_display, 0);
                                        }
                                }
                                prePosition = position;

                        }
                });
                display.setAdapter(adapter);
        }

        private void initActionBar() {
                ToolBarOption toolBarOption = new ToolBarOption();
                toolBarOption.setRightText("完成");
                toolBarOption.setTitle("1选择背景图片");
                toolBarOption.setAvatar(UserCacheManager.getInstance().getUser().getAvatar());
                toolBarOption.setNeedNavigation(true);
                toolBarOption.setRightListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                if (from.equals("title_wallpaper")) {
                                        if (selectedImage != null && !selectedImage.equals(UserCacheManager.getInstance().getUser()
                                                .getTitleWallPaper())) {
                                                UserManager.getInstance().updateUserInfo("titleWallPaper", selectedImage, new UpdateListener() {
                                                        @Override
                                                        public void onSuccess() {
                                                                LogUtil.e("更改标题背景成功");
                                                                UserCacheManager.getInstance().getUser().setTitleWallPaper(selectedImage);
                                                                setResult(RESULT_OK);
                                                                finish();
                                                        }

                                                        @Override
                                                        public void onFailure(int i, String s) {
                                                                LogUtil.e("上传背景图片到服务器上失败" + s + i);
                                                        }
                                                });
                                        }
                                } else {
                                        if (selectedImage != null && !selectedImage.equals(UserCacheManager.getInstance().getUser().getWallPaper())) {
                                                UserManager.getInstance().updateUserInfo("wallPaper", selectedImage, new UpdateListener() {
                                                        @Override
                                                        public void onSuccess() {
                                                                UserCacheManager.getInstance().getUser().setWallPaper(selectedImage);
                                                                setResult(RESULT_OK);
                                                                finish();
                                                        }

                                                        @Override
                                                        public void onFailure(int i, String s) {
                                                                LogUtil.e("上传背景图片到服务器上失败" + s + i);
                                                        }
                                                });
                                        }
                                }
                        }
                });
                setToolBar(toolBarOption);
        }


}
