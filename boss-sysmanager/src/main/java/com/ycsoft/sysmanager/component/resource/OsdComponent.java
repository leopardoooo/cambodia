package com.ycsoft.sysmanager.component.resource;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ycsoft.beans.config.TOsdPhrase;
import com.ycsoft.beans.config.TOsdStop;
import com.ycsoft.beans.core.job.JCaCommand;
import com.ycsoft.beans.core.job.JCaCommandOsdInvalid;
import com.ycsoft.business.dao.core.job.JCaCommandDao;
import com.ycsoft.business.dao.core.job.JCaCommandOsdInvalidDao;
import com.ycsoft.business.dao.core.job.TOsdPhraseDao;
import com.ycsoft.business.dao.core.job.TOsdStopDao;
import com.ycsoft.commons.abstracts.BaseComponent;
import com.ycsoft.commons.exception.ComponentException;
import com.ycsoft.commons.helper.OsdCheckHelper;
import com.ycsoft.commons.helper.StringHelper;
import com.ycsoft.daos.core.Pager;

@Component
public class OsdComponent extends BaseComponent{
	private JCaCommandDao jCaCommandDao;
	private JCaCommandOsdInvalidDao jCaCommandOsdInvalidDao;
	private TOsdPhraseDao tOsdPhraseDao;
	private TOsdStopDao tOsdStopDao;
	private OsdCheckHelper osdChecker = new OsdCheckHelper();

	/**
	 * saveOrUpdate osd phrase.
	 * @param phrase
	 * @throws Exception
	 */
	public void saveOrUpdatePhrase(TOsdPhrase phrase) throws Exception {
		if(StringHelper.isEmpty(phrase.getPid())){
			phrase.setPid(tOsdPhraseDao.findSequence().toString());
			tOsdPhraseDao.save(phrase);
		}else{
			tOsdPhraseDao.update(phrase);
		}
	}
	
	/**
	 * 停止所有OSD.
	 * @param stop
	 * @throws Exception
	 */
	public void stopAll(TOsdStop stop) throws Exception{
		stop.setDone_code("" + getDoneCOde());
		tOsdStopDao.save(stop);
	}
	
	/**
	 * 查询最晚的有效的全部停机的配置.
	 * @throws Exception
	 */
	public TOsdStop queryLatestStop() throws Exception {
		return tOsdStopDao.queryLatestStop();
	}
	
	/**
	 * 取消停止所有OSD.
	 * @param stop
	 * @throws Exception
	 */
	public void cancelStopAll() throws Exception{
		tOsdStopDao.cancelStopAll();
	}
	
	/**
	 * 删除osd phrase.
	 * @param phrase
	 * @throws Exception
	 */
	public void removePhrase(TOsdPhrase phrase) throws Exception {
		if(phrase ==null || StringHelper.isEmpty(phrase.getPid())){
			throw new ComponentException("参数有误.");
		}
		tOsdPhraseDao.remove(phrase.getPid());
	}
	
	/**
	 * 手工 使 OSD 非法。
	 * @param transnum
	 * @throws Exception
	 */
	public void invalidOsd(String transnum)throws Exception {
//		update j_ca_command set is_sent = 'I' 并移入j_ca_command_invalid
		JCaCommand cmd = jCaCommandDao.findByKey(transnum);
		if(cmd==null){//可能被后台调度程序跑掉了 .
			return;
		}
		cmd.setIs_sent("I");
//		String errInfo = osdChecker.LawfulCheck(osdChecker.extractOsdContent(cmd));
		jCaCommandDao.invalidOsd(cmd);
//		cmd.setError_info(errInfo);
		jCaCommandOsdInvalidDao.copyInsert(cmd);
	}
	
	/**
	 * 查询合法OSD词组.
	 * @param start
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public Pager<TOsdPhrase> queryOsdPhrase(Integer start, Integer limit) throws Exception{
		return tOsdPhraseDao.findAll(start, limit);
	}
	
	/**
	 * 查询 当天已发送OSD。
	 * @param start
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public Pager<JCaCommand> querySended(Integer start, Integer limit) throws Exception{
//		select t.* from j_ca_command_day t where t.cmd_type = 'SendOsd'
		return jCaCommandDao.querySended(start,limit);
	}
	
	/**
	 * 查询队列中带发送的OSD.
	 * @param start
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public Pager<JCaCommand> queryQueued(Integer start, Integer limit) throws Exception{
		Pager<JCaCommand> page = jCaCommandDao.queryQueuedOsd(start,limit);
		List<JCaCommand> records = page.getRecords();
		for(JCaCommand ca:records){
			String content = null;
			String result = null;
			try{
				content = osdChecker.extractOsdContent(ca);
				result = osdChecker.LawfulCheck(content);
			}catch (Exception e) {
				result = e.getMessage();
			}
			ca.setError_info(result);
		}
		return page;
	}
	
	
	/**
	 * @param jCaCommandDao the jCaCommandDao to set
	 */
	public void setJCaCommandDao(JCaCommandDao jCaCommandDao) {
		this.jCaCommandDao = jCaCommandDao;
	}
	/**
	 * @param tOsdPhraseDao the tOsdPhraseDao to set
	 */
	public void setTOsdPhraseDao(TOsdPhraseDao tOsdPhraseDao) {
		this.tOsdPhraseDao = tOsdPhraseDao;
	}

	/**
	 * @param jCaCommandOsdInvalidDao the jCaCommandOsdInvalidDao to set
	 */
	public void setJCaCommandOsdInvalidDao( JCaCommandOsdInvalidDao jCaCommandOsdInvalidDao) {
		this.jCaCommandOsdInvalidDao = jCaCommandOsdInvalidDao;
	}

	/**
	 * @param tOsdStopDao the tOsdStopDao to set
	 */
	public void setTOsdStopDao(TOsdStopDao tOsdStopDao) {
		this.tOsdStopDao = tOsdStopDao;
	}

	/**
	 * 查询已经是非法的OSD.从 JCaCommandOsdInvalid 表获取数据.
	 * @param start
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public Pager<JCaCommandOsdInvalid> queryErrorData(Integer start, Integer limit) throws Exception {
		return this.jCaCommandOsdInvalidDao.findAll(start, limit);
	}

}
