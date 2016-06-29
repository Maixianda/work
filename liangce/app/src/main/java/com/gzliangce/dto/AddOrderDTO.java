package com.gzliangce.dto;

/**
 * Created by leo on 16/3/1.
 * 普通用户添加订单
 */
public class AddOrderDTO extends BaseDTO {
    /**
     * orderId : 81
     * number : 20160223593950218
     * areaName : 白云区
     * areaId : 440111
     * address : 测试
     * linkman : 测试人员
     * linkPhone : 13800138000
     * accountPhone : 13535556058
     * status : signed
     * productId : 5
     * productName : 小型装修
     * createTime : 1456216195000
     * realName : 按揭测试
     * icon : /icon/2016/02/3c37556599498b347b0ecd9b541f0e83.jpg
     * sender : 171
     * receiver : 2
     * evaluate : false
     */

    private OrderDetailInfo orderDetail;

    public void setOrderDetail(OrderDetailInfo orderDetail) {
        this.orderDetail = orderDetail;
    }

    public OrderDetailInfo getOrderDetail() {
        return orderDetail;
    }

    public static class OrderDetailInfo {
        private int orderId;
        private String number;
        private String areaName;
        private int areaId;
        private String address;
        private String linkman;
        private String linkPhone;
        private String accountPhone;
        private String status;
        private int productId;
        private String productName;
        private long createTime;
        private String realName;
        private String icon;
        private int sender;
        private int receiver;
        private boolean evaluate;

        public void setOrderId(int orderId) {
            this.orderId = orderId;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }

        public void setAreaId(int areaId) {
            this.areaId = areaId;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public void setLinkman(String linkman) {
            this.linkman = linkman;
        }

        public void setLinkPhone(String linkPhone) {
            this.linkPhone = linkPhone;
        }

        public void setAccountPhone(String accountPhone) {
            this.accountPhone = accountPhone;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public void setProductId(int productId) {
            this.productId = productId;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public void setSender(int sender) {
            this.sender = sender;
        }

        public void setReceiver(int receiver) {
            this.receiver = receiver;
        }

        public void setEvaluate(boolean evaluate) {
            this.evaluate = evaluate;
        }

        public int getOrderId() {
            return orderId;
        }

        public String getNumber() {
            return number;
        }

        public String getAreaName() {
            return areaName;
        }

        public int getAreaId() {
            return areaId;
        }

        public String getAddress() {
            return address;
        }

        public String getLinkman() {
            return linkman;
        }

        public String getLinkPhone() {
            return linkPhone;
        }

        public String getAccountPhone() {
            return accountPhone;
        }

        public String getStatus() {
            return status;
        }

        public int getProductId() {
            return productId;
        }

        public String getProductName() {
            return productName;
        }

        public long getCreateTime() {
            return createTime;
        }

        public String getRealName() {
            return realName;
        }

        public String getIcon() {
            return icon;
        }

        public int getSender() {
            return sender;
        }

        public int getReceiver() {
            return receiver;
        }

        public boolean isEvaluate() {
            return evaluate;
        }

        @Override
        public String toString() {
            return "OrderDetailInfo{" +
                    "orderId=" + orderId +
                    ", number='" + number + '\'' +
                    ", areaName='" + areaName + '\'' +
                    ", areaId=" + areaId +
                    ", address='" + address + '\'' +
                    ", linkman='" + linkman + '\'' +
                    ", linkPhone='" + linkPhone + '\'' +
                    ", accountPhone='" + accountPhone + '\'' +
                    ", status='" + status + '\'' +
                    ", productId=" + productId +
                    ", productName='" + productName + '\'' +
                    ", createTime=" + createTime +
                    ", realName='" + realName + '\'' +
                    ", icon='" + icon + '\'' +
                    ", sender=" + sender +
                    ", receiver=" + receiver +
                    ", evaluate=" + evaluate +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "AddOrderDTO{" +
                "orderDetail=" + orderDetail +
                '}';
    }
}
