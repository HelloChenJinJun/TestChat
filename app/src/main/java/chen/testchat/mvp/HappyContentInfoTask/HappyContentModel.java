package chen.testchat.mvp.HappyContentInfoTask;

import java.util.List;

import chen.testchat.bean.HappyContentBean;
import chen.testchat.db.ChatDB;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/8      20:32
 * QQ:             1981367757
 */

public class HappyContentModel implements HappyContentContacts.Model {
        @Override
        public boolean saveHappyContentInfo(String key, String json) {
                ChatDB.create().saveHappyContentInfo(key,json);
                return false;
        }

        @Override
        public List<HappyContentBean> getHappyContentInfo(String key) {
                return ChatDB.create().getHappyContentInfo(key);
        }

        @Override
        public boolean clearAllCacheHappyContentData() {
                return ChatDB.create().clearAllCacheHappyContentData();
        }
}
