package chen.testchat.demo;

import org.pointstone.cugappplat.baseadapter.baseitem.MultipleItem;

import java.io.Serializable;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/5/25      17:06
 * QQ:             1981367757
 */

public class Demo implements Serializable, MultipleItem {

        public static final int TYPE_NORMAL = 50;
        public static final int TYPE_SPECIAL = 51;


        private int demoType = TYPE_NORMAL;
        private String data;


        public String getData() {
                return data;
        }

        public void setData(String data) {
                this.data = data;
        }

        public int getDemoType() {
                return demoType;
        }

        public void setDemoType(int demoType) {
                this.demoType = demoType;
        }

        @Override
        public int getItemViewType() {
                return getDemoType();
        }
}
