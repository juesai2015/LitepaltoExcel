package com.example.admin.litepaltoexcel.excel;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.admin.litepaltoexcel.bean.Product;

import java.io.File;
import java.util.ArrayList;

import jxl.Sheet;
import jxl.Workbook;

/**
 * Created by admin on 2018/7/3.
 * 读取Excel到数据库
 */

public class ExcelAsynTask extends AsyncTask<String,Integer,ArrayList<Product>> {

    private Context mContext = null;
    private File mFile = null;

    public ExcelAsynTask(Context context,File file) {
        mContext = context;
        mFile = file;
    }

    @Override
    protected ArrayList<Product> doInBackground(String... strings) {
        ArrayList<Product> products = new ArrayList<>();

        //执行线程
        try {
            Workbook workbook = Workbook.getWorkbook(mFile);
            //获取第一张excel数据表。
            Sheet sheet=workbook.getSheet(0);
            int rows=sheet.getRows();//获取该表中有多少行数据。
            Log.e("readExcelToDB",rows+"-------rows-------");
            Product product=null;

            for(int i=1;i<rows;i++){
                //sheet.getCell(0,i))，在这里i表示第几行数据，012346表示第几列，从0开始算。
                String boatName=(sheet.getCell(1,i)).getContents();
                String dateTime=(sheet.getCell(2,i)).getContents();
                String productName=(sheet.getCell(3,i)).getContents();
                String productBatch=(sheet.getCell(4,i)).getContents();
                String area=(sheet.getCell(5,i)).getContents();
                String Lat=(sheet.getCell(6,i)).getContents();
                String Lon=(sheet.getCell(7,i)).getContents();
                String note=(sheet.getCell(8,i)).getContents();
                product=new Product(boatName,dateTime,productName,productBatch,area,Lat,Lon,note);
                Log.e("ExcelUtils",product.toString());
                product.save();//把数据保存到数据库中。
                products.add(product);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return products;
    }

    @Override
    protected void onPreExecute() {
        Log.d("tag","1111");
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        Log.d("tag",values+"");
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(ArrayList<Product> products) {
        Log.d("tag",2222+"");
        //读取完毕后，删除Excel文件
        if (mFile.delete()){
            Toast.makeText(mContext, "Excel文件删除", Toast.LENGTH_SHORT).show();
        }
        super.onPostExecute(products);
    }

}
