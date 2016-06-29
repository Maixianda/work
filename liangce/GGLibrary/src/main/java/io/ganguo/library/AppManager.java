package io.ganguo.library;

import android.app.Activity;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import io.ganguo.library.core.event.EventHub;
import io.ganguo.library.core.event.OnExitEvent;
import io.ganguo.library.util.Strings;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;

/**
 * 应用程序Activity管理类：用于Activity管理和应用程序退出
 *
 * @author ZhiHui_Chen
 * @Email: 6208317#qq.com
 * @Date 2013-5-14
 */
public class AppManager {
    private static final Logger LOG = LoggerFactory.getLogger(AppManager.class);

    /**
     * Activity记录栈
     */
    private static final Stack<Activity> activityStack = new Stack<Activity>();

    /**
     * 添加Activity到堆栈
     */
    public static void addActivity(Activity activity) {
        activityStack.add(activity);
    }

    /**
     * 获取栈顶的activity，最后打开的
     */
    public static Activity getLastsOpenActivity() {
        if (activityStack.isEmpty()) {
            return null;
        }
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public static void finishActivity() {
        if (activityStack.isEmpty()) {
            return;
        }
        Activity activity = activityStack.firstElement();
        finishActivity(activity);
    }


    /**
     * 获取当前栈底的activity，第一个打开的
     */
    public static Activity getFristOpenActivity() {
        if (activityStack.isEmpty()) {
            return null;
        }
        return activityStack.firstElement();
    }

    /**
     * 结束指定类名的Activity
     */
    public static Activity getActivity(Class<?> cls) {
        if (activityStack.isEmpty()) {
            return null;
        }
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                return activity;
            }
        }
        return null;
    }

    /**
     * 移除指定的Activity
     */
    public static void removeActivity(Activity activity) {
        if (activityStack.isEmpty()) {
            return;
        }
        if (activity != null) {
            activityStack.remove(activity);
        }
    }

    /**
     * 结束指定的Activity
     */
    public static void finishActivity(Activity activity) {
        if (activityStack.isEmpty()) {
            return;
        }
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public static void finishActivity(Class<?> cls) {
        if (activityStack.isEmpty()) {
            return;
        }
        if (activityStack.size() == 0) {
            return;
        }
        LOG.e("Activity:Size" + activityStack.size());
        Iterator<Activity> iterator = activityStack.iterator();
        while (iterator.hasNext()) {
            Activity activity = iterator.next();
            if (activity.getClass().equals(cls)) {
                activity.finish();
                iterator.remove();
                break;
            }
        }
    }


    /**
     * 结束所有Activity
     */
    public static void finishAllActivity() {
        if (activityStack.isEmpty()) {
            return;
        }
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }


    /**
     * 结束除栈顶以外的Activity
     */
    public static void finishOtherActivity(Activity inActivity, Activity fristActivity) {
        if (activityStack.isEmpty()) {
            return;
        }
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (activityStack.get(i) != null && activityStack.get(i) != inActivity && activityStack.get(i) != fristActivity) {
                activityStack.get(i).finish();
            }
        }
    }

    /**
     * 结束除传入activity以外的activity
     */
    public static void finishOtherActivity(Class<?> cls) {
        if (activityStack.isEmpty()) {
            return;
        }
        if (activityStack.size() == 0) {
            return;
        }
        Iterator<Activity> iterator = activityStack.iterator();
        while (iterator.hasNext()) {
            Activity activity = iterator.next();
            if (!activity.getClass().equals(cls)) {
                activity.finish();
                iterator.remove();
            }
        }
    }


    /**
     * 判断是否已经打开过这个Activity
     */
    public static boolean isOpenActivity(Class<?> cls) {
        if (activityStack.isEmpty()) {
            return false;
        }
        List<Activity> removed = new LinkedList<>();
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                removed.add(activity);
                return true;
            }
        }
        return false;
    }

    /**
     * 退出应用程序
     */
    public static void exitApp() {
        try {
            // exit event
            EventHub.post(new OnExitEvent());
            // finish all
            finishAllActivity();
        } catch (Exception e) {
            LOG.w("Exit application failure", e);
        } finally {
            System.exit(0);
        }
    }

}
