package org.pointstone.cugappplat.base;

import android.app.Application;

import org.pointstone.cugappplat.util.LogUtil;


/**
 * 项目名称:    JsbridgeDemo
 * 创建人:        陈锦军
 * 创建时间:    2017/4/24      21:00
 * QQ:             1981367757
 */

public class BaseApplication extends Application {
        private static BaseApplication sInstance;
//        public static IWXAPI api;
//        private DownLoadCallBack<DownInfo> mDownLoadCallBack;
//
//
//        public void setDownLoadCallBack(DownLoadCallBack<DownInfo> downLoadCallBack) {
//                mDownLoadCallBack = downLoadCallBack;
//        }


        @Override
        public void onCreate() {
                super.onCreate();
                LogUtil.e("initBaseApplication");
                sInstance = this;
                initWeiXinInfo();
//                try {
//                        LogUtil.e("init");
//                        Atlas.getInstance().init(this, false);
//                } catch (Exception e) {
//                        e.printStackTrace();
//                        LogUtil.e("出错" + e.getMessage());
//                }
//                Atlas.getInstance().setClassNotFoundInterceptorCallback(new ClassNotFoundInterceptorCallback() {
//                        @Override
//                        public Intent returnIntent(final Intent intent) {
//                                final String className = intent.getComponent().getClassName();
//                                final String bundleName = AtlasBundleInfoManager.instance().getBundleForComponet(className);
//                                LogUtil.e("notfound" + intent.getComponent().getClassName() + "bundleName" + bundleName);
//                                if (!TextUtils.isEmpty(bundleName) && !AtlasBundleInfoManager.instance().isInternalBundle(bundleName)) {
//                                        LogUtil.e("这里");
//                                        //远程bundle
//                                        final Activity activity = ActivityTaskMgr.getInstance().peekTopActivity();
//                                        final String bundleLibName = "lib" + bundleName.replace(".", "_") + ".so";
//                                        final File remoteBundleFile = new File(activity.getExternalCacheDir(), "lib" + bundleName.replace(".", "_") + ".so");
//
//                                        String path = "";
//                                        if (remoteBundleFile.exists()) {
//                                                path = remoteBundleFile.getAbsolutePath();
//                                        } else {
//                                                Toast.makeText(activity, " 远程bundle不存在，请确定 : " + remoteBundleFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
//                                                final BaseDialog baseDialog = new BaseDialog(activity);
//                                                baseDialog.setMessage("远程bundle不存在，是否需要下载")
//                                                        .setLeftButton("需要", new View.OnClickListener() {
//                                                                @Override
//                                                                public void onClick(View v) {
//                                                                        baseDialog.dismiss();
//                                                                        DownLoadManager.getInstance().download("http://bmob-cdn-7375.b0.upaiyun.com/2017/05/16/cec84a9140fdb0cb8040ee20954a35a6.so", activity.getExternalCacheDir().getPath(), bundleLibName, new OnDownLoadFileListener() {
//                                                                                @Override
//                                                                                public void onStart() {
//                                                                                        LogUtil.e("onStart");
//
//                                                                                }
//
//                                                                                @Override
//                                                                                public void onProgress(int value) {
//                                                                                        LogUtil.e("onProgress" + value);
//
//                                                                                }
//
//                                                                                @Override
//                                                                                public void onSuccess(String localPath) {
//                                                                                        LogUtil.e("onSuccess" + localPath);
//                                                                                        PackageInfo info = activity.getPackageManager().getPackageArchiveInfo(localPath, 0);
//                                                                                        try {
//                                                                                                Atlas.getInstance().installBundle(info.packageName, new File(localPath));
//                                                                                        } catch (BundleException e) {
//                                                                                                Toast.makeText(activity, " 远程bundle 安装失败，" + e.getMessage(), Toast.LENGTH_LONG).show();
//                                                                                                e.printStackTrace();
//                                                                                        }
//                                                                                        activity.startActivity(intent);
//
//                                                                                }
//
//                                                                                @Override
//                                                                                public void onFailed(Exception e) {
//                                                                                        LogUtil.e("onFailed" + e.getMessage());
//                                                                                }
//                                                                        });
//                                                                }
//                                                        }).setRightButton("取消", new View.OnClickListener() {
//                                                        @Override
//                                                        public void onClick(View v) {
//                                                                baseDialog.dismiss();
//                                                        }
//                                                }).show();
//                                                return intent;
//                                        }
//                                        PackageInfo info = activity.getPackageManager().getPackageArchiveInfo(path, 0);
//                                        try {
//                                                Atlas.getInstance().installBundle(info.packageName, new File(path));
//                                        } catch (BundleException e) {
//                                                Toast.makeText(activity, " 远程bundle 安装失败，" + e.getMessage(), Toast.LENGTH_LONG).show();
//                                                e.printStackTrace();
//                                        }
//
//                                        activity.startActivities(new Intent[]{intent});
//
//                                }
//
//                                return intent;
//                        }
//                });
        }


        private void initWeiXinInfo() {
//                api = WXAPIFactory.createWXAPI(this, Constant.WX_APP_ID, true);
//                api.registerApp(Constant.WX_APP_ID);
        }







        public static BaseApplication getInstance() {
                return sInstance;
        }
}
