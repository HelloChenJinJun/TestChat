package org.pointstone.cugappplat.util;

import com.orhanobut.logger.Logger;

import org.pointstone.cugappplat.base.BaseApplication;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import okhttp3.ResponseBody;

/**
 * 项目名称:    JsbridgeDemo
 * 创建人:        陈锦军
 * 创建时间:    2017/4/24      22:08
 * QQ:             1981367757
 */

public class FileUtil {
        private static final String CACHE_NAME="net_tx_cache";
        private static File cacheFile;
        public static File getCacheDirs(){
                if (cacheFile==null) {
                         cacheFile=  new File(BaseApplication.getInstance().getCacheDir(), "tx_cache");
                        if (!cacheFile.exists()) {
                                try {
                                        cacheFile.createNewFile();
                                } catch (IOException e) {
                                        e.printStackTrace();
                                        Logger.d("创建文件失败"+e.getMessage());
                                }
                        }
                }
                return cacheFile;
        }

        public static void writeToCache(ResponseBody responseBody, String savedFilePath, long readLength, long totalLength) {
                try {
                        File file = newFile(savedFilePath);
                        long writeLength;
                        if (totalLength == 0) {
                                writeLength = responseBody.contentLength();
                        } else {
                                writeLength = totalLength;
                        }
                        FileChannel fileChannel;
                        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rwd");
                        fileChannel = randomAccessFile.getChannel();
                        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, readLength, writeLength);
                        byte[] buffer = new byte[1024 * 8];
                        int length = 0;
                        while ((length = responseBody.byteStream().read(buffer)) != -1) {
                                mappedByteBuffer.put(buffer, 0, length);
                        }
                        responseBody.byteStream().close();
                        if (fileChannel != null) {
                                fileChannel.close();
                        }
                        if (randomAccessFile != null) {
                                randomAccessFile.close();
                        }
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }

        public static File newFile(String path) {
                try {
                        File file = new File(path);
                        if (!file.exists()) {
                                file.createNewFile();
                        }
                        return file;
                } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                }
        }

        public static File newDir(String dir) {
                try {
                        File file = new File(dir);
                        if (!file.exists()) {
                                file.mkdirs();
                        }
                        return file;
                } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                }
        }

//        public static String getUserVoiceFilePath(String uid, String time) {
//                try {
//                        File file = new File(ChatConstant.VOICE_CACHE_DIR + CommonUtil.md5(UserManager.getInstance().getCurrentUserObjectId()) + File.separator + uid + File.separator + time + ".amr");
//                        if (!file.exists()) {
//                                file.createNewFile();
//                        }
//                        return file.getAbsolutePath();
//                } catch (IOException e) {
//                        e.printStackTrace();
//                        return "";
//                }
//        }

//        public static boolean isExistFileLocalPath(String uid, String time) {
//                File file = new File(ChatConstant.VOICE_CACHE_DIR + CommonUtil.md5(UserManager.getInstance().getCurrentUserObjectId()) + File.separator + uid + File.separator + time + ".amr");
//                return file.exists();
//        }
}
