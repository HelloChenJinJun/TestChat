package chen.testchat.mvp.ShareMessageTask;

import java.util.List;

import chen.testchat.bean.SharedMessage;
import chen.testchat.listener.AddShareMessageCallBack;
import chen.testchat.listener.DealCommentMsgCallBack;
import chen.testchat.listener.DealMessageCallBack;
import chen.testchat.listener.LoadShareMessageCallBack;
import chen.testchat.util.LogUtil;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/12/2      20:26
 * QQ:             1981367757
 */

public class ShareMessagePresenter extends ShareMessageContacts.Presenter {

        @Override
        public void addShareMessage(final SharedMessage shareMessage) {
                mView.showLoading("正在发送说说......请稍候.........");
                mModel.addShareMessage(shareMessage, new AddShareMessageCallBack() {
                        @Override
                        public void onSuccess(SharedMessage shareMessage) {
                                mView.hideLoading();
                                mView.updateShareMessageAdded(shareMessage);
                        }

                        @Override
                        public void onFailed(int errorId, String errorMsg) {
                                mView.hideLoading();
                                mView.showError("发送说说消息失败" + errorMsg + errorId, null);
                        }
                });
        }

        @Override
        public void deleteShareMessage(String id) {
                mView.showLoading("正在删除说说.......请稍后.........");
                mModel.deleteShareMessage(id, new DealMessageCallBack() {
                        @Override
                        public void onSuccess(String id) {
                                mView.hideLoading();
                                mView.updateShareMessageDeleted(id);
                        }

                        @Override
                        public void onFailed(String id, int errorId, String errorMsg) {
                                mView.hideLoading();
                                mView.showError("删除说说(ObjectId：" + id + ")消息失败" + errorMsg + errorId, null);
                        }
                });

        }

        @Override
        public void addLiker(String id) {
//                这里的id为说说的id
                mModel.addLiker(id, new DealMessageCallBack() {
                        @Override
                        public void onSuccess(String id) {
                                mView.updateLikerAdd(id);
                        }

                        @Override
                        public void onFailed(String id, int errorId, String errorMsg) {
                                mView.showError("添加点赞消息失败" + errorMsg + errorId, null);
                        }
                });
        }

        @Override
        public void deleteLiker(String id) {
                mModel.deleteLiker(id, new DealMessageCallBack() {
                        @Override
                        public void onSuccess(String id) {
                                mView.updateLikerDeleted(id);

                        }

                        @Override
                        public void onFailed(String id, int errorId, String errorMsg) {
                                mView.showError("删除点赞失败" + errorMsg + errorId, null);
                        }
                });

        }

        @Override
        public void addComment(String id, String content) {
                mView.showLoading("正在添加评论.....");
                mModel.addComment(id, content, new DealCommentMsgCallBack() {
                        @Override
                        public void onSuccess(String shareMessageId, String content, int position) {
                                mView.hideLoading();
                                mView.updateCommentAdded(shareMessageId, content, position);
                        }

                        @Override
                        public void onFailed(String shareMessageId, String content, int position, int errorId, String errorMsg) {
                                mView.hideLoading();
                                mView.showError("添加评论消息失败:位置为" + position + errorMsg + errorId, null);
                        }
                });

        }

        @Override
        public void deleteComment(String id, int position) {
                mView.showLoading("正在删除评论");
                mModel.deleteComment(id, position, new DealCommentMsgCallBack() {
                        @Override
                        public void onSuccess(String shareMessageId, String content, int position) {
                                mView.hideLoading();
                                LogUtil.e("删除评论成功");
                                mView.updateCommentDeleted(shareMessageId, content, position);
                        }

                        @Override
                        public void onFailed(String shareMessageId, String content, int position, int errorId, String errorMsg) {
                                mView.hideLoading();
                                LogUtil.e("删除评论失败");
                                LogUtil.e("评论的内容是" + content);
                                mView.showError("删除评论失败:位置为" + position + errorMsg + errorId, null);
                        }
                });
        }

        @Override
        public void loadAllShareMessages(final boolean isPullRefresh, String time) {
                LogUtil.e("加载所有的说说消息");
//                mView.showLoading("正在加载......请稍后.......");
                mModel.loadAllShareMessages(isPullRefresh, time, new LoadShareMessageCallBack() {
                        @Override
                        public void onSuccess(List<SharedMessage> data) {
                                mView.hideLoading();
                                mView.updateAllShareMessages(data, isPullRefresh);
                        }

                        @Override
                        public void onFailed(String errorMsg, int errorId) {
                                mView.hideLoading();
                                mView.showError(errorMsg + errorId, null);
                        }
                });
        }

        @Override
        public void loadShareMessages(String uid, final boolean isPullRefresh, String time) {
//                if (isPullRefresh) {
//                        mView.showLoading("加载说说消息中................");
//                }
                mModel.loadShareMessages(uid, isPullRefresh, time, new LoadShareMessageCallBack() {
                        @Override
                        public void onSuccess(List<SharedMessage> data) {
                                mView.hideLoading();
                                mView.updateAllShareMessages(data, isPullRefresh);
                        }

                        @Override
                        public void onFailed(String errorMsg, int errorId) {
                                mView.hideLoading();
                                mView.showError(errorMsg + errorId,null);
                        }
                });
        }

//        @Override
//        public void loadAllMyShareMessage(final boolean isPullRefresh, String time) {
////                LogUtil.e("加载所有的自己的说说消息");
////                mView.showLoading("正在加载......请稍后.......");
//                mModel.loadAllMyShareMessages(isPullRefresh, time, new LoadShareMessageCallBack() {
//                        @Override
//                        public void onSuccess(List<SharedMessage> data) {
////                                mView.hideLoading();
//                                mView.updateAllShareMessages(data, isPullRefresh);
//                        }
//
//                        @Override
//                        public void onFailed(String errorMsg, int errorId) {
////                                mView.hideLoading();
//                                mView.showError(errorMsg + errorId,null);
//                        }
//                });
//        }
}
