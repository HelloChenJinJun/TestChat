package org.pointstone.cugappplat.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


/**
 * 项目名称:    JsbridgeDemo
 * 创建人:        陈锦军
 * 创建时间:    2017/4/25      9:19
 * QQ:             1981367757
 */

public class ParseDataUtil {
        private static final Gson sGson=new Gson();

        public static <T> T parseObject(String json,Class<T> tClass){
                return sGson.fromJson(json,tClass);
        }


        public static <T> List<T> parseArray(String json,Class<T> tClass){
                Type type = new TypeToken<ArrayList<JsonObject>>() {
                }.getType();
                List<JsonObject> list=sGson.fromJson(json,type);
                List<T> result=new ArrayList<>();
                T item;
                for (JsonObject jsonObject :
                        list) {
                        item=sGson.fromJson(jsonObject,tClass);
                        result.add(item);
                }
                return result;
        }

        public static <T> List<T> parseList(String json,Class<List<T>> classList){
                return sGson.fromJson(json,classList);
        }



        public static <T>  T parseXml(String json,Class<T> tClass ){
//                Serializer serializer=new Persister();
//                try {
//                        InputStreamReader inReader=new InputStreamReader(new ByteArrayInputStream(json.getBytes("utf-8")),"utf-8");
//                       return serializer.read(tClass,inReader);
//                } catch (Exception e) {
//                        e.printStackTrace();
//                        LogUtil.e("解析xml失败"+e.getMessage());
//                }
                return null;
        }







}
