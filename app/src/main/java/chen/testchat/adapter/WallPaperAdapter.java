package chen.testchat.adapter;

import org.pointstone.cugappplat.baseadapter.BaseWrappedAdapter;
import org.pointstone.cugappplat.baseadapter.BaseWrappedViewHolder;

import java.util.List;

import chen.testchat.R;
import chen.testchat.util.LogUtil;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/2/26      21:09
 * QQ:             1981367757
 */
public class WallPaperAdapter extends BaseWrappedAdapter<String, BaseWrappedViewHolder> {


        private int selectedPosition = -1;


        public interface OnWallPaperClickListener {
                public void onClick(BaseWrappedViewHolder holder, int prePosition, int currentPosition);
        }


        public WallPaperAdapter(List<String> data, int layoutId) {
                super(data, layoutId);
        }


        public void setSelectedPosition(int selectedPosition) {
                this.selectedPosition = selectedPosition;
        }

        @Override
        protected void convert(BaseWrappedViewHolder holder, String data) {
                LogUtil.e("重绘" + selectedPosition);
                holder.setImageBg(R.id.iv_wallpaper_item_display, data);
                if (selectedPosition == holder.getAdapterPosition()) {
                        holder.setImageResource(R.id.iv_wallpaper_item_display, R.drawable.change_background_picture_btn);
                }
        }


//                LogUtil.e("重绘" + selectedPosition);
//                if (mOnItemClickListener != null) {
//                        baseRecyclerHolder.setOnClickListener(R.id.iv_wallpaper_item_display, new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                        mOnItemClickListener.onItemClick(v, data, position);
//                                }
//                        });
//                }
//                Picasso.with(mContext).load(data).centerCrop()
//                        .into(new Target() {
//                                @Override
//                                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                                        baseRecyclerHolder.getView(R.id.iv_wallpaper_item_display).setBackgroundDrawable(new BitmapDrawable(mContext.getResources(), bitmap));
//                                }
//
//                                @Override
//                                public void onBitmapFailed(Drawable errorDrawable) {
//
//                                }
//
//                                @Override
//                                public void onPrepareLoad(Drawable placeHolderDrawable) {
//
//                                }
//                        });
//                if (selectedPosition == position) {
//                        baseRecyclerHolder.setImageResource(R.id.iv_wallpaper_item_display, R.drawable.change_background_picture_btn);
//                } else {
//                        ((ImageView) baseRecyclerHolder.getView(R.id.iv_wallpaper_item_display)).setImageResource(0);
//                }


}
