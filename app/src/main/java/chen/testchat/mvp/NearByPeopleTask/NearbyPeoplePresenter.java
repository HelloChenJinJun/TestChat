package chen.testchat.mvp.NearByPeopleTask;

import org.pointstone.cugappplat.baseadapter.baseloadview.EmptyLayout;

import java.util.List;

import chen.testchat.bean.User;
import cn.bmob.v3.listener.FindListener;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/5/30      23:09
 * QQ:             1981367757
 */

public class NearbyPeoplePresenter extends NearbyPeopleContacts.Presenter {

        private double longitude;
        private double latitude;
        private boolean sex;
        private FindListener<User> mUserFindListener;
        private boolean isAll;

        @Override
        public void queryNearbyPeople(double longitude, double latitude, boolean isAll,boolean sex) {
                this.longitude = longitude;
                this.latitude = latitude;
                this.sex = sex;
                if (mUserFindListener == null) {
                        mUserFindListener = new FindListener<User>() {
                                @Override
                                public void onSuccess(List<User> list) {
                                        mView.hideLoading();
                                        mView.updateNearbyPeople(list);
                                }

                                @Override
                                public void onError(int i, String s) {
                                        mView.hideLoading();
                                        mView.showError(s, new EmptyLayout.OnRetryListener() {
                                                @Override
                                                public void onRetry() {
                                                        queryNearbyPeople(NearbyPeoplePresenter.this.longitude, NearbyPeoplePresenter.this.latitude, NearbyPeoplePresenter.this.isAll,NearbyPeoplePresenter.this.sex);
                                                }
                                        });
                                }
                        };
                }
                mView.showLoading("正在加载附近人消息。。。请稍后");
                mModel.queryNearbyPeople(longitude, latitude, isAll,sex, mUserFindListener);
        }
}
