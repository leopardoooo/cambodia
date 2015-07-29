package com.ycsoft.web.action.core;

import org.springframework.stereotype.Controller;

import com.ycsoft.web.commons.abstracts.BaseBusiAction;

@Controller
public class ProdOrderAction extends BaseBusiAction {
	
	
	public String queryProdOrderInit(){
		
		return JSON_OTHER;
	}
}
