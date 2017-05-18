package com.hongshaohua.jtools.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class TaskHandler implements Runnable {
	
	private static final Logger logger = LoggerFactory.getLogger(TaskHandler.class);
	
	private TaskQueueLocker taskQueueLocker;
	
	public TaskHandler(TaskQueueLocker taskQueueLocker) {
		this.taskQueueLocker = taskQueueLocker;
	}
	
	public void run() {
		while(true) {
			Task task = this.taskQueueLocker.top();
			if(this.taskQueueLocker.isExit()) {
				break;
			}
			if(task != null) {
				try {
					task.execute();
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
	}
}
