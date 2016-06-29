package com.gzliangce.ui.adapter;

import android.widget.TextView;

import com.gzliangce.R;
import com.gzliangce.entity.CompanyInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leo on 16/5/24.
 * Company数据 筛选Adapter
 */
public class CompanyFilterAdapter extends BaseFilterAdapter<CompanyInfo> {
    public CompanyFilterAdapter(int layoutId) {
        super(layoutId);
    }

    @Override
    public void onBindDataToView(DataHodler hodler, CompanyInfo companyInfo) {
        TextView textView = hodler.getView(R.id.tv_name);
        textView.setText(companyInfo.getCompanyName());
    }

    @Override
    public List<CompanyInfo> onFilterRule(String prefixString, List<CompanyInfo> unfilteredValues) {
        ArrayList<CompanyInfo> newValue = new ArrayList<>();
        for (CompanyInfo info : unfilteredValues) {
            if (info.getCompanyName().contains(prefixString)) {
                newValue.add(info);
            }
        }
        return newValue;
    }
}
