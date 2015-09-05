/*
 * @(#) BossExternalAction.java 1.0.0 Aug 12, 2015 6:07:48 PM
 */
package com.ycsoft.web.action.external;

import org.springframework.stereotype.Controller;

import com.ycsoft.commons.abstracts.AbstractAction;
import com.ycsoft.commons.exception.ErrorCode;
import com.ycsoft.commons.exception.ServicesException;

/**
 * 该接口定义了外部调用的HTTP + JSON接口，主要有个人移动端的一些业务接口
 * 
 * @author Killer
 */
@SuppressWarnings("serial")
@Controller
public class BossExternalAction extends AbstractAction{
	private static final String JSON = "json";
	private ResultBody resultBody;
	
	/*! 用户相关属性 */
	private String user_id;
	private String version;
	private String user_ip;
	private String user_passwd;
	private String user_name;
	private String user_rank;
	private String telephone;
	private String email;
	private String user; 
	
	/*! 产品相关属性 */
	private String product_ids;
	private String product_id;	
	private String product_fee_id;	
	private Integer amount;	
	private String film_name;
	private String boss_data;
	private String ott_data; 
	
	private String update_product;
	private float price;
	private String currency_type;
	
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// 用户相关接口定义
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	/**
	 * 获取用户帐户信息接口
	 * @param user_id 用户ID
	 * @param version OTT业务版本号
	 * @param user_ip 来源IP
	 */
	public String getAccountInfo(){
		this.doActionInCallbackForCommonResult(new Callback(){
			@Override
			public Object doCallback() throws Throwable {
				// TODO get_account_info
				return "你好";
			}
		}, null);
		
		return JSON;
	}

	/**
	 * 修改用户帐户信息接口
	 * @param user_id 用户ID
	 * @param version OTT业务版本号
	 * @param user_passwd 用户密码
	 * @param user_name 用户名称
	 * @param user_rank 用户等级
	 * @param telephone 手机号码
	 */
	public String modifyAccountInfo(){
		return JSON;
	}
	
	/**
	 * 用户注册接口，通过手机注册用户，BOSS后台将进行客户开户和基本包订购，生成用户
	 * @param user_id 用户ID
	 * @param version OTT业务版本号
	 * @param user_passwd 用户密码
	 * @param user_name 用户名称
	 * @param user_rank 用户等级
	 * @param telephone 手机号码
	 * @param email 邮箱
	 */
	public String registerAccount(){
		return JSON;
	}
	
	/**
	 * 用户验证接口
	 * @param user 可输入用户ID或昵称(user_id, user_name)
	 */
	public String userValidate(){
		return JSON;
	}
	
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// 产品相关的接口
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * 根据用户ID获取用户的已订购列表
	 * @param user_id 用户ID
	 * @param version OTT业务版本号
	 * @param user_ip 来源IP
	 */
	public String getOrderedProductList(){
		return JSON;
	}
	
	/**
	 * 根据用户ID获取用户的可订购产品列表；产品ID列表不为空时，
	 * 返回包含该产品列表的组合产品列表
	 * 
	 * @param user_id 用户ID
	 * @param version OTT业务版本号
	 * @param user_ip 来源IP
	 * @param product_ids 产品ID列表, 可以为空,多产品以逗号分隔
	 */
	public String getProductList(){
		return JSON;
	}
	
	/**
	 * 用户购买产品，购买指定产品包下的指定资费。
	 * @param user_id 用户ID
	 * @param version OTT业务版本号
	 * @param product_id 产品ID	
	 * @param product_fee_id 产品资费ID	
	 * @param amount 订购数量（仅对周期性产品有效）
	 * @param film_name 影片名称,可以为空，针对单片有效
	 * @param boss_data BOSS扩展参数,从可订购产品列表中获取，订购产品透传BOSS
	 * @param ott_data OTT扩展参数,同步产品授权时回传CMS
	 */
	public String buyProduct(){
		// TODO buy_product
		return JSON;
	}
	
	/**
	 * 根据用户ID获取用户购买产品记录
	 * @param user_id 用户ID
	 * @param version OTT业务版本号
	 * @param user_ip 来源IP
	 */
	public String getBuyProductHistory(){
		// TODO get_buy_product_history
		return JSON;
	}
	
	/**
	 * 根据用户ID获取用户的可升级产品列表；产品ID列表不为空时，
	 * 返回包含该产品列表的组合产品列表
	 * 
	 * @param user_id 用户ID
	 * @param version OTT业务版本号
	 * @param user_ip 来源IP
	 * @param product_ids 产品ID列表, 可以为空,多产品以逗号分隔
	 */
	public String getProductListByUpdate(){
		// TODO get_product_list_by_update
		return JSON;
	}
	
	/**
	 * 把指定产品包升级到另外一个产品包，实现产品包的换购
	 * 
	 * @param user_id 用户ID
	 * @param version OTT业务版本号
	 * @param user_ip 来源IP
	 * @param product_id 升级之前的产品ID
	 * @param update_product_id 升级之后的产品ID
	 * @param price 升级所需价格
	 * @param currency_type RMB：人民币, USD：美元
	 */
	public String updateProduct(){
		// TODO update_product
		return JSON;
	}
	
	/**
	 * 
	 * @param taskCallback 业务回调函数
	 * @param errMsg 如果发生错误，可以通过该接口返回一些错误的msg数据体
	 * @return
	 */
	private ResultBody doActionInCallbackForCommonResult(Callback taskCallback, ErrorMsgDataExtractor errMsg){
		ResultBody body = null;
		try{
			Object msg = taskCallback.doCallback();
			if(msg == null){
				msg = new Object();
			}
			body = ResultBody.createWithMsg(msg);
		}catch(Throwable e){
			Object msg = new Object();
			if(errMsg != null){
				msg = errMsg.extract();
			}
			// 处理异常
			ServicesException tex = null;
			if(e instanceof ServicesException){
				tex = (ServicesException)e;
			}else{
				e.printStackTrace();
				tex = new ServicesException(ErrorCode.UNKNOW_EXCEPTION);
			}
			body = ResultBody.createWithExceptionAndMsg(tex, msg);
		}
		// 设置到当前上下文中
		this.setResultBody(body);
		return body;
	}
	
	private interface Callback{
		Object doCallback()throws Throwable;
	}
	
	private interface ErrorMsgDataExtractor{
		Object extract();
	}
	
	
	public ResultBody getResultBody() {
		return resultBody;
	}

	public void setResultBody(ResultBody resultBody) {
		this.resultBody = resultBody;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public void setUser_ip(String user_ip) {
		this.user_ip = user_ip;
	}

	public void setUser_passwd(String user_passwd) {
		this.user_passwd = user_passwd;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public void setUser_rank(String user_rank) {
		this.user_rank = user_rank;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void setProduct_ids(String product_ids) {
		this.product_ids = product_ids;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public void setProduct_fee_id(String product_fee_id) {
		this.product_fee_id = product_fee_id;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public void setFilm_name(String film_name) {
		this.film_name = film_name;
	}

	public void setBoss_data(String boss_data) {
		this.boss_data = boss_data;
	}

	public void setOtt_data(String ott_data) {
		this.ott_data = ott_data;
	}

	public void setUpdate_product(String update_product) {
		this.update_product = update_product;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public void setCurrency_type(String currency_type) {
		this.currency_type = currency_type;
	}
}
