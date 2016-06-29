package com.gzliangce.http;

import android.os.Build;

import com.gzliangce.AppContext;
import com.gzliangce.AppEnv;
import com.gzliangce.bean.Constants;
import com.gzliangce.dto.BaseDTO;
import com.gzliangce.ui.dialog.ErrorPasswordDialog;
import com.gzliangce.util.DialogUtil;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.io.IOException;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.ganguo.library.util.Systems;
import io.ganguo.library.util.crypto.Rsas;
import io.ganguo.library.util.gson.Gsons;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * api service 产生器
 * <p/>
 */
public class API {
    private final static Logger logger = LoggerFactory.getLogger(Constants.TAG_GG_API);

    /**
     * api base
     */
    public final static String API_BASE_URL = AppEnv.BASE_URL;

    /**
     * okhttp
     */
    private static OkHttpClient httpClient;

    /**
     * retrofit builder
     */
    private static Retrofit.Builder builder;

    /**
     * keep service singleton
     */
    private static Map<Class, Object> mServices;

    private static Deque<ErrorInterceptor> interceptors;

    // init okhttp
    static {
        httpClient = new OkHttpClient();
        builder = new Retrofit.Builder().baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(Gsons.getGson()));
        mServices = new ConcurrentHashMap<>();
        interceptors = new LinkedList<>();

        final Interceptor defaultInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                // app/1.0_dev (android; 4.4.4; 19)
                String userAgent = "app/" + Systems.getVersionName(AppContext.me()) + " (android; " + Build.VERSION.RELEASE + "; " + Build.VERSION.SDK_INT + ")";
                // 1.0.0
                String version = Systems.getVersionName(AppContext.me()) + "";
                // TODO: 7/12/15 在这里添加user token
                String token = "";
                String signature = "";
                String timestamp = System.currentTimeMillis() + "";
                String deviceId = Systems.getDeviceId(AppContext.me());
                if (AppContext.me().isLogined()) {
                    token = AppContext.me().getUser().getToken();
                    signature = Rsas.getMD5(token + timestamp + deviceId);
                }

                logger.v("signature" + signature + " : " + timestamp + " : " + deviceId);
                logger.e("haha token:" + signature + " : " + timestamp + " : " + deviceId);
                Request request = chain
                        .request()
                        .newBuilder()
                        .addHeader("UserInfo-Agent", userAgent)
                        .addHeader("version", version)
                        .addHeader("deviceId", deviceId)
                        .addHeader("timestamp", timestamp)
                        .addHeader("signature", signature)
                        .addHeader("from", "android")
                        .build();
                logger.e(request.urlString());
                return chain.proceed(request);
            }
        };
        HttpLoggingInterceptor httpLogging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                logger.d(message);
            }
        });
        httpLogging.setLevel(AppEnv.isDebug ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

        //添加拦截器
        httpClient.interceptors().add(defaultInterceptor);
        httpClient.interceptors().add(httpLogging);
        addResponseInterceptors();
    }

    /**
     * 创建一个api service(单例)
     *
     * @param clazz
     * @param <S>
     * @return
     */
    public static <S> S of(Class<S> clazz) {
        if (mServices.containsKey(clazz)) {
            return (S) mServices.get(clazz);
        }

        Retrofit retrofit = builder.client(httpClient).build();
        S service = retrofit.create(clazz);
        mServices.put(clazz, service);
        return service;
    }

    public final static <T> HttpError intercept(T body) {
        for (ErrorInterceptor rin : interceptors) {
            HttpError error = rin.intercept(body);
            if (error != null) {
                return error;
            }
        }
        return null;
    }

    /**
     * 错误消息拦截 code, status, message等
     * 具体配置最好配合 {@link BaseDTO}
     */
    private static void addResponseInterceptors() {
        interceptors.add(new ErrorInterceptor() {
            @Override
            public <T> HttpError intercept(T body) {
                // TODO: 14/12/15 在这里拦截json中的错误信息,范例中使用了BaseDTO, 也可以考虑使用JSONObject
                if (body instanceof BaseDTO) {
                    BaseDTO baseDTO = ((BaseDTO) body);
                    //status
                    logger.e("baseDTO:" + baseDTO.getStatus());
                    if (baseDTO.getStatus() != 200) {
                        if (baseDTO.getStatus() == 401) {
                            DialogUtil.showLogoutDialog();
                        }
                        return new HttpError(baseDTO.getStatus() + "", baseDTO.getMessage());
                    }
                    //code
                    //message
                }
                return null;
            }
        });
    }


    private interface ErrorInterceptor {
        <T> HttpError intercept(T body);
    }
}
