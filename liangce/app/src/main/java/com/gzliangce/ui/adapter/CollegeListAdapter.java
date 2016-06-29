package com.gzliangce.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Xml;
import android.view.View;

import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ItemCollegeListBinding;
import com.gzliangce.databinding.ItemSwipeBinding;
import com.gzliangce.entity.CourseInfo;
import com.gzliangce.ui.activity.WebActivity;
import com.gzliangce.ui.activity.college.CourseDetailedActivity;
import com.gzliangce.ui.activity.college.CourseDetailedVideoActivity;
import com.gzliangce.ui.dialog.DeleteDialog;
import com.gzliangce.util.LiangCeUtil;
import com.gzliangce.util.XmlUtil;

import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.core.event.extend.OnSingleClickListener;
import io.ganguo.library.ui.adapter.v7.BaseAdapter;
import io.ganguo.library.ui.adapter.v7.ListAdapter;
import io.ganguo.library.ui.adapter.v7.LoadMoreAdapter;
import io.ganguo.library.ui.adapter.v7.ViewHolder.BaseViewHolder;


/**
 * Created by aaron on 10/20/15.
 */
public class CollegeListAdapter extends LoadMoreAdapter<CourseInfo, ItemCollegeListBinding> {
    private Activity activity;
    private Class className;

    public CollegeListAdapter(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    protected int getItemLayoutId(int position) {
        return R.layout.item_college_list;
    }

    @Override
    public void onBindViewBinding(final BaseViewHolder<ItemCollegeListBinding> vh, int position) {
        vh.getBinding().rlAction.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (vh.getAdapterPosition() >= size()) {
                    return;
                }
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
    }

}
