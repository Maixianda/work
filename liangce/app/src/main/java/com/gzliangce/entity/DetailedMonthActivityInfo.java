package com.gzliangce.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by leo on 16/2/19.
 * 月供详情 - 数据对象
 */
public class DetailedMonthActivityInfo implements Serializable {
    private double loanPerMonth;//月均还款
    private double repaymentSum;//还款总额，单位：万元
    private double interestPayment;//支付利息款,单位：元
    private List<LoanInfo> loanInfoList;
    private int index;
    private int mortgageMonth;//分期月数
    private double loanSum;//贷款总额

    private double oneLoanPerMonth;//月均还款，list0位置
    private double towLoanPerMonth;//月均还款，list1位置

    public double getTowLoanPerMonth() {
        return towLoanPerMonth;
    }

    public void setTowLoanPerMonth(double towLoanPerMonth) {
        this.towLoanPerMonth = towLoanPerMonth;
    }

    public double getOneLoanPerMonth() {
        return oneLoanPerMonth;
    }

    public void setOneLoanPerMonth(double oneLoanPerMonth) {
        this.oneLoanPerMonth = oneLoanPerMonth;
    }

    public double getLoanSum() {
        return loanSum;
    }

    public void setLoanSum(double loanSum) {
        this.loanSum = loanSum;
    }

    public int getMortgageMonth() {
        return mortgageMonth;
    }

    public void setMortgageMonth(int mortgageMonth) {
        this.mortgageMonth = mortgageMonth;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }


    public double getLoanPerMonth() {
        return loanPerMonth;
    }

    public void setLoanPerMonth(double loanPerMonth) {
        this.loanPerMonth = loanPerMonth;
    }

    public double getRepaymentSum() {
        return repaymentSum;
    }

    public void setRepaymentSum(double repaymentSum) {
        this.repaymentSum = repaymentSum;
    }

    public double getInterestPayment() {
        return interestPayment;
    }

    public void setInterestPayment(double interestPayment) {
        this.interestPayment = interestPayment;
    }

    public List<LoanInfo> getLoanInfoList() {
        return loanInfoList;
    }

    public void setLoanInfoList(List<LoanInfo> loanInfoList) {
        this.loanInfoList = loanInfoList;
    }

    @Override
    public String toString() {
        return "DetailedMonthActivityInfo{" +
                "loanPerMonth=" + loanPerMonth +
                ", repaymentSum=" + repaymentSum +
                ", interestPayment=" + interestPayment +
                ", loanInfoList=" + loanInfoList +
                ", index=" + index +
                ", mortgageMonth=" + mortgageMonth +
                ", loanSum=" + loanSum +
                '}';
    }
}
