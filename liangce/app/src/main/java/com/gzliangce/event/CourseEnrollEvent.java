package com.gzliangce.event;

import com.gzliangce.entity.CourseInfo;

import io.ganguo.library.core.event.Event;

/**
 * Created by leo on 16/3/25.
 * 课程数据刷新
 */
public class CourseEnrollEvent implements Event {
    private CourseInfo courseInfo;
    private boolean isEnroll;

    public boolean isEnroll() {
        return isEnroll;
    }

    public void setEnroll(boolean enroll) {
        isEnroll = enroll;
    }

    public CourseEnrollEvent(CourseInfo courseInfo) {
        this.courseInfo = courseInfo;
    }

    public CourseEnrollEvent(CourseInfo courseInfo, boolean isEnroll) {
        this.courseInfo = courseInfo;
        this.isEnroll = isEnroll;
    }

    public void setCourseInfo(CourseInfo courseInfo) {
        this.courseInfo = courseInfo;
    }

    public CourseInfo getCourseInfo() {
        return courseInfo;
    }

    @Override
    public String toString() {
        return "CourseEnrollEvent{" +
                "courseInfo=" + courseInfo +
                '}';
    }
}
