package com.gzliangce.dto;

import com.google.gson.annotations.SerializedName;
import com.gzliangce.entity.VersionInfo;

/**
 * Created by leo on 16/5/31.
 * 版本信息
 */
public class VersionDTO extends BaseDTO {
    @SerializedName("version")
    VersionInfo version;

    public VersionInfo getVersion() {
        return version;
    }

    public void setVersion(VersionInfo version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "VersionDTO{" +
                "version=" + version +
                '}';
    }
}
