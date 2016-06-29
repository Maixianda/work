package com.gzliangce.ui.activity.college;

import android.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.net.Uri;

import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ActivityCollegeVideoOpenBinding;
import com.gzliangce.entity.CourseInfo;
import com.gzliangce.ui.base.BaseSwipeBackActivityBinding;

import io.ganguo.library.common.ToastHelper;

/**
 * Created by aaron on 16/3/17.
 * 视频播放界面
 */
public class CourseVideoOpenActivity extends BaseSwipeBackActivityBinding implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {
    private ActivityCollegeVideoOpenBinding binding;
    private String videoUrl;
    private CourseInfo courseInfo;

    @Override
    public void beforeInitView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_college_video_open);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initListener() {
        binding.exoVideoView.setOnPreparedListener(this);
        binding.exoVideoView.setOnErrorListener(this);
    }

    @Override
    public void initData() {
        courseInfo = (CourseInfo) getIntent().getSerializableExtra(Constants.COLLEGE_COURSE_LIST_ITEM);
        binding.setCover(courseInfo.getCover());
        videoUrl = courseInfo.getUrl();
//        videoUrl = "http://www.sample-videos.com/video/mp4/480/big_buck_bunny_480p_10mb.mp4";
        setupVideoView();
    }

    @Override
    public void onBackClicked() {
        onBackPressed();
    }

    private void setupVideoView() {
        //For now we just picked an arbitrary item to play.  More can be found at
        //https://archive.org/details/more_animation
        binding.exoVideoView.setVideoURI(Uri.parse(videoUrl));
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        //Starts the video playback as soon as it is ready
        binding.exoVideoView.start();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        ToastHelper.showMessage(this, "暂时无法播放该视频！");
        finish();
        return false;
    }

}
