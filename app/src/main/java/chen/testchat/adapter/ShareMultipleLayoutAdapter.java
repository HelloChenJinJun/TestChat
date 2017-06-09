package chen.testchat.adapter;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.pointstone.cugappplat.baseadapter.BaseMultipleWrappedAdapter;

import java.util.ArrayList;
import java.util.List;

import chen.testchat.R;
import chen.testchat.adapter.viewholder.BaseShareMessageViewHolder;
import chen.testchat.adapter.viewholder.ImageShareViewHolder;
import chen.testchat.adapter.viewholder.LinkShareViewHolder;
import chen.testchat.adapter.viewholder.VideoShareViewHolder;
import chen.testchat.base.Constant;
import chen.testchat.bean.SharedMessage;
import chen.testchat.util.LogUtil;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/3/26      16:44
 * QQ:             1981367757
 */

public class ShareMultipleLayoutAdapter extends BaseMultipleWrappedAdapter<SharedMessage, BaseShareMessageViewHolder> {

        private OnShareMessageItemClickListener mOnShareMessageItemClickListener;


        public void setOnShareMessageItemClickListener(OnShareMessageItemClickListener onShareMessageItemClickListener) {
                mOnShareMessageItemClickListener = onShareMessageItemClickListener;
        }

        public ShareMultipleLayoutAdapter(List<SharedMessage> data, int layoutId) {
                super(data, layoutId);
        }


        public ShareMultipleLayoutAdapter(List<SharedMessage> data) {
                this(data, 0);
        }

        public ShareMultipleLayoutAdapter() {
                this(null, 0);
        }


        /**
         * 这里添加所需要的布局id  map
         *
         * @return
         */
        @Override
        protected SparseArray<Integer> getLayoutIdMap() {
                SparseArray<Integer> sparseArray = new SparseArray<>();
//                每个view类型都是一样的布局，只是在需要的时候通过view_stub加载成自己的布局view
                sparseArray.put(Constant.MSG_TYPE_SHARE_MESSAGE_TEXT, R.layout.share_fragment_item_main_layout);
                sparseArray.put(Constant.MSG_TYPE_SHARE_MESSAGE_LINK, R.layout.share_fragment_item_main_layout);
                sparseArray.put(Constant.MSG_TYPE_SHARE_MESSAGE_VIDEO, R.layout.share_fragment_item_main_layout);
                sparseArray.put(Constant.MSG_TYPE_SHARE_MESSAGE_IMAGE, R.layout.share_fragment_item_main_layout);
                return sparseArray;
        }


        /**
         * 如果需要多种类型的ViewHolder的情况，需要重写onCreateView,因为默认返回的是同一种的ViewHolder
         * 并且需要注意的是不能和系统内置的HEADER_VIEW    EMPTY_VIEW   FOOTER_VIEW 的id一样
         *
         * @param parent
         * @param viewType
         * @return
         */
        @Override
        public BaseShareMessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                if (viewType == Constant.MSG_TYPE_SHARE_MESSAGE_IMAGE) {
                        return new ImageShareViewHolder(LayoutInflater.from(parent.getContext()).inflate(getLayoutIds().get(viewType), parent, false));
                } else if (viewType == Constant.MSG_TYPE_SHARE_MESSAGE_VIDEO) {
                        return new VideoShareViewHolder(LayoutInflater.from(parent.getContext()).inflate(getLayoutIds().get(viewType), parent, false));
                } else if (viewType == Constant.MSG_TYPE_SHARE_MESSAGE_LINK) {
                        return new LinkShareViewHolder(LayoutInflater.from(parent.getContext()).inflate(getLayoutIds().get(viewType), parent, false));
                } else if (viewType == Constant.MSG_TYPE_SHARE_MESSAGE_TEXT) {
                        return new BaseShareMessageViewHolder(LayoutInflater.from(parent.getContext()).inflate(getLayoutIds().get(viewType), parent, false));
                }
                return super.onCreateViewHolder(parent, viewType);
        }

        /**
         * 这里只有正常item才能绑定数据，header foot empty 都不能绑定数据(在基类中已经过滤掉了)
         *
         * @param holder
         * @param data
         */
        @Override
        protected void convert(BaseShareMessageViewHolder holder, SharedMessage data) {
                holder.initCommonData(data, mOnShareMessageItemClickListener);
                LogUtil.e("convert");
                Integer type = getItemViewType(holder.getAdapterPosition());
                LogUtil.e("type：" + type);
                LogUtil.e("holder中包含的ItemViewType：" + holder.getItemViewType());
                switch (type) {
                        case Constant.MSG_TYPE_SHARE_MESSAGE_TEXT:
                                break;
                        case Constant.MSG_TYPE_SHARE_MESSAGE_LINK:
                                if (holder instanceof LinkShareViewHolder) {
                                        LogUtil.e("链接对啦");
                                        ((LinkShareViewHolder) holder).bindData(data, mOnShareMessageItemClickListener);
                                }
                                break;
                        case Constant.MSG_TYPE_SHARE_MESSAGE_VIDEO:
                                if (holder instanceof VideoShareViewHolder) {
                                        LogUtil.e("视频链接");
                                        ((VideoShareViewHolder) holder).bindData(data, mOnShareMessageItemClickListener);
                                }
                                break;
                        case Constant.MSG_TYPE_SHARE_MESSAGE_IMAGE:
                                if (holder instanceof ImageShareViewHolder) {
                                        LogUtil.e("图片链接");
                                        ((ImageShareViewHolder) holder).bindData(data, mOnShareMessageItemClickListener);
                                }
                                break;
                        default:
                                LogUtil.e("这应该是纯文本");
                                break;
                }
        }

        public SharedMessage getSharedMessageById(String objectId) {
                for (SharedMessage sharedMessage :
                        getAllData()) {
                        if (sharedMessage.getObjectId().equals(objectId)) {
                                return sharedMessage;
                        }
                }
                return null;
        }


        @Override
        public void addData(int position, SharedMessage newData) {
                if (data.contains(newData)) {
                        int index = data.indexOf(newData);
                        data.set(index, newData);
                        notifyDataSetChanged();
                } else {
                        super.addData(position, newData);
                }
        }


        @Override
        public void addData(int position, List<SharedMessage> newData) {
                LogUtil.e("添加数据1234567891");
                if (newData == null || newData.size() == 0) {
                        return;
                }
                List<SharedMessage> temp=new ArrayList<>();
                List<SharedMessage> copyData=new ArrayList<>(data);
                for (SharedMessage message :
                        newData) {
                        for (SharedMessage item :
                                copyData) {
                                if (message.equals(item)) {
                                        temp.add(message);
//                                        Collections.replaceAll(data,item,message);
                                }
                        }
                }




                if (temp.size() > 0) {
                        LogUtil.e("大于0");
                        int index=0;
                        for (SharedMessage message :
                                temp) {
                                index=data.indexOf(message);
                                data.set(index,message);
                                newData.remove(message);
                        }
                }
                LogUtil.e("这里呢123456");
                LogUtil.e("position"+position);
                LogUtil.e("newData"+newData.size());
                LogUtil.e("data"+data.size());
                super.addData(position, newData);
        }
}
