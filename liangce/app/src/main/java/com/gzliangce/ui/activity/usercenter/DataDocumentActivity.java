package com.gzliangce.ui.activity.usercenter;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.view.View;

import com.gzliangce.R;
import com.gzliangce.bean.Constants;
import com.gzliangce.databinding.ActivityDataDocumentBinding;
import com.gzliangce.databinding.ActivityMyCollectionBinding;
import com.gzliangce.enums.DocumentType;
import com.gzliangce.ui.base.BaseSwipeBackActivityBinding;
import com.gzliangce.ui.model.HeaderModel;

import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;

/**
 * 文档资料界面
 */
public class DataDocumentActivity extends BaseSwipeBackActivityBinding implements View.OnClickListener {
    private ActivityDataDocumentBinding binding;
    private String orderNumber;

    @Override
    public void beforeInitView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_data_document);
    }

    @Override
    public void initView() {
    }

    @Override
    public void initListener() {
        binding.rlyContract.setOnClickListener(this);
        binding.rlyBank.setOnClickListener(this);
        binding.rlyLoan.setOnClickListener(this);
    }

    @Override
    public void initData() {
        orderNumber = getIntent().getStringExtra(Constants.ORDER_NUMBER);
        setHeader();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rly_contract:
                doIntent(DocumentType.contract.toString());
                break;
            case R.id.rly_bank:
                doIntent(DocumentType.bankDetail.toString());
                break;
            case R.id.rly_loan:
                doIntent(DocumentType.loanDetail.toString());
                break;
        }
    }

    @Override
    public void onBackClicked() {
        finish();
    }

    @Override
    public void onMenuClicked() {
    }

    /**
     * 设置header
     */
    private void setHeader() {
        header = new HeaderModel(this);
        header.setMidTitle(orderNumber);
        binding.setHeader(header);
    }

    private void doIntent(String DocumentType) {
        Intent intent = new Intent(this, DocumentPhotoActivity.class);
        intent.putExtra(Constants.ORDER_NUMBER, orderNumber);
        intent.putExtra(Constants.DOCUMENT_DATA_TYPE, DocumentType);
        startActivity(intent);
    }

}
