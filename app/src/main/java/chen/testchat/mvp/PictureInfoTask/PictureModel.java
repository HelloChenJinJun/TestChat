package chen.testchat.mvp.PictureInfoTask;

import java.nio.channels.Channel;
import java.util.List;

import chen.testchat.bean.PictureBean;
import chen.testchat.db.ChatDB;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/9      0:10
 * QQ:             1981367757
 */

public class PictureModel implements PictureContacts.Model {
        @Override
        public boolean savePictureInfo(String key, String json) {
                return ChatDB.create().savePictureInfo(key, json);
        }

        @Override
        public List<PictureBean> getPictureInfo(String key) {
                return ChatDB.create().getPictureInfo(key);
        }

        @Override
        public boolean clearAllCacheData() {
                ChatDB.create().clearAllPictureData();
                return false;
        }
}
