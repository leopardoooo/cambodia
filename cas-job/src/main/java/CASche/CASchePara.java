package CASche;

import java.util.HashMap;
import java.util.Map;

public class CASchePara {
	String srcTable;
	String srcTableBak;
	String dstTable;
	String dstTableBak;
	String seqDstTransnum;
	int orderCount;
	int order_notsupport=0;//指定某个优先级层不开启指令优化，0表示不启用
	Map<String, String> mapOrder = null;
	Map<String, CASPrivatePara> mapCAS = null;
	//是否开启osd内容合法性验证
	boolean osd_content_check=false; 
	//osd内容合法性验证配置刷新时长
	int osd_base_refresh_time=600;
	
	public CASchePara()
	{
		mapOrder = new HashMap<String, String>();
		mapCAS = new HashMap<String, CASPrivatePara>();
		
	}

	public String getSrcTable() {
		return srcTable;
	}
	public void setSrcTable(String srcTable) {
		this.srcTable = srcTable;
	}
	public String getSrcTableBak() {
		return srcTableBak;
	}
	public void setSrcTableBak(String srcTableBak) {
		this.srcTableBak = srcTableBak;
	}
	public String getDstTable() {
		return dstTable;
	}
	public void setDstTable(String dstTable) {
		this.dstTable = dstTable;
	}
	public String getDstTableBak() {
		return dstTableBak;
	}
	public void setDstTableBak(String dstTableBak) {
		this.dstTableBak = dstTableBak;
	}
	public String getSeqDstTransnum() {
		return seqDstTransnum;
	}
	public void setSeqDstTransnum(String seqDstTransnum) {
		this.seqDstTransnum = seqDstTransnum;
	}
	public int getOrderCount() {
		return orderCount;
	}
	public void setOrderCount(int orderCount) {
		this.orderCount = orderCount;
	}
	public Map<String, String> getMapOrder() {
		return mapOrder;
	}
	public void setMapOrder(Map<String, String> mapOrder) {
		this.mapOrder = mapOrder;
	}

	public Map<String, CASPrivatePara> getMapCAS() {
		return mapCAS;
	}
	public void setMapCAS(Map<String, CASPrivatePara> mapCAS) {
		this.mapCAS = mapCAS;
	}

	public int getOrder_notsupport() {
		return order_notsupport;
	}

	public void setOrder_notsupport(int order_notsupport) {
		this.order_notsupport = order_notsupport;
	}

	public boolean isOsd_content_check() {
		return osd_content_check;
	}

	public void setOsd_content_check(boolean osd_content_check) {
		this.osd_content_check = osd_content_check;
	}

	public int getOsd_base_refresh_time() {
		return osd_base_refresh_time;
	}

	public void setOsd_base_refresh_time(int osd_base_refresh_time) {
		this.osd_base_refresh_time = osd_base_refresh_time;
	}

}
