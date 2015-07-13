package com.ycsoft.commons.helper;

import java.util.HashSet;
import java.util.Set;

import com.ycsoft.beans.core.job.JCaCommand;
import com.ycsoft.beans.system.SItemvalue;
import com.ycsoft.commons.constants.DictKey;
import com.ycsoft.commons.store.MemoryDict;

public class OsdCheckHelper {
	
	//JCaCommandDto
	private final static String osd_begin=";MSG:'";
	private final static String osd_end="';style:";
	
	private final static String SendOsd="SendOsd";
	/**
	 * 从指令中提取OSD内容
	 * @param ca
	 * @return
	 * @throws Exception 
	 */
	public String extractOsdContent(JCaCommand ca) throws Exception{
		if(ca==null) return null;
		//当指令是osd时
		if(SendOsd.equals(ca.getCmd_type())){
			if(ca.getDetail_params()==null||ca.getDetail_params().trim().length()<=0){
				throw new Exception("OSD内容为空");
			}
			int begin=ca.getDetail_params().indexOf(osd_begin);
			int end=ca.getDetail_params().indexOf(osd_end);
			if(begin+6>=end||begin<=0||end<=0){
				throw new Exception("OSD内容格式无法识别{*;MSG:'*';style:*}");
			}
			String osd=ca.getDetail_params().substring(begin+6, end);
			return osd;
		}
		return null;
	}
	/**
	 * OSD内容合法性验证
	 * 
	 * 返回值为非法内容，返回null表示内容合法
	 */
	public String LawfulCheck(String content){
		if(content==null) return null;
		Set<Integer> check_num_set=new HashSet<Integer>(content.length());
		int check_num_size=content.length();
		
		for(SItemvalue sitem:MemoryDict.getDicts(DictKey.OSD_PHRASE)){
			String phrase=sitem.getItem_value();
			int fromIndex=0;
			int _index=0;
			do{
				_index=content.indexOf(phrase, fromIndex);
				fromIndex=_index+phrase.length();;
				if(_index>=0){
					for(int i=_index;i<fromIndex;i++){
						if(!check_num_set.contains(i)){
							check_num_set.add(i);
						}
					}
				}
			}while(_index>=0);
			
			if(check_num_set.size()==check_num_size){
				return null;
			}
		}
		
		if(check_num_set.size()>check_num_size){
			content="Error:字符串长度"+check_num_size+",而计算长度为"+check_num_set.size();
		}else{
			char[] chars=content.toCharArray();
			char[] error_content=new char[check_num_size-check_num_set.size()];
			for(int i=0,j=0;i<check_num_size;i++){
				if(!check_num_set.contains(i)){
					error_content[j]=chars[i];
					j++;
				}
			}
			content=new String(error_content);
		}
		
		return content;
	}
}
