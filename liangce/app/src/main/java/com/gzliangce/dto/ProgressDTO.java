package com.gzliangce.dto;

import com.google.gson.annotations.SerializedName;
import com.gzliangce.entity.Progress;
import com.gzliangce.entity.Supplement;

import java.io.Serializable;
import java.util.HashMap;

import io.ganguo.library.util.Strings;

/**
 * Created by aaron on 2/3/16.
 */
public class ProgressDTO extends BaseDTO implements Serializable {
    @SerializedName("progress")
    private Progress progress;

    public Progress getProgress() {
        return progress;
    }

    public void setProgress(Progress progress) {
        this.progress = progress;
    }

    @Override
    public String toString() {
        return "ProgressDTO{" +
                "progress=" + progress +
                '}';
    }
}
