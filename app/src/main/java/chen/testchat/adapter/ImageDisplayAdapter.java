package chen.testchat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import org.pointstone.cugappplat.baseadapter.BaseWrappedAdapter;
import org.pointstone.cugappplat.baseadapter.BaseWrappedViewHolder;

import java.io.File;
import java.util.List;

import chen.testchat.R;
import chen.testchat.util.LogUtil;
import chen.testchat.util.PixelUtil;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/18      13:29
 * QQ:             1981367757
 */

public class ImageDisplayAdapter extends BaseWrappedAdapter<String, BaseWrappedViewHolder> {
        private int mPagePadding = 15;
        private int showCardWidth = 15;


        public ImageDisplayAdapter(List<String> data, int layoutId) {
                super(data, layoutId);
        }


        public int getPagePadding() {
                return mPagePadding;
        }

        public void setPagePadding(int pagePadding) {
                mPagePadding = pagePadding;
        }

        public int getShowCardWidth() {
                return showCardWidth;
        }

        public void setShowCardWidth(int showCardWidth) {
                this.showCardWidth = showCardWidth;
        }

        @Override
        public BaseWrappedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                BaseWrappedViewHolder baseWrappedViewHolder = super.onCreateViewHolder(parent, viewType);
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) baseWrappedViewHolder.itemView.getLayoutParams();
                layoutParams.width = (parent.getWidth() - 2 * (PixelUtil.dp2px(mPagePadding + showCardWidth)));
                baseWrappedViewHolder.itemView.setLayoutParams(layoutParams);
                baseWrappedViewHolder.itemView.setPadding(PixelUtil.dp2px(mPagePadding), 0, PixelUtil.dp2px(mPagePadding), 0);
                return baseWrappedViewHolder;
        }


        @Override
        protected void convert(BaseWrappedViewHolder holder, String data) {
                int position = holder.getAdapterPosition();
//                                这里最后一个和第一个设置margin
                int leftMargin = position == 0 ? PixelUtil.dp2px(mPagePadding + showCardWidth) : 0;
                int rightMargin = position == getItemCount() - 1 ? PixelUtil.dp2px(mPagePadding + showCardWidth) : 0;
                if (leftMargin != 0 || rightMargin != 0) {
                        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
                        marginLayoutParams.setMargins(leftMargin, 0, rightMargin, 0);
                        holder.itemView.setLayoutParams(marginLayoutParams);
                }
                File file = new File(data);
                if (file.exists()) {
                        LogUtil.e("加载文件");
                        holder.setImageUrl(R.id.iv_image_display_item_display, file);
                } else {
                        LogUtil.e("11加载" +
                                "url");
                        holder.setImageUrl(R.id.iv_image_display_item_display, data);
                }
        }
}
