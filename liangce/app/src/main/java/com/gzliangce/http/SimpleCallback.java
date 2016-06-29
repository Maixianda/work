package com.gzliangce.http;

import com.gzliangce.AppContext;
import com.gzliangce.bean.Constants;
import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.util.Strings;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;

/**
 * Created by Wilson on 12/12/15.
 */
public abstract class SimpleCallback<T> extends APICallback<T> {
    private final static Logger logger = LoggerFactory.getLogger(Constants.TAG_GG_API);

    /**
     * 请求失败的默认处理方案
     *
     * @param message
     */
    public void onFailed(String message) {
        logger.i("request onFailed:" + message);
        if (Strings.isNotEmpty(message)) {
            ToastHelper.showMessage(AppContext.me(), message);
        }
    }

    /**
     * 请求完成的默认处理方案
     */
    public void onFinish() {
        logger.d("request onFinish.");
    }


}
