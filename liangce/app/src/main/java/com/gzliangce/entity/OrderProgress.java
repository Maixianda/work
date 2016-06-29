package com.gzliangce.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by aaron on 2/15/16.
 */
public class OrderProgress implements Serializable {
    /**
     * conditionName : searchDate
     * conditionAlias : 查册日期
     */
    @SerializedName("mortgage")
    private List<ConditionEntity> mortgage;
    /**
     * conditionName : trialDate
     * conditionAlias : 送审日期
     */
    @SerializedName("other")
    private List<ConditionEntity> other;

    public void setMortgage(List<ConditionEntity> mortgage) {
        this.mortgage = mortgage;
    }

    public void setOther(List<ConditionEntity> other) {
        this.other = other;
    }

    public List<ConditionEntity> getMortgage() {
        return mortgage;
    }

    public List<ConditionEntity> getOther() {
        return other;
    }

    public static class ConditionEntity implements Serializable {
        @SerializedName("conditionName")
        private String conditionName;
        @SerializedName("conditionAlias")
        private String conditionAlias;
        @SerializedName("conditionValues")
        private String conditionValues;

        public void setConditionName(String conditionName) {
            this.conditionName = conditionName;
        }

        public void setConditionAlias(String conditionAlias) {
            this.conditionAlias = conditionAlias;
        }

        public String getConditionName() {
            return conditionName;
        }

        public String getConditionAlias() {
            return conditionAlias;
        }

        public String getConditionValues() {
            return conditionValues;
        }

        public void setConditionValues(String conditionValues) {
            this.conditionValues = conditionValues;
        }

        @Override
        public String toString() {
            return "ConditionEntity{" +
                    "conditionName='" + conditionName + '\'' +
                    ", conditionAlias='" + conditionAlias + '\'' +
                    ", conditionValues='" + conditionValues + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "OrderProgress{" +
                "mortgage=" + mortgage +
                ", other=" + other +
                '}';
    }
}
