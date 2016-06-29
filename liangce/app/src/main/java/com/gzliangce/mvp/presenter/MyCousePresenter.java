package com.gzliangce.mvp.presenter;

import com.gzliangce.dto.ListDTO;
import com.gzliangce.entity.CourseInfo;
import com.gzliangce.http.APICallback;
import com.gzliangce.mvp.view.MyCouseMvpView;
import com.gzliangce.service.OtherDataService;
import com.gzliangce.util.ApiUtil;

import java.util.List;

import io.ganguo.library.util.Tasks;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;
import retrofit.Call;

/**
 * Created by leo on 16/3/25.
 * 我的课程 - 逻辑数据处理层
 */
public class MyCousePresenter {
    private static Logger logger = LoggerFactory.getLogger(MyCousePresenter.class);
    private MyCouseMvpView<CourseInfo> myCouseMvpView;
    private OtherDataService otherDataService;

    public MyCousePresenter(MyCouseMvpView<CourseInfo> myCouseMvpView) {
        this.myCouseMvpView = myCouseMvpView;
        otherDataService = ApiUtil.getOtherDataService();
    }


    /**
     * 加载列表数据
     */
    public void loadData() {
        Call<ListDTO<CourseInfo>> call = otherDataService.getMyCourseListData(myCouseMvpView.getPage(), 20);
        call.enqueue(new APICallback<ListDTO<CourseInfo>>() {
            @Override
            public void onSuccess(ListDTO<CourseInfo> courseInfoListDTO) {
                if (courseInfoListDTO != null) {
                    myCouseMvpView.showCourseListData(courseInfoListDTO);
                }
            }

            @Override
            public void onFailed(String message) {
                logger.e("onFailed:" + message);
                if (myCouseMvpView.getPage() > 1) {
                    myCouseMvpView.setPage();
                }
            }

            @Override
            public void onFinish() {
                myCouseMvpView.hideLoading();
            }
        });
    }


    /**
     * 删除对应的课程数据
     */
    public void deleteCourse(final CourseInfo courseInfo) {
        Tasks.runOnQueue(new Runnable() {
            @Override
            public void run() {
                removeData(courseInfo);
            }
        });
    }

    /**
     * 遍历删除
     */
    private void removeData(CourseInfo courseInfo) {
        List<CourseInfo> list = myCouseMvpView.getListData();
        for (CourseInfo info : list) {
            if (courseInfo.getCourseId() == info.getCourseId()) {
                myCouseMvpView.getListData().remove(info);
                Tasks.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        myCouseMvpView.setHint();
                    }
                });
                break;
            }
        }
    }
}
