package chen.testchat.extendnet;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/11/23      10:25
 * QQ:             1981367757
 */

public class BaseResultEntity {
        /**
         * 错误信息
         */
        private String errorMsg;
        /**
         * 数据内容标志
         */
        private int flag;
        /**
         * 返回需要处理的json数据
         */
        private String data;

        public String getErrorMsg() {
                return errorMsg;
        }

        public void setErrorMsg(String errorMsg) {
                this.errorMsg = errorMsg;
        }

        public int getFlag() {
                return flag;
        }

        public void setFlag(int flag) {
                this.flag = flag;
        }

        public String getData() {
                return data;
        }

        public void setData(String data) {
                this.data = data;
        }
}
