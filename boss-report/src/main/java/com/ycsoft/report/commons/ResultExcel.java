package com.ycsoft.report.commons;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.ycsoft.report.bean.RepHead;

public class ResultExcel {
	// 创建Workbook对象（这一个对象代表着对应的一个Excel文件）
	// HSSFWorkbook表示以xls为后缀名的文件
	private HSSFWorkbook workbook = null;

	private HSSFSheet wsheet = null;

	private int workbookNum = 0;

	private int cheetNum = 0;

	private int rowNum = 0;

	private boolean isnumbersign[] = null;

	private String query_id = null;

	private ZipOutputStream os = null;

	private BufferedOutputStream bos=null;
	
	private Map<String,Integer> export_cols_map=null;//导出列配置
	
	private boolean isexportsign[]=null;
	
	public ResultExcel(String query_id)
			throws ReportException {
		if (query_id == null || query_id.equals(""))
			throw new ReportException("query_id is null");

		this.query_id = query_id;
		this.workbookNum = 0;
	}
	
	public ResultExcel(String query_id,Integer... col_index) throws ReportException{
		if (query_id == null || query_id.equals(""))
			throw new ReportException("query_id is null");
		this.query_id = query_id;
		this.workbookNum = 0;
		if(col_index!=null&&col_index.length>0){
			export_cols_map=new HashMap<String,Integer>();
			for(Integer o:col_index)
				export_cols_map.put(o.toString(), o);
		}
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
				if(os==null){
					String zipfile=ReportConstants.REP_TEMP_TXT+this.query_id+ReportConstants.ZIPPOSTFIX;
					bos=new BufferedOutputStream(new FileOutputStream(zipfile));
					os= new ZipOutputStream(bos);
				}
				os.putNextEntry(new ZipEntry(this.query_id + "_"+ (this.workbookNum++)+ ReportConstants.EXCELPOSTFIX));
				this.workbook.write(os);
				//os.flush();
				//this.workbook.wr
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
					filename=ReportConstants.REP_TEMP_TXT + this.query_id + ReportConstants.EXCELPOSTFIX;
					bos=new BufferedOutputStream(new FileOutputStream(filename));
					this.workbook.write(bos);
					bos.close();
					bos=null;
					return filename;
				}else{
				
					os.putNextEntry(new ZipEntry(this.query_id + "_"+ (this.workbookNum++)+ ReportConstants.EXCELPOSTFIX));
					this.workbook.write(os);
					os.close();
					os=null;
					return ReportConstants.REP_TEMP_TXT + this.query_id+ ReportConstants.ZIPPOSTFIX ;
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
	 * 创建工作页 表头设置
	 * 
	 * @throws RowsExceededException
	 * @throws WriteException
	 */
	private void createRowTitle(List<List<RepHead>> headlist) {
		this.wsheet = workbook.createSheet(String.valueOf(this.cheetNum++));
		this.rowNum = 0;

		HSSFFont font = this.workbook.createFont();
		font.setFontName("黑体");
		HSSFCellStyle style = this.workbook.createCellStyle();
		style.setFont(font);
		for (List<RepHead> list : headlist) {
			HSSFRow row = this.wsheet.createRow(this.rowNum);
			short cellindex = 0;
			for (int i=0;i<list.size();i++) {
				if(this.isexportsign[i]){
					RepHead head = list.get(i);
					HSSFCell cell = row.createCell(cellindex);
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue(new HSSFRichTextString(head.getCol_desc()));
					short celllength = head.getCol_length().shortValue();
					// 单元格合并
					if (celllength > 1)
						this.wsheet.addMergedRegion(new Region(this.rowNum,
								cellindex, this.rowNum,
								(short) (cellindex + celllength)));
					cellindex = (short) (cellindex + celllength);
				}
			}
			this.rowNum++;
		}
	}

	/**
	 * 配置导出列，多表头的情况下可能存在问题
	 * @param headlist
	 * @throws ReportException 
	 */
	private void configexport(List<List<RepHead>> headlist) throws ReportException{
		//获取最后一列
		List<RepHead> list=headlist.get(headlist.size()-1);
		//导出列设置
		this.isexportsign = new boolean[list.size()];
		boolean checkexportsign=false;
		
		for(int i=0;i<list.size();i++){
			if(this.export_cols_map==null){
				this.isexportsign[i]=true;
				checkexportsign=true;
			}else if(this.export_cols_map.containsKey(String.valueOf(i))){
				this.isexportsign[i]=true;
				checkexportsign=true;
			}else{
				this.isexportsign[i]=false;
			}
		}
		if(!checkexportsign)
			throw new ReportException("rep_optr_export error:导出列不存在有效配置");
		
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

		FileObjectInputStream foi = null;
		try {
			foi = new FileObjectInputStream(ReportConstants.REP_TEMP_TXT
					+ this.query_id);

			List<List<RepHead>> headlist = (List<List<RepHead>>) foi
					.readObject();
			if (headlist == null || headlist.size() == 0)
				throw new ReportException("QueryResult(" + this.query_id
						+ "):head is null.");
			this.configexport(headlist);
			this.createWorkbook();
			this.createRowTitle(headlist);
			List list = null;
			int query_index = 0;
			while ((list = (List) foi.readObject()) != null) {
				
				query_index++;
				// 最后一行合计行要单独处理
//				if (query_index == this.queryrowsize)
//					break;
				// 设置列的属性
				if (this.isnumbersign == null) {
					this.isnumbersign = new boolean[list.size()];
					
					for (int i = 0; i < list.size(); i++) {
						if (list.get(i) instanceof String || list.get(i) instanceof Date)
							this.isnumbersign[i] = false;
						else
							this.isnumbersign[i] = true;
						//第一列默认是非数值行
						if(i==0)
							this.isnumbersign[i] = false;
						
					}
					
				}
				if (this.rowNum % ReportConstants.WORKBOOKMAXROWS == 0) {
					this.createWorkbook();
					this.createRowTitle(headlist);
				}
				if (this.rowNum % ReportConstants.WHEETMAXROWS == 0) {
					this.createRowTitle(headlist);
				}
				HSSFRow row = this.wsheet.createRow(this.rowNum);
				short cellindex=0;
				for (short i = 0; i < list.size(); i++) {
					if(this.isexportsign[i]){
						HSSFCell cell = row.createCell(cellindex);
						cellindex++;
						
						if (this.isnumbersign[i]) {
							if(list.get(i)==null||"".equals(list.get(i))){
								cell.setCellType(HSSFCell.CELL_TYPE_STRING);
								cell.setCellValue(new HSSFRichTextString(""));
							}else{
							cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
							cell.setCellValue(Double.valueOf(list.get(i).toString()));
							}
						} else {
							cell.setCellType(HSSFCell.CELL_TYPE_STRING);
							cell.setCellValue(new HSSFRichTextString(list.get(i).toString()));
						}
					}
				}
				this.rowNum++;
			}
		} catch (IOException e) {
			throw new ReportException("excel_workbook_error", e);
		} catch (ReportException e) {
			throw e;
		} catch (ClassNotFoundException e) {
			throw new ReportException("excel_workbook_error", e);
		}finally{
			if(foi!=null){
				try {
					foi.close();
					foi=null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}
