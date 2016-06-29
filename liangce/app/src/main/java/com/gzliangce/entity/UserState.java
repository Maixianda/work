package com.gzliangce.entity;

import android.databinding.ObservableField;

/**
 * Created by leo on 16/5/24.
 * 用户状态
 */
public class UserState {
    private ObservableField<String> attestState;

    public UserState(String attestState) {
        this.attestState = new ObservableField<>(attestState);
    }

    public ObservableField<String> getAttestState() {
        return attestState;
    }

    public void setAttestState(ObservableField<String> attestState) {
        this.attestState = attestState;
    }

    @Override
    public String toString() {
        return "UserState{" +
                "attestState=" + attestState +
                '}';
    }
}
