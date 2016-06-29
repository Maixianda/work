package com.gzliangce.dto;

import com.google.gson.annotations.SerializedName;
import com.gzliangce.entity.TutorInfo;

/**
 * Created by leo on 16/3/28.
 * 导师详情
 */
public class TutorDTO extends BaseDTO {
    @SerializedName("tutor")
    private TutorInfo tutor;

    public TutorInfo getTutor() {
        return tutor;
    }

    public void setTutor(TutorInfo tutor) {
        this.tutor = tutor;
    }

    @Override
    public String toString() {
        return "TutorDTO{" +
                "tutor=" + tutor +
                '}';
    }
}
