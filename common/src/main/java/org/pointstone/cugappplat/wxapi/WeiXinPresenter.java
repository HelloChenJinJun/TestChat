package org.pointstone.cugappplat.wxapi;

import org.pointstone.cugappplat.base.basemvp.BaseModel;
import org.pointstone.cugappplat.base.basemvp.BasePresenter;
import org.pointstone.cugappplat.base.basemvp.BaseView;
import org.pointstone.cugappplat.baseadapter.baseloadview.EmptyLayout;
import org.pointstone.cugappplat.db.ResultEntity;
import org.pointstone.cugappplat.net.NetManager;
import org.pointstone.cugappplat.rxbus.RxBusManager;
import org.pointstone.cugappplat.util.Constant;
import org.pointstone.cugappplat.util.LogUtil;
import org.pointstone.cugappplat.util.SPUtils;
import org.pointstone.cugappplat.weixin.CUGUserInfoItem;
import org.pointstone.cugappplat.weixin.WXAccessTokenItem;
import org.pointstone.cugappplat.weixin.WXUserInfoItem;
import org.pointstone.cugappplat.weixin.event.WeiXinEvent;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 项目名称:    Cugappplat
 * 创建人:        陈锦军
 * 创建时间:    2017/4/8      20:19
 * QQ:             1981367757
 */

public class WeiXinPresenter extends BasePresenter<BaseView, BaseModel> {


        private String code;
        private EmptyLayout.OnRetryListener mOnRetryListener = new EmptyLayout.OnRetryListener() {
                @Override
                public void onRetry() {
                        LogUtil.e("重试登录");
                        getData(code);
                }
        };


        public void getData(String code) {
                this.code = code;
                LogUtil.e("getData");
                NetManager.getInstance().getWXUserInfo().getWXAccessToken(Constant.WX_APP_ID, Constant.WX_SECRET, code, "authorization_code")
                        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<WXAccessTokenItem>() {
                                           @Override
                                           public void onCompleted() {
                                                   mView.hideLoading();
                                           }

                                           @Override
                                           public void onError(Throwable e) {
                                                   LogUtil.e(e.getMessage());
                                                   mView.hideLoading();
                                                   mView.showError(e.getMessage(), mOnRetryListener);
                                           }

                                           @Override
                                           public void onNext(WXAccessTokenItem wxAccessTokenItem) {
                                                   LogUtil.e("wxAccessTokenItem");
                                                   NetManager.getInstance().getWXUserInfo().getWXUserInfo(wxAccessTokenItem.getAccess_token(), wxAccessTokenItem.getOpenid())
                                                           .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                                           .subscribe(new Subscriber<WXUserInfoItem>() {
                                                                   @Override
                                                                   public void onCompleted() {
                                                                           mView.hideLoading();
                                                                   }

                                                                   @Override
                                                                   public void onError(Throwable e) {
                                                                           LogUtil.e(e.getMessage());
                                                                           mView.hideLoading();
                                                                           if (e != null) {
                                                                                   mView.showError(e.getMessage(), mOnRetryListener);
                                                                           } else {
                                                                                   mView.showError("错误", mOnRetryListener);
                                                                           }


                                                                   }

                                                                   @Override
                                                                   public void onNext(WXUserInfoItem wxUserInfoItem) {
                                                                           LogUtil.e("wxUserInfoItem");
                                                                           SPUtils.put(SPUtils.UNION_ID, wxUserInfoItem.getUnionid());
                                                                           LogUtil.e("unionid" + SPUtils.get(SPUtils.UNION_ID, ""));
                                                                           SPUtils.put(SPUtils.USER_NICK, wxUserInfoItem.getNickname());
                                                                           LogUtil.e("nick" + SPUtils.get(SPUtils.USER_NICK, ""));
                                                                           SPUtils.put(SPUtils.USER_AVATAR, wxUserInfoItem.getHeadimgurl());
                                                                           LogUtil.e("avatar" + SPUtils.get(SPUtils.USER_AVATAR, ""));
                                                                           SPUtils.put(SPUtils.IS_LOGIN, Boolean.valueOf(true));
                                                                           NetManager.getInstance().getCugUserInfo().getCUGUserInfo(Constant.X_API_KEY, wxUserInfoItem.getUnionid())
                                                                                   .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                                                                   .subscribe(new Subscriber<ResultEntity<CUGUserInfoItem>>() {
                                                                                           @Override
                                                                                           public void onCompleted() {
                                                                                                   LogUtil.e("onCompleted");
                                                                                                   mView.hideLoading();
                                                                                           }

                                                                                           @Override
                                                                                           public void onError(Throwable e) {
                                                                                                   if (e != null) {
                                                                                                           LogUtil.e("转化cuguser出错" + e.getMessage());
                                                                                                   } else {
                                                                                                           LogUtil.e("转化错误");
                                                                                                   }
                                                                                                   mView.hideLoading();
                                                                                                   if (e != null) {
                                                                                                           mView.showError(e.getMessage(), mOnRetryListener);
                                                                                                   } else {
                                                                                                           mView.showError("错误", mOnRetryListener);
                                                                                                   }
                                                                                           }

                                                                                           @Override
                                                                                           public void onNext(ResultEntity<CUGUserInfoItem> cugUserInfoItemResultEntity) {
                                                                                                   LogUtil.e("CUGUserInfoItem");
                                                                                                   final List<CUGUserInfoItem> item = cugUserInfoItemResultEntity.getData();
                                                                                                   LogUtil.e("得到的学生数据");
                                                                                                   LogUtil.e("姓名1" + item.get(0).getName());
                                                                                                   LogUtil.e("学号1:" + item.get(0).getStudent_or_staff_id() + ",," + item.get(0).getIdentity_card_number());
                                                                                                   if (cugUserInfoItemResultEntity.isStatus()) {
                                                                                                           SPUtils.put(SPUtils.USER_NAME, item.get(0).getName());
                                                                                                           LogUtil.e("姓名" + SPUtils.get(SPUtils.USER_NAME, ""));
                                                                                                           SPUtils.put(SPUtils.ACCOUNT_ID, item.get(0).getStudent_or_staff_id());
                                                                                                           LogUtil.e("id" + SPUtils.get("id", ""));
                                                                                                           SPUtils.put("status", Boolean.valueOf(true));
                                                                                                           LogUtil.e("status" + SPUtils.get("status", Boolean.valueOf(false)));
                                                                                                           RxBusManager.getInstance().post(new WeiXinEvent(WeiXinEvent.ACTION_UPDATE));
//                                                                                                           UserManager.getInstance().queryUsers(item.get(0).getStudent_or_staff_id(), new FindListener<User>() {
//                                                                                                                   @Override
//                                                                                                                   public void onSuccess(List<User> list) {
//                                                                                                                           LogUtil.e("用户存在，证明已经注册过，现在登录");
//                                                                                                                           loginBmob();
//
//                                                                                                                   }
//
//                                                                                                                   @Override
//                                                                                                                   public void onError(int i, String s) {
//                                                                                                                           LogUtil.e("查找用户失败，证明没有该用户，在这里注册");
//                                                                                                                           UserManager.getInstance().signUp((String) SPUtils.get(SPUtils.USER_AVATAR, ""), (String) SPUtils
//                                                                                                                                   .get(SPUtils.USER_NICK, ""), (String) SPUtils
//                                                                                                                                   .get(SPUtils.USER_NAME, ""), item.get(0).getStudent_or_staff_id(), CommonUtil.encode(item.get(0).getStudent_or_staff_id()), new SaveListener() {
//                                                                                                                                   @Override
//                                                                                                                                   public void onSuccess() {
//                                                                                                                                           LogUtil.e("注册成功");
//                                                                                                                                           ToastUtils.showShortToast("注册成功");
//                                                                                                                                           loginBmob();
//                                                                                                                                   }
//
//                                                                                                                                   @Override
//                                                                                                                                   public void onFailure(int i, String s) {
//                                                                                                                                           LogUtil.e("注册失败" + s + i);
//                                                                                                                                           mView.hideLoading();
//                                                                                                                                           ToastUtils.showShortToast("注册失败" + s + i);
//                                                                                                                                           mView.showError(s, mOnRetryListener);
//
//                                                                                                                                   }
//                                                                                                                           });
//
//                                                                                                                   }
//                                                                                                           });
//                                                                                                           与bmob后台user 绑定
                                                                                                   }
                                                                                           }
                                                                                   });
                                                                   }
                                                           });
                                           }
                                   }
                        );
        }

//        private void loginBmob() {
//                LogUtil.e("loginBmob");
//                UserManager.login((String) SPUtils.get(SPUtils.ACCOUNT_ID, ""), CommonUtil.encode((String) SPUtils.get(SPUtils.ACCOUNT_ID, "")),
//                        new SaveListener() {
//                                @Override
//                                public void onSuccess() {
//                                        LogUtil.e("登录成功");
//                                        ToastUtils.showShortToast("登录成功");
//                                        mView.hideLoading();
//                                        RxBusManager.getInstance().post(new WeiXinEvent(WeiXinEvent.ACTION_UPDATE));
//                                }
//
//                                @Override
//                                public void onFailure(int i, String s) {
//                                        LogUtil.e("登录失败" + s + i);
//                                        ToastUtils.showShortToast("登录失败" + s + i);
//                                        mView.hideLoading();
//                                        mView.showError(s, mOnRetryListener);
//                                }
//                        });
//        }
}
