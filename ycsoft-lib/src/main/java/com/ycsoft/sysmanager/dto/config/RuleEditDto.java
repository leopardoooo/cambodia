package com.ycsoft.sysmanager.dto.config;

import java.util.List;

import com.ycsoft.beans.config.TRuleDefine;
import com.ycsoft.beans.config.TRuleEdit;

public class RuleEditDto {

	private RuleDefineDto rule;
	private List<TRuleEdit> ruleEdits;
	public RuleDefineDto getRule() {
		return rule;
	}
	public void setRule(RuleDefineDto rule) {
		this.rule = rule;
	}
	public List<TRuleEdit> getRuleEdits() {
		return ruleEdits;
	}
	public void setRuleEdits(List<TRuleEdit> ruleEdits) {
		this.ruleEdits = ruleEdits;
	}
}
