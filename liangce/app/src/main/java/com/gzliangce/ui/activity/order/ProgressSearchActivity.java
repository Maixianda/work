package com.gzliangce.ui.activity.order;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ActivityProgressSearchBinding;
import com.gzliangce.dto.ProgressDTO;
import com.gzliangce.entity.OrderProgress;
import com.gzliangce.entity.Progress;
import com.gzliangce.enums.UserType;
import com.gzliangce.http.APICallback;
import com.gzliangce.ui.adapter.ProgressSearchAdapter;
import com.gzliangce.ui.base.BaseSwipeBackActivityBinding;
import com.gzliangce.ui.model.HeaderModel;
import com.gzliangce.util.ApiUtil;
import com.gzliangce.util.LiangCeUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.util.Strings;
import io.ganguo.library.util.gson.Gsons;
import retrofit.Call;

/**
 * 进度查询
 * Created by aaron on 2/14/16.
 */
public class ProgressSearchActivity extends BaseSwipeBackActivityBinding implements View.OnClickListener {
    private ActivityProgressSearchBinding binding;
    private ProgressSearchAdapter adapter;
    private List<OrderProgress.ConditionEntity> conditionEntityList = new ArrayList<>();

    private String orderNumber;
    private List<String> keyList = new ArrayList<>();
    private List<String> valuesList = new ArrayList<>();

    @Override
    public void beforeInitView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_progress_search);
        setHeader();
    }

    @Override
    public void initView() {
        adapter = new ProgressSearchAdapter(this);
        binding.rcvProgressList.setAdapter(adapter);
        binding.rcvProgressList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        orderNumber = getIntent().getStringExtra(Constants.ORDER_NUMBER);
        getLocalJson();
        getProgressData();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onBackClicked() {
        finish();
    }

    /**
     * 设置header
     */
    private void setHeader() {
        header = new HeaderModel(this);
        header.setMidTitle("进度查询");
        header.setRightBackground(0);
        header.setRightClickable(false);
        binding.setHeader(header);
    }

    private void getLocalJson() {
        final String json = Strings.getStringFromAssets(this, "order_progress.json");
        OrderProgress orderProgress = Gsons.fromJson(json, OrderProgress.class);
        conditionEntityList.clear();
        if (LiangCeUtil.judgeUserType(UserType.mediator)) {
            conditionEntityList.addAll(orderProgress.getOther());
        } else {
            conditionEntityList.addAll(orderProgress.getMortgage());
        }
        adapter.addAll(conditionEntityList);
        adapter.notifyDataSetChanged();
    }

    /**
     * 获取进度资料
     */
    private void getProgressData() {
        Call<ProgressDTO> call = ApiUtil.getOrderService().getProgress(orderNumber);
        call.enqueue(new APICallback<ProgressDTO>() {
            @Override
            public void onSuccess(ProgressDTO progressDTO) {
                logger.e(progressDTO.getProgress().toString());
                handleData(progressDTO.getProgress().toHashMap());
            }

            @Override
            public void onFailed(String message) {
                ToastHelper.showMessage(ProgressSearchActivity.this, message);
            }

            @Override
            public void onFinish() {
            }
        });
    }

    private void handleData(HashMap<String, String> progress) {
        Iterator iter = progress.entrySet().iterator();

        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            if (!Strings.isEmpty((String) entry.getValue())) {
                keyList.add(entry.getKey().toString());
                valuesList.add(entry.getValue().toString());
            }
        }

        for (OrderProgress.ConditionEntity conditionEntity : conditionEntityList) {
            for (int i = 0; i < keyList.size(); i++) {
                if (Strings.isEquals(conditionEntity.getConditionName(), keyList.get(i))) {
                    conditionEntity.setConditionValues(valuesList.get(i));
                }
            }
        }
        adapter.notifyDataSetChanged();

    }
}
