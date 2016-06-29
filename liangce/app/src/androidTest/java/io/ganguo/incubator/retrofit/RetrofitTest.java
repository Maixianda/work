package com.gzliangce.retrofit;

import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;

import java.util.List;

import com.gzliangce.ApplicationTest;
import com.gzliangce.entity.Contributor;
import com.gzliangce.http.API;
import com.gzliangce.http.APICallback;
import com.gzliangce.http.SimpleCallback;
import com.gzliangce.service.GitHubService;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Tony on 10/22/15.
 */
public class RetrofitTest extends ApplicationTest {

    private static final String TAG = RetrofitTest.class.getSimpleName();

    public void testGenerator() {
        GitHubService github = API.of(GitHubService.class);
        Call<List<Contributor>> call = github.contributors("square", "retrofit");
        call.enqueue(new Callback<List<Contributor>>() {
            @Override
            public void onResponse(Response<List<Contributor>> response, Retrofit retrofit) {
                testLooper();

                Log.d(TAG, response.code() + "");
//                try {
//                    Log.d(TAG, response.errorBody().string());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

            }

            @Override
            public void onFailure(Throwable t) {
                Log.d(TAG, "Throwable " + t);

                testLooper();
            }
        });

        SystemClock.sleep(2000);
    }

    private void testLooper() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            Log.d(TAG, "main");
        } else {
            Log.d(TAG, "thread");
        }
    }

    public void testSimpleCallback() throws InterruptedException {
        API.of(GitHubService.class).contributors("square", "retrofit").enqueue(new SimpleCallback<List<Contributor>>() {
            @Override
            public void onSuccess(List<Contributor> contributors) {
                Log.i(TAG, "onSuccess, data: " + contributors);
            }
        });

        //sleep 一分钟 等待网络请求回调当前线程（仅用于测试）
        Thread.sleep(1 * 60 * 1000l);
    }

    public void testAPICallback() throws InterruptedException {
        API.of(GitHubService.class).contributors("square", "retrofit").enqueue(new APICallback<List<Contributor>>() {
            @Override
            public void onSuccess(List<Contributor> contributors) {
                Log.i(TAG, "onSuccess, data: " + contributors);
            }

            @Override
            public void onFailed(String message) {
                Log.i(TAG, "onFailed, message: " + message);
            }

            @Override
            public void onFinish() {
                Log.i(TAG, "onFinish ");
            }
        });

        //sleep 一分钟 等待网络请求回调当前线程（仅用于测试）
        Thread.sleep(1 * 60 * 1000l);
    }


}
