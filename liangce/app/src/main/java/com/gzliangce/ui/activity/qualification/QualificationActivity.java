package com.gzliangce.ui.activity.qualification;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;

import com.gzliangce.AppContext;
import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ActivityQualificationBinding;
import com.gzliangce.dto.AuthUploadDTO;
import com.gzliangce.dto.BaseDTO;
import com.gzliangce.dto.ListDTO;
import com.gzliangce.entity.AccountInfo;
import com.gzliangce.entity.AccountStatusInfo;
import com.gzliangce.entity.CompanyInfo;
import com.gzliangce.entity.DepartmentInfo;
import com.gzliangce.entity.QualificationImageInfo;
import com.gzliangce.enums.AttestationType;
import com.gzliangce.event.DeletePreviewImageEvent;
import com.gzliangce.http.APICallback;
import com.gzliangce.ui.activity.PreviewImageActivity;
import com.gzliangce.ui.adapter.CompanyFilterAdapter;
import com.gzliangce.ui.adapter.DepartmentFilterAdapter;
import com.gzliangce.ui.base.BaseSwipeBackActivityBinding;
import com.gzliangce.ui.dialog.PictureChoseDialog;
import com.gzliangce.ui.model.HeaderModel;
import com.gzliangce.util.ApiDataUtil;
import com.gzliangce.util.ApiUtil;
import com.gzliangce.util.PhotoUtil;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.ganguo.library.common.LoadingHelper;
import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.core.event.extend.OnSingleClickListener;
import io.ganguo.library.core.image.PhotoLoader;
import io.ganguo.library.util.Collections;
import io.ganguo.library.util.Regs;
import io.ganguo.library.util.Strings;
import io.ganguo.library.util.Tasks;
import retrofit.Call;

/**
 * Created by leo on 16/1/14.
 * 资格认证界面
 */
public class QualificationActivity extends BaseSwipeBackActivityBinding implements TextWatcher {
    private ActivityQualificationBinding binding;
    private PictureChoseDialog pictureChoseDialog;
    private boolean isIdCard = true;
    private File idCardFile, certificateFile;
    private List<QualificationImageInfo> imageInfos = new ArrayList<QualificationImageInfo>();
    private String IDCARD = "IDCARD", CERTIFICATEFILE = "CERTIFICATEFILE";
    //保存图片上传成功后的数据
    private List<AuthUploadDTO> uploadDTOList = new ArrayList<>();
    private int upLoadIndex = 0;
    //审核状态时图片url
    private String idCardUrl = null, otherCardUrl = null;
    private CompanyFilterAdapter companyFilterAdapter;
    private DepartmentFilterAdapter departmentFilterAdapter;

    @Override
    public void beforeInitView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_qualification);
        setHeader();
    }

    @Override
    public void initView() {
        companyFilterAdapter = new CompanyFilterAdapter(R.layout.item_complete_textview);
        binding.etInstitutions.setAdapter(companyFilterAdapter);
        binding.etInstitutions.setThreshold(1);
        departmentFilterAdapter = new DepartmentFilterAdapter(R.layout.item_complete_textview);
        binding.etBranch.setAdapter(departmentFilterAdapter);
        binding.etBranch.setThreshold(1);
    }

    @Override
    public void initListener() {
        header.onBackClickListener();
        binding.etInstitutions.setOnItemClickListener(companyItem);
        binding.etBranch.setOnItemClickListener(departmentItem);
        binding.etUserName.addTextChangedListener(this);
        binding.etInstitutions.addTextChangedListener(this);
        binding.etBranch.addTextChangedListener(this);
        binding.tvCommit.setOnClickListener(onSingleClickListener);
        binding.ibtnIdCard.setOnClickListener(onSingleClickListener);
        binding.ibtnCertificate.setOnClickListener(onSingleClickListener);
    }


    /**
     * 机构onItemClick
     */
    private AdapterView.OnItemClickListener companyItem = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            binding.etInstitutions.setText(companyFilterAdapter.get(position).getCompanyName());
            binding.etInstitutions.setSelection(binding.etInstitutions.getText().length());
            departmentFilterAdapter.getData().clear();
            if (!Collections.isEmpty(companyFilterAdapter.get(position).getChild())) {
                departmentFilterAdapter.addAll(companyFilterAdapter.get(position).getChild());
            }
            departmentFilterAdapter.refreshFilterData();
            departmentFilterAdapter.notifyDataSetChanged();
        }
    };


    /**
     * 机构departmentItem
     */
    private AdapterView.OnItemClickListener departmentItem = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            binding.etBranch.setText(departmentFilterAdapter.get(position).getDepartmentName());
            binding.etBranch.setSelection(binding.etBranch.getText().length());
        }
    };


    @Override
    public void initData() {
        header.setMidTitle("资格认证");
        delayGetOrganization();
        setBtnTextState();
    }

    /**
     * 延时获取机构数据
     */
    private void delayGetOrganization() {
        LoadingHelper.showMaterLoading(this, "加载中");
        Tasks.handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getOrganizationData();
            }
        }, 700);
    }


    /**
     * 设置按钮文字状态
     */
    private void setBtnTextState() {
        if (!AppContext.me().isLogined()) {
            return;
        }
        logger.e("user:" + AppContext.me().getUser().toString());
        AccountInfo info = AppContext.me().getUser();
        String status = info.getInfo().getStatus();
        if (Strings.isEquals(status, AttestationType.check.toString())) {
            setAllWidghtState(false);
            setCheckData(info, AttestationType.check);
            binding.tvCommit.setText("待审核");
            return;
        }
        if (Strings.isEquals(status, AttestationType.noauth.toString())) {
            binding.tvCommit.setText("提交资料");
            return;
        }
        if (Strings.isEquals(status, AttestationType.nopass.toString())) {
            binding.tvCommit.setText("重新提交");
            return;
        }
        setCheckData(info, AttestationType.pass);
        binding.tvCommit.setText("重新提交");
    }


    /**
     * 设置全部的控件状态
     */
    private void setAllWidghtState(boolean flag) {
        binding.tvCommit.setEnabled(flag);
        binding.etBranch.setFocusable(flag);
        binding.etInstitutions.setFocusable(flag);
        binding.etUserName.setFocusable(flag);
        binding.etBranch.setFocusableInTouchMode(flag);
        binding.etInstitutions.setFocusableInTouchMode(flag);
        binding.etUserName.setFocusableInTouchMode(flag);
    }

    /**
     * 设置审核中的资料显示
     */
    private void setCheckData(AccountInfo info, AttestationType type) {
        binding.etUserName.setText(info.getRealName());
        binding.etBranch.setText(info.getInfo().getBranch());
        binding.etInstitutions.setText(info.getInfo().getOrganizationName());
        if (type == AttestationType.pass) {
            return;
        }
        otherCardUrl = info.getInfo().getOtherCard();
        idCardUrl = info.getInfo().getIdentityCard();
        if (Strings.isNotEmpty(idCardUrl)) {
            idCardUrl = getUrl(idCardUrl);
            PhotoLoader.display(binding.ibtnIdCard, idCardUrl, getResources().getDrawable(R.drawable.shape_image_loading));
        }
        if (Strings.isNotEmpty(otherCardUrl)) {
            otherCardUrl = getUrl(otherCardUrl);
            PhotoLoader.display(binding.ibtnCertificate, otherCardUrl, getResources().getDrawable(R.drawable.shape_image_loading));
        }
    }


    /**
     * 判断图片链接
     */
    public String getUrl(String url) {
        if (Regs.isUrl(url)) {
            return url;
        }
        return "http://jinrongqiao.oss-cn-shenzhen.aliyuncs.com" + url;
    }

    /**
     * 设置header
     */
    private void setHeader() {
        header = new HeaderModel(this);
        binding.setHeader(header);
    }

    @Override
    public void onBackClicked() {
        onBackPressed();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            //拍照返回的结果
            case PhotoUtil.TAKE_PHOTO_VALUS:
                if (!PhotoUtil.getPicPath().exists()) {
                    return;
                }
                getCameoData();
                break;
            //选择图库返回的结果
            case PhotoUtil.CHOSE_PHOTO_GALLERY_VALUES:
                if (data == null || data.getData() == null) {
                    return;
                }
                getPhotoGalleryData(data);
                break;
        }
        setBtnState();
    }

    /**
     * 获取牌照数据
     */
    private void getCameoData() {
        if (isIdCard) {
            idCardFile = PhotoUtil.getPicPath();
        } else {
            certificateFile = PhotoUtil.getPicPath();
        }
        loadImageFile();
    }


    /**
     * 获取选择照片数据
     */
    private void getPhotoGalleryData(Intent data) {
        File file = PhotoUtil.getTempFile();
        if (isIdCard) {
            idCardFile = file;
            PhotoUtil.saveBitmapToFile(idCardFile, getContentResolver(), data.getData());
        } else {
            certificateFile = file;
            PhotoUtil.saveBitmapToFile(certificateFile, getContentResolver(), data.getData());
        }
        loadImageFile();
    }

    /**
     * 加载图片
     */
    private void loadImageFile() {
        if (idCardFile != null) {
            PhotoLoader.display(binding.ibtnIdCard, idCardFile, getResources().getDrawable(R.drawable.shape_image_loading));
        }
        if (certificateFile != null) {
            PhotoLoader.display(binding.ibtnCertificate, certificateFile, getResources().getDrawable(R.drawable.shape_image_loading));
        }
    }


    /**
     * onClick事件
     */
    private OnSingleClickListener onSingleClickListener = new OnSingleClickListener() {
        @Override
        public void onSingleClick(View v) {
            switch (v.getId()) {
                case R.id.ibtn_certificate:
                    isIdCard = false;
                    actionSelector();
                    break;
                case R.id.ibtn_id_card:
                    isIdCard = true;
                    actionSelector();
                    break;
                case R.id.tv_commit:
                    actionCommitData();
                    break;
            }
        }
    };

    /**
     * 提交资料
     */
    private void actionCommitData() {
        if (!isEditComplete(true)) {
            return;
        }
        LoadingHelper.showMaterLoading(this, "正在提交");
        Tasks.handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                actionUpLoad();
            }
        }, 1000);
    }


    /**
     * 上传照片到服务器
     */
    private void upLoadImageFile(File file) {
        Map<String, RequestBody> map = new HashMap<>();
        RequestBody body = RequestBody.create(MediaType.parse("image/jpg"), file);
        map.put("file\"; filename=\"" + file.getAbsolutePath() + "", body);
        Call<AuthUploadDTO> call = ApiUtil.getAttestation().posAuthtUpLoadImage(map);
        call.enqueue(new APICallback<AuthUploadDTO>() {
            @Override
            public void onSuccess(AuthUploadDTO authUploadDTO) {
                if (authUploadDTO != null) {
                    uploadDTOList.add(authUploadDTO);
                    upLoadIndex++;
                    actionUpLoad();
                    return;
                }
                onFailed("上传照片失败");
            }

            @Override
            public void onFailed(String message) {
                upLoadIndex = 0;
                LoadingHelper.hideMaterLoading();
                ToastHelper.showMessage(QualificationActivity.this, message);
            }

            @Override
            public void onFinish() {
            }
        });
    }


    /**
     * 提交上传
     */
    private void actionUpLoad() {
        if (imageInfos.size() <= 0) {
            imageInfos.add(new QualificationImageInfo(idCardFile, IDCARD, "身份证", null));
            imageInfos.add(new QualificationImageInfo(certificateFile, CERTIFICATEFILE, "资格证书/名片", null));
        }
        if (uploadDTOList.size() >= 2 || upLoadIndex >= imageInfos.size()) {
            upLoadIndex = 0;
            postMediatorAuth();
        } else {
            upLoadImageFile(imageInfos.get(upLoadIndex).getFile());
        }

    }

    /**
     * 打开头像选择Dialog
     */
    private void postMediatorAuth() {
        Call<BaseDTO> call = ApiUtil.getAttestation().postMediatorAuthData(getPostData());
        call.enqueue(new APICallback<BaseDTO>() {
            @Override
            public void onSuccess(BaseDTO dto) {
                handlerMediatorData();
                finish();
            }

            @Override
            public void onFailed(String message) {
                ToastHelper.showMessage(QualificationActivity.this, message);
            }

            @Override
            public void onFinish() {
                LoadingHelper.hideMaterLoading();
            }
        });
    }


    /**
     * 数据提交成功
     */
    private void handlerMediatorData() {
        AccountStatusInfo info = AppContext.me().getUser().getInfo();
        info.setOrganizationName(binding.etInstitutions.getText().toString().trim());
        info.setStatus(AttestationType.check.toString());
        info.setBranch(binding.etBranch.getText().toString().trim());
        info.setIdentityCard(uploadDTOList.get(0).getLink());
        info.setOtherCard(uploadDTOList.get(1).getLink());
        AppContext.me().setUser(AppContext.me().getUser());
        ToastHelper.showMessage(QualificationActivity.this, "提交成功，等待审核");
    }

    /**
     * 获取需要提交的数据
     */
    private Map<String, String> getPostData() {
        HashMap<String, String> map = new HashMap<>();
        map.put("realName", binding.etUserName.getText().toString().trim());
        map.put("identityCard", uploadDTOList.get(0).getValue());
        map.put("otherCard", uploadDTOList.get(1).getValue());
        map.put("organizationName", binding.etInstitutions.getText().toString().trim());
        map.put("branch", binding.etBranch.getText().toString().trim());
        return map;
    }


    /**
     * 打开头像选择Dialog
     */
    private void showSelecterDialog() {
        if (pictureChoseDialog == null) {
            pictureChoseDialog = new PictureChoseDialog(this);
        }
        pictureChoseDialog.show();
    }

    /**
     * 照片点击
     */
    private void actionSelector() {
        if (idCardUrl != null && otherCardUrl != null) {
            actionImageUrlPreviewActivity();
            return;
        }
        if (isIdCard && idCardFile == null) {
            showSelecterDialog();
            return;
        }
        if (!isIdCard && certificateFile == null) {
            showSelecterDialog();
            return;
        }
        actionFilePreviewActivity();
    }

    /**
     * 传图片url到预览页
     */
    private void actionImageUrlPreviewActivity() {
        imageInfos.clear();
        int index = 0;
        imageInfos.add(new QualificationImageInfo(null, IDCARD, "身份证", idCardUrl));
        imageInfos.add(new QualificationImageInfo(null, CERTIFICATEFILE, "资格证书/名片", otherCardUrl));
        if (!isIdCard) {
            index = 1;
        }
        actionPreviewActivity(index);
    }


    /**
     * 传图片file到预览页
     */
    private void actionFilePreviewActivity() {
        imageInfos.clear();
        int index = 0;
        if (idCardFile != null) {
            imageInfos.add(new QualificationImageInfo(idCardFile, IDCARD, "身份证", null));
        }
        if (certificateFile != null) {
            imageInfos.add(new QualificationImageInfo(certificateFile, CERTIFICATEFILE, "资格证书/名片", null));
        }
        if (!isIdCard && certificateFile != null && idCardFile != null) {
            index = 1;
        }
        actionPreviewActivity(index);
    }


    /**
     * 跳转到图片预览页
     */
    private void actionPreviewActivity(int index) {
        Intent intent = new Intent(this, PreviewImageActivity.class);
        intent.putExtra(Constants.PREVIEW_IMAGE_ACTIVITY_DATA, (Serializable) imageInfos);
        intent.putExtra(Constants.PREVIEW_IMAGE_ACTIVITY_INDEX_DATA, index);
        startActivity(intent);
    }


    /**
     * 接收广播删除照片
     */
    @Subscribe
    public void onDeletePreviewImageEvent(DeletePreviewImageEvent event) {
        if (event.getInfo() == null) {
            return;
        }
        String flag = event.getInfo().getFlag();
        if (Strings.isEquals(flag, IDCARD)) {
            idCardFile = null;
            PhotoLoader.display(this, R.drawable.shape_image_loading, R.drawable.ic_up_load_id_card, binding.ibtnIdCard);
        } else {
            certificateFile = null;
            PhotoLoader.display(this, R.drawable.shape_image_loading, R.drawable.ic_up_load_certificate, binding.ibtnCertificate);
        }
        for (QualificationImageInfo info : imageInfos) {
            if (Strings.isEquals(info.getFlag(), flag)) {
                imageInfos.remove(info);
                return;
            }
        }
        loadImageFile();
    }


    /**
     * 输入监听
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        setBtnState();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }


    /**
     * 设置按钮是否可以点击
     */
    private void setBtnState() {
        if (!isEditComplete(false)) {
            binding.tvCommit.setEnabled(false);
            return;
        }
        binding.tvCommit.setEnabled(true);
    }

    /**
     * 判断必填选项是否全部输入完毕
     */
    private boolean isEditComplete(boolean isHint) {
        String hint = null;
        boolean isComplete = true;
        if (Strings.isEmpty(binding.etUserName.getText().toString())) {
            hint = "请填写真实姓名";
            isComplete = false;
        } else if (Strings.isEmpty(binding.etInstitutions.getText().toString())) {
            hint = "请填写中介所属机构";
            isComplete = false;
        } else if (Strings.isEmpty(binding.etBranch.getText().toString())) {
            hint = "请填写中介所属分行";
            isComplete = false;
        } else if (idCardFile == null) {
            hint = "请添加要上传的身份证照片";
            isComplete = false;
        } else if (certificateFile == null) {
            hint = "请添加要上传的资格证书/名片照片";
            isComplete = false;
        }
        if (!isComplete && isHint) {
            ToastHelper.showMessage(this, hint);
        }
        return isComplete;
    }

    /**
     * 获取机构列表数据
     */
    private void getOrganizationData() {
        ApiUtil.getOtherDataService().getOrganizationData().enqueue(new APICallback<ListDTO<CompanyInfo>>() {
            @Override
            public void onSuccess(ListDTO<CompanyInfo> companyInfoListDTO) {
                logger.e("companyInfoListDTO:" + companyInfoListDTO.toString());
                handlerOrganization(companyInfoListDTO);
            }

            @Override
            public void onFailed(String message) {
                ToastHelper.showMessage(QualificationActivity.this, message);
            }

            @Override
            public void onFinish() {
                LoadingHelper.hideMaterLoading();
            }
        });
    }

    /**
     * 处理机构数据
     */
    private void handlerOrganization(ListDTO<CompanyInfo> dto) {
        if (dto == null) {
            return;
        }
        if (Collections.isEmpty(dto.getList())) {
            return;
        }
        companyFilterAdapter.addAll(dto.getList());
        companyFilterAdapter.refreshFilterData();
        companyFilterAdapter.notifyDataSetChanged();
    }


}
