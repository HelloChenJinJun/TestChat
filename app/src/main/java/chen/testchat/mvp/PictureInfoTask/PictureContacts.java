package chen.testchat.mvp.PictureInfoTask;

import org.pointstone.cugappplat.base.basemvp.BasePresenter;
import org.pointstone.cugappplat.base.basemvp.BaseView;

import java.util.List;

import chen.testchat.bean.PictureBean;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/9      0:08
 * QQ:             1981367757
 */

public interface PictureContacts {
        public interface View extends BaseView {
                void onUpdatePictureInfo(List<PictureBean> data);
        }


        public interface Model {
                boolean savePictureInfo(String key, String json);

                List<PictureBean> getPictureInfo(String key);

                boolean clearAllCacheData();
        }

        abstract class Presenter extends BasePresenter<View, Model> {
                abstract public void getPictureInfo(int page);
        }
}
