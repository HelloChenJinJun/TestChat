package chen.testchat.mvp.NearByPeopleTask;

import org.pointstone.cugappplat.base.basemvp.BaseModel;
import org.pointstone.cugappplat.base.basemvp.BasePresenter;
import org.pointstone.cugappplat.base.basemvp.BaseView;

import java.util.List;

import chen.testchat.bean.User;
import cn.bmob.v3.listener.FindListener;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/5/30      23:10
 * QQ:             1981367757
 */

public interface NearbyPeopleContacts {


        public interface View extends BaseView {
                void updateNearbyPeople(List<User> list);
        }


        public interface Model extends BaseModel {
            public   void queryNearbyPeople(double longitude, double latitude,boolean isAll, boolean sex, FindListener<User> listener);
        }

        public abstract class Presenter extends BasePresenter<View, Model> {
                public abstract void queryNearbyPeople(double longitude, double latitude, boolean isAll,boolean sex);
        }
}
