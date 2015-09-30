import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 */

/**
 * @author liujiaqi
 *
 */
public class CC {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
//		Parameter p = JsonHelper.toObject("{'custFullInfo':{'cust':{'acct_id':null,'addr_id':'010100032483','addr_id_text':'全名：琅东12组','address':'aa','area_id':'0100','busi_name':'010110000521','county_id':'0101','create_time':null,'cust_id':'10000520','cust_level':'YBYH','cust_level_text':'一般用户','cust_name':'ttt','cust_type':'RESIDENT','cust_type_text':'居民客户','is_black':'T','is_black_text':'是','net_type':'CITY','net_type_text':'城网客户','open_time':'2010-06-17 15:12:16','password':'111111','status':'INACTIVE','status_text':''},'linkman':{'address':'aa','area_id':'0100','birthday':'2010-07-01 00:00:00','county_id':'0101','cust_id':'10000520','email':null,'linkman_name':null,'mobile':'asdf','postcode':null,'sex':'MALE','sex_text':'男','tel':'123'},'resident':{'area_id':'0100','cert_num':'123','cert_type':'HKB','cert_type_text':'户口本','county_id':'0101','cust_class':'YBYH','cust_class_text':'一般用户','cust_colony':'YBYH','cust_colony_text':'一般用户','cust_id':'10000520','cust_level':'YBYH','cust_level_text':'一般用户'}},'selectedAtvs':[],'selectedDtvs':[],'selectedBands':[{'user_id':'3062','cust_id':'10000520','acct_id':'100000122','user_type':'BAND','user_addr':null,'stop_type':null,'status':'ACTIVE','net_type':'CABLE','stb_id':null,'card_id':null,'modem_mac':'0022CEF5E68A','open_time':'2010-07-13 16:57:24','area_id':'0100','county_id':'0101','user_type_text':'宽带','status_text':'正常','stop_type_text':'','net_type_text':'同轴','user_name':null,'terminal_type':null,'terminal_type_text':null,'serv_type':null,'serv_type_text':null,'check_type':'','check_type_text':'','login_name':'','password':'','bind_type':'','bind_type_text':'','max_connection':'','prods':null}],'busiCode':'1024','fees':[{'fee_id':'3028','value_id':'6','count':'1','real_pay':'10000','fee_price_value':'100.00'}],'pay':null,'extAttrForm':{'extAttrs':{}}}", new Parameter().getClass());
//		System.out.print(p);
//		CFee c = new CFee();
//		System.out.println(PropertyUtils.getProperty(c, "optr_id"));
		String one = "装箱号1";
		String one1 = "CSS0001";
		String one2 = "A1中";
		Pattern p = Pattern.compile("[^a-zA-Z0-9]"); 
		Matcher m = p.matcher(one); 
		String newFrist = m.replaceAll("");
		System.out.println(newFrist.length()+"-"+one1.length()+"-"+one2.length());
//		String filePath = "E:\\work\\TCL1009台机顶盒入库文件.txt";  
//        readTxtFile(filePath);  
	}

	
	
	 /** 
     * 功能：Java读取txt文件的内容 
     * 步骤：1：先获得文件句柄 
     * 2：获得文件句柄当做是输入一个字节码流，需要对这个输入流进行读取 
     * 3：读取到输入流后，需要读取生成字节流 
     * 4：一行一行的输出。readline()。 
     * 备注：需要考虑的是异常情况 
     * @param filePath 
     */  
    public static void readTxtFile(String filePath){  
        try {  
                String encoding="GBK";  
                File file=new File(filePath);  
                if(file.isFile() && file.exists()){ //判断文件是否存在  
                    InputStreamReader read = new InputStreamReader(  
                    new FileInputStream(file),encoding);//考虑到编码格式  
                    BufferedReader bufferedReader = new BufferedReader(read);  
                    String lineTxt = null;
                    List<String> list = new ArrayList<String>();
                    while((lineTxt = bufferedReader.readLine()) != null){  
                    	lineTxt = lineTxt.replaceAll("，", ",");  
                        list.add(lineTxt);
                        System.out.println(lineTxt+"---"+list.size());  
                    }  
//                    System.out.println(list.get(list.size()).toString());
                    read.close();  
        }else{  
            System.out.println("找不到指定的文件");  
        }  
        } catch (Exception e) {  
            System.out.println("读取文件内容出错");  
            e.printStackTrace();  
        }  
      
    }  
      
}
