package com.gzliangce.util;


import android.os.CountDownTimer;

/**
 * Created by leo on 15/12/14.
 * 获取验证码倒计时
 */
public class TimeCount extends CountDownTimer {
    private TimeCountListener timeCountListener;

    public TimeCount(long millisInFuture, long countDownInterval, TimeCountListener timeCountListener) {
        super(millisInFuture, countDownInterval);
        this.timeCountListener = timeCountListener;
    }

    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    public TimeCount(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        if (timeCountListener != null) {
            timeCountListener.onTick(millisUntilFinished);
        }
    }

    @Override
    public void onFinish() {
        if (timeCountListener != null) {
            timeCountListener.onFinish();
        }
    }

    public interface TimeCountListener {
        void onTick(long millisUntilFinished);

        void onFinish();
    }
}

