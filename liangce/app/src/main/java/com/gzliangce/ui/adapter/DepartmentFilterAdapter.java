package com.gzliangce.ui.adapter;

import android.widget.TextView;

import com.gzliangce.R;
import com.gzliangce.entity.CompanyInfo;
import com.gzliangce.entity.DepartmentInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leo on 16/5/24.
 * Department 类型数据筛选Adapter
 */
public class DepartmentFilterAdapter extends BaseFilterAdapter<DepartmentInfo> {
    public DepartmentFilterAdapter(int layoutId) {
        super(layoutId);
    }

    @Override
    public void onBindDataToView(DataHodler hodler, DepartmentInfo departmentInfo) {
        TextView textView = hodler.getView(R.id.tv_name);
        textView.setText(departmentInfo.getDepartmentName());
    }


    @Override
    public List<DepartmentInfo> onFilterRule(String prefixString, List<DepartmentInfo> unfilteredValues) {
        ArrayList<DepartmentInfo> newValue = new ArrayList<>();
        for (DepartmentInfo info : unfilteredValues) {
            if (info.getDepartmentName().contains(prefixString)) {
                newValue.add(info);
            }
        }
        return newValue;
    }
}
