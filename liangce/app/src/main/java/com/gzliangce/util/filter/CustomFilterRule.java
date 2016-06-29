package com.gzliangce.util.filter;

import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leo on 16/5/23.
 * Filter 数据过滤
 */
public abstract class CustomFilterRule<T> extends Filter {
    private List<T> mUnfilteredData;

    public CustomFilterRule(List<T> data) {
        this.mUnfilteredData = new ArrayList<>(data);
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        String prefixString = constraint.toString().toLowerCase();
        ArrayList<T> newValues = (ArrayList<T>) onFilterData(prefixString, mUnfilteredData);
        results.values = newValues;
        results.count = newValues.size();
        return results;
    }

    public void setUnfilteredData(List<T> data) {
        this.mUnfilteredData = new ArrayList<>(data);
    }

    public abstract List<T> onFilterData(String prefixString, List<T> unfilteredValues);
}
