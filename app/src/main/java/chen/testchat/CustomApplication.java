package chen.testchat;

import android.os.Environment;

import com.zhy.http.okhttp.OkHttpUtils;

import org.pointstone.cugappplat.base.BaseApplication;

import java.io.File;
import java.util.concurrent.TimeUnit;

import chen.testchat.base.Constant;
import chen.testchat.base.CrashHandler;
import chen.testchat.bean.CustomInstallation;
import chen.testchat.manager.LocationManager;
import chen.testchat.util.LogUtil;
import chen.testchat.util.SharedPreferencesUtil;
import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.statistics.AppStat;
import mabeijianxi.camera.VCamera;
import mabeijianxi.camera.util.DeviceUtils;
import okhttp3.OkHttpClient;

/**
 * 项目名称:    HappyChat
 * 创建人:        陈锦军
 * 创建时间:    2016/9/11      9:49
 * QQ:             1981367757
 */
public class CustomApplication extends BaseApplication {
        private static final String SHAREDPREFERENCE_NAME = "share_name";
        private SharedPreferencesUtil mSharedPreferencesUtil = null;
        //        private List<String> locationList;
        private static CustomApplication INSTANCE;
        //        好友列表缓存
//        private Map<String, User> contacts = new HashMap<>();
        //        临时会话的好友
//        private Map<String, User> tempContacts;
        /**
         * 本用户
         */
//        private User mUser;

        /**
         * 黑名单好友
         */
//        private Map<String, User> blackList;

//        private Map<String, GroupTableMessage> groupList;
//        private Map<String, String> mUserDataTimeMap = new HashMap<>();
//        private Map<String, List<GroupNumberInfo>> groupNumberMap;
//        private double longitude = 0;
//        private double latitude = 0;
//        private Map<String, List<String>> shareMap;
        public void setINSTANCE(CustomApplication customApplication) {
                INSTANCE = customApplication;
        }

        @Override
        public void onCreate() {
                super.onCreate();
//                if (LeakCanary.isInAnalyzerProcess(this)) {
//                        // This process is dedicated to LeakCanary for heap analysis.
//                        // You should not init your app in this process.
//                        return;
//                }
//                LeakCanary.install(this);
                setINSTANCE(this);
                Bmob.initialize(this, Constant.KEY);
                AppStat.i(Constant.KEY, null);
                LogUtil.e("1服务器端初始化完成");
                CustomInstallation.getCurrentInstallation(this).save();
                LogUtil.e("设备ID在这里上传了");
                BmobPush.startWork(this);
                LogUtil.e("推送服务初始化完成");
                initOkHttp();
                initSmallVideo();
                initLocationClient();
//                CrashHandler.getInstance().init(this);
        }


        private void initLocationClient() {
//                LocationManager.getInstance().registerLocationListener(this);
                LocationManager.getInstance().startLocation();
        }


        private void initSmallVideo() {
                LogUtil.e("初始化小视频缓存目录");
                // 设置拍摄视频缓存路径
                File dcim = Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                if (DeviceUtils.isZte()) {
                        if (dcim.exists()) {
                                VCamera.setVideoCachePath(dcim + "/chen/");
                        } else {
                                VCamera.setVideoCachePath(dcim.getPath().replace("/sdcard/",
                                        "/sdcard-ext/")
                                        + "/mabeijianxi/");
                        }
                } else {
                        VCamera.setVideoCachePath(dcim + "/mabeijianxi/");
                }
                VCamera.setDebugMode(true);
                VCamera.initialize(this);
        }

        private void initOkHttp() {
                OkHttpUtils.initClient(new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).readTimeout(10, TimeUnit.SECONDS).build());
        }

        public static CustomApplication getInstance() {
                return INSTANCE;
        }


//        public Map<String, User> getContacts() {
//                return contacts;
//        }

//        public void setContactsList(Map<String, User> friends) {
//                if (friends != null) {
//                        if (contacts != null) {
//                                contacts.clear();
//                                contacts.putAll(friends);
//                        } else {
//                                contacts = new HashMap<>();
//                                contacts.putAll(friends);
//                        }
//                        LogUtil.e("全部的用户列表");
//                        for (User user :
//                                friends.values()) {
//                                LogUtil.e(user);
//                        }
//                }
//        }

//        public void addContact(User user) {
//                LogUtil.e("添加好友到内存总成功");
//                if (user != null) {
//                        LogUtil.e(user);
//                        if (contacts == null) {
//                                contacts = new HashMap<>();
//                                List<User> contactsWithoutBlack = ChatDB.create().getContactsWithoutBlack();
//                                if (contactsWithoutBlack != null && contactsWithoutBlack.size() > 0) {
//                                        contacts.putAll(BmobUtils.list2map(contactsWithoutBlack));
//                                }
//                        }
//                        if (!contacts.containsKey(user.getObjectId())) {
//                                contacts.put(user.getObjectId(), user);
//                        } else {
////                                更新内存中用户数据
//                                contacts.remove(user.getObjectId());
//                                contacts.put(user.getObjectId(), user);
//                        }
//                }
//        }

//        public List<User> getContactsList() {
//                List<User> list = new ArrayList<>();
//                if (contacts == null || contacts.size() == 0) {
//                        if (contacts == null) {
//                                contacts = new HashMap<>();
//                        }
//                        List<User> contactsWithoutBlack = ChatDB.create().getContactsWithoutBlack();
//                        LogUtil.e("从数据库中获取得到的用户");
//                        if (contactsWithoutBlack != null) {
//                                for (User user :
//                                        contactsWithoutBlack) {
//                                        LogUtil.e(user);
//                                }
//                        }
//                        if (contactsWithoutBlack != null && contactsWithoutBlack.size() > 0) {
//                                contacts.putAll(BmobUtils.list2map(contactsWithoutBlack));
//                        }
//                }
//                list.addAll(BmobUtils.map2list(contacts));
//                return list;
//        }


//        public List<User> getAllContacts() {
//                List<User> list = new ArrayList<>();
//                if (getContactsList().size() > 0) {
//                        list.addAll(getContactsList());
//                }
//                if (getAllBlackUser().size() > 0) {
//                        list.addAll(getAllBlackUser());
//                }
//                Collections.sort(list);
//                LogUtil.e("获取所有的好友用户的大小" + list.size());
//                return list;
//        }

//        public User getUser(String uid) {
//                if (uid != null) {
//                        if (contacts == null) {
//                                contacts = new HashMap<>();
//                                List<User> contactsWithoutBlack = ChatDB.create().getContactsWithoutBlack();
//                                if (contactsWithoutBlack != null && contactsWithoutBlack.size() > 0) {
//                                        contacts.putAll(BmobUtils.list2map(contactsWithoutBlack));
//                                }
//                        }
//                        if (contacts.containsKey(uid)) {
//                                return contacts.get(uid);
//                        } else {
//                                User user = ChatDB.create().getContact(uid);
//                                if (user != null) {
//                                        contacts.put(user.getObjectId(), user);
//                                }
//                                return user;
//                        }
//                }
//                return null;
//        }


//        public void logout() {
//                if (contacts != null) {
//                        contacts.clear();
//                        contacts = null;
//                }
//                LogUtil.e("好友内存数据清除");
//                if (blackList != null) {
//                        blackList.clear();
//                        blackList = null;
//                }
//                LogUtil.e("黑名单内存数据清除");
//                if (tempContacts != null) {
//                        tempContacts.clear();
//                        tempContacts = null;
//                }
//
//
//                if (groupList != null) {
//                        LogUtil.e("有群结构消息，群结构消息清除 ");
//                        groupList.clear();
//                        groupList = null;
//                }
//                if (shareMap != null) {
//                        LogUtil.e("有缓存的说说图片或视频，这里清除");
//                        shareMap.clear();
//                        shareMap = null;
//                }
//                LogUtil.e("临时好友内容数据清除");
//                getSharedPreferencesUtil().setLogin(false);
//                LogUtil.e("用户已退出，缓存数据已清除");
//                LogUtil.e("退出时时间置为0");
//                if (groupMap != null) {
//                        groupMap.clear();
//                }
//                if (userTime != null) {
//                        userTime.clear();
//                }
//                if (groupNumberMap != null) {
//                        groupNumberMap.clear();
//                }
//                mUser = null;
//                UserManager.getInstance().logout();
//        }

        public synchronized SharedPreferencesUtil getSharedPreferencesUtil() {
                if (mSharedPreferencesUtil == null) {
                        mSharedPreferencesUtil = new SharedPreferencesUtil(this, SHAREDPREFERENCE_NAME);
                }
                return mSharedPreferencesUtil;
        }
//
//        /**
//         * 保存临时会话的用户
//         *
//         * @param user 用户实体
//         */
//        public void saveTempUser(User user) {
//                if (user != null) {
//                        if (tempContacts == null) {
//                                tempContacts = new HashMap<>();
//                        }
//                        if (!tempContacts.containsKey(user.getObjectId()))
//                                tempContacts.put(user.getObjectId(), user);
//                }
//        }
//
//        /**
//         * 根据用户ID获取用户实体
//         *
//         * @param uid 用户名
//         * @return 用户实体
//         */
//        public User getTempUser(String uid) {
//                if (uid != null) {
//                        if (tempContacts == null) {
//                                tempContacts = new HashMap<>();
//                        }
//                        if (tempContacts.containsKey(uid)) {
//                                return tempContacts.get(uid);
//                        }
//                }
//                return null;
//        }

//        /**
//         * 删除用户
//         *
//         * @param uid 用户名
//         */
//        public void deleteUser(String uid) {
//                if (uid != null) {
//                        if (contacts == null) {
//                                contacts = new HashMap<>();
//                                List<User> list = ChatDB.create().getContactsWithoutBlack();
//                                if (list != null && list.size() > 0) {
//                                        contacts.putAll(BmobUtils.list2map(list));
//                                }
//                        }
//                        if (contacts.containsKey(uid)) {
//                                contacts.remove(uid);
//                        }
//                }
//        }

//
//        public void addBlackUser(User user) {
//                if (user != null) {
//                        if (blackList == null) {
//                                blackList = new HashMap<>();
//                                List<User> list = ChatDB.create().getAllBlackUser();
//                                if (list != null && list.size() > 0) {
//                                        blackList.putAll(BmobUtils.list2map(list));
//                                }
//                        }
//                        if (!blackList.containsKey(user.getObjectId()))
//                                blackList.put(user.getObjectId(), user);
//                }
//        }


//        public User getBlackUser(String uid) {
//                if (uid != null) {
//                        if (blackList == null) {
//                                blackList = new HashMap<>();
//                                List<User> list = ChatDB.create().getAllBlackUser();
//                                if (list != null && list.size() > 0) {
//                                        blackList.putAll(BmobUtils.list2map(list));
//                                }
//                        }
//                        if (blackList.containsKey(uid)) {
//                                return blackList.get(uid);
//                        }
//                }
//                return null;
//        }

//        public void deleteBlackUser(String uid) {
//                if (uid != null) {
//                        if (blackList == null) {
//                                blackList = new HashMap<>();
//                                List<User> list = ChatDB.create().getAllBlackUser();
//                                if (list != null && list.size() > 0) {
//                                        blackList.putAll(BmobUtils.list2map(list));
//                                }
//                        }
//                        if (blackList.containsKey(uid)) {
//                                blackList.remove(uid);
//                        }
//                }
//        }
//
//        public List<User> getAllBlackUser() {
//                if (blackList == null || blackList.size() == 0) {
//                        if (blackList == null) {
//                                blackList = new HashMap<>();
//                        }
//                        List<User> list = ChatDB.create().getAllBlackUser();
//                        if (list != null && list.size() > 0) {
//                                blackList.putAll(BmobUtils.list2map(list));
//                        }
//                }
//                return BmobUtils.map2list(blackList);
//        }

//        public GroupTableMessage getGroupTableMessage(String groupId) {
//                if (groupId == null) {
//                        return null;
//                }
//                if (groupList == null) {
//                        groupList = new HashMap<>();
//                        List<GroupTableMessage> list = ChatDB.create().getAllGroupMessage();
//                        if (list != null && list.size() > 0) {
//                                groupList.putAll(BmobUtils.list_map(list));
//                        }
//                }
//                if (groupList.containsKey(groupId)) {
//                        return groupList.get(groupId);
//                }
//                return null;
//        }
//
//        public void addGroupTableMessage(GroupTableMessage message) {
//                if (message == null) {
//                        return;
//                }
//                if (groupList == null) {
//                        groupList = new HashMap<>();
//                        List<GroupTableMessage> list = ChatDB.create().getAllGroupMessage();
//                        if (list != null && list.size() > 0) {
//                                groupList.putAll(BmobUtils.list_map(list));
//                        }
//                }
//                if (!getAllGroupId().contains(message.getGroupId())) {
//                        getAllGroupId().add(message.getGroupId());
//                }
//                LogUtil.e("执行到这里了吧，这里是把查询得到的群结构表消息存入内存中");
//                if (!groupList.containsKey(message.getGroupId())) {
//                        LogUtil.e(message);
//                        groupList.put(message.getGroupId(), message);
//                }
//        }

//        public List<GroupTableMessage> getAllGroupTableMessage() {
//                if (groupList == null) {
//                        groupList = new HashMap<>();
//                        List<GroupTableMessage> list = ChatDB.create().getAllGroupMessage();
//                        if (list != null && list.size() > 0) {
//                                groupList.putAll(BmobUtils.list_map(list));
//                        }
//                }
//                return BmobUtils.map_list(groupList);
//        }

//        public void addGroupTableMessage(List<GroupTableMessage> list) {
//                for (GroupTableMessage message :
//                        list) {
//                        addGroupTableMessage(message);
//                }
//        }

//        private Map<String, String> groupMap = new HashMap<>();

//        public String getLastGroupMessageTime(String groupId) {
//                if (groupMap.get(groupId) == null) {
//                        String time = getSharedPreferencesUtil().getLastGroupMessageTime(groupId);
//                        groupMap.put(groupId, time);
//                }
//                return groupMap.get(groupId);
//        }

//        private Map<String, String> userTime = new HashMap<>();


//        public String getLastShareMessageTime(String uid) {
//                if (userTime == null) {
//                        return null;
//                }
//                if (userTime.get(uid) == null) {
//                        String time = getSharedPreferencesUtil().getLastShareMessageTime(uid);
//                        userTime.put(uid, time);
//                }
//                return userTime.get(uid);
//        }
//
//        public void setLastShareMessageTime(String uid, String lastShareMessageTime) {
//                userTime.put(uid, lastShareMessageTime);
//                if (getSharedPreferencesUtil().putLastShareMessageTime(uid, lastShareMessageTime)) {
//                        LogUtil.e("保存最新的说说消息时间到外部存储中");
//                }
//        }

//        public void setLastGroupMessageTime(String groupId, String lastGroupMessageTime) {
//                groupMap.put(groupId, lastGroupMessageTime);
//                if (getSharedPreferencesUtil().putLastGroupMessageTime(groupId, lastGroupMessageTime)) {
//                        LogUtil.e("保存最新的群消息时间到外部存储中");
//                }
//        }


//        private List<String> allGroupId;

//        public List<String> getAllGroupId() {
//                if (allGroupId == null) {
//                        allGroupId = new ArrayList<>();
//                        for (GroupTableMessage groupTableMessage :
//                                getAllGroupTableMessage()) {
//                                allGroupId.add(groupTableMessage.getGroupId());
//                        }
//                }
//                return allGroupId;
//        }
//
//        public List<String> getAllUserId() {
//                List<String> list = null;
//                if (getContacts() != null && getContacts().keySet().size() > 0) {
//                        list = new ArrayList<>(getContacts().keySet());
//                }
//                return list;
//        }

//        public void setUserDataLastUpdateTime(String objectId, String userDataLastUpdateTime) {
//                mUserDataTimeMap.put(objectId, userDataLastUpdateTime);
//                getSharedPreferencesUtil().putLastUserDataUpdateTime(objectId, userDataLastUpdateTime);
//        }


//        public String getUserDataLastUpdateTime(String objectId) {
//                if (!mUserDataTimeMap.containsKey(objectId)) {
//                        String time = getSharedPreferencesUtil().getLastUserDataUpdateTime(objectId);
//                        mUserDataTimeMap.put(objectId, time);
//                }
//                return mUserDataTimeMap.get(objectId);
//        }

//        public void saveShareMessageCache(String id, List<String> list) {
//                if (shareMap == null) {
//                        shareMap = new HashMap<>();
//                }
//                shareMap.put(id, list);
//        }
//
//
//        public List<String> getShareMessageCache(String id) {
//                if (shareMap != null) {
//                        return shareMap.get(id);
//                } else {
//                        return null;
//                }
//        }

//        public User getUser() {
//                if (mUser == null) {
//                        mUser = UserManager.getInstance().getCurrentUser();
//                }
//                return mUser;
//        }

//        public void setUser(User user) {
//                mUser = user;
//        }

//        public List<GroupNumberInfo> getAllGroupNumberInfo(String groupId) {
//                if (groupNumberMap == null) {
//                        groupNumberMap = new HashMap<>();
//                        return null;
//                }
//                return groupNumberMap.get(groupId);
//        }

//        public void setAllGroupNumberInfo(String groupId, List<GroupNumberInfo> list) {
//                if (groupNumberMap == null) {
//                        groupNumberMap = new HashMap<>();
//                }
//                groupNumberMap.put(groupId, list);
//        }


//        public List<String> getLocationList() {
//                return locationList;
//        }
//
//
//        public double getLongitude() {
//                return longitude;
//        }
//
//        public double getLatitude() {
//                return latitude;
//        }
//
//        @Override
//        public void onLocationChanged(List<String> addressList, double latitude, double longitude) {
//                if (this.longitude != longitude || this.latitude != latitude) {
//                        locationList = new ArrayList<>();
//                        locationList.addAll(addressList);
//                        this.longitude = longitude;
//                        this.latitude = latitude;
//                        if (UserCacheManager.getInstance().getUser() != null) {
//                                UserManager.getInstance().updateUserInfo("location", longitude + "&" + latitude, new UpdateListener() {
//                                        @Override
//                                        public void onSuccess() {
//                                                UserCacheManager.getInstance().getUser().setLocation(new BmobGeoPoint(CustomApplication.this.longitude, CustomApplication.this.latitude));
//                                        }
//
//                                        @Override
//                                        public void onFailure(int i, String s) {
//                                                LogUtil.e("定位获取的到的经纬度信息更新到服务器上失败");
//                                        }
//                                });
//                        }
//                }
//        }
//
//        @Override
//        public void onLocationFailed(int errorId, String errorMsg) {
//                LogUtil.e("定位失败" + errorMsg + errorId);
//        }
}
