package com.gzliangce.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by aaron on 2/15/16.
 */
public class Progress implements Serializable {

    @SerializedName("id")
    private String id;
    @SerializedName("number")
    private String number;
    @SerializedName("searchDate")
    private long searchDate;
    @SerializedName("bespeakNetDate")
    private long bespeakNetDate;
    @SerializedName("transferNetDate")
    private long transferNetDate;
    @SerializedName("signDate")
    private long signDate;
    @SerializedName("hereDate")
    private long hereDate;
    @SerializedName("protocolSendDate")
    private long protocolSendDate;
    @SerializedName("protocolReturnDate")
    private long protocolReturnDate;
    @SerializedName("buyserEntrustSendDate")
    private long buyserEntrustSendDate;
    @SerializedName("buyserEntrustReturnDate")
    private long buyserEntrustReturnDate;
    @SerializedName("sellerEntrustSendDate")
    private long sellerEntrustSendDate;
    @SerializedName("sellerEntrustReturnDate")
    private long sellerEntrustReturnDate;
    @SerializedName("contractSendDate")
    private long contractSendDate;
    @SerializedName("contractReturnDate")
    private long contractReturnDate;
    @SerializedName("assessmentDate")
    private long assessmentDate;
    @SerializedName("lookHouseDate")
    private long lookHouseDate;
    @SerializedName("preliminarySend")
    private long preliminarySend;
    @SerializedName("trialDate")
    private long trialDate;
    @SerializedName("businessTrialDate")
    private long businessTrialDate;
    @SerializedName("fundTrialDate")
    private long fundTrialDate;
    @SerializedName("agreeLoanDate")
    private long agreeLoanDate;
    @SerializedName("businessAgreeLoanDate")
    private long businessAgreeLoanDate;
    @SerializedName("fundAgreeLoanDate")
    private long fundAgreeLoanDate;
    @SerializedName("signContractDate")
    private long signContractDate;
    @SerializedName("issueContractDate")
    private long issueContractDate;
    @SerializedName("taxDate")
    private long taxDate;
    @SerializedName("deliveryDate")
    private long deliveryDate;
    @SerializedName("finalAssessmentDate")
    private long finalAssessmentDate;
    @SerializedName("newCertDate")
    private long newCertDate;
    @SerializedName("filingDate")
    private long filingDate;
    @SerializedName("keepMortgageDate")
    private long keepMortgageDate;
    @SerializedName("hisCert")
    private long hisCert;
    @SerializedName("sendLoanDate")
    private long sendLoanDate;
    @SerializedName("sendBusinessLoanDate")
    private long sendBusinessLoanDate;
    @SerializedName("sendFundLoanDate")
    private long sendFundLoanDate;
    @SerializedName("loanDate")
    private long loanDate;
    @SerializedName("businessLoanDate")
    private long businessLoanDate;
    @SerializedName("fundLoanDate")
    private long fundLoanDate;
    @SerializedName("returnFileDate")
    private long returnFileDate;
    @SerializedName("closeFileDate")
    private long closeFileDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public long getSearchDate() {
        return searchDate;
    }

    public void setSearchDate(long searchDate) {
        this.searchDate = searchDate;
    }

    public long getBespeakNetDate() {
        return bespeakNetDate;
    }

    public void setBespeakNetDate(long bespeakNetDate) {
        this.bespeakNetDate = bespeakNetDate;
    }

    public long getTransferNetDate() {
        return transferNetDate;
    }

    public void setTransferNetDate(long transferNetDate) {
        this.transferNetDate = transferNetDate;
    }

    public long getSignDate() {
        return signDate;
    }

    public void setSignDate(long signDate) {
        this.signDate = signDate;
    }

    public long getHereDate() {
        return hereDate;
    }

    public void setHereDate(long hereDate) {
        this.hereDate = hereDate;
    }

    public long getProtocolSendDate() {
        return protocolSendDate;
    }

    public void setProtocolSendDate(long protocolSendDate) {
        this.protocolSendDate = protocolSendDate;
    }

    public long getProtocolReturnDate() {
        return protocolReturnDate;
    }

    public void setProtocolReturnDate(long protocolReturnDate) {
        this.protocolReturnDate = protocolReturnDate;
    }

    public long getBuyserEntrustSendDate() {
        return buyserEntrustSendDate;
    }

    public void setBuyserEntrustSendDate(long buyserEntrustSendDate) {
        this.buyserEntrustSendDate = buyserEntrustSendDate;
    }

    public long getBuyserEntrustReturnDate() {
        return buyserEntrustReturnDate;
    }

    public void setBuyserEntrustReturnDate(long buyserEntrustReturnDate) {
        this.buyserEntrustReturnDate = buyserEntrustReturnDate;
    }

    public long getSellerEntrustSendDate() {
        return sellerEntrustSendDate;
    }

    public void setSellerEntrustSendDate(long sellerEntrustSendDate) {
        this.sellerEntrustSendDate = sellerEntrustSendDate;
    }

    public long getSellerEntrustReturnDate() {
        return sellerEntrustReturnDate;
    }

    public void setSellerEntrustReturnDate(long sellerEntrustReturnDate) {
        this.sellerEntrustReturnDate = sellerEntrustReturnDate;
    }

    public long getContractSendDate() {
        return contractSendDate;
    }

    public void setContractSendDate(long contractSendDate) {
        this.contractSendDate = contractSendDate;
    }

    public long getContractReturnDate() {
        return contractReturnDate;
    }

    public void setContractReturnDate(long contractReturnDate) {
        this.contractReturnDate = contractReturnDate;
    }

    public long getAssessmentDate() {
        return assessmentDate;
    }

    public void setAssessmentDate(long assessmentDate) {
        this.assessmentDate = assessmentDate;
    }

    public long getLookHouseDate() {
        return lookHouseDate;
    }

    public void setLookHouseDate(long lookHouseDate) {
        this.lookHouseDate = lookHouseDate;
    }

    public long getPreliminarySend() {
        return preliminarySend;
    }

    public void setPreliminarySend(long preliminarySend) {
        this.preliminarySend = preliminarySend;
    }

    public long getTrialDate() {
        return trialDate;
    }

    public void setTrialDate(long trialDate) {
        this.trialDate = trialDate;
    }

    public long getBusinessTrialDate() {
        return businessTrialDate;
    }

    public void setBusinessTrialDate(long businessTrialDate) {
        this.businessTrialDate = businessTrialDate;
    }

    public long getFundTrialDate() {
        return fundTrialDate;
    }

    public void setFundTrialDate(long fundTrialDate) {
        this.fundTrialDate = fundTrialDate;
    }

    public long getAgreeLoanDate() {
        return agreeLoanDate;
    }

    public void setAgreeLoanDate(long agreeLoanDate) {
        this.agreeLoanDate = agreeLoanDate;
    }

    public long getBusinessAgreeLoanDate() {
        return businessAgreeLoanDate;
    }

    public void setBusinessAgreeLoanDate(long businessAgreeLoanDate) {
        this.businessAgreeLoanDate = businessAgreeLoanDate;
    }

    public long getFundAgreeLoanDate() {
        return fundAgreeLoanDate;
    }

    public void setFundAgreeLoanDate(long fundAgreeLoanDate) {
        this.fundAgreeLoanDate = fundAgreeLoanDate;
    }

    public long getSignContractDate() {
        return signContractDate;
    }

    public void setSignContractDate(long signContractDate) {
        this.signContractDate = signContractDate;
    }

    public long getIssueContractDate() {
        return issueContractDate;
    }

    public void setIssueContractDate(long issueContractDate) {
        this.issueContractDate = issueContractDate;
    }

    public long getTaxDate() {
        return taxDate;
    }

    public void setTaxDate(long taxDate) {
        this.taxDate = taxDate;
    }

    public long getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(long deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public long getFinalAssessmentDate() {
        return finalAssessmentDate;
    }

    public void setFinalAssessmentDate(long finalAssessmentDate) {
        this.finalAssessmentDate = finalAssessmentDate;
    }

    public long getNewCertDate() {
        return newCertDate;
    }

    public void setNewCertDate(long newCertDate) {
        this.newCertDate = newCertDate;
    }

    public long getFilingDate() {
        return filingDate;
    }

    public void setFilingDate(long filingDate) {
        this.filingDate = filingDate;
    }

    public long getKeepMortgageDate() {
        return keepMortgageDate;
    }

    public void setKeepMortgageDate(long keepMortgageDate) {
        this.keepMortgageDate = keepMortgageDate;
    }

    public long getHisCert() {
        return hisCert;
    }

    public void setHisCert(long hisCert) {
        this.hisCert = hisCert;
    }

    public long getSendLoanDate() {
        return sendLoanDate;
    }

    public void setSendLoanDate(long sendLoanDate) {
        this.sendLoanDate = sendLoanDate;
    }

    public long getSendBusinessLoanDate() {
        return sendBusinessLoanDate;
    }

    public void setSendBusinessLoanDate(long sendBusinessLoanDate) {
        this.sendBusinessLoanDate = sendBusinessLoanDate;
    }

    public long getSendFundLoanDate() {
        return sendFundLoanDate;
    }

    public void setSendFundLoanDate(long sendFundLoanDate) {
        this.sendFundLoanDate = sendFundLoanDate;
    }

    public long getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(long loanDate) {
        this.loanDate = loanDate;
    }

    public long getBusinessLoanDate() {
        return businessLoanDate;
    }

    public void setBusinessLoanDate(long businessLoanDate) {
        this.businessLoanDate = businessLoanDate;
    }

    public long getFundLoanDate() {
        return fundLoanDate;
    }

    public void setFundLoanDate(long fundLoanDate) {
        this.fundLoanDate = fundLoanDate;
    }

    public long getReturnFileDate() {
        return returnFileDate;
    }

    public void setReturnFileDate(long returnFileDate) {
        this.returnFileDate = returnFileDate;
    }

    public long getCloseFileDate() {
        return closeFileDate;
    }

    public void setCloseFileDate(long closeFileDate) {
        this.closeFileDate = closeFileDate;
    }

    @Override
    public String toString() {
        return "Progress{" +
                "id='" + id + '\'' +
                ", number='" + number + '\'' +
                ", searchDate=" + searchDate +
                ", bespeakNetDate=" + bespeakNetDate +
                ", transferNetDate=" + transferNetDate +
                ", signDate=" + signDate +
                ", hereDate=" + hereDate +
                ", protocolSendDate=" + protocolSendDate +
                ", protocolReturnDate=" + protocolReturnDate +
                ", buyserEntrustSendDate=" + buyserEntrustSendDate +
                ", buyserEntrustReturnDate=" + buyserEntrustReturnDate +
                ", sellerEntrustSendDate=" + sellerEntrustSendDate +
                ", sellerEntrustReturnDate=" + sellerEntrustReturnDate +
                ", contractSendDate=" + contractSendDate +
                ", contractReturnDate=" + contractReturnDate +
                ", assessmentDate=" + assessmentDate +
                ", lookHouseDate=" + lookHouseDate +
                ", preliminarySend=" + preliminarySend +
                ", trialDate=" + trialDate +
                ", businessTrialDate=" + businessTrialDate +
                ", fundTrialDate=" + fundTrialDate +
                ", agreeLoanDate=" + agreeLoanDate +
                ", businessAgreeLoanDate=" + businessAgreeLoanDate +
                ", fundAgreeLoanDate=" + fundAgreeLoanDate +
                ", signContractDate=" + signContractDate +
                ", issueContractDate=" + issueContractDate +
                ", taxDate=" + taxDate +
                ", deliveryDate=" + deliveryDate +
                ", finalAssessmentDate=" + finalAssessmentDate +
                ", newCertDate=" + newCertDate +
                ", filingDate=" + filingDate +
                ", keepMortgageDate=" + keepMortgageDate +
                ", hisCert=" + hisCert +
                ", sendLoanDate=" + sendLoanDate +
                ", sendBusinessLoanDate=" + sendBusinessLoanDate +
                ", sendFundLoanDate=" + sendFundLoanDate +
                ", loanDate=" + loanDate +
                ", businessLoanDate=" + businessLoanDate +
                ", fundLoanDate=" + fundLoanDate +
                ", returnFileDate=" + returnFileDate +
                ", closeFileDate=" + closeFileDate +
                '}';
    }

    public HashMap<String, String> toHashMap() {
        HashMap<String, String> result = new HashMap<>();
        result.put("id", id);
        result.put("number", number);
        result.put("searchDate", String.valueOf(searchDate));
        result.put("bespeakNetDate", String.valueOf(bespeakNetDate));
        result.put("transferNetDate", String.valueOf(transferNetDate));
        result.put("signDate", String.valueOf(signDate));
        result.put("hereDate", String.valueOf(hereDate));
        result.put("protocolSendDate", String.valueOf(protocolSendDate));
        result.put("protocolReturnDate", String.valueOf(protocolReturnDate));
        result.put("buyserEntrustSendDate", String.valueOf(buyserEntrustSendDate));
        result.put("buyserEntrustReturnDate", String.valueOf(buyserEntrustReturnDate));
        result.put("sellerEntrustSendDate", String.valueOf(sellerEntrustSendDate));
        result.put("sellerEntrustReturnDate", String.valueOf(sellerEntrustReturnDate));
        result.put("contractSendDate", String.valueOf(contractSendDate));
        result.put("contractReturnDate", String.valueOf(contractReturnDate));
        result.put("assessmentDate", String.valueOf(assessmentDate));
        result.put("lookHouseDate", String.valueOf(lookHouseDate));
        result.put("preliminarySend", String.valueOf(preliminarySend));
        result.put("trialDate", String.valueOf(trialDate));
        result.put("businessTrialDate", String.valueOf(businessTrialDate));
        result.put("fundTrialDate", String.valueOf(fundTrialDate));
        result.put("agreeLoanDate", String.valueOf(agreeLoanDate));
        result.put("businessAgreeLoanDate", String.valueOf(businessAgreeLoanDate));
        result.put("fundAgreeLoanDate", String.valueOf(fundAgreeLoanDate));
        result.put("signContractDate", String.valueOf(signContractDate));
        result.put("issueContractDate", String.valueOf(issueContractDate));
        result.put("taxDate", String.valueOf(taxDate));
        result.put("deliveryDate", String.valueOf(deliveryDate));
        result.put("finalAssessmentDate", String.valueOf(finalAssessmentDate));
        result.put("newCertDate", String.valueOf(newCertDate));
        result.put("filingDate", String.valueOf(filingDate));
        result.put("keepMortgageDate", String.valueOf(keepMortgageDate));
        result.put("hisCert", String.valueOf(hisCert));
        result.put("sendLoanDate", String.valueOf(sendLoanDate));
        result.put("sendBusinessLoanDate", String.valueOf(sendBusinessLoanDate));
        result.put("sendFundLoanDate", String.valueOf(sendFundLoanDate));
        result.put("loanDate", String.valueOf(loanDate));
        result.put("businessLoanDate", String.valueOf(businessLoanDate));
        result.put("fundLoanDate", String.valueOf(fundLoanDate));
        result.put("returnFileDate", String.valueOf(returnFileDate));
        result.put("closeFileDate", String.valueOf(closeFileDate));

        return result;
    }
}