package org.pointstone.cugappplat.weixin.event;

import android.support.annotation.IntDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 项目名称:    Cugappplat
 * 创建人:        陈锦军
 * 创建时间:    2017/4/8      20:41
 * QQ:             1981367757
 */

public class WeiXinEvent {


        public static final int ACTION_UPDATE = 100;

        //       如有其他类型，可继续添加
        @Retention(RetentionPolicy.SOURCE)
        @Target(ElementType.PARAMETER)
        @IntDef(ACTION_UPDATE)
        public @interface WeiXinAction {
        }


        private int action;


        public int getAction() {
                return action;
        }

        public void setAction(@WeiXinAction int action) {
                this.action = action;
        }

        public WeiXinEvent(@WeiXinAction int action) {

                this.action = action;
        }
}
