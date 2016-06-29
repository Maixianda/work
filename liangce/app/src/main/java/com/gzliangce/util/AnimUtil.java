package com.gzliangce.util;

import android.animation.Animator;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.view.View;

import com.gzliangce.R;

/**
 * Created by leo on 16/1/21.
 * 动画相关工具类
 */
public class AnimUtil {
    private static final int TIME_ANIMATION = 400;

    /**
     * 获取一个以Y轴为基准的位移动画
     */
    public static ObjectAnimator getYSubtractAnim(View view, Animator.AnimatorListener animationListener) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "y", view.getY(),
                view.getY() - view.getHeight());
        if (animationListener != null) {
            animator.addListener(animationListener);
        }
        animator.setDuration(TIME_ANIMATION);
        return animator;
    }


    /**
     * 获取一个以Y轴为基准的位移动画
     */
    public static ObjectAnimator getYSubtractAnim(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "y", view.getY(),
                view.getY() - view.getHeight());
        animator.setDuration(TIME_ANIMATION);
        return animator;
    }

    /**
     * 获取一个以Y轴为基准的隐藏动画，根据相对位置，隐藏或者显示，例如1080P屏幕，在屏幕最下边，1920+控件高度度=隐藏动画
     */
    public static ObjectAnimator getYAppendAnim(View view, Animator.AnimatorListener animationListener) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "y", view.getY(),
                view.getY() + view.getHeight());
        if (animationListener != null) {
            animator.addListener(animationListener);
        }
        animator.setDuration(TIME_ANIMATION);
        return animator;
    }


    /**
     * 获取一个以X轴为基准的位移动画,根据相对位置，隐藏或者显示，例如1080P屏幕，在屏幕最右边，1280+控件宽度=隐藏动画
     */
    public static ObjectAnimator getXAppendAnim(View view, Animator.AnimatorListener animationListener) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "x", view.getX(),
                view.getX() + view.getWidth());
        animator.setDuration(TIME_ANIMATION);
        if (animationListener != null) {
            animator.addListener(animationListener);
        }
        return animator;
    }


    /**
     * 获取一个以X轴为基准的显示动画
     */
    public static ObjectAnimator getXSubtractAnim(View view, Animator.AnimatorListener animationListener) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "x", view.getX(),
                view.getX() - view.getWidth());
        animator.setDuration(TIME_ANIMATION);
        if (animationListener != null) {
            animator.addListener(animationListener);
        }
        return animator;
    }


    /**
     * 播放一个左右摇晃动画
     */
    public static void startShakeAnimation(View view) {
        int delta = view.getResources().getDimensionPixelOffset(R.dimen.dp_20);
        PropertyValuesHolder pvhTranslateX = PropertyValuesHolder.ofKeyframe(View.TRANSLATION_X,
                Keyframe.ofFloat(0f, 0),
                Keyframe.ofFloat(.10f, -delta),
                Keyframe.ofFloat(.26f, delta),
                Keyframe.ofFloat(.42f, -delta),
                Keyframe.ofFloat(.58f, delta),
                Keyframe.ofFloat(.74f, -delta),
                Keyframe.ofFloat(.90f, delta),
                Keyframe.ofFloat(1f, 0f));
        ObjectAnimator.ofPropertyValuesHolder(view, pvhTranslateX).
                setDuration(500).start();
    }


    /**
     * 获取一个放大动画
     */
    public static ObjectAnimator getAmplificationAnim(View view) {
        ObjectAnimator amplification = ObjectAnimator.ofFloat(view, "zhy", 1.3F, 1.0F).setDuration(TIME_ANIMATION);
        amplification.addUpdateListener(getUpdateListener(view));
        return amplification;
    }


    /**
     * 获取一个缩小动画
     */
    public static ObjectAnimator getNarrowAnim(View view) {
        ObjectAnimator narrowAnim = ObjectAnimator.ofFloat(view, "zhy", 1.0F, 1.3F).setDuration(TIME_ANIMATION);
        narrowAnim.addUpdateListener(getUpdateListener(view));
        return narrowAnim;
    }


    /**
     * 动画监听接口
     */
    public static ValueAnimator.AnimatorUpdateListener getUpdateListener(final View view) {
        ValueAnimator.AnimatorUpdateListener updateListener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float cVal = (Float) valueAnimator.getAnimatedValue();
                view.setAlpha(cVal);
                view.setScaleX(cVal);
                view.setScaleY(cVal);
            }
        };
        return updateListener;
    }


    /**
     * 获取一个向下翻转的动画
     */
    public static ObjectAnimator getRotationDown(View view) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "rotation", 180f, 0);
        objectAnimator.setDuration(TIME_ANIMATION);
        return objectAnimator;
    }

    /**
     * 获取一个向上翻转的动画
     */
    public static ObjectAnimator getRotationUp(View view) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "rotation",360f, 180f);
        objectAnimator.setDuration(TIME_ANIMATION);
        return objectAnimator;
    }

}
