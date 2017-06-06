package chen.testchat.mvp.ShareMessageTask;

import org.pointstone.cugappplat.base.basemvp.BasePresenter;
import org.pointstone.cugappplat.base.basemvp.BaseView;

import java.util.List;

import chen.testchat.bean.SharedMessage;
import chen.testchat.listener.AddShareMessageCallBack;
import chen.testchat.listener.DealCommentMsgCallBack;
import chen.testchat.listener.DealMessageCallBack;
import chen.testchat.listener.LoadShareMessageCallBack;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/12/2      19:09
 * QQ:             1981367757
 */

public interface ShareMessageContacts {
        interface View extends BaseView {
                //                这里添加view更新数据的功能
//                添加一个说说item id 为objectId
                void updateShareMessageAdded(SharedMessage shareMessage);

                void updateShareMessageDeleted(String id);

                void updateLikerAdd(String id);

                void updateLikerDeleted(String id);

                /**
                 */
                void updateCommentAdded(String id, String content, int position);

                /**
                 * @param id      说说ID
                 * @param content 评论内容
                 */
                void updateCommentDeleted(String id, String content, int position);


                void updateAllShareMessages(List<SharedMessage> data, boolean isPullRefresh);
        }


        interface Model {
                //                添加一个说说item id 为objectId
                void addShareMessage(SharedMessage shareMessage, AddShareMessageCallBack addShareMessageCallBack);

                // 删除一个说说item
                void deleteShareMessage(String id, DealMessageCallBack dealMessageCallBack);

                //                点赞
                void addLiker(String id, DealMessageCallBack dealMessageCallBack);

                //                取消赞
                void deleteLiker(String id, DealMessageCallBack dealMessageCallBack);

                //添加评论
                void addComment(String id, String content, DealCommentMsgCallBack dealCommentMsgCallBack);

                //删除评论
                void deleteComment(String id, int position, DealCommentMsgCallBack dealCommentMsgCallBack);


                //                加载所有的说说消息
                void loadAllShareMessages(boolean isPullRefresh, String time, LoadShareMessageCallBack loadShareMessageCallBack);

//                void loadAllMyShareMessages(boolean isPullRefresh, String time, LoadShareMessageCallBack loadShareMessageCallBack);

                void loadShareMessages(String uid,boolean isPullRefresh,String time,LoadShareMessageCallBack loadShareMessageCallBack);
        }


        abstract class Presenter extends BasePresenter<View, Model> {
                //                添加一个说说item id 为objectId
                abstract void addShareMessage(SharedMessage shareMessage);

                // 删除一个说说item
                abstract void deleteShareMessage(String id);

                //                点赞
                abstract void addLiker(String id);

                //                取消赞
                abstract void deleteLiker(String id);

                //添加评论
                abstract void addComment(String id, String content);

                //删除评论
                abstract void deleteComment(String id, int position);

                //                加载所有的说说消息
                abstract void loadAllShareMessages(boolean isPullRefresh, String time);

//                public abstract void loadAllMyShareMessage(boolean isPullRefresh, String time);
                public abstract void loadShareMessages(String uid,boolean isPullRefresh,String time);

        }


}
