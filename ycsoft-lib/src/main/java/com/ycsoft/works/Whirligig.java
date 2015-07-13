package com.ycsoft.works;

import java.util.List;

import com.ycsoft.beans.core.job.JSignal;
import com.ycsoft.business.component.config.MemoryComponent;
import com.ycsoft.business.dao.core.job.JSignalDao;

public class Whirligig {
	private static String loadLastId = null;
	private JSignalDao jSignalDao;
	private MemoryComponent memoryComponent;

	@SuppressWarnings("unused")
	private void synchronize() {
		try {
			SyncSystemParmer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void SyncSystemParmer() throws Exception {
		// init
		if (loadLastId == null) {
			loadLastId = jSignalDao.loadLastId();
		}
		// setup
		List<JSignal> datas = jSignalDao.signalDatas(loadLastId);
		for (JSignal d : datas) {
			loadLastId = d.getSignal_id();
			setupData(d);
		}
	}

	private void setupData(JSignal d) throws Exception {
		if (d.getSignal_type().equals("M")) {
			System.out.println("加载dict");
			memoryComponent.setupMemoryDict(d);
		} else if (d.getSignal_type().equals("T")) {
			System.out.println("加载template");
			memoryComponent.setupMemoryTemplate();
		} else if (d.getSignal_type().equals("P")) {
			System.out.println("加载print");
			memoryComponent.setupMemoryPrintData();
		}
	}


	/**
	 * @param signalDao the jSignalDao to set
	 */
	public void setJSignalDao(JSignalDao signalDao) {
		jSignalDao = signalDao;
	}

	/**
	 * @param memoryComponent the memoryComponent to set
	 */
	public void setMemoryComponent(MemoryComponent memoryComponent) {
		this.memoryComponent = memoryComponent;
	}
}
