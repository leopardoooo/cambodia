/*
 * @(#) JobProxy.java 1.0.0 Aug 1, 2011 4:53:47 PM
 *
 * Copyright 2011 YaoChen, Ltd. All rights reserved.
 * YaoChen PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.yaochen.boss.commons;

import com.yaochen.myquartz.Job2;

/**
 * 
 *
 * @author allex
 * @since 1.0
 */
public class JobProxy {
	
	public Job2 targetJob;

	public void setTargetJob(Job2 targetJob) {
		this.targetJob = targetJob;
	}

}
