package com.gzliangce.ui.model;

import android.databinding.BaseObservable;
import android.view.View;

import com.gzliangce.R;

import io.ganguo.library.core.event.extend.OnSingleClickListener;


/**
 * Created by aaron on 11/9/15.
 */
public class FooterModel extends BaseObservable {
    private FootView mView;
    private int position = 1;
    private int background = R.color.white;

    public FooterModel(FootView mView) {
        this.mView = mView;
    }

    public View.OnClickListener onOrderClicked() {
        return new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                mView.onOrderClicked();
            }
        };
    }

    public View.OnClickListener onMessageClicked() {
        return new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                mView.onMessageClicked();
            }
        };
    }

    public View.OnClickListener onCollegeClicked() {
        return new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                mView.onCollegeClicked();
            }
        };
    }

    public View.OnClickListener onNewsClicked() {
        return new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                mView.onNewsClicked();
            }
        };
    }

    public View.OnClickListener onMineClicked() {
        return new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                mView.onMineClicked();
            }
        };
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }

    public FootView getmView() {
        return mView;
    }

    public void setmView(FootView mView) {
        this.mView = mView;
    }

    public interface FootView {

        void onOrderClicked();

        void onMessageClicked();

        void onCollegeClicked();

        void onNewsClicked();

        void onMineClicked();

    }

}
