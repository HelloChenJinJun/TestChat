package chen.testchat.extendnet;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/11/23      11:11
 * QQ:             1981367757
 */
public enum DownState {
        START(0),
        DOWN(1),
        PAUSE(2),
        STOP(3),
        ERROR(4),
        FINISH(5);
        private int state;

        public int getState() {
                return state;
        }

        public void setState(int state) {
                this.state = state;
        }

        DownState(int state) {
                this.state = state;
        }
}
