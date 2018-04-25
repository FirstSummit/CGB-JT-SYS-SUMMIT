package com.jt.common.util;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.lowagie.text.Chunk;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPTable;



public class PDFUtil {
	
	//基本字体和样式
	public static BaseFont bf =null;
	public static Font font = null;
	public static String CHANG_LINE="\n";
	public static Font UNDER_LINE=null;
	
	public static final String BEGIN = "{";  
    public static final String END = "}";  
    public static final String NEW_LINE = "#";  
    public static final float DEFAULT_LEADING = 20;  
    public static final float DEFAULT_LINE_INDENT = 30;  
	
    
	static {
		try {
			bf=BaseFont.createFont("C:/Windows/Fonts/ARIALUNI.TTF", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
			font = new Font(bf, 14, Font.NORMAL);
		} catch (DocumentException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Paragraph getParagraph(Chunk chunk){
		return new Paragraph(chunk);
	}
	
	public static Paragraph getParagraph(String string){
		return new Paragraph(string,font);
	}
	
	// 根据传入的数据制作表格
	// 制作表格标题
	public static <T> PdfPTable createPDF(List<T> list) throws Exception{
		Field[] fields = list.get(0).getClass().getDeclaredFields();
	    Field[] fields1 = list.get(0).getClass().getSuperclass().getDeclaredFields();
	    fields= Arrays.copyOfRange(fields, 1, fields.length);
	    int len=fields.length;
	    fields=Arrays.copyOf(fields, len+fields1.length);
	    System.arraycopy(fields1, 0, fields, len, fields1.length);
	    //创建表格列数
		PdfPTable table = new PdfPTable(fields.length);
		//设置一行的首位缩进
		table.setSpacingBefore(20);
		table.setSpacingAfter(30);
		//创建表头
		for (Field field : fields) {
			table.addCell(getParagraph(field.getName()));
		}
		//创建body
		for (T t : list) {
			for (Field field : fields) {
				String name = field.getName();
	    		name ="get"+name.substring(0, 1).toUpperCase().concat(name.substring(1));
	    		Object value = t.getClass().getMethod(name).invoke(t);
	    		if(field.getType()==String.class && value!=null){
	    			   table.addCell((String)value);
	    		   }else if(field.getType()==Integer.class && value!=null){
	    			   table.addCell(((Integer)value).toString());
	    		   }else if(field.getType()==Date.class && value!=null){
	    			   table.addCell(((Date)value).toLocaleString());
	    		   }else if(value==null){
	    			   table.addCell("null");
				}
			}
		}
		return table;
	}
	
	 // 默认段落样式  
    public static Paragraph getDefaultParagraph(String context){  
        Paragraph p = getParagraph(context);  
        // 默认行间距  
        p.setLeading(DEFAULT_LEADING);  
        // 默认首行空隙  
        p.setFirstLineIndent(DEFAULT_LINE_INDENT);  
        return p;  
    }
    
    //解析段落文字
    public static List<Paragraph> getPars(String context ,Map<String,String> map){  
        int index = 0;  
        List<Paragraph> list = new ArrayList<Paragraph>();  
        Paragraph p = getDefaultParagraph(null);  
        while((index  = context.indexOf(BEGIN)) > -1){  
            String text = context.substring(0,index);  
            context = context.substring(index, context.length());  
            index = context.indexOf(END);  
            String param =  null;  
            if(index > 0){  
                 param = context.substring(BEGIN.length(),index);  
            }  
            p.add(text);  
            if(!NEW_LINE.equals(param)){  
                Object value = map.get(param);  
                if(value != null){  
                    p.add(new Chunk(value.toString(),UNDER_LINE));  
                }else{  
                    p.add(new Chunk(param));  
                }  
            }else{  
                list.add(p);  
                p = getDefaultParagraph(null);  
                p.setSpacingBefore(0);  
            }  
            context = context.substring(index+END.length(),context.length());  
        }  
        list.add(p);  
        list.add(getParagraph(context));  
        return list;  
    }  

}
