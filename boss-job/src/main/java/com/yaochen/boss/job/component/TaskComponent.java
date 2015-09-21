package com.yaochen.boss.job.component;

import org.springframework.beans.factory.annotation.Autowired;

import com.ycsoft.business.dao.task.WTaskBaseInfoDao;
import com.ycsoft.business.dao.task.WTaskLogDao;
import com.ycsoft.business.dao.task.WTaskUserDao;
import com.ycsoft.commons.abstracts.BaseComponent;

public class TaskComponent extends BaseComponent {
	@Autowired
	private WTaskBaseInfoDao wTaskBaseInfoDao;
	@Autowired
	private WTaskUserDao wTaskUserDao;
	@Autowired
	private WTaskLogDao wTaskLogDao;
}
