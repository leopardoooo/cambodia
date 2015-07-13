package com.ycsoft.report.commons;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.poi2.hssf.usermodel.HSSFCell;
import org.apache.poi2.hssf.usermodel.HSSFCellStyle;
import org.apache.poi2.hssf.usermodel.HSSFFont;
import org.apache.poi2.hssf.usermodel.HSSFRichTextString;
import org.apache.poi2.hssf.usermodel.HSSFRow;
import org.apache.poi2.hssf.usermodel.HSSFSheet;
import org.apache.poi2.hssf.usermodel.HSSFWorkbook;
import org.apache.poi2.hssf.util.Region;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.report.query.QueryResultOlap;
import com.ycsoft.report.query.cube.CubeCell;
import com.ycsoft.report.query.cube.CubeHeadCell;
import com.ycsoft.report.query.cube.DimensionType;

public class OlapPageExcel {

	// 创建Workbook对象（这一个对象代表着对应的一个Excel文件）
	// HSSFWorkbook表示以xls为后缀名的文件
	private HSSFWorkbook workbook = null;

	private HSSFSheet wsheet = null;

	private int workbookNum = 0;

	private int cheetNum = 0;

	private int rowNum = 0;

	private boolean isnumbersign[] = null;

	private ZipOutputStream os = null;

	private BufferedOutputStream bos=null;
	
	private QueryResultOlap olap=null;
	
	private int page_max=0;
	
	public OlapPageExcel(QueryResultOlap olap,int page_max) throws ReportException{
		this.olap=olap;
		this.workbookNum = 0;
		this.page_max=page_max;
		if(this.page_max<100)
			throw new ReportException("page_max is ");
	}
	
	
	/**
	 * 创建新的工作薄
	 * 如果原工作不用空，则写入文件保存，之后生成一个新的工作薄
	 * @return
	 * @throws ReportException
	 */
	private void createWorkbook() throws ReportException {
		try {
			if (this.workbook != null) {
				//多个工作页
				if(os==null){
					String zipfile=ReportConstants.REP_TEMP_TXT+this.olap.getQueryId()+ReportConstants.ZIPPOSTFIX;
					bos=new BufferedOutputStream(new FileOutputStream(zipfile));
					os= new ZipOutputStream(bos);
				}
				os.putNextEntry(new ZipEntry(this.olap.getQueryId() + "_"+ (this.workbookNum++)+ ReportConstants.EXCELPOSTFIX));
				this.workbook.write(os);//写入zip压缩流
				this.cheetNum = 0;
				this.workbook = new HSSFWorkbook();
			} else {
				this.cheetNum = 0;
				this.workbook = new HSSFWorkbook();
			}
		} catch (FileNotFoundException e) {
			throw new ReportException("excel_workbook_error", e);
		} catch (IOException e) {
			throw new ReportException("excel_workbook_error", e);
		} 
	}

	/**
	 * 获取导出文件路径
	 * 
	 * @return
	 * @throws ReportException
	 */
	public String getExportFile() throws ReportException {
		try {
			this.exceExport();
			//把工作薄中的剩余数据写入文件
			String filename=null;
			if (this.workbook != null) {
				if(this.workbookNum==0){
					filename=ReportConstants.REP_TEMP_TXT + this.olap.getQueryId() + ReportConstants.EXCELPOSTFIX;
					bos=new BufferedOutputStream(new FileOutputStream(filename));
					this.workbook.write(bos);
					bos.close();
					bos=null;
					return filename;
				}else{
				
					os.putNextEntry(new ZipEntry(this.olap.getQueryId() + "_"+ (this.workbookNum++)+ ReportConstants.EXCELPOSTFIX));
					this.workbook.write(os);
					os.close();
					os=null;
					return ReportConstants.REP_TEMP_TXT + this.olap.getQueryId()+ ReportConstants.ZIPPOSTFIX ;
				}
			}
			return null;
		} catch (FileNotFoundException e) {
			throw new ReportException("excel_workbook_error", e);
		} catch (IOException e) {
			throw new ReportException("excel_workbook_error", e);
		} finally {
			try {
				if (os != null)
					os.close();
				os = null;
			} catch (Exception e) {}
			try {
				if (bos != null)
					bos.close();
				bos = null;
			} catch (Exception e) {}
		}
	}
	
	/**
	 *  创建工作页 表头设置
	 * @param headlist
	 */
	private void createRowTitle(List<CubeHeadCell[]> headlist){
		this.wsheet = workbook.createSheet(String.valueOf(this.cheetNum++));
		this.rowNum = 0;
		//字体设置
		HSSFFont font = this.workbook.createFont();
		font.setFontName("黑体");
		HSSFCellStyle style = this.workbook.createCellStyle();
		style.setFont(font);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
		for (CubeHeadCell[] heads: headlist) {
			HSSFRow row = this.wsheet.createRow(this.rowNum);
			short cellindex = 0;//列索引
			for (CubeHeadCell head:heads) {
				    if(head.getColspan()>0&&head.getRowspan()>0){
						HSSFCell cell = row.createCell(cellindex);
						cell.setCellStyle(style);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellValue(new HSSFRichTextString(head.getName()));
						// 单元格合并
						if ( head.getRowspan() > 1||head.getColspan()>1){

							Region region= new Region(this.rowNum,cellindex, this.rowNum+head.getRowspan()-1,(short) (cellindex + head.getColspan()-1));
							this.setRegionStyle(this.wsheet,region,style);
							this.wsheet.addMergedRegion(region);
							
						}
						cellindex = (short) (cellindex + head.getColspan());
				    }else{
				    	cellindex++;
				    }
			}
			this.rowNum++;
		}
	}
	/**
	 * 合并单元格设置边框
	 * @param sheet
	 * @param region
	 * @param cs
	 */
	private void setRegionStyle(HSSFSheet sheet, Region region , HSSFCellStyle cs) {
        //int toprowNum = region.getRowFrom();
        for (int i = region.getRowFrom(); i <= region.getRowTo(); i ++) {
            //HSSFRow row = this.wsheet.getRow(i, sheet);
        	HSSFRow row = this.wsheet.getRow(i);
        	if(row==null){
        		row=this.wsheet.createRow(i);
        	}
            for (int j = region.getColumnFrom(); j <= region.getColumnTo(); j++) {
                //HSSFCell cell =  HSSFCellUtil.getCell(row, (short)j);
            	HSSFCell cell=row.getCell((short)j);
            	if(cell==null){
            		cell=row.createCell((short)j);
            	}
                cell.setCellStyle(cs);
            }
        }
	}
	/**
	 * 执行导出
	 * 
	 * @param rl
	 * @return
	 * @throws ReportException 
	 * @throws SystemException
	 */
	private void exceExport() throws ReportException {

		List<CubeHeadCell[]> headlist = this.olap.getHead();
		this.isnumbersign = new boolean[headlist.get(headlist.size()-1).length];
		for(int i=0;i<this.isnumbersign.length;i++){
			this.isnumbersign[i]=!headlist.get(headlist.size()-1)[i].getDim_type().equals(DimensionType.crosswise);
		}
		HSSFCellStyle style = null;
		
		int start_index = 0;
		while(this.olap.getRowSize()>start_index){
			List<CubeCell[]> rowcells=this.olap.getPage(start_index, this.page_max);
			start_index=start_index+this.page_max;
			this.createWorkbook();
			this.createRowTitle(headlist);
			if(style==null){
				style=this.workbook.createCellStyle();
				style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
				style.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
				style.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
				style.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
				style.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
			}
			for(CubeCell[]  cubecells:rowcells){
				HSSFRow row = this.wsheet.createRow(this.rowNum);
				short cellindex=0;
				for (short i = 0; i < cubecells.length; i++) {
						HSSFCell cell = row.createCell(cellindex);
						cell.setCellStyle(style);
						if(cubecells[i].getRowspan()>0&&cubecells[i].getColspan()>0){
							if (this.isnumbersign[i]) {
								cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
								cell.setCellValue(Double.valueOf(cubecells[i].getId().toString()));	
							} else {
								//单元格合并处理
								cell.setCellType(HSSFCell.CELL_TYPE_STRING);
								cell.setCellValue(new HSSFRichTextString(cubecells[i].getName()));
							}
							
							//单元格合并
							if ( cubecells[i].getRowspan() > 1||cubecells[i].getColspan()>1){
								Region region=new Region(this.rowNum,cellindex, 
										this.rowNum+cubecells[i].getRowspan()-1,(short)(cellindex+cubecells[i].getColspan()-1));
								this.setRegionStyle(this.wsheet,region,style);
								this.wsheet.addMergedRegion(region);
								
							}
							cellindex = (short) (cellindex + cubecells[i].getColspan());
						}else{
							cellindex++;
						}
				}
				this.rowNum++;
			}
		}
	}

}
