package chen.testchat.mvp.WinXinInfoTask;

import java.util.List;

import chen.testchat.bean.WinXinBean;
import chen.testchat.db.ChatDB;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/7      10:12
 * QQ:             1981367757
 */

public class WinXinInfoModel implements WinXinInfoContacts.Model {


        @Override
        public boolean saveCacheWeiXinInfo(String key, String json) {
                return ChatDB.create().saveWeiXinInfo(key, json);
        }

        @Override
        public List<WinXinBean> getCacheWeiXinInfo(String key) {
                return ChatDB.create().getWeiXinInfo(key);
        }

        @Override
        public boolean clearAllData() {
                return ChatDB.create().clearAllWeiXinData();
        }
}
