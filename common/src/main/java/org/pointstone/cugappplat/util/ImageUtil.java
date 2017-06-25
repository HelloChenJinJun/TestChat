package org.pointstone.cugappplat.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import java.io.File;

/**
 * 项目名称:    Cugappplat
 * 创建人:        陈锦军
 * 创建时间:    2017/5/18      12:38
 * QQ:             1981367757
 */

public class ImageUtil {
        public static void takePhoto(Activity activity, int requestCodeTakePicture, File imageFile) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
                activity.startActivityForResult(intent, requestCodeTakePicture);
        }

        public static void pickPhoto(Activity activity, int requestCode) {
                Intent intent = new Intent();
                if (Build.VERSION.SDK_INT < 19) {
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                } else {
                        intent.setAction(Intent.ACTION_PICK);
                        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                }
                activity.startActivityForResult(intent, requestCode);
        }
}
