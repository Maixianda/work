package com.gzliangce.ui.callback;

import java.util.List;

/**
 * Created by leo on 16/5/19.
 */
public interface ListCallback<T> {
    public T get(int position);

    public void add(T t);

    public void add(int position, T t);

    public void addAll(List<T> allData);

    public void remove(T t);

    public void remove(int position);

    public int size();

    public List<T> getData();
}
