package com.example.admin.litepaltoexcel.excel;

import android.os.Environment;

import java.io.File;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class CreateExcel {
	// 准备设置excel工作表的标题
	private WritableSheet sheet;
	/**创建Excel工作薄*/
	private WritableWorkbook wwb;
	private String[] title = { "ID","船只名称", "时间", "产品名称", "产品批次", "捕捞区域", "纬度", "经度", "备注说明" };

	public CreateExcel() {
		excelCreate();
	}

	public void excelCreate() {
		try {
			/**输出的excel文件的路径*/
			String filePath = Environment.getExternalStorageDirectory() + "/trace_bill";
			File file = new File(filePath, "bill.xls");
			if (!file.exists()) {
				file.createNewFile();
			}
			wwb = Workbook.createWorkbook(file);
			/**添加第一个工作表并设置第一个Sheet的名字*/
			sheet = wwb.createSheet("捕捞明细表", 0);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void saveDataToExcel(int index, String[] content) throws Exception {
		Label label;
		for (int i = 0; i < title.length; i++) {
			/**Label(x,y,z)其中x代表单元格的第x+1列，第y+1行, 单元格的内容是y
			 * 在Label对象的子对象中指明单元格的位置和内容
			 * */
			label = new Label(i, 0, title[i]);
			/**将定义好的单元格添加到工作表中*/
			sheet.addCell(label);
		}
		/*
		 * 把数据填充到单元格中
		 * 需要使用jxl.write.Number
		 * 路径必须使用其完整路径，否则会出现错误
		 */
		for (int i = 0; i < title.length; i++) {
			Label labeli = new Label(i, index, content[i]);
			sheet.addCell(labeli);
		}
		// 写入数据
		wwb.write();
		// 关闭文件
		wwb.close();
	}

}
