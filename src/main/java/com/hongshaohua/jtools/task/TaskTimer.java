package com.hongshaohua.jtools.task;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class TaskTimer {
	
	private Timer timer;
	
	private TaskEngine taskEngine;

	private Task task;
	
	private long delay;
	
	private Date firstTime;
	
	private long period;
	
	private TimerTask timerTask;
	
	private boolean isRunning;
	private boolean isCancelled;
	private boolean isExpired;
	
	private TaskTimer(Timer timer, TaskEngine taskEngine, Task task, long delay, TimeUnit delayTimeUnit, Date firstTime, long period, TimeUnit periodTimeUnit) {
		this.timer = timer;
		this.taskEngine = taskEngine;
		this.task = task;
		this.delay = delayTimeUnit.toMillis(delay);
		this.firstTime = firstTime;
		this.period = periodTimeUnit.toMillis(period);
	}
	
	public TaskTimer(Timer timer, TaskEngine taskEngine, Task task, long delay, TimeUnit delayTimeUnit, long period, TimeUnit periodTimeUnit) {
		this(timer, taskEngine, task, delay, delayTimeUnit, null, period, periodTimeUnit);
	}
	
	public TaskTimer(Timer timer, TaskEngine taskEngine, Task task, long delay, TimeUnit delayTimeUnit) {
		this(timer, taskEngine, task, delay, delayTimeUnit, 0, TimeUnit.MILLISECONDS);
	}

	public TaskTimer(Timer timer, TaskEngine taskEngine, Task task, Date firstTime, long period, TimeUnit periodTimeUnit) {
		this(timer, taskEngine, task, 0, TimeUnit.MILLISECONDS, firstTime, period, periodTimeUnit);
	}
	
	public TaskTimer(Timer timer, TaskEngine taskEngine, Task task, Date firstTime) {
		this(timer, taskEngine, task, firstTime, 0, TimeUnit.MILLISECONDS);
	}
	
	public synchronized boolean start() {
		if(this.isRunning()) {
			return false;
		}
		this.timerTask = new TaskTimerTask();
		if(this.firstTime == null) {
			if(this.period > 0) {
				this.timer.schedule(this.timerTask, this.delay, this.period);
			} else {
				this.timer.schedule(this.timerTask, this.delay);
			}
		} else {
			if(this.period > 0) {
				this.timer.schedule(this.timerTask, this.firstTime, this.period);
			} else {
				this.timer.schedule(this.timerTask, this.firstTime);
			}
		}
		this.isRunning = true;
		this.isCancelled = false;
		this.isExpired = false;
		return true;
	}
	
	public synchronized boolean cancel() {
		if(!this.isRunning()) {
			return false;
		}
		if(!this.timerTask.cancel()) {
			return false;
		}
		this.isRunning = false;
		this.isCancelled = true;
		return true;
	}
	
	public synchronized boolean restart() {
		this.cancel();
		return this.start();
	}
	
	public synchronized boolean isRunning() {
		return this.isRunning;
	}
	
	public synchronized boolean isCancelled() {
		return this.isCancelled;
	}
	
	public synchronized boolean isExpired() {
		return this.isExpired;
	}
	
	private class TaskTimerTask extends TimerTask {
		@Override
		public void run() {
			TaskTimer.this.timeout();
		}
	}
	
	private synchronized void timeout() {
		this.isExpired = true;
		if(this.period <= 0) {
			this.isRunning = false;
		}
		this.taskEngine.post(this.task);
	}
}
