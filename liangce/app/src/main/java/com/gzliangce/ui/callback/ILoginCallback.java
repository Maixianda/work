package com.gzliangce.ui.callback;

import com.gzliangce.entity.AccountInfo;

/**
 * Created by Tony on 10/7/15.
 */
public interface ILoginCallback {

    void loginSuccess(AccountInfo userInfo);
    void accountError();

}
