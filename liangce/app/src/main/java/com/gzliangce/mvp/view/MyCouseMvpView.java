package com.gzliangce.mvp.view;

import com.gzliangce.dto.ListDTO;
import com.gzliangce.entity.CourseInfo;

/**
 * Created by leo on 16/3/25.
 * 我的课程列表
 */
public interface MyCouseMvpView<C> extends MvpView {
    void showCourseListData(ListDTO<CourseInfo> dto);

    void hideLoading();

    void setHint();
}
