package chen.testchat.mvp.WinXinInfoTask;

import org.pointstone.cugappplat.base.basemvp.BasePresenter;
import org.pointstone.cugappplat.base.basemvp.BaseView;

import java.util.List;

import chen.testchat.bean.WinXinBean;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/7      0:16
 * QQ:             1981367757
 */

public interface WinXinInfoContacts {
        public interface View extends BaseView {
                void updateData(List<WinXinBean> data);
        }


        public interface Model {
                boolean saveCacheWeiXinInfo(String key, String json);

                List<WinXinBean> getCacheWeiXinInfo(String key);


                boolean clearAllData();
        }

        abstract class Presenter extends BasePresenter<View, Model> {
                abstract public void getWinXinInfo(int page);
        }
}
