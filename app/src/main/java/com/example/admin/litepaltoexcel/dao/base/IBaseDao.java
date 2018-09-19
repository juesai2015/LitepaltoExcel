package com.example.admin.litepaltoexcel.dao.base;

import java.util.List;

/**
 * Created by user on 2017/4/5.
 */

public interface IBaseDao<T> {
    //增加-------------------------------------------------------------
    boolean save(T t);

    //删除-------------------------------------------------------------
    int delete();
    int delete(String string);
    int delete(int id);
    int deleteByEntityID(String entityId);

    //修改
    int update(int id);
    int update(String string);
    //第一次更新，不需要循环判断数据库是否存在
    void firstUpdateList(List<T> list);
    void updateList(List<T> list);

    //查询--------------------------------------------------------------
    //分页查询
    List<T> findAllByLimit(int pageNum, int page);
    List<T> findAllByLimit(int pageNum, int page, T t);
    List<T> findAllByLimit(int pageNum, int page, String keyWord);
    List<T> findAllByLimit(int pageNum, int page, String keyWord, T t);


    //统计--------------------------------------------------------------
    int count();

    boolean isExist(String code);



}
