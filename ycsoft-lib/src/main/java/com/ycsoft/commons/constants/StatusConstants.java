package com.ycsoft.commons.constants;

/**
 * select 'public static String '||status_id||' =
 * "'||status_id||'";//'||status_desc from t_status ;
 * @author liujiaqi
 *
 */
public class StatusConstants {

	public static String ACTIVE = "ACTIVE";//正常    黑
	public static String STOP = "STOP"; //暂停
	public static String INVALID = "INVALID";//失效  红
	public static String PREOPEN = "PREOPEN";//预开户 绿
	public static String PREAUTHOR = "PREAUTH";//产品 '预开通'
	public static String RELOCATE =  "RELOCATE";//拆迁
	public static String LYRELOCATE = "LYRELOCATE";//溧阳拆迁
	public static String REQSTOP = "REQSTOP";//报停   红
	public static String OWESTOP = "OWESTOP";//欠费停 红
	public static String OWENOTSTOP = "OUNSTOP";//欠费未停  橙
	public static String LINKSTOP = "LINKSTOP";//关联停 红
	public static String FORSTOP = "FORSTOP";//到期停
	public static String TMPOPEN = "TMPOPEN";//临时开通
    public static String LNKUSTOP = "LNKUSTOP";//关联未停
    public static String CANCEL = "CANCEL";//失效  红
    public static String INSTALL_FAILURE = "INSTALL_FAILURE";//施工失败
    public static String UNTUCK = "UNTUCK";//拆机
    public static String UNTUCKEND = "UNTUCKEND";//拆机完成
 
	public static String IDLE = "IDLE";//空闲       黑
	public static String USE = "USE";//使用		   红
	public static String CORRUPT = "CORRUPT";//损坏  紫
	public static String SCRAP = "SCRAP";//报废     红
	public static String PAY = "PAY";//已支付       蓝
	public static String UNPAY = "UNPAY";//未支付   绿
	public static String DEPOSIT = "DEPOSIT";//押金已退
	public static String UNCONFIRM = "UNCONFIRM";//未确认
	public static String CONFIRM = "CONFIRM";//确认
	public static String NOTCONFIRM = "NOTCONFIRM" ;//不确认
	public static String CLOSE = "CLOSE";//缴销
	public static String CHECKED = "CHECKED";//结账
	public static String CUSTLINE = "CUSTLINE";//剪线
	public static String PAUSE = "PAUSE";//不计费
	public static String DATACLOSE = "DATACLOSE";//资料隔离
	public static String DORMANCY = "DORMANCY";//休眠
	public static String ATVCLOSE = "ATVCLOSE";//关模隔离
	public static String OWELONG = "OWELONG";//长期欠费
	public static String ISOLATED = "ISOLATED";//隔离
	public static String LOGOFF = "LOGOFF";	//销户
	public static String RELOCATE_LOGOFF = "RELOCATELOGOFF";	//拆迁销户
	public static String TRANSFER_LOGOFF = "TRANSFERLOGOFF";	//移出销户 ,客户迁移之后的状态
	public static String WAITLOGOFF = "WAITLOGOFF";//待销户
	public static String TMPPAUSE = "TMPPAUSE";//暂停
	
	//工单状态
	public static String TASK_INIT = "INIT";//施工中
	public static String TASK_END = "END";//完工
	public static String TASK_CANCEL = "CANCEL";//已取消
	public static String TASK_VISIT = "VISIT";//回访完成
	public static String TASK_CREATE = "CREATE";//工单新建待派单
	public static String TASK_ENDWAIT = "ENDWAIT";//完工等待
	
	public static String REQSTOPLONG = "REQSTOPLONG";//长报停   红
	public static String REQSTOPSLEEP = "REQSTOPSLEEP";//休眠报停   红
	
	public static String INSTALL = "INSTALL";//施工中
	
	public static String NONE = "NONE";//空
	public static String SUCCESS = "SUCCESS";//成功
	public static String FAILURE = "FAILURE";//失败
	public static String NOT_EXEC = "NOT_EXEC";//未执行
	
	
}
