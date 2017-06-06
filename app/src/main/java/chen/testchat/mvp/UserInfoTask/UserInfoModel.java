package chen.testchat.mvp.UserInfoTask;

import chen.testchat.bean.User;
import chen.testchat.db.ChatDB;
import chen.testchat.listener.DealUserInfoCallBack;
import chen.testchat.listener.SimpleUserInfoListenerImpl;
import chen.testchat.manager.MsgManager;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/12/25      14:04
 * QQ:             1981367757
 */

public class UserInfoModel implements UserInfoContacts.Model {
        @Override
        public void updateUserAvatar(String uid, String avatar, final DealUserInfoCallBack dealUserInfoCallBack) {
                MsgManager.getInstance().updateUserAvatar(uid, avatar, new SimpleUserInfoListenerImpl() {
                        @Override
                        public void updateAvatarSuccess(String id, String avatar) {
                                super.updateAvatarSuccess(id, avatar);
                                ChatDB.create().updateUserAvatar(id, avatar);
                                dealUserInfoCallBack.updateAvatarSuccess(id, avatar);
                        }


                        @Override
                        public void onFailed(String uid, int errorId, String errorMsg) {
                                super.onFailed(uid, errorId, errorMsg);
                                dealUserInfoCallBack.onFailed(uid, errorId, errorMsg);
                        }
                });
        }

        @Override
        public void updateUserSignature(String uid, String signature, final DealUserInfoCallBack dealUserInfoCallBack) {
                MsgManager.getInstance().updateUserSignature(uid, signature, new SimpleUserInfoListenerImpl() {
                        @Override
                        public void updateSignatureSuccess(String id, String signature) {
                                super.updateSignatureSuccess(id, signature);
                                ChatDB.create().updateUserSignature(id, signature);
                                dealUserInfoCallBack.updateSignatureSuccess(id, signature);
                        }

                        @Override
                        public void onFailed(String uid, int errorId, String errorMsg) {
                                super.onFailed(uid, errorId, errorMsg);
                                dealUserInfoCallBack.onFailed(uid, errorId, errorMsg);
                        }
                });

        }

        @Override
        public void updateUserNick(String uid, String nick, final DealUserInfoCallBack dealUserInfoCallBack) {
                MsgManager.getInstance().updateUserNick(uid, nick, new SimpleUserInfoListenerImpl() {
                        @Override
                        public void updateNickSuccess(String id, String nick) {
                                super.updateNickSuccess(id, nick);
                                ChatDB.create().updateUserNick(id, nick);
                                dealUserInfoCallBack.updateNickSuccess(id, nick);


                        }

                        @Override
                        public void onFailed(String uid, int errorId, String errorMsg) {
                                super.onFailed(uid, errorId, errorMsg);
                                dealUserInfoCallBack.onFailed(uid, errorId, errorMsg);
                        }
                });

        }

        @Override
        public void updateUserInfo(final User user, final DealUserInfoCallBack dealUserInfoCallBack) {
                MsgManager.getInstance().updateUserinfo(user, new SimpleUserInfoListenerImpl() {
                        @Override
                        public void updateUserInfoSuccess(User user) {
                                super.updateUserInfoSuccess(user);
                                ChatDB.create().addOrUpdateContacts(user);
                                dealUserInfoCallBack.updateUserInfoSuccess(user);
                        }

                        @Override
                        public void onFailed(String uid, int errorId, String errorMsg) {
                                super.onFailed(uid, errorId, errorMsg);
                                dealUserInfoCallBack.onFailed(uid, errorId, errorMsg);
                        }
                });
        }
}
