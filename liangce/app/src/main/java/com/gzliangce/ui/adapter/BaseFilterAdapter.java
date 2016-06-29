package com.gzliangce.ui.adapter;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.gzliangce.R;
import com.gzliangce.ui.callback.ListCallback;
import com.gzliangce.util.filter.CustomFilterRule;

import java.util.ArrayList;
import java.util.List;

import io.ganguo.library.util.Collections;

/**
 * Created by leo on 16/5/23.
 * 基类 自定义数据过滤规则 用于AutoCompleteTextView
 */
public abstract class BaseFilterAdapter<T> extends BaseAdapter implements Filterable, ListCallback<T> {
    private CustomFilterRule<T> filter;
    private List<T> data;
    private int layoutId = -1;

    public BaseFilterAdapter(int layoutId) {
        this.layoutId = layoutId;
    }


    @Override
    public CustomFilterRule<T> getFilter() {
        if (filter == null) {
            filter = getCostomFliter();
        }
        return filter;
    }

    public int getLayoutId() {
        if (layoutId != -1) {
            return layoutId;
        }
        return R.layout.item_complete_textview;
    }

    @Override
    public int getCount() {
        return getData().size();
    }

    @Override
    public T getItem(int position) {
        return getData().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DataHodler dataHodler;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(getLayoutId(), null);
            dataHodler = new DataHodler(convertView);
            convertView.setTag(dataHodler);
        } else {
            dataHodler = (DataHodler) convertView.getTag();
        }
        onBindDataToView(dataHodler, getItem(position));
        return convertView;
    }

    @Override
    public T get(int position) {
        return getData().get(position);
    }

    @Override
    public void add(T t) {
        getData().add(t);
    }

    @Override
    public void add(int position, T t) {
        getData().add(position, t);
    }

    @Override
    public void addAll(List<T> allData) {
        if (Collections.isEmpty(allData)) {
            throw new NullPointerException("List is object not null");
        }
        getData().addAll(allData);
    }

    @Override
    public void remove(T t) {
        getData().remove(t);
    }

    @Override
    public void remove(int position) {
        if (position >= getData().size()) {
            throw new IndexOutOfBoundsException();
        }
        getData().remove(position);
    }

    @Override
    public int size() {
        return getData().size();
    }

    @Override
    public List<T> getData() {
        if (data == null) {
            data = new ArrayList<>();
        }
        return data;
    }


    /**
     * 重新设置Filter中的数据
     */
    public void refreshFilterData() {
        getFilter().setUnfilteredData(getData());
    }

    /**
     * 复用ViewHodler
     */
    public class DataHodler {
        private View convertView;
        private SparseArray viewRes = new SparseArray();

        public DataHodler(View convertView) {
            this.convertView = convertView;
        }

        /**
         * 获取View
         */
        public <V extends View> V getView(int viewId) {
            V view = (V) viewRes.get(viewId);
            if (view == null) {
                view = (V) convertView.findViewById(viewId);
                viewRes.put(viewId, view);
            }
            return view;
        }
    }

    /**
     * 创建Filter筛选器
     */
    private CustomFilterRule<T> getCostomFliter() {
        CustomFilterRule<T> customFilter = new CustomFilterRule<T>(getData()) {
            @Override
            public List<T> onFilterData(String prefixString, List<T> unfilteredValues) {
                return onFilterRule(prefixString, unfilteredValues);
            }

            @Override
            protected void publishResults(CharSequence constraint, Filter.FilterResults results) {
                data = (List<T>) results.values;
                if (results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return customFilter;
    }

    /**
     * 绑定数据
     */
    public abstract void onBindDataToView(DataHodler hodler, T t);

    /**
     * 自定义数据过滤规则
     */
    public abstract List<T> onFilterRule(String prefixString, List<T> unfilteredValues);

}
