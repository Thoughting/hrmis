package com.eastcom.baseframe.common.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 定时任务实现类
 * @author wutingguang <br>
 */
public abstract class TaskSuppport implements Task {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
}
