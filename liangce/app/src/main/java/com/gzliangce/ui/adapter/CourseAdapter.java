package com.gzliangce.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ItemCollegeCourseBinding;
import com.gzliangce.databinding.ItemProductBinding;
import com.gzliangce.entity.CourseInfo;
import com.gzliangce.entity.ProductsInfo;
import com.gzliangce.ui.activity.WebActivity;
import com.gzliangce.ui.activity.college.CourseDetailedActivity;
import com.gzliangce.util.XmlUtil;

import io.ganguo.library.core.event.extend.OnSingleClickListener;
import io.ganguo.library.core.image.PhotoLoader;
import io.ganguo.library.ui.adapter.v7.ListAdapter;
import io.ganguo.library.ui.adapter.v7.ViewHolder.BaseViewHolder;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;

/**
 * Created by leo on 16/1/20.
 * 产品分类列表adapter
 */
public class CourseAdapter extends ListAdapter<CourseInfo, ItemCollegeCourseBinding> {
    private Logger logger = LoggerFactory.getLogger(CourseAdapter.class);
    private Activity activity;

    public CourseAdapter(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    public void onBindViewBinding(final BaseViewHolder<ItemCollegeCourseBinding> vh, int position) {
        vh.getBinding().llAction.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                Class className;
                if (XmlUtil.displayVedioIcon(get(vh.getAdapterPosition()).getForm())) {
                    className = WebActivity.class;
                } else {
                    className = CourseDetailedActivity.class;
                }
                Intent intent = new Intent(getContext(), className);
                intent.putExtra(Constants.COLLEGE_COURSE_LIST_ITEM, get(vh.getAdapterPosition()));
                intent.putExtra(Constants.ABOUT_TEXT_URL, get(vh.getAdapterPosition()).getUrl());
                intent.putExtra(Constants.ABOUT_TEXT_TITLE, get(vh.getAdapterPosition()).getCourseName());
                activity.startActivity(intent);
            }
        });
        if (XmlUtil.displayVedioIcon(get(position).getForm())) {
            vh.getBinding().ivVideo.setVisibility(View.VISIBLE);
        } else {
            vh.getBinding().ivVideo.setVisibility(View.GONE);
        }
    }

    @Override
    protected int getItemLayoutId(int position) {
        return R.layout.item_college_course;
    }

}
