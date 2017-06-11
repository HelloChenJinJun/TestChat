package chen.testchat.mvp.UserInfoTask;

import org.pointstone.cugappplat.base.basemvp.BasePresenter;
import org.pointstone.cugappplat.base.basemvp.BaseView;

import chen.testchat.bean.User;
import chen.testchat.listener.DealUserInfoCallBack;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/12/25      13:49
 * QQ:             1981367757
 */

public interface UserInfoContacts {
        interface View extends BaseView {
                void updateUserAvatar(String uid, String avatar);

                void updateUserSignature(String uid, String signature);

                void updateUserNick(String uid, String nick);

                void updateUserInfo(User user);
        }


        interface Model {
                void updateUserAvatar(String uid, String avatar, DealUserInfoCallBack dealUserInfoCallBack);

                void updateUserSignature(String uid, String signature, DealUserInfoCallBack dealUserInfoCallBack);

                void updateUserNick(String uid, String nick, DealUserInfoCallBack dealUserInfoCallBack);

                void updateUserInfo(User user, DealUserInfoCallBack dealUserInfoCallBack);
        }

        abstract class Presenter extends BasePresenter<View, Model> {

                public abstract void modifyUserAvatar(String uid, String avatar, DealUserInfoCallBack dealUserInfoCallBack);

                public abstract void modifyUserNick(String uid, String nick, DealUserInfoCallBack dealUserInfoCallBack);

                public abstract void modifyUserSignature(String uid, String signature, DealUserInfoCallBack dealUserInfoCallBack);

                public abstract void modifyUserInfo(User user, DealUserInfoCallBack dealUserInfoCallBack);
        }


}
