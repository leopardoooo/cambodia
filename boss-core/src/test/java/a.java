import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ycsoft.business.commons.pojo.Parameter;
import com.ycsoft.commons.helper.JsonHelper;

/**
 * 
 */

/**
 * @author liujiaqi
 *
 *
/**
 * @author liujiaqi
 *
 */
public class a {
	static String[] datefunname= {"TODAY"};
	public static void main(String[] args) throws Exception {
		String ruleStr ="ccust.create_time1=TODAY and ccust.create_time2=TODAY";
		converDateFunction(ruleStr);
	}
	
	public static String converDateFunction(String ruleStr) throws Exception {
		ruleStr = " " +ruleStr;
		for (String s : datefunname) {
			int end = ruleStr.indexOf("="+s) ;
			while (end > -1) {
				String tt = ruleStr.substring(ruleStr.substring(0, end).lastIndexOf(" ")+1,end+s.length()+1);
				ruleStr = ruleStr.replaceAll(tt, s+"("+tt.replaceAll("="+s, ")"));
				end = ruleStr.indexOf("="+s) ;
			}
		}
		return ruleStr;
	}
}
