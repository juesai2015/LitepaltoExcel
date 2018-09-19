package com.example.admin.litepaltoexcel.dao.base;



import java.util.List;

/**
 * Created by zx on 2016/10/21.
 */
public abstract class BaseDao<T> implements IBaseDao<T> {
    protected String mUserId;

    public BaseDao() {
      //  mUserId = PreferencesUtils.getString(IApplication.getContext(), GetLoginRespons.USERID);
    }


    //可有可无的方法就在抽象类实现，让开发者自己去选择实现,多类型就加泛型
    //必须的方法就放在接口中去

    @Override
    public int deleteByEntityID(String entityId) {
        return 0;
    }

    @Override
    public List<T> findAllByLimit(int pageNum, int page) {
        return null;
    }

    @Override
    public List<T> findAllByLimit(int pageNum, int page, T t) {
        return null;
    }

    @Override
    public List<T> findAllByLimit(int pageNum, int page, String keyWord) {
        return null;
    }

    @Override
    public List<T> findAllByLimit(int pageNum, int page, String keyWord, T t) {
        return null;
    }


}
