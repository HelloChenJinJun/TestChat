package chen.testchat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import org.pointstone.cugappplat.util.ToastUtils;

import chen.testchat.manager.UserManager;

/**
 * 项目名称:    HappyChat
 * 创建人:        陈锦军
 * 创建时间:    2016/9/13      11:46
 * QQ:             1981367757
 */
public abstract class MainBaseActivity extends org.pointstone.cugappplat.base.basemvp.BaseActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void onResume() {
        super.onResume();
        checkLogin();
    }

    private void checkLogin() {
        if (UserManager.getInstance().getCurrentUser() == null) {
            ToastUtils.showShortToast("你的帐号已在其他设备登陆,请重新登录!");
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

}
