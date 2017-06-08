package chen.testchat.util;

import java.lang.reflect.ParameterizedType;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/12/4      14:20
 * QQ:             1981367757
 */

public class TUtils {
        public static <T> T getT(Object o, int position) {
                try {
                        return ((Class<T>) ((ParameterizedType) (o.getClass()
                                .getGenericSuperclass())).getActualTypeArguments()[position])
                                .newInstance();
                } catch (InstantiationException e) {
                        e.printStackTrace();
                } catch (IllegalAccessException e) {
                        e.printStackTrace();
                } catch (ClassCastException e) {
                        e.printStackTrace();
                }
                return null;
        }
}
