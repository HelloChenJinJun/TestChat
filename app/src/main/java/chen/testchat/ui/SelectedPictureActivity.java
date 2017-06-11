package chen.testchat.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.pointstone.cugappplat.util.ToastUtils;

import java.io.File;
import java.util.List;

import chen.testchat.R;
import chen.testchat.adapter.ImageFoldersAdapter;
import chen.testchat.adapter.SelectedPictureAdapter;
import chen.testchat.base.CommonImageLoader;
import chen.testchat.base.Constant;
import chen.testchat.bean.ImageFolder;
import chen.testchat.bean.ImageItem;
import chen.testchat.listener.OnImageLoadListener;
import chen.testchat.util.LoadPictureUtil;
import chen.testchat.util.LogUtil;
import chen.testchat.view.ImageFolderPopupWindow;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/11/29      12:01
 * QQ:             1981367757
 */

public class SelectedPictureActivity extends SlideBaseActivity implements View.OnClickListener, OnImageLoadListener, SelectedPictureAdapter.OnItemCheckClickListener {
        private ImageView back;
        private Button finish;
        private Button all;
        private Button pre;
        private RelativeLayout bottomView;
        private SelectedPictureAdapter mAdapter;
        private RecyclerView display;
        private File photoFile;
        private ImageFoldersAdapter mImageFoldersAdapter;
        private ImageFolderPopupWindow imageFolderPopupWindow;


        @Override
        public void initView() {
                back = (ImageView) findViewById(R.id.iv_picture_top_bar_back);
                finish = (Button) findViewById(R.id.btn_picture_top_bar_finish);
                all = (Button) findViewById(R.id.btn_selected_picture_all);
                pre = (Button) findViewById(R.id.btn_selected_picture_pre);
                display = (RecyclerView) findViewById(R.id.rcv_selected_picture_display);
                bottomView = (RelativeLayout) findViewById(R.id.rl_selected_picture_bottom);
                back.setOnClickListener(this);
                finish.setOnClickListener(this);
                all.setOnClickListener(this);
                pre.setOnClickListener(this);
        }


        @Override
        public void initData() {
                LogUtil.e("这里1");
                new LoadPictureUtil().getAllImageFolder(this, this);
                mAdapter = new SelectedPictureAdapter();
                mImageFoldersAdapter = new ImageFoldersAdapter(null, R.layout.image_folder_item);
                mAdapter.setOnItemCheckClickListener(this);
                display.setLayoutManager(new GridLayoutManager(this, 3));
                display.setItemAnimator(new DefaultItemAnimator());
                display.setAdapter(mAdapter);
        }


        @Override
        protected void onResume() {
                super.onResume();
                if (CommonImageLoader.getInstance().getImageFolders().size() > 0) {
                        LogUtil.e("onResume绑定数据");
                        mAdapter.notifyDataSetChanged();
                        notifyTextChanged();
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
                return R.layout.selected_picture;
        }

        @Override
        public void onClick(View v) {
                switch (v.getId()) {
                        case R.id.iv_picture_top_bar_back:
                                CommonImageLoader.getInstance().getSelectedImages().clear();
                                finish();
                                break;
                        case R.id.btn_picture_top_bar_finish:
                                setResult(Activity.RESULT_OK);
                                finish();
                                break;
                        case R.id.btn_selected_picture_all:
                                List<ImageFolder> list = CommonImageLoader.getInstance().getImageFolders();
                                if (list == null || list.size() == 0) {
                                        ToastUtils.showShortToast("当前没有任何照片");
                                        return;
                                }
                                if (imageFolderPopupWindow == null) {
                                        LogUtil.e("创建下拉窗口123");
                                        imageFolderPopupWindow = new ImageFolderPopupWindow(this, mImageFoldersAdapter) {
                                                @Override
                                                public void onItemClick(View view, int position, long id) {
                                                        mImageFoldersAdapter.setCurrentSelectedPosition(position);
                                                        LogUtil.e("1");
                                                        CommonImageLoader.getInstance().setCurrentImageFolderPosition(position);
                                                        LogUtil.e("2");
                                                        ImageFolder imageFolder = mImageFoldersAdapter.getData(position);
                                                        LogUtil.e("3");
                                                        if (imageFolder != null) {
                                                                LogUtil.e("4");
                                                                mAdapter.clearData();
                                                                LogUtil.e("55");
                                                                ImageItem imageItem = new ImageItem();
                                                                imageItem.setItemType(ImageItem.ITEM_CAMERA);
                                                                mAdapter.addData(imageItem);
                                                                LogUtil.e("66");
                                                                mAdapter.addData(imageFolder.getAllImages());
                                                                LogUtil.e("7");
                                                                all.setText(imageFolder.getName());
//                                                                display.smoothScrollToPosition(0);
                                                        }
                                                        imageFolderPopupWindow.dismiss();
                                                }
                                        };
//                                        imageFolderPopupWindow.setMarginSize(bottomView.getHeight());
                                        imageFolderPopupWindow.showAtLocation(bottomView, Gravity.NO_GRAVITY, 0, 0);
                                } else {
                                        if (imageFolderPopupWindow.isShowing()) {
                                                LogUtil.e("这里隐藏");
                                                imageFolderPopupWindow.dismiss();
                                        } else {
                                                LogUtil.e("这里展开");
                                                imageFolderPopupWindow.showAtLocation(bottomView, Gravity.NO_GRAVITY, 0, 0);
                                                mImageFoldersAdapter.notifyDataSetChanged();
                                        }
                                }
                                break;
                        case R.id.btn_selected_picture_pre:
                                Intent intent = new Intent(this, BasePreViewActivity.class);
                                intent.putExtra(CommonImageLoader.PREVIEW_FROM, CommonImageLoader.PREVIEW_SELECT);
                                startActivity(intent);
                                break;
                }
        }


        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                if (resultCode == Activity.RESULT_OK) {
                        switch (requestCode) {
                                case Constant.REQUEST_CODE_TAKE_PHOTO:
                                        if (data == null) {
//                                                由于拍照指定了uri，所以返回的数据为null
                                                ImageItem imageItem = new ImageItem();
                                                imageItem.setPath(photoFile.getAbsolutePath());
                                                CommonImageLoader.getInstance().getSelectedImages().add(imageItem);
                                                setResult(Activity.RESULT_OK);
                                                finish();
                                        }
                                        break;
                        }
                }
                super.onActivityResult(requestCode, resultCode, data);

        }


//        /**
//         * 拍照操作
//         * 并把照片存储在file中
//         */
//        private File takePicture() {
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                File file=null;
//                if (intent.resolveActivity(getPackageManager()) != null) {
//                        File dir;
//                        if (FileUtil.isExistSDCard()) {
//                                dir = FileUtil.newDir(Constant.IMAGE_CACHE_DIR + "take_picture/");
//                        } else {
//                                dir = Environment.getDataDirectory();
//                        }
//                        if (dir != null) {
//                                file = FileUtil.newFile(dir.getAbsolutePath() + System.currentTimeMillis() + ".jpg");
//                                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
//                                startActivityForResult(intent, Constant.REQUEST_CODE_TAKE_PHOTO);
//                        } else {
//                                Toast.makeText(this, "创建图片目录失败", Toast.LENGTH_SHORT).show();
//                        }
//                }
//                return file;
//        }


        private void notifyTextChanged() {
                int selectedSize = CommonImageLoader.getInstance().getSelectedImages().size();
                if (selectedSize > 0) {
                        pre.setEnabled(true);
                        pre.setText(getString(R.string.bottom_preview_count, selectedSize));
                        finish.setEnabled(true);
                        finish.setText(getString(R.string.finish_count, selectedSize, CommonImageLoader.getInstance().getMaxSelectedCount()));
                } else {
                        pre.setEnabled(false);
                        pre.setText(getString(R.string.bottom_preview));
                        finish.setEnabled(false);
                        finish.setText(getString(R.string.finish));
                }
        }

        @Override
        public void onImageLoaded(List<ImageFolder> imageFolderList) {
                LogUtil.e("加载数据完成");
                if (imageFolderList.size() > 0) {
                        LogUtil.e("存入内存");
                        CommonImageLoader.getInstance().setImageFolders(imageFolderList);
                        LogUtil.e("图片文件夹个数:" + CommonImageLoader.getInstance().getImageFolders().size());
                        LogUtil.e("现在选择的图片文件夹中图片的个数" + CommonImageLoader.getInstance().getCurrentImageFolder().getAllImages().size());
                        LogUtil.e("加载数据完成" + imageFolderList.size());
                        mAdapter.clearData();
                        mAdapter.addData(CommonImageLoader.getInstance().getCurrentImageFolder().getAllImages());
                        ImageItem imageItem = new ImageItem();
                        imageItem.setItemType(ImageItem.ITEM_CAMERA);
                        mAdapter.addData(0, imageItem);
                        mImageFoldersAdapter.addData(CommonImageLoader.getInstance().getImageFolders());
                }
                notifyTextChanged();
        }

        @Override
        public void onItemCheck(CheckBox checkBox, int position) {
                notifyTextChanged();
        }

        @Override
        public void onItemClick(View view, int position) {
                if (position != 0) {




                        LogUtil.e("位置是多少" + position);
                        Intent intent = new Intent(SelectedPictureActivity.this, BasePreViewActivity.class);
                        intent.putExtra(CommonImageLoader.CURRENT_POSITION, (position - 1));
                        intent.putExtra(CommonImageLoader.PREVIEW_FROM, CommonImageLoader.PREVIEW_SELECT);
                        startActivity(intent);
                } else {
                        photoFile = CommonImageLoader.getInstance().takePhoto(this, Constant.REQUEST_CODE_TAKE_PHOTO);
                }
        }

        @Override
        public void onBackPressed() {
                if (imageFolderPopupWindow != null && imageFolderPopupWindow.isShowing()) {
                        imageFolderPopupWindow.dismiss();
                } else {
                        super.onBackPressed();
                        CommonImageLoader.getInstance().clearAllData();
                }
        }
}
