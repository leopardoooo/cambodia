package com.ycsoft.report.bean;

import java.io.Serializable;

import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;
import com.ycsoft.daos.config.POJO;

@POJO(
		tn="REP_DETAIL_DATA",
		sn="",
		pk="")
public class RepDetailData implements Serializable {
	private String rep_id;
	private String optr_id;
	private String context_id;
	private String context_json;
	private String dim1;
	private String dim2;
	private String dim3;
	private String dim4;
	private String dim5;
	private String dim6;
	private String dim7;
	private String dim8;
	private String dim9;
	private String dim10;
	private String dim11;
	private String dim12;
	private String dim13;
	private String dim14;
	private String dim15;
	private String dim16;
	
	private String optr_name;
	
	public String getOptr_name() {
		return MemoryDict.getDictName(DictKey.OPTR, this.optr_id);
	}
	public void setOptr_name(String optr_name) {
		this.optr_name = optr_name;
	}
	public String getRep_id() {
		return rep_id;
	}
	public void setRep_id(String rep_id) {
		this.rep_id = rep_id;
	}
	public String getOptr_id() {
		return optr_id;
	}
	public void setOptr_id(String optr_id) {
		this.optr_id = optr_id;
	}
	public String getContext_id() {
		return context_id;
	}
	public void setContext_id(String context_id) {
		this.context_id = context_id;
	}
	public String getContext_json() {
		return context_json;
	}
	public void setContext_json(String context_json) {
		this.context_json = context_json;
	}
	public String getDim1() {
		return dim1;
	}
	public void setDim1(String dim1) {
		this.dim1 = dim1;
	}
	public String getDim2() {
		return dim2;
	}
	public void setDim2(String dim2) {
		this.dim2 = dim2;
	}
	public String getDim3() {
		return dim3;
	}
	public void setDim3(String dim3) {
		this.dim3 = dim3;
	}
	public String getDim4() {
		return dim4;
	}
	public void setDim4(String dim4) {
		this.dim4 = dim4;
	}
	public String getDim5() {
		return dim5;
	}
	public void setDim5(String dim5) {
		this.dim5 = dim5;
	}
	public String getDim6() {
		return dim6;
	}
	public void setDim6(String dim6) {
		this.dim6 = dim6;
	}
	public String getDim7() {
		return dim7;
	}
	public void setDim7(String dim7) {
		this.dim7 = dim7;
	}
	public String getDim8() {
		return dim8;
	}
	public void setDim8(String dim8) {
		this.dim8 = dim8;
	}
	public String getDim9() {
		return dim9;
	}
	public void setDim9(String dim9) {
		this.dim9 = dim9;
	}
	public String getDim10() {
		return dim10;
	}
	public void setDim10(String dim10) {
		this.dim10 = dim10;
	}
	public String getDim11() {
		return dim11;
	}
	public void setDim11(String dim11) {
		this.dim11 = dim11;
	}
	public String getDim12() {
		return dim12;
	}
	public void setDim12(String dim12) {
		this.dim12 = dim12;
	}
	public String getDim13() {
		return dim13;
	}
	public void setDim13(String dim13) {
		this.dim13 = dim13;
	}
	public String getDim14() {
		return dim14;
	}
	public void setDim14(String dim14) {
		this.dim14 = dim14;
	}
	public String getDim15() {
		return dim15;
	}
	public void setDim15(String dim15) {
		this.dim15 = dim15;
	}
	public String getDim16() {
		return dim16;
	}
	public void setDim16(String dim16) {
		this.dim16 = dim16;
	}
}
