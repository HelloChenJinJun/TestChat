package chen.testchat.mvp.HappyInfoTask;

import java.util.List;

import chen.testchat.bean.HappyBean;
import chen.testchat.db.ChatDB;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/7      21:05
 * QQ:             1981367757
 */

public class HappyInfoModel implements HappyContacts.Model {
        @Override
        public boolean saveHappyInfo(String key, String json) {
                return ChatDB.create().saveHappyInfo(key, json);
        }

        @Override
        public List<HappyBean> getHappyInfo(String key) {
                return ChatDB.create().getHappyInfo(key);
        }

        @Override
        public boolean clearAllCacheHappyData() {
                return ChatDB.create().clearAllCacheHappyData();
        }
}
