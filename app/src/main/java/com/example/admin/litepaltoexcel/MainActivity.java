package com.example.admin.litepaltoexcel;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.admin.litepaltoexcel.bean.Product;
import com.example.admin.litepaltoexcel.dao.ProductDao;
import com.example.admin.litepaltoexcel.excel.ExcelAsynTask;
import com.example.admin.litepaltoexcel.excel.ExcelUtils;

import org.litepal.util.LogUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;

public class MainActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST = 111;
    private static final int CHECK_PERMISSION = 222;
    private String[] title = {"ID","船只名称", "时间", "产品名称", "产品批次", "捕捞区域", "纬度", "经度", "备注说明"};
    private EditText edtBoatName;
    private EditText edtDateTime;
    private EditText edtProductName;
    private EditText edtProductBatch;
    private EditText edtArea;
    private EditText edtLat;
    private EditText edtLon;
    private EditText edtNote;
    private Button mSaveBtn;
    private String[] saveData;
    private ProductDao mProductDao;
    private File file;
    private ArrayList<ArrayList<String>> bill2List;
    private String mFileName;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewsById();
        mContext = getApplicationContext();
        mProductDao = new ProductDao();
        bill2List = new ArrayList<ArrayList<String>>();
        getPermissions();
        initPhotoError();
    }

    private void initPhotoError(){
        // android 7.0系统解决获取文件和拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }
    }

    private void getPermissions() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS) == PackageManager.PERMISSION_GRANTED) {

            //获取权限后 才能读写数据库

        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS}, CHECK_PERMISSION);
        }
    }

    private void findViewsById() {
        edtBoatName = (EditText) findViewById(R.id.edt_boatName);
        edtDateTime = (EditText) findViewById(R.id.edt_dateTime);
        edtProductName = (EditText) findViewById(R.id.edt_productName);
        edtProductBatch = (EditText) findViewById(R.id.edt_productBatch);
        edtArea = (EditText) findViewById(R.id.edt_area);
        edtLat = (EditText) findViewById(R.id.edt_Lat);
        edtLon = (EditText) findViewById(R.id.edt_Lon);
        edtNote = (EditText) findViewById(R.id.edt_note);
        mSaveBtn = (Button) findViewById(R.id.btn_save);

        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
        String times = new SimpleDateFormat("yyyy_MM_dd").format(new Date());
        edtDateTime.setText(time);
        edtProductBatch.setText(times+"_aaa");
        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDB();
            }
        });
    }

    private void saveDB()  {
        saveData = new String[]{edtBoatName.getText().toString().trim(), edtDateTime.getText().toString().trim(), edtProductName.getText().toString().trim(),
                edtProductBatch.getText().toString().trim(), edtArea.getText().toString().trim(), edtLat.getText().toString().trim(),
                edtLon.getText().toString().trim(), edtNote.getText().toString().trim()};
        if (canSave(saveData)) {
            Product product = new Product();
            product.setBoatName(edtBoatName.getText().toString().trim());
            product.setDateTime(edtDateTime.getText().toString().trim());
            product.setProductName(edtProductName.getText().toString().trim());
            product.setProductBatch(edtProductBatch.getText().toString().trim());
            product.setArea(edtArea.getText().toString().trim());
            product.setLat(edtLat.getText().toString().trim());
            product.setLon(edtLon.getText().toString().trim());
            product.setNote(edtNote.getText().toString().trim());
            boolean save = product.save();
            // mProductDao.save(product);
            int id = product.getId();
            Log.d("tag", id + "");
            if (save){
                Toast.makeText(MainActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(MainActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(MainActivity.this, "请填写任意一项内容", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean canSave(String[] data) {
        boolean isOk = false;
        for (int i = 0; i < data.length; i++) {
            if (i > 0 && i < data.length) {
                if (!TextUtils.isEmpty(data[i])) {
                    isOk = true;
                }
            }
        }
        return isOk;
    }


    /**
     * ----------------------------------------LItepal to Excel---------------------------------------------------------------
     **/

    //当客户点击MENU按钮的时候，调用该方法
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 1, "查看数据库");
        menu.add(0, 2, 2, "保存到Excel");
        menu.add(0, 3, 3, "蓝牙发送Excel数据");
        menu.add(0, 4, 4, "解析Excel数据");
        return super.onCreateOptionsMenu(menu);
    }

    //当客户点击菜单当中的某一个选项时，会调用该方法
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 1) {

        Intent intent = new Intent(this , BillListActivity.class);
        startActivity(intent);

        }else if (item.getItemId() == 2) {
            Log.d("TAG", "保存到Excel");
            List<Product> productList = mProductDao.findAllByLimit();
            if (productList.size()>0){
                createExcel();
            }else {
                Toast.makeText(MainActivity.this, "数据库无内容", Toast.LENGTH_SHORT).show();
            }

        } else if (item.getItemId() == 3) {
            Log.d("TAG", "蓝牙发送Excel数据");  //    /storage/emulated/0/Family/bill.xls
            String s = getSDPath() + "/Family/bill.xls";
//            Log.d("TAG", s);
//            openAssignFolder(s);
            checkBluetoothPermission();

        }else if (item.getItemId() == 4){
            Log.d("TAG", "解析Excel数据");
            String bluetoothPath = getSDPath() + "/bluetooth/bill.xls"; //   /storage/emulated/0/bluetooth/bill.xls
            readExcel();

        }

        return super.onOptionsItemSelected(item);
    }

    private void readExcel() {

        String bluetoothPath = getSDPath() + "/bluetooth/bill.xls"; //   /storage/emulated/0/bluetooth/bill.xls
        Log.d("tag", bluetoothPath);
        File bluetoothFile = new File(bluetoothPath);

        if (bluetoothFile.exists()) {
            if (bluetoothFile.getName().endsWith(".xls") || file.getName().endsWith(".xlsx")) {
                //设置视图
                Log.d("tag","正在读取");

                new ExcelAsynTask(mContext,bluetoothFile).execute();


//                boatName;
//                dateTime;
//                productName;
//                productBatch;
//                area;
//                Lat;
//                Lon;
//                note;
                //执行线程

//                try {
//                    Workbook workbook = Workbook.getWorkbook(bluetoothFile);
//                    //获取第一张excel数据表。
//                    Sheet sheet=workbook.getSheet(0);
//                    int rows=sheet.getRows();//获取该表中有多少行数据。
//                    Log.e("readExcelToDB",rows+"-------rows-------");
//                    Product product=null;
//                    for(int i=1;i<rows;i++){
//                        //sheet.getCell(0,i))，在这里i表示第几行数据，012346表示第几列，从0开始算。
//                        String boatName=(sheet.getCell(1,i)).getContents();
//                        String dateTime=(sheet.getCell(2,i)).getContents();
//                        String productName=(sheet.getCell(3,i)).getContents();
//                        String productBatch=(sheet.getCell(4,i)).getContents();
//                        String area=(sheet.getCell(5,i)).getContents();
//                        String Lat=(sheet.getCell(6,i)).getContents();
//                        String Lon=(sheet.getCell(7,i)).getContents();
//                        String note=(sheet.getCell(8,i)).getContents();
//                        product=new Product(boatName,dateTime,productName,productBatch,area,Lat,Lon,note);
//                        Log.e("ExcelUtils",product.toString());
//                        product.save();//把数据保存到数据库中。
//
//                    }
//                    //读取完毕后，删除Excel文件
//                    if (bluetoothFile.delete()){
//                        Toast.makeText(this, "Excel文件删除", Toast.LENGTH_SHORT).show();
//                    }
//
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }



            } else {
                Toast.makeText(this, "请选择Excel文件进行操作", Toast.LENGTH_SHORT).show();

                return;
            }



        }else {
            Toast.makeText(this, "文件不存在", Toast.LENGTH_SHORT).show();
        }

    }

    //创建Excel表格 并保存数据到Excel表格中
    @SuppressLint("SimpleDateFormat")
    private void createExcel() {
        //创建文件
        file = new File(getSDPath() + "/Trace");
        makeDir(file);
        //创建Excel表格
        mFileName = file.toString() + "/bill.xls";
        ExcelUtils.initExcel(file.toString() + "/bill.xls", title);
        //将数据写入Excel表格
        ExcelUtils.writeObjListToExcel(getBillData(), getSDPath() + "/Trace/bill.xls", this);

    }


    private ArrayList<ArrayList<String>> getBillData() {
        List<Product> productList = mProductDao.findAllByLimit();

            for (Product product : productList) {
                ArrayList<String> beanList = new ArrayList<String>();
//            boatName;
//            dateTime;
//            productName;
//            productBatch;
//            area;
//            Lat;
//            Lon;
//            note;
                beanList.add(product.getId()+"");
                beanList.add(product.getBoatName());
                beanList.add(product.getDateTime());
                beanList.add(product.getProductName());
                beanList.add(product.getProductBatch());
                beanList.add(product.getArea());
                beanList.add(product.getLat());
                beanList.add(product.getLon());
                beanList.add(product.getNote());
                bill2List.add(beanList);
            }
            return bill2List;
    }


    public static void makeDir(File dir) {
        if (!dir.getParentFile().exists()) {
            makeDir(dir.getParentFile());
        }
        dir.mkdir();
    }

    public String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();
        }
        String dir = sdDir.toString();
        return dir;

    }


    /***----------------------------蓝牙发送Excel文件------------------------------------------------------------------*/


    private void connectBluetooth() {

        String dir = getSDPath() + "/Trace/bill.xls";
        //mFileName
        File file = new File(dir);

        if(null==file || !file.exists()){
            Toast.makeText(this, "Excel文件不存在", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
//      intent.setType("application/octet-stream");
        intent.setType("*/*"); //
        intent.setClassName("com.android.bluetooth"
                , "com.android.bluetooth.opp.BluetoothOppLauncherActivity");
        //  intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile( new File("/sdcard/20095111013295162.jpg")));
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile( new File(dir)));
        startActivity(intent);

    }




    //校验蓝牙权限
    private void checkBluetoothPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            //校验是否已具有模糊定位权限
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                //具有权限
                connectBluetooth();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST);
            }
        } else {
            //系统不高于6.0直接执行
            connectBluetooth();
        }
    }



    // 对返回值进行处理，类似于startActivityForResult方法：
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        doNext(requestCode, grantResults);
    }
    private void doNext(int requestCode, int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //同意权限
                connectBluetooth();
            } else {
                // 权限拒绝
                // 下面的方法最好写一个跳转，可以直接跳转到权限设置页面，方便用户
                //  denyPermission();
                Toast.makeText(this, "没有获取蓝牙权限,请先获取蓝牙权限", Toast.LENGTH_SHORT).show();
            }


        }else  if (requestCode == CHECK_PERMISSION //文件操作的权限
                && grantResults.length == 3
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED
                && grantResults[2] == PackageManager.PERMISSION_GRANTED) {

            //获取权限后的操作

        }


    }

}
