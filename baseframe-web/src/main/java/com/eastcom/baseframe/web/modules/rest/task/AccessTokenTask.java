package com.eastcom.baseframe.web.modules.rest.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.eastcom.baseframe.common.task.Task;
import com.eastcom.baseframe.common.task.TaskSuppport;
import com.eastcom.baseframe.web.modules.rest.entity.AccessToken;

/**
 * 监控access token失效
 * @author wutingguang <br>
 */
@Component
public class AccessTokenTask extends TaskSuppport implements Task{

	@Override
	@Scheduled(cron="* * * * * ? ")
	public void execute() {
		AccessToken.refresh();
	}

}
