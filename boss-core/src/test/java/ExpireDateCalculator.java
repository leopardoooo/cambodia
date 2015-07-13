import java.util.Date;

/**
 * 根据金额、日租、日租的计算方法、上一次失效日期，开始延算本次到期日
 * 
 * @author Tom
 */
public class ExpireDateCalculator {

	static enum RentType{
		AT, // 按天
		AY  // 按月 
	}
	
	/**
	 * 
	 * @param fee 金额
	 * @param rent 日租
	 * @param type 计算方式
	 * @param lastExpireDate 上一个失效日期
	 */
	public void calc(int fee, int rent, RentType type, Date lastExpireDate){
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
