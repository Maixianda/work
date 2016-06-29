package com.gzliangce.http;

import com.gzliangce.bean.Constants;

import io.ganguo.library.exception.NetworkException;
import io.ganguo.library.exception.ServerException;
import io.ganguo.library.util.Strings;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Wilson on 12/12/15.
 */
public abstract class APICallback<T> implements retrofit.Callback<T> {
    private final static Logger logger = LoggerFactory.getLogger(Constants.TAG_GG_API);

    @Override
    public final void onResponse(Response<T> response, Retrofit retrofit) {
        logger.d("response code:" + response.code());
        //judge response code
        if (response.code() < 300) {

            //无返回内容
            if (response.body() == null) {
                logger.w("empty response, message:" + response.message());
                if (Strings.isNotEmpty(response.message())) {
                    onFailed(response.message());
                } else {
                    onFailed(new NetworkException(null).getMessage());
                }
                onFinish();
                return;
            }

            //拦截json中的错误信息
            HttpError error = API.intercept(response.body());
            if (error != null) {
                logger.w("intercept http error:" + error);
                onFailed(error.getMessage());
                onFinish();
                return;
            }

            //请求正常返回
            onSuccess(response.body());
            onFinish();
        } else {
            //服务器错误 30x 40x 50x
            onFailed(new ServerException(response.code()).getMessage());
            onFinish();
        }

    }

    @Override
    public final void onFailure(Throwable t) {
        //一般是一些网络异常
        NetworkException exception = new NetworkException(t);
        logger.w("network occurs failure:", exception);
        onFailed(exception.getMessage());
        onFinish();
    }


    /**
     * 请求成功
     *
     * @param t
     */
    public abstract void onSuccess(T t);

    /**
     * 请求失败
     *
     * @param message 简单点只返回必要的message, ui层不再关心code.
     */
    public abstract void onFailed(String message);

    /**
     * 请求完成, 成功与否都执行
     */
    public abstract void onFinish();
}
