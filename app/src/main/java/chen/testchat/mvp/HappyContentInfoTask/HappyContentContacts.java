package chen.testchat.mvp.HappyContentInfoTask;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/8      20:29
 * QQ:             1981367757
 */

import org.pointstone.cugappplat.base.basemvp.BasePresenter;
import org.pointstone.cugappplat.base.basemvp.BaseView;

import java.util.List;

import chen.testchat.bean.HappyContentBean;


/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/7      19:12
 * QQ:             1981367757
 */

public interface HappyContentContacts {


        public interface View extends BaseView {
                void onUpdateHappyContentInfo(List<HappyContentBean> data);
        }


        public interface Model {
                public boolean saveHappyContentInfo(String key, String json);

                public List<HappyContentBean> getHappyContentInfo(String key);
                boolean clearAllCacheHappyContentData();
        }

        abstract class Presenter extends BasePresenter<View, Model> {
                abstract public void getHappyContentInfo(int page);
        }
}

