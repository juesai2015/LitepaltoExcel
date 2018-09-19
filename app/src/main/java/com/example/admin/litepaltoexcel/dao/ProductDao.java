package com.example.admin.litepaltoexcel.dao;

import android.content.ContentValues;

import com.example.admin.litepaltoexcel.bean.Product;
import com.example.admin.litepaltoexcel.dao.base.BaseDao;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by admin on 2018/6/23.
 */

public class ProductDao extends BaseDao<Product> {

    @Override
    public boolean save(Product product) {
        return product.save();
    }

    @Override
    public int delete() {
        return LitePal.deleteAll(Product.class);
    }

    @Override
    public int delete(String string) {
        return 0;
    }

    @Override
    public int delete(int id) {
        return LitePal.delete(Product.class, id);
    }

    @Override
    public int update(int id) {

        return 0;
    }

    @Override
    public int update(String string) {
        return 0;
    }

    public int update(Product product ,int id) {
        return product.update(id);
    }

    @Override
    public void firstUpdateList(List<Product> list) {

    }

    @Override
    public void updateList(List<Product> list) {

    }

    @Override
    public int count() {
        return LitePal
//                .where("userId = ?", mUserId)
                .count(Product.class);
    }

    @Override
    public boolean isExist(String code) {
        return false;
    }



    public List<Product> findAllByLimit() {

        List<Product> list = LitePal
//                .select("")
//                .order("createTime desc")
//                .where("userid = ?", mUserId)
//                .limit(pageNum)
//                .offset(pageNum * (page - 1))
                .findAll(Product.class);
        return list;
    }


}
