package org.pointstone.cugappplat.db;

import java.io.Serializable;
import java.util.List;

/**
 * 项目名称:    Cugappplat
 * 创建人:        陈锦军
 * 创建时间:    2017/4/7      11:34
 * QQ:             1981367757
 */

public class ResultEntity<T> implements Serializable {
    private boolean status;
    private String message;
    List<T> data;

    public boolean isStatus() {

        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
