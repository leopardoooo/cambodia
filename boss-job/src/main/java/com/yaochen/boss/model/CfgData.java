package com.yaochen.boss.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ycsoft.beans.config.TRuleDefine;
import com.ycsoft.beans.config.TServer;
import com.ycsoft.beans.config.TServerRes;
import com.ycsoft.beans.device.RCardModel;
import com.ycsoft.business.dto.core.prod.PPromotionDto;
import com.ycsoft.commons.exception.ComponentException;
import com.ycsoft.sysmanager.dto.prod.ProdDto;

public class CfgData {
	//根据卡型号获取服务区供应商
	public String getServerSupplier(String cardModel) throws Exception{
		try {
			return cardCaMap.get(cardModel.trim()).getCa_type();
		} catch (Exception e){
			throw new ComponentException("找不到智能卡对应的CAS");
		}
	}
	//根据县市和服务器供应商获取对应的服务器
	public List<TServer> getServer(String supplierId,String countyId) throws Exception{
		List<TServer> servList = new ArrayList<TServer>();
		for (TServer server:this.serverList){
			if (server.getSupplier_id().equals(supplierId)){
				for (String county:server.getCountyList()){
					if (county.equals(countyId)){
						servList.add(server);
						break;
					}
				}
			}
		}
		if (servList.size()==0)
			throw new ComponentException("找不到"+supplierId+"在"+countyId+"的服务器");
		else
			return servList;
	}
	//根据boss资源id和服务id获取对应的资源信息
	public TServerRes getExtenalRes(String serverId,String bossResId)throws Exception{
		try {
			TServerRes res = resMap.get(bossResId).get(serverId);
			return res;
		} catch (Exception e){
			throw new ComponentException("找不到"+bossResId+"在服务器"+serverId+"对应的资源");
		}

	}
	//根据产品id获取产品信息
	public ProdDto getProd(String prodId)throws Exception{
		try {
			return prodMap.get(prodId);
		} catch (Exception e){
			throw new ComponentException("产品不存在");
		}

	}

	//根据产品id获取产品信息
	public List<String> getProdRes(String prodId) throws Exception {
		List<String> resList = new ArrayList<String>();
		try {
			ProdDto prod = prodMap.get(prodId);
			for (String resId : prod.getResList()) {
				resList.add(resId);
			}
			return resList;
		} catch (Exception e) {
			throw new ComponentException("产品不存在");
		}
	}
	
	//根据业务指令编号和服务器类型获取对应的服务器指令
	public List<String> getCmd(String busiCmdType,String supplierId)throws Exception{
		try {
			return busiCmdMap.get(busiCmdType).get(supplierId);
		} catch (Exception e){
			throw new ComponentException("找不到"+busiCmdType+"在服务器"+supplierId+"对应的业务指令");
		}

	}
	//根据规则id获取规则内容
	public String getRuleStr(String ruleId) {
		String ruleStr= "";
		for (TRuleDefine rule:ruleList){
			if (ruleId.equals(rule.getRule_id())){
				ruleStr = rule.getRule_str();
				break;
			}
		}
		return ruleStr;
	}
	private Map<String,ProdDto> prodMap;
	private Map<String,RCardModel> cardCaMap;
	private List<TServer> serverList;
	private Map<String,Map<String,TServerRes>> resMap;
	private Map<String,Map<String,List<String>>> busiCmdMap;
	private List<PPromotionDto> promotionList;
	private List<TRuleDefine> ruleList;

	public void setProdMap(Map<String, ProdDto> prodMap) {
		this.prodMap = prodMap;
	}
	public Map<String, RCardModel> getCardCaMap() {
		return cardCaMap;
	}
	public void setCardCaMap(Map<String, RCardModel> cardCaMap) {
		this.cardCaMap = cardCaMap;
	}
	public List<TServer> getServerList() {
		return serverList;
	}
	public void setServerList(List<TServer> serverList) {
		this.serverList = serverList;
	}
	public Map<String, Map<String,TServerRes>> getResMap() {
		return resMap;
	}
	public void setResMap(Map<String, Map<String,TServerRes>> resMap) {
		this.resMap = resMap;
	}
	public Map<String, Map<String,List<String>>> getBusiCmdMap() {
		return busiCmdMap;
	}
	public void setBusiCmdMap(Map<String, Map<String,List<String>>> busiCmdMap) {
		this.busiCmdMap = busiCmdMap;
	}
	public List<PPromotionDto> getPromotionList() {
		return promotionList;
	}
	public void setPromotionList(List<PPromotionDto> promotionList) {
		this.promotionList = promotionList;
	}
	public List<TRuleDefine> getRuleList() {
		return ruleList;
	}
	public void setRuleList(List<TRuleDefine> ruleList) {
		this.ruleList = ruleList;
	}
}
