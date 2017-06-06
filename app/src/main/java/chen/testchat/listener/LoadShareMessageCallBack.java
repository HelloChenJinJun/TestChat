package chen.testchat.listener;

import java.util.List;

import chen.testchat.bean.SharedMessage;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/12/6      21:01
 * QQ:             1981367757
 */

public interface LoadShareMessageCallBack {
        void onSuccess(List<SharedMessage> data);

        void onFailed(String errorMsg, int errorId);
}
