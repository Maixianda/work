package com.gzliangce.ui.activity.college;

import android.databinding.DataBindingUtil;

import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ActivityIntroduceTeacherBinding;
import com.gzliangce.dto.TutorDTO;
import com.gzliangce.http.APICallback;
import com.gzliangce.ui.base.BaseSwipeBackActivityBinding;
import com.gzliangce.ui.model.HeaderModel;
import com.gzliangce.util.ApiUtil;
import com.gzliangce.util.DialogUtil;
import com.leo.gesturelibray.util.StringUtils;

import io.ganguo.library.common.LoadingHelper;
import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.common.UIHelper;
import io.ganguo.library.util.Tasks;
import retrofit.Call;

/**
 * Created by leo on 16/3/15.
 * 导师详情界面
 */
public class TeacherIntroduceActivity extends BaseSwipeBackActivityBinding {
    private ActivityIntroduceTeacherBinding binding;
    private String id;

    @Override
    public void beforeInitView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_introduce_teacher);
        setHeader();
    }

    @Override
    public void initView() {

    }

    @Override
    public void initListener() {
        header.onBackClickListener();
    }

    @Override
    public void initData() {
        id = getIntent().getStringExtra(Constants.TEACHER_ID);
        getTutorData(id);
    }

    /**
     * 获取导师详细
     */
    private void getTutorData(String userId) {
        if (StringUtils.isEmpty(userId)) {
            return;
        }
        LoadingHelper.showMaterLoading(this, "加载中");
        Call<TutorDTO> call = ApiUtil.getOtherDataService().getTutorData(userId);
        call.enqueue(new APICallback<TutorDTO>() {
            @Override
            public void onSuccess(TutorDTO tutorDTO) {
                if (tutorDTO == null) {
                    return;
                }
                binding.icdHeader.midHeader.setText(tutorDTO.getTutor().getName());
                binding.setData(tutorDTO.getTutor());
            }

            @Override
            public void onFailed(String message) {
                ToastHelper.showMessage(getBaseContext(), "" + message);
            }

            @Override
            public void onFinish() {
                DialogUtil.hideLoading();
            }
        });
    }

    /**
     * 设置header
     */
    private void setHeader() {
        header = new HeaderModel(this);
        header.setMidTitle("");
        header.setRightBackground(0);
        binding.setHeader(header);
    }

    @Override
    public void onBackClicked() {
        onBackPressed();
    }
}
