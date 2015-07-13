package CASche.caschedule;

import java.util.List;

import CASche.common.SchedultException;


public interface Merge {

	/**
	 * 同个智能卡的相同控制字的加减指令合并
	 * @param list
	 * @return
	 */
	public List<JCaCommand> mergeSameCotrolId(List<JCmdDay> daylist);
	
	/**
	 * 同个智能卡的多个控制字合并成一个条指令
	 * @param list
	 * @return
	 * @throws Exception 
	 */
	public List<JCaCommand> mergeMultiCotrolId(List<JCmdDay> daylist) throws SchedultException;
}
