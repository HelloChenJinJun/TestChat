package org.pointstone.cugappplat.wxapi;

import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

import org.pointstone.cugappplat.base.BaseApplication;
import org.pointstone.cugappplat.base.basemvp.BaseActivity;
import org.pointstone.cugappplat.util.LogUtil;


/**
 * Created by Administrator on 2016/12/1.
 */

public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {
        private WeiXinPresenter mWeiXinPresenter;

        private void handleIntent(Intent paramIntent) {
                BaseApplication.api.handleIntent(paramIntent, this);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                LogUtil.e("common oncreate");
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
                return org.common.R.layout.activity_weixin;
        }

        @Override
        protected void initView() {
                LogUtil.e("entryInitView111111");

        }

        @Override
        protected void initData() {
                mWeiXinPresenter=new WeiXinPresenter();
                mWeiXinPresenter.setViewAndModel(this,null);
                handleIntent(getIntent());
        }

        @Override
        public void onReq(BaseReq arg0) {
                LogUtil.e("onReq");
        }

        @Override

        public void onResp(BaseResp baseResp) {
                LogUtil.e("接收到onResp");
                if (baseResp.errCode == BaseResp.ErrCode.ERR_OK) {
                        mWeiXinPresenter.getData(((SendAuth.Resp) baseResp).code);
                }else{
                        hideLoading();
                        LogUtil.e("接收到的errCode"+baseResp.errCode+baseResp.errStr);
                }
        }

        @Override
        public void hideLoading() {
                super.hideLoading();
                finish();
        }
}

