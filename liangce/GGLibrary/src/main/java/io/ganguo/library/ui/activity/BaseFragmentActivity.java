package io.ganguo.library.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import java.util.HashMap;
import java.util.Map;

import io.ganguo.library.AppManager;
import io.ganguo.library.R;
import io.ganguo.library.common.UIHelper;
import io.ganguo.library.core.event.EventHub;
import io.ganguo.library.util.Systems;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;

/**
 * FragmentActivity - 基类
 * <p>
 * Created by hulk on 10/20/14.
 */
public abstract class BaseFragmentActivity extends FragmentActivity implements InitResources, AMapLocationListener {
    protected Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());
    //声明AMapLocationClient类对象
    public AMapLocationClientOption mLocationOption = null;
    private AMapLocationClient mlocationClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // register
        AppManager.addActivity(this);
        EventHub.register(this);
        initLocation();
        // init resources
        beforeInitView();
        initView();
        initListener();
        initData();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        // bind back action
        UIHelper.bindActionBack(this, findViewById(R.id.action_back));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // unregister event
        EventHub.unregister(this);
        AppManager.removeActivity(this);
    }

    /**
     * fragment 显示处理
     */
    private Map<Class<? extends Fragment>, Fragment> fragmentMap = new HashMap<>();
    private Fragment currentFragment;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        logger.i("onSaveInstanceState " + outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        logger.i("onRestoreInstanceState " + savedInstanceState);
    }

    @Override
    protected void onRestart() {
        mlocationClient.startLocation();
        super.onRestart();
    }

    @Override
    protected void onPause() {
        mlocationClient.stopLocation();
        super.onPause();
    }

    public void doStartLocation() {
        mlocationClient.startLocation();
    }

    /**
     * 显示Fragment到容器中
     *
     * @param res
     * @param fragment
     */
    protected void showFragment(int res, Fragment fragment) {
        showFragment(res, fragment, null);
    }

    /**
     * 显示Fragment到容器中
     *
     * @param res
     * @param fragment
     * @param tag
     */
    protected void showFragment(int res, Fragment fragment, String tag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(res, fragment, tag);
        transaction.commit();
        getSupportFragmentManager().executePendingTransactions();
        currentFragment = fragment;
    }

    /**
     * 显示Fragment到容器中
     *
     * @param clazz
     */
    protected void showFragment(int res, final Class<? extends Fragment> clazz) {
        if (!fragmentMap.containsKey(clazz)) {
            try {
                fragmentMap.put(clazz, clazz.newInstance());
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        Fragment target = fragmentMap.get(clazz);
        if (currentFragment != null && target != null && currentFragment.getClass().equals(target.getClass())) {
            return;
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (currentFragment != null) {
            transaction.hide(currentFragment);
            logger.i("hide " + currentFragment);
        }
        if (target.isAdded()) {
            if (target.isDetached()) {
                transaction.attach(currentFragment);
            } else {
                transaction.show(target);
            }
        } else {
            transaction.add(res, target);
        }
        transaction.commitAllowingStateLoss();
        currentFragment = target;
    }

    /**
     * 让fragment重新走生命周期
     */
    protected void updateFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        for (Class<? extends Fragment> clazz : fragmentMap.keySet()) {
            Fragment fragment = fragmentMap.get(clazz);
            if (fragment != currentFragment) {
                transaction.remove(fragment);
            }
        }
        transaction.detach(currentFragment);
        transaction.attach(currentFragment);
        transaction.commitAllowingStateLoss();
    }

    /**
     * 初始化定位
     */
    private void initLocation() {
        // 初始化定位，
        mlocationClient = new AMapLocationClient(getApplicationContext());
        // 初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setOnceLocation(true);
        //设置为高精度定位模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        // 设置定位回调监听
        mlocationClient.setLocationListener(this);
        // 设置定位参数
        mlocationClient.setLocationOption(mLocationOption);

        mlocationClient.startLocation();
    }

}
