package org.pointstone.cugappplat.base.basemvp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import org.common.R;
import org.pointstone.cugappplat.base.cusotomview.BaseDialog;
import org.pointstone.cugappplat.base.cusotomview.RoundAngleImageView;
import org.pointstone.cugappplat.base.cusotomview.ToolBarOption;
import org.pointstone.cugappplat.baseadapter.baseloadview.EmptyLayout;
import org.pointstone.cugappplat.rxbus.RxBusManager;
import org.pointstone.cugappplat.util.LogUtil;
import org.pointstone.cugappplat.util.ToastUtils;
import org.pointstone.cugappplat.weixin.event.WeiXinEvent;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

import static android.view.View.GONE;

/**
 * 项目名称:    Cugappplat
 * 创建人:        陈锦军
 * 创建时间:    2017/4/3      14:21
 * QQ:             1981367757
 */

public abstract class BaseActivity extends RxAppCompatActivity implements BaseView, Action1<WeiXinEvent> {

    //  这里的布局view可能为空，取决于子类布局中是否含有该空布局
    private EmptyLayout mEmptyLayout;
    protected int fragmentContainerResId = 0;
    protected Fragment currentFragment;
    private RelativeLayout headerLayout;
    private ProgressDialog mProgressDialog;
    protected BaseDialog mBaseDialog;
    private RoundAngleImageView icon;
    private TextView right;
    private TextView title;
    private ImageView rightImage;
    protected ImageView back;


    public ImageView getBack() {
        return back;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isNeedHeadLayout()) {
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            headerLayout = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.header_layout, null);
            linearLayout.addView(headerLayout);
            if (isNeedEmptyLayout()) {
                FrameLayout frameLayout = new FrameLayout(this);
                frameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                mEmptyLayout = new EmptyLayout(this);
                mEmptyLayout.setVisibility(GONE);
                frameLayout.addView(LayoutInflater.from(this).inflate(getContentLayout(), null));
                frameLayout.addView(mEmptyLayout);
                linearLayout.addView(frameLayout);
                setContentView(linearLayout);
            } else {
                linearLayout.addView(LayoutInflater.from(this).inflate(getContentLayout(), null));
                setContentView(linearLayout);
            }
        } else {
            if (isNeedEmptyLayout()) {
                FrameLayout frameLayout = new FrameLayout(this);
                frameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                mEmptyLayout = new EmptyLayout(this);
                mEmptyLayout.setVisibility(GONE);
                frameLayout.addView(LayoutInflater.from(this).inflate(getContentLayout(), null));
                frameLayout.addView(mEmptyLayout);
                setContentView(frameLayout);
            } else {
                setContentView(getContentLayout());
            }
        }
        RxBusManager.getInstance().registerEvent(WeiXinEvent.class, this, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                if (throwable != null) {
                    LogUtil.e("微信登录出现错误" + throwable.getMessage());
                }
            }
        });
        LogUtil.e("onCreate123456");
        initBaseView();
        initData();
    }

    protected RoundAngleImageView getIcon() {
        return icon;
    }


    @Override
    protected void onResume() {
        super.onResume();
        checkLogin();
    }

    private void checkLogin() {
    }

    protected TextView getCustomTitle() {
        return title;
    }

    protected abstract boolean isNeedHeadLayout();

    protected abstract boolean isNeedEmptyLayout();

    protected abstract int getContentLayout();

    protected abstract void initView();

    protected abstract void initData();


    protected void initBaseView() {
        LogUtil.e("base initView111222333");
//                mEmptyLayout = (EmptyLayout) headerLayout.findViewById(R.id.fl_empty_layout);
        if (isNeedHeadLayout()) {
            icon = (RoundAngleImageView) headerLayout.findViewById(R.id.riv_header_layout_icon);
            title = (TextView) headerLayout.findViewById(R.id.tv_header_layout_title);
            right = (TextView) headerLayout.findViewById(R.id.tv_header_layout_right);
            back = (ImageView) headerLayout.findViewById(R.id.iv_header_layout_back);
            rightImage = (ImageView) headerLayout.findViewById(R.id.iv_header_layout_right);
            rightImage.setVisibility(View.GONE);
            right.setVisibility(View.VISIBLE);
        }
        mProgressDialog = new ProgressDialog(this);
        mBaseDialog = new BaseDialog(this);
        initView();
    }


    /**
     * 隐藏软键盘
     */
    protected void hideSoftInpuutView() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN && getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 隐藏软键盘-一般是EditText.getWindowToken()
     *
     * @param token 标识
     */
    protected void hideSoftInput(IBinder token) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
    }


    protected void showEmptyLayout(int status) {
        if (mEmptyLayout != null) {
            mEmptyLayout.setCurrentStatus(status);
        }
    }


    public void setToolBar(ToolBarOption option) {
        if (!isNeedHeadLayout()) {
            return;
        }
        if (option.getAvatar() != null) {
            icon.setVisibility(View.VISIBLE);
            Glide.with(this).load(option.getAvatar()).into(icon);
        } else {
            icon.setVisibility(GONE);
        }

        if (option.getRightResId() != 0) {
            right.setVisibility(GONE);
            rightImage.setVisibility(View.VISIBLE);
            rightImage.setImageResource(option.getRightResId());
            rightImage.setOnClickListener(option.getRightListener());
        } else if (option.getRightText() != null) {
            right.setVisibility(View.VISIBLE);
            rightImage.setVisibility(GONE);
            right.setText(option.getRightText());
            right.setOnClickListener(option.getRightListener());
        } else {
            right.setVisibility(GONE);
            rightImage.setVisibility(GONE);
        }
        if (option.getTitle() != null) {
            title.setVisibility(View.VISIBLE);
            title.setText(option.getTitle());
        } else {
            title.setVisibility(GONE);
        }
        if (option.isNeedNavigation()) {
            back.setVisibility(View.VISIBLE);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } else {
            back.setVisibility(GONE);
        }

    }

    public int getStatusHeight() {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    public void showBaseDialog(String title, String message, String leftName, String rightName, View.OnClickListener leftListener, View.OnClickListener rightListener) {
        mBaseDialog.setTitle(title).setMessage(message).setLeftButton(leftName, leftListener).setRightButton(rightName, rightListener).show();
    }

    public void dismissBaseDialog() {
        if (mBaseDialog != null && mBaseDialog.isShowing()) {
            mBaseDialog.dismiss();
        }
    }

    public void cancelBaseDialog() {
        if (mBaseDialog != null && mBaseDialog.isShowing()) {
            mBaseDialog.cancel();
        }
    }


    public void showLoadDialog(final String message) {
        LogUtil.e("message111222");
        if (!isFinishing()) {
            mProgressDialog.setMessage(message);
            mProgressDialog.show();
        }
    }

    public void dismissLoadDialog() {
        showEmptyLayout(EmptyLayout.STATUS_HIDE);
        if (!isFinishing()) {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        }
    }

    public void cancelLoadDialog() {
        if (!isFinishing()) {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.cancel();
            }
        }
    }

    public void showChooseDialog(String title, List<String> list, AdapterView.OnItemClickListener listener) {
        ListView view = (ListView) getLayoutInflater().inflate(R.layout.base_dialog_list, null);
        view.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list));
        view.setOnItemClickListener(listener);
        mBaseDialog.setDialogContentView(view).setTitle(title).setBottomLayoutVisible(false).show();
    }


    public void showEditDialog(String title, List<String> names, final OnEditDataCompletedListener listener) {
        if (names != null && names.size() > 0) {
            mBaseDialog.setTitle(title).setEditViewsName(names).setLeftButton("取消", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancelBaseDialog();
                }
            }).setRightButton("确定", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int size = mBaseDialog.getMiddleLayout().getChildCount();
                        List<String> data = new ArrayList<>();
                        for (int i = 0; i < size; i++) {
                            String result = ((EditText) ((LinearLayout) mBaseDialog.getMiddleLayout().getChildAt(i)).getChildAt(1)).getText().toString().trim();
                            if (result.equals("")) {
                                ToastUtils.showShortToast("1输入内容不能为空");
                                LogUtil.e("输入框不能为空");
                                return;
                            }
                            data.add(result);
                        }
                        listener.onDataInputCompleted(data);
                        dismissBaseDialog();
                    }
                }
            }).show();
        }
    }


    public void addOrReplaceFragment(Fragment fragment) {
        addOrReplaceFragment(fragment, 0);
    }

    /**
     * 第一次加载的时候调用该方法设置resId
     *
     * @param fragment
     * @param resId
     */
    public void addOrReplaceFragment(Fragment fragment, int resId) {
        if (resId != 0) {
            fragmentContainerResId = resId;
        }
        if (fragment == null) {
            return;
        }
        if (currentFragment == null) {
            getSupportFragmentManager().beginTransaction().add(resId, fragment).show(fragment).commitAllowingStateLoss();
            currentFragment = fragment;
            return;
        }
        if (fragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().hide(currentFragment).show(fragment).commit();
        } else {
            getSupportFragmentManager().beginTransaction().hide(currentFragment).add(fragmentContainerResId, fragment).show(fragment).commitAllowingStateLoss();
        }
        currentFragment = fragment;
    }


    @Override
    public void showLoading(String loadingMsg) {
        if (mEmptyLayout != null) {
            mEmptyLayout.setCurrentStatus(EmptyLayout.STATUS_LOADING);
        } else {
            showLoadDialog(loadingMsg);
        }
    }

    @Override
    public void hideLoading() {
        if (mEmptyLayout != null) {
            if (mEmptyLayout.getCurrentStatus() != EmptyLayout.STATUS_HIDE) {
                mEmptyLayout.setCurrentStatus(EmptyLayout.STATUS_HIDE);
            }
        } else {
            dismissLoadDialog();
        }

    }

    @Override
    public void showError(String errorMsg, EmptyLayout.OnRetryListener listener) {
        if (mEmptyLayout != null) {
            mEmptyLayout.setCurrentStatus(EmptyLayout.STATUS_NO_NET);
            if (listener != null) {
                mEmptyLayout.setOnRetryListener(listener);
            }
        } else {
            ToastUtils.showShortToast(errorMsg);

        }

    }

    @Override
    public <t> LifecycleTransformer<t> bindLife() {
        return bindToLifecycle();
    }

    @Override
    public void call(WeiXinEvent weiXinEvent) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBusManager.getInstance().unSubscrible(WeiXinEvent.class);
    }
}
