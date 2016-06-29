package com.gzliangce.dto;

import com.gzliangce.entity.MessageCenterInfo;

import java.util.List;

/**
 * Created by leo on 16/2/24.
 */
public class MessageCenterDTO extends BaseDTO {

    /**
     * page : 1
     * list : [{"id":1,"title":"test","content":"testttt","createTime":1456109039000}]
     */

    private int page;
    /**
     * id : 1
     * title : test
     * content : testttt
     * createTime : 1456109039000
     */

    private List<MessageCenterInfo> list;

    public void setPage(int page) {
        this.page = page;
    }

    public void setList(List<MessageCenterInfo> list) {
        this.list = list;
    }

    public int getPage() {
        return page;
    }

    public List<MessageCenterInfo> getList() {
        return list;
    }

    @Override
    public String toString() {
        return "MessageCenterDTO{" +
                "page=" + page +
                ", list=" + list +
                '}';
    }
}
