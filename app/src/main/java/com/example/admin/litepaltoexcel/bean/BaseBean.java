package com.example.admin.litepaltoexcel.bean;


import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

/**
 * Created by admin on 2018/6/23.
 */

public class BaseBean extends LitePalSupport implements Serializable {

    protected int id;//（数据的自增长id）

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
