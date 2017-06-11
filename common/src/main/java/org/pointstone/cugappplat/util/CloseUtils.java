package org.pointstone.cugappplat.util;

import java.io.Closeable;
import java.io.IOException;

/**
 * 项目名称:    JsbridgeDemo
 * 创建人:        陈锦军
 * 创建时间:    2017/4/26      14:21
 * QQ:             1981367757
 */

public class CloseUtils {
        /**
         * 关闭IO
         *
         * @param closeables closeable
         */
        public static void closeIO(Closeable... closeables) {
                if (closeables == null) return;
                for (Closeable closeable : closeables) {
                        if (closeable != null) {
                                try {
                                        closeable.close();
                                } catch (IOException e) {
                                        e.printStackTrace();
                                }
                        }
                }
        }
}
