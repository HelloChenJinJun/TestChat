package chen.testchat.mvp.NearByPeopleTask;

import chen.testchat.bean.User;
import chen.testchat.manager.UserManager;
import cn.bmob.v3.listener.FindListener;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/5/30      23:10
 * QQ:             1981367757
 */

public class NearbyPeopleModel implements NearbyPeopleContacts.Model {

        @Override
        public void queryNearbyPeople(double longitude, double latitude,boolean isAll, boolean sex, FindListener<User> listener) {
                UserManager.getInstance().queryNearbyPeople(longitude, latitude, isAll,sex, listener);
        }
}
