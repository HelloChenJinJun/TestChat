package chen.testchat.listener;

import chen.testchat.bean.SharedMessage;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/12/3      13:20
 * QQ:             1981367757
 */
public interface AddShareMessageCallBack {
        void onSuccess(SharedMessage shareMessage);

        void onFailed(int errorId, String errorMsg);
}
