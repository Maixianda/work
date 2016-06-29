package com.gzliangce.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ItemMyCourseBinding;
import com.gzliangce.entity.CourseInfo;
import com.gzliangce.ui.activity.college.CourseDetailedActivity;

import io.ganguo.library.core.event.extend.OnSingleClickListener;
import io.ganguo.library.core.image.PhotoLoader;
import io.ganguo.library.ui.adapter.v7.ViewHolder.BaseViewHolder;

/**
 * Created by leo on 16/3/14.
 * 我的课程 - adapter
 */
public class MyCourseAdapter extends LoadMoreBaseAdapter<CourseInfo, ItemMyCourseBinding> {

    public MyCourseAdapter(Context context) {
        super(context);
    }

    @Override
    public void onBindViewBinding(final BaseViewHolder<ItemMyCourseBinding> vh, int position) {
        PhotoLoader.display(vh.getBinding().ibtnIcon, get(position).getLogo(), getContext().getResources().getDrawable(R.drawable.ic_product_loading));
        vh.getBinding().llAction.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (vh.getAdapterPosition() < size()) {
                    Intent intent = new Intent(getContext(), CourseDetailedActivity.class);
                    intent.putExtra(Constants.COLLEGE_COURSE_LIST_ITEM, get(vh.getAdapterPosition()));
                    getContext().startActivity(intent);
                }
            }
        });
    }

    @Override
    protected int getItemLayoutId(int position) {
        return R.layout.item_my_course;
    }
}
