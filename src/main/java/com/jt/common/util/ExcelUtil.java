package com.jt.common.util;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {
	
	public static <T> Workbook creatExcel(String title,List<T> list) throws Exception{
		
		//创建一个workbook
		Workbook workbook = new XSSFWorkbook();
		//根据表头,创建一张sheet
		Sheet sheet = workbook.createSheet(title);
		// 用于格式化单元格的数据
        DataFormat format = workbook.createDataFormat();
        
        // 设置字体
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 20); // 字体高度
        font.setColor(Font.COLOR_NORMAL); // 字体颜色
        font.setFontName("黑体"); // 字体
        font.setBoldweight(Font.BOLDWEIGHT_BOLD); // 宽度
        
        // 设置单元格类型保存文字
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER); // 水平布局：居中
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // 垂直居中
        cellStyle.setWrapText(true);  // 自动换行
        
        //设置单元格格式保存数字
        CellStyle cellStyle2 = workbook.createCellStyle();
        cellStyle2.setDataFormat(format.getFormat("＃,##0.0"));
        
        //设置单元格格式保存日期
        CellStyle cellStyle3 = workbook.createCellStyle();
        cellStyle3.setDataFormat(format.getFormat("yyyy-MM-dd HH:mm:ss"));
        
        //创建表头
        Cell head = sheet.createRow(0).createCell(0);
        head.setCellValue(title);
        head.setCellStyle(cellStyle);
        Row row = sheet.createRow(1);
        Field[] fields = list.get(0).getClass().getDeclaredFields();
        Field[] fields1 = list.get(0).getClass().getSuperclass().getDeclaredFields();
        int len=fields.length;
        fields=Arrays.copyOf(fields, len+fields1.length);
        System.out.println(Arrays.toString(fields));
        System.arraycopy(fields1, 0, fields, len, fields1.length);
       for(int i=1; i<fields.length;i++){
    	   System.out.println(fields[i]);
    	   Cell cell = row.createCell(i-1);
    	   String name = fields[i].getName();
    	   System.out.println(name);
    	   cell.setCellValue(name);
    	   cell.setCellStyle(cellStyle);
       }
       //插入数据
       for(int i =0;i<list.size();i++){
    	   T t = list.get(i);
    	   row = sheet.createRow(2+i);
    	   for(int j=1; j<fields.length;j++){
    		   Cell cell = row.createCell(j-1);
    		   Field field = fields[j];
    		   String name = field.getName();
    		   name ="get"+name.substring(0, 1).toUpperCase().concat(name.substring(1));
    		   Object value = t.getClass().getMethod(name).invoke(t);
    		   System.out.println(field.getType());
    		   if(field.getType()==String.class && value!=null){
    			   cell.setCellValue((String)value);
    		   }else if(field.getType()==Integer.class && value!=null){
    			   cell.setCellValue((Integer)value);
    			   cell.setCellStyle(cellStyle2);
    		   }else if(field.getType()==Date.class && value!=null){
    			   System.out.println(value);
    			   cell.setCellValue((Date)value);
    			   cell.setCellStyle(cellStyle3);
    		   }
    	   }
       }
       return workbook;
	}
}
