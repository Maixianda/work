package com.gzliangce.ui.adapter;

import android.content.Context;
import android.view.View;

import com.gzliangce.R;
import com.gzliangce.databinding.ItemOrderDataImageBinding;
import com.gzliangce.entity.OrderInfo;

import io.ganguo.library.core.event.extend.OnSingleClickListener;
import io.ganguo.library.core.image.PhotoLoader;
import io.ganguo.library.ui.adapter.v7.ListAdapter;
import io.ganguo.library.ui.adapter.v7.ViewHolder.BaseViewHolder;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;

/**
 * Created by zzwdream on 1/21/16.
 */
public class MyOrderDataAdapter extends ListAdapter<OrderInfo, ItemOrderDataImageBinding> {
    private Logger logger = LoggerFactory.getLogger(MyDataAdapter.class);

    public MyOrderDataAdapter(Context context) {
        super(context);
    }

    @Override
    public void onBindViewBinding(BaseViewHolder<ItemOrderDataImageBinding> vh, int position) {
        vh.getBinding().ivImage.setOnClickListener(new onClick(position));
        PhotoLoader.display(getContext(), vh.getBinding().ivImage, "https://gitlab.cngump.com/uploads/user/avatar/41/7f121f48b4d0182a9c5f3a0f357d37f8.jpg", getContext().getResources().getDrawable(R.drawable.shape_image_loading));
    }

    @Override
    protected int getItemLayoutId(int position) {
        return R.layout.item_order_data_image;
    }

    /**
     * 点击事件监听
     */
    private class onClick extends OnSingleClickListener {
        private int position;

        public onClick(int position) {
            this.position = position;
        }

        @Override
        public void onSingleClick(View v) {

        }
    }
}