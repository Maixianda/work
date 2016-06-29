package com.gzliangce.dto;

import com.gzliangce.entity.AccountInfo;

/**
 * public filed
 * <p>
 * Created by Tony on 10/15/15.
 */
public class UserDTO extends BaseDTO {

    /**
     * accountId : 176
     * accountName : 小明
     * phone : 18613117395
     * status : normal
     * type : mediator
     * token : 591f1619c5be1874bc2ca7e54916a897
     * info : {"status":"noauth","otherCard":null,"identityCard":null}
     */
    private AccountInfo account;

    public void setAccount(AccountInfo account) {
        this.account = account;
    }

    public AccountInfo getAccount() {
        return account;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "account=" + account +
                '}';
    }
}
