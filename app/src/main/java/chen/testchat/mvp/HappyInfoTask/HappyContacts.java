package chen.testchat.mvp.HappyInfoTask;

import org.pointstone.cugappplat.base.basemvp.BasePresenter;
import org.pointstone.cugappplat.base.basemvp.BaseView;

import java.util.List;

import chen.testchat.bean.HappyBean;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/7      19:12
 * QQ:             1981367757
 */

public interface HappyContacts {


        public interface View extends BaseView {
                void onUpdateHappyInfo(List<HappyBean> data);
        }


        public interface Model {
                boolean saveHappyInfo(String key, String json);
                List<HappyBean> getHappyInfo(String key);
                boolean clearAllCacheHappyData();
        }

        abstract class Presenter extends BasePresenter<View, Model> {
                abstract public void getHappyInfo(int page);
        }
}
