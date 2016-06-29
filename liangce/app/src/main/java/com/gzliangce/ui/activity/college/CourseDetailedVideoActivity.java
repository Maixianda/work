package com.gzliangce.ui.activity.college;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.view.View;

import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ActivityCourseVideoDetailedBinding;
import com.gzliangce.entity.CourseInfo;
import com.gzliangce.entity.NewsTypeInfo;
import com.gzliangce.ui.base.BaseSwipeBackActivityBinding;
import com.gzliangce.ui.model.HeaderModel;

/**
 * Created by leo on 16/3/16.
 * 视频详情界面
 */
public class CourseDetailedVideoActivity extends BaseSwipeBackActivityBinding implements View.OnClickListener {
    private ActivityCourseVideoDetailedBinding binding;
    private CourseInfo courseInfo;

    @Override
    public void beforeInitView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_course_video_detailed);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initListener() {
        binding.ibPlayVideo.setOnClickListener(this);
        binding.flyOpenVedio.setOnClickListener(this);
    }

    @Override
    public void initData() {
        courseInfo = (CourseInfo) getIntent().getSerializableExtra(Constants.COLLEGE_COURSE_LIST_ITEM);
        logger.e("courseInfo: " + courseInfo.toString());
        binding.setData(courseInfo);
        setHeader();
    }

    /**
     * 设置header
     */
    private void setHeader() {
        header = new HeaderModel(this);
        header.setMidTitle(courseInfo.getCourseName());
        header.setRightBackground(0);
        binding.setHeader(header);
    }

    @Override
    public void onBackClicked() {
        onBackPressed();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fly_open_vedio:
            case R.id.ib_play_video:
                playVideo();
                break;
        }
    }

    private void playVideo() {
        Intent intent = new Intent(this, CourseVideoOpenActivity.class);
        intent.putExtra(Constants.COLLEGE_COURSE_LIST_ITEM, courseInfo);
        startActivity(intent);
    }
}
