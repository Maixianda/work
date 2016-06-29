package com.gzliangce.ui.activity.usercenter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.bigkoo.pickerview.OptionsPickerView;
import com.gzliangce.AppContext;
import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ActivityUserSwipeBinding;
import com.gzliangce.dto.BaseDTO;
import com.gzliangce.dto.MetadataDTO;
import com.gzliangce.entity.AccountInfo;
import com.gzliangce.entity.AccountStatusInfo;
import com.gzliangce.http.APICallback;
import com.gzliangce.ui.adapter.UserSwipedAdapter;
import com.gzliangce.ui.base.BaseActivityBinding;
import com.gzliangce.ui.base.BaseSwipeBackActivityBinding;
import com.gzliangce.ui.model.HeaderModel;
import com.gzliangce.util.ApiUtil;
import com.gzliangce.util.MetadataUtil;
import com.gzliangce.util.PhotoUtil;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.ganguo.library.common.LoadingHelper;
import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;
import retrofit.Call;

/**
 * 个人语言跟职能范围界面
 */
public class UserSwipeActivity extends BaseSwipeBackActivityBinding implements View.OnClickListener {
    private Logger logger = LoggerFactory.getLogger(UserSwipeActivity.class);
    private ActivityUserSwipeBinding binding;
    private UserSwipedAdapter adapter;
    private AccountInfo userInfo;
    private AccountStatusInfo statusInfo;
    private OptionsPickerView pvOptions;
    private MetadataDTO dto;
    private List<String> oldBusiness = new ArrayList<>();

    private ArrayList<String> options1Items = new ArrayList<String>();

    private int fromType;
    private String pickerValues, updateValues;

    @Override
    public void beforeInitView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_swipe);
        fromType = getIntent().getIntExtra(Constants.ACTIVITY_FROM_TYPE, 1);
        setHeader();
    }

    @Override
    public void initView() {
        adapter = new UserSwipedAdapter(this, fromType);
        binding.rcvLanguage.setAdapter(adapter);
        binding.rcvLanguage.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void initListener() {
    }

    @Override
    public void initData() {
        getMetaData();
        getUserInfo();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    @Override
    public void onBackClicked() {
        finish();
    }

    @Override
    public void onMenuClicked() {
        if (pvOptions != null) {
            pvOptions.show();
        }
    }

    /**
     * 设置header
     */
    private void setHeader() {
        header = new HeaderModel(this);
        if (fromType == 1) {
            header.setMidTitle("语言");
        } else {
            header.setMidTitle("职能范围");
        }
        header.setRightTitle("添加");
        binding.setHeader(header);
    }

    /**
     * 从缓存中获取数据
     */
    private void getMetaData() {
        dto = (MetadataDTO) MetadataUtil.getGCache(this, Constants.METADATA_DATA_KEY);
        if (dto != null) {
            if (fromType == 1) {
                initPickViewData(dto.getLanguages(), ":", true);
            } else {
                initPickViewData(dto.getBusiness(), ":", true);
            }
        }
        initPickerView();
    }

    /**
     * 初始化选择器数据
     *
     * @param sourceData
     */
    private void initPickViewData(String sourceData, String splitSign, boolean isPickView) {
        String[] dataArray = sourceData.split(splitSign);
        if (fromType != 1 && isPickView) {
            options1Items.add("全部");
        }
        for (int i = 0; i < dataArray.length; i++) {
            if (isPickView) {
                options1Items.add(dataArray[i]);
            } else {
                adapter.add(dataArray[i]);
            }
        }
    }

    /**
     * 例子：获取用户信息
     */
    private void getUserInfo() {
        if (AppContext.me().isLogined()) {
            AccountInfo accountInfo = AppContext.me().getUser();
            if (fromType == 1) {
                initPickViewData(accountInfo.getInfo().getLanguage(), ",", false);
            } else {
                initPickViewData(accountInfo.getInfo().getBusiness(), ",", false);
            }
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 例子：选择器
     */
    private void initPickerView() {
        //选项选择器
        pvOptions = new OptionsPickerView(this);

        //三级联动效果
        pvOptions.setPicker(options1Items, null, null, true);
        pvOptions.setCyclic(false, true, true);
        //设置默认选中的三级项目
        //监听确定选择按钮
        pvOptions.setSelectOptions(0);
        pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                //返回的分别是三个级别的选中位置
                pickerValues = options1Items.get(options1);
                if (fromType != 1 && options1 == 0) {
                    addAllBusiness();
                    return;
                }
                if (adapter.getData().contains(pickerValues)) {
                    if (fromType == 1) {
                        ToastHelper.showMessage(UserSwipeActivity.this, "已添加过该语言了");
                    } else {
                        ToastHelper.showMessage(UserSwipeActivity.this, "已添加过该职能范围了");
                    }
                    return;
                }
                adapter.add(0, pickerValues);
                updateDatum(true, 0, "");
            }
        });
    }


    /**
     * 添加全部职能
     */
    private void addAllBusiness() {
        oldBusiness.clear();
        oldBusiness.addAll(adapter.getData());
        if (dto == null) {
            ToastHelper.showMessage(this, "数据异常");
            return;
        }
        adapter.clear();
        adapter.addAll(options1Items.subList(1, options1Items.size()));
        updateDatum(true, -1, "");
    }

    /**
     * 更新语言或者职能
     *
     * @param isAdd
     * @param postion
     * @param postionValues
     */
    public void updateDatum(final boolean isAdd, final int postion, final String postionValues) {
        updateValues = "";
        LoadingHelper.showMaterLoading(UserSwipeActivity.this, "添加数据中...");
        for (int i = 0; i < adapter.size(); i++) {
            if (i == 0) {
                updateValues = adapter.get(i);
            } else {
                updateValues = updateValues + "," + adapter.get(i);
            }
        }
        Map<String, String> map = new HashMap<>();
        if (fromType == 1) {
            map.put("languages", updateValues);
        } else {
            map.put("business", updateValues);
        }
        Call<BaseDTO> call = ApiUtil.getUserCenterService().postUserDatum(map);
        call.enqueue(new APICallback<BaseDTO>() {
            @Override
            public void onSuccess(BaseDTO baseDTO) {
                handlerData(isAdd, postion);
            }

            @Override
            public void onFailed(String message) {
                handlerFaileData(isAdd, postion, postionValues);
                ToastHelper.showMessage(UserSwipeActivity.this, message);
            }

            @Override
            public void onFinish() {
                LoadingHelper.hideMaterLoading();
            }
        });
    }

    /**
     * 处理接口成功数据
     *
     * @param isAdd
     * @param position
     */
    private void handlerData(boolean isAdd, int position) {
        AccountInfo accountInfo = AppContext.me().getUser();
        if (fromType == 1) {
            accountInfo.getInfo().setLanguage(updateValues);
        } else {
            accountInfo.getInfo().setBusiness(updateValues);
        }
        AppContext.me().setUser(accountInfo);
        if (isAdd) {
            if (position != -1) {
                adapter.notifyItemInserted(0);
            } else {
                adapter.notifyDataSetChanged();
            }
        } else {
            adapter.notifyItemRemoved(position);
        }
    }

    /**
     * 处理请求接口失败的情况
     */
    private void handlerFaileData(boolean isAdd, int postion, String postionValues) {
        if (isAdd) {
            if (postion == -1) {
                adapter.clear();
                adapter.addAll(oldBusiness);
                adapter.notifyDataSetChanged();
                oldBusiness.clear();
            } else {
                adapter.remove(0);
            }
        } else {
            adapter.add(postion, postionValues);
        }
    }

    @Override
    public void onBackPressed() {
        if (pvOptions.isShowing()) {
            pvOptions.dismiss();
            return;
        }
        super.onBackPressed();
    }
}
