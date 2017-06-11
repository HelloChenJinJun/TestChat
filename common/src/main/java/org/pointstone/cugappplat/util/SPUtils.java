package org.pointstone.cugappplat.util;

import android.content.Context;
import android.content.SharedPreferences;

import org.pointstone.cugappplat.base.BaseApplication;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/8.
 */

public class SPUtils {

        /**
         * 保存在手机里面的文件名
         */
        public static final String FILE_NAME = "share_data";
        public static final String UNION_ID = "unionId";
        public static final String IS_LOGIN = "IS_LOGIN";
        public static final String ACCOUNT_ID = "account_id";
        //    与服务器时间的差值
        public static final String DELTA_TIME = "delta_time";
        public static final String USER_NICK = "user_nick";
        public static final String PUSH_NOTIFY = "push_notify";
        public static final String VOICE = "voice";
        public static final String VIBRATE = "vibrate";
        public static final String LAST_SHARE_MESSAGE_TIME = "last_share_message_time";
        public static final String LAST_GROUP_MESSAGE_TIME = "last_group_message_time";
        public static final String REMIND = "remind";
        public static final String USER_DATA_LAST_UPDATE_TIME = "user_data_last_update_time";
        public static Context context = BaseApplication.getInstance();

        public static final String USER_ID = "user_id";
        public static final String USER_AVATAR = "user_avatar";
        public static final String USER_NAME = "user_name";


        /**
         * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
         *
         * @param key
         * @param object
         */
        public static void put(String key, Object object) {
                SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();

                if (object instanceof String) {
                        editor.putString(key, (String) object);
                } else if (object instanceof Integer) {
                        editor.putInt(key, (Integer) object);
                } else if (object instanceof Boolean) {
                        editor.putBoolean(key, (Boolean) object);
                } else if (object instanceof Float) {
                        editor.putFloat(key, (Float) object);
                } else if (object instanceof Long) {
                        editor.putLong(key, (Long) object);
                } else {
                        editor.putString(key, object.toString());
                }

                SharedPreferencesCompat.apply(editor);
        }

        /**
         * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
         *
         * @param key
         * @param defaultObject
         * @return
         */
        public static Object get(String key, Object defaultObject) {
                SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                        Context.MODE_PRIVATE);

                if (defaultObject instanceof String) {
                        return sp.getString(key, (String) defaultObject);
                } else if (defaultObject instanceof Integer) {
                        return sp.getInt(key, (Integer) defaultObject);
                } else if (defaultObject instanceof Boolean) {
                        return sp.getBoolean(key, (Boolean) defaultObject);
                } else if (defaultObject instanceof Float) {
                        return sp.getFloat(key, (Float) defaultObject);
                } else if (defaultObject instanceof Long) {
                        return sp.getLong(key, (Long) defaultObject);
                }

                return null;
        }

        /**
         * 移除某个key值已经对应的值
         *
         * @param key
         */
        public static void remove(String key) {
                SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.remove(key);
                SharedPreferencesCompat.apply(editor);
        }

        /**
         * 清除所有数据
         */
        public static void clear() {
                SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                SharedPreferencesCompat.apply(editor);
        }

        /**
         * 查询某个key是否已经存在
         *
         * @param key
         * @return
         */
        public static boolean contains(String key) {
                SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                        Context.MODE_PRIVATE);
                return sp.contains(key);
        }

        /**
         * 返回所有的键值对
         *
         * @return
         */
        public static Map<String, ?> getAll() {
                SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                        Context.MODE_PRIVATE);
                return sp.getAll();
        }

        /**
         * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
         *
         * @author zhy
         */
        private static class SharedPreferencesCompat {
                private static final Method sApplyMethod = findApplyMethod();

                /**
                 * 反射查找apply的方法
                 *
                 * @return
                 */
                @SuppressWarnings({"unchecked", "rawtypes"})
                private static Method findApplyMethod() {
                        try {
                                Class clz = SharedPreferences.Editor.class;
                                return clz.getMethod("apply");
                        } catch (NoSuchMethodException e) {
                        }

                        return null;
                }

                /**
                 * 如果找到则使用apply执行，否则使用commit
                 *
                 * @param editor
                 */
                public static void apply(SharedPreferences.Editor editor) {
                        try {
                                if (sApplyMethod != null) {
                                        sApplyMethod.invoke(editor);
                                        return;
                                }
                        } catch (IllegalArgumentException e) {
                        } catch (IllegalAccessException e) {
                        } catch (InvocationTargetException e) {
                        }
                        editor.commit();
                }
        }

}