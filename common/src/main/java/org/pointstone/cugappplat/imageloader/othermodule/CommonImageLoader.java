package org.pointstone.cugappplat.imageloader.othermodule;

import android.content.Context;

/**
 * 项目名称:    Cugappplat
 * 创建人:        陈锦军
 * 创建时间:    2017/5/15      15:19
 * QQ:             1981367757
 */

public class CommonImageLoader implements IImageLoader{

        private static CommonImageLoader instance;

        public static CommonImageLoader getInstance() {
                if (instance == null) {
                        instance=new CommonImageLoader();
                }
                return instance;
        }

        @Override
        public void displayImage(Context context) {

        }


}
