package CASche;

import CASche.common.CaConstants;
import CASche.help.LoggerHelper;

public class ParaFromFile implements ParaBase {

	String fileName=null;
	CASchePara schePara = null;
	
	public ParaFromFile()
	{
		fileName = null;
	}
	
	public ParaFromFile(String fileName)
	{
		this.fileName = fileName;
	}
	
	public void setSchePara(CASchePara schePara)
	{
		this.schePara = schePara;
	}
	
	public CASchePara getSchePara()
	{
		return this.schePara;
	}
	
	public int GetPara(String processName) {
		ConfigFile cf = new ConfigFile(fileName);
		String srcTable = cf.getValue("SRC_TABLE");
		if (srcTable==null||"".equals(srcTable.trim())){
			LoggerHelper.error(this.getClass(),"SRC_TABLE that define in " + fileName + " is error!");
			return -1;
		}
		schePara.setSrcTable(srcTable.trim());
		
		String srcTableBak = cf.getValue("SRC_TABLE_BAK");
		if (srcTableBak==null||"".equals(srcTableBak.trim())){
			LoggerHelper.error(this.getClass(),"SRC_TABLE_BAK that define in " + fileName + " is error!");
			return -1;
		}
		schePara.setSrcTableBak(srcTableBak.trim());
		
		String dstTable = cf.getValue("DST_TABLE");
		if (dstTable==null||"".equals(dstTable.trim())){
			LoggerHelper.error(this.getClass(),"DST_TABLE that define in " + fileName + " is error!");
			return -1;
		}
		schePara.setDstTable(dstTable.trim());
		
		String dstTableBak = cf.getValue("DST_TABLE_BAK");
		if (dstTableBak==null||"".equals(dstTableBak.trim())){
			LoggerHelper.error(this.getClass(),"DST_TABLE_BAK that define in " + fileName + " is error!");
			return -1;
		}
		schePara.setDstTableBak(dstTableBak.trim());
		
		String seqDstTransnum = cf.getValue("SEQ_DST_TRANSNUM");
		if (seqDstTransnum==null||"".equals(seqDstTransnum.trim())){
			LoggerHelper.error(this.getClass(),"SEQ_DST_TRANSNUM that define in " + fileName + " is error !");
			return -1;
		}
		schePara.setSeqDstTransnum(seqDstTransnum.trim());
		//指定优先级不启用指令优化
		String order_notsupport=cf.getValue("ORDER_NOTSUPPORT");
		if(order_notsupport!=null&&!"".equals(order_notsupport.trim())){
			schePara.setOrder_notsupport(Integer.parseInt(order_notsupport.trim()));
		}
		//回写处理速度
		String writebak=cf.getValue("WRITEBAK_SPEED");
		if(writebak!=null&&!"".equals(writebak.trim())){
			int speed=Integer.parseInt(writebak.trim());
			if(speed>=0)
			CaConstants.movecaouttohis_speed=speed;
		}
		//程序空闲等待或者CA拥堵等待
		String free=cf.getValue("FREE_TIME");
		if(free!=null&&!"".equals(free.trim())){
			int speed=Integer.parseInt(free.trim());
			if(speed>0)
				CaConstants.free_sleep_time=speed;
		}
		String sCount = cf.getValue("ORDER_COUNT");
		int  iCount = 0;
		try{
			iCount = Integer.parseInt(sCount);
		}catch(	java.lang.NumberFormatException e){
			e.printStackTrace();
			iCount = 0;
		}
		
		if (iCount<=0){
			LoggerHelper.error(this.getClass(),"ORDER_COUNT that define in " + fileName + " is error!");
			return -1;
		}
		
		schePara.setOrderCount(iCount);
		
		for (int i=1; i<=iCount; i++){
			String orderN = cf.getValue("ORDER"+i);
			if (orderN==null||"".equals(orderN.trim())){
				LoggerHelper.error(this.getClass(),"ORDER" + i + "that defined in " + fileName + " is error!");
				return -1;
			}
			schePara.getMapOrder().put("ORDER"+i, orderN.trim());
		}
		
		// name, castype,  casSQL, pools, addmode, cancelmode
		for (int i=1; ;i++){
			String ca = cf.getValue("CA"+i);
			if (ca==null||"".equals(ca.trim())){
				break;
			}
			String[] cas = ca.split(",");
			if(cas.length!=6){
				LoggerHelper.error(this.getClass(),"CA"+i+ "that defined in " + fileName + " is error!");
				return -1;
			}
			CASPrivatePara caspp = new CASPrivatePara();
			caspp.setName(cas[0].trim());
			caspp.setCasType(cas[1].trim());
			caspp.setCasSQL(cas[2].trim());
			caspp.setPool(Integer.parseInt(cas[3].trim()));
			caspp.setAddMode(cas[4].trim());
			caspp.setCancelMode(cas[5].trim());
			schePara.getMapCAS().put(cas[0].trim(), caspp);
		}
		
		String OSD_CONTENT_CHENK = cf.getValue("OSD_CONTENT_CHENK");
		if (OSD_CONTENT_CHENK!=null&&!"".equals(OSD_CONTENT_CHENK.trim())){
			schePara.setOsd_content_check(Boolean.parseBoolean(OSD_CONTENT_CHENK.trim()));
		}
		
		String OSD_BASE_REFRESH_TIME = cf.getValue("OSD_BASE_REFRESH_TIME");
		if (OSD_BASE_REFRESH_TIME!=null&&!"".equals(OSD_BASE_REFRESH_TIME.trim())){
			int refresh_time=Integer.parseInt(OSD_BASE_REFRESH_TIME.trim());
			if(refresh_time>0){
				schePara.setOsd_base_refresh_time(refresh_time);
			}
		}
		
		return 1;
	}

}
