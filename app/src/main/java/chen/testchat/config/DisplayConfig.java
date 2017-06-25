package chen.testchat.config;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * 项目名称:    HappyChat
 * 创建人:        陈锦军
 * 创建时间:    2016/9/11      15:51
 * QQ:             1981367757
 */
public class DisplayConfig {
        /**
         * 获取图片加载器的图片的配置
         *
         * @param isRound  是否是加载圆形图片
         * @param defaultRes  默认图片ID
         * @return  返回配置
         */
        public static DisplayImageOptions getDefaultConfig(boolean isRound,int defaultRes) {
                DisplayImageOptions.Builder builder=new DisplayImageOptions.Builder();
//                设置缓存到内存和SD卡中，设置图片的解码格式
                builder.cacheInMemory(true).cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565);
//                考虑JPEG图像EXIF参数（旋转和翻转）
                builder.imageScaleType(ImageScaleType.EXACTLY_STRETCHED);
//                设置圆形图片
                if (isRound) {
                        builder.considerExifParams(true).displayer(new RoundedBitmapDisplayer(12));
                }
//                设置下载前图片重置复位
                builder.resetViewBeforeLoading(true);
//                设置图片下载失败的时候加载默认的图片
                if (defaultRes != 0) {
                        builder.showImageForEmptyUri(defaultRes).showImageOnFail(defaultRes);
                }
                return builder.build();
        }
}
