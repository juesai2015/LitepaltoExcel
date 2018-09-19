package com.example.admin.litepaltoexcel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.admin.litepaltoexcel.bean.Product;
import com.example.admin.litepaltoexcel.dao.ProductDao;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

public class BillListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private List<Product> mProductList = new ArrayList<>();
    private CommonAdapter<Product> mProductAdapter;
    private ProductDao mProductDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_list);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mProductDao = new ProductDao();
        initView();
        initData();
    }



    private void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mProductAdapter = new CommonAdapter<Product>(this, R.layout.item_product_list, mProductList) {
            @Override
            protected void convert(ViewHolder holder, Product product, int position) {
                holder.setText(R.id.tv_boatName, product.getBoatName());
                holder.setText(R.id.tv_dateTime, product.getDateTime());
                holder.setText(R.id.tv_productName, product.getProductName());
                holder.setText(R.id.tv_productBatch, product.getProductBatch());
                holder.setText(R.id.tv_area, product.getArea());
                holder.setText(R.id.tv_Lat, product.getLat());
                holder.setText(R.id.tv_Lon, product.getLon());
                holder.setText(R.id.tv_note, product.getNote());
            }

        };

        mRecyclerView.setAdapter(mProductAdapter);

        mProductAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Toast.makeText(BillListActivity.this, mProductList.get(position).getId()+"", Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
              //  mProductList.remove(position);
                return false;
            }
        });




    }

    private void initData() {
        List<Product> productList = mProductDao.findAllByLimit();
        if (productList.size()>0){
            mProductList.addAll(productList);
            mProductAdapter.notifyDataSetChanged();
        }else {
            Toast.makeText(this, "数据库无内容", Toast.LENGTH_SHORT).show();
        }

    }


}
