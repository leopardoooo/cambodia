package com.ycsoft.commons.expression;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * 表达式处理单元
 *
 * @author liujiaqi
 *
 */
@Deprecated
public class ExpUnit {
	private StandardEvaluationContext context = new StandardEvaluationContext();
	private ExpressionParser parser = new SpelExpressionParser();

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
			return parser.parseExpression(exp).getValue(context, resultType);
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
