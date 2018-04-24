package com.jt.sys.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.document.AbstractPdfView;


import com.jt.common.util.PDFUtil;
import com.jt.sys.entity.SysUser;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

@Service
public class UserPdfView extends AbstractPdfView{
	
	public static final String TITLE="sys_user";
	
	@Override
	protected void buildPdfDocument(Map<String, Object> map, Document document, PdfWriter writer,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		document.open();
		//设置标题字体与居中显示
		Paragraph title = PDFUtil.getParagraph(new Chunk(
				TITLE, new Font(PDFUtil.bf, 16, Font.BOLD)));
		title.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(title);
		
		@SuppressWarnings("unchecked")
		List<SysUser> sysUsers =(List<SysUser>) map.get("sysUsers");
		PdfPTable table = PDFUtil.createPDF(sysUsers);
		
		document.add(table);
		document.close();
	}
}


