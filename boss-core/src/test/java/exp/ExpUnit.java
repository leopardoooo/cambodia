package exp;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import com.ycsoft.beans.core.cust.CCust;

/**
 * 表达式处理单元
 * 
 * @author liujiaqi
 * 
 */
class Fee {
	private int ACCTITEM_2701=10000;
	private int ACCTITEM_2721=10000;
	private int ACCTITEM_2740=0;
	private int ACCTITEM_2763=0;
	private int ACCTITEM_2792=0;
	private int ACCTITEM_2804=0;
	private int times=0;
	public int getACCTITEM_2701() {
		return ACCTITEM_2701;
	}
	public void setACCTITEM_2701(int acctitem_2701) {
		ACCTITEM_2701 = acctitem_2701;
	}
	public int getACCTITEM_2721() {
		return ACCTITEM_2721;
	}
	public void setACCTITEM_2721(int acctitem_2721) {
		ACCTITEM_2721 = acctitem_2721;
	}
	public int getACCTITEM_2740() {
		return ACCTITEM_2740;
	}
	public void setACCTITEM_2740(int acctitem_2740) {
		ACCTITEM_2740 = acctitem_2740;
	}
	public int getACCTITEM_2763() {
		return ACCTITEM_2763;
	}
	public void setACCTITEM_2763(int acctitem_2763) {
		ACCTITEM_2763 = acctitem_2763;
	}
	public int getACCTITEM_2792() {
		return ACCTITEM_2792;
	}
	public void setACCTITEM_2792(int acctitem_2792) {
		ACCTITEM_2792 = acctitem_2792;
	}
	public int getACCTITEM_2804() {
		return ACCTITEM_2804;
	}
	public void setACCTITEM_2804(int acctitem_2804) {
		ACCTITEM_2804 = acctitem_2804;
	}
	public int getTimes() {
		return times;
	}
	public void setTimes(int times) {
		this.times = times;
	}
	
	
	
}

public class ExpUnit {
	private StandardEvaluationContext context = new StandardEvaluationContext();
	private ExpressionParser parser = new SpelExpressionParser();
	
	
	public static void main(String[] args){
		ExpressionParser p1 = new SpelExpressionParser();
		Expression exp = p1.parseExpression("cust_name");
		
		CCust cust = new CCust();
		cust.setCust_name("潘玉奔");
		StandardEvaluationContext context = new StandardEvaluationContext();
		context.setRootObject(cust);
		
		System.out.println(exp.getValue(context).toString());
		
		Fee fee = new Fee();
		String expStr = "(((ACCTITEM_2701>0?1:0)+ "+
						" (ACCTITEM_2721>0?1:0)+"+
						" (ACCTITEM_2740>0?1:0)+"+
						" (ACCTITEM_2763>0?1:0)+"+
						" (ACCTITEM_2792>0?1:0)+"+
						" (ACCTITEM_2804>0?1:0))>=2 "+
						" and "+
						" ((ACCTITEM_2701>=600*times) or"+
						" (ACCTITEM_2721>=600*times) or"+
						" (ACCTITEM_2740>=600*times) or"+
						" (ACCTITEM_2763>=600*times) or"+
						" (ACCTITEM_2792>=600*times) or"+
						" (ACCTITEM_2804>=600*times) ))";
		System.out.println(expStr);
		exp = p1.parseExpression(expStr);
		context.setRootObject(fee);
		for (int i=0;i<1000;i++){
			fee.setTimes(i);
			if (exp.getValue(context).toString().equalsIgnoreCase("false")){
				System.out.println(i-1);
				break;
			}
		}
		
	}

	public StandardEvaluationContext getContext() {
		return context;
	}

	public ExpressionParser getParser() {
		return parser;
	}

	/**
	 * 返回指定类型的表达式结果
	 * 
	 * @param <T>
	 *            返回类型
	 * @param exp
	 *            表达式
	 * @param resultType
	 * @return
	 */
	public <T> T getVariable(String exp, Class<T> resultType) {
		try {
			return parser.parseExpression(exp).getValue(context, resultType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getVariable(String exp) {
		 return getVariable(exp, String.class); 
	}

	/**
	 * 返回Boolean类型的表达式结果
	 * 
	 * @param exp
	 *            表达式
	 * @return
	 */
	public boolean parseBoolean(String exp) {
		return getVariable(exp, Boolean.class);
	}

	public void setContext(StandardEvaluationContext context) {
		this.context = context;
	}

	public void setParser(ExpressionParser parser) {
		this.parser = parser;
	}

	public void setVariable(Object variables) {
		
		context.setRootObject(variables);
	}

	/**
	 * 将变量加入表达式容器 以#varname的方式调用
	 * 
	 * @param varname
	 * @param value
	 */
	public void setVariable(String varname, Object value) {
		context.setVariable(varname, value);
	}
}
