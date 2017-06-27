package com.hongshaohua.jtools.task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskEngine {

	private static final int DEFAULT_LIMIT = 10000;
	private int threads = 0;
	private int limit = 0;

	private ExecutorService pool;
	private boolean isStarted = false;
	
	private TaskQueueLocker taskQueueLocker;
	private TaskHandler taskHandler;

	
	public TaskEngine() {
		this(DEFAULT_LIMIT);
	}

	public TaskEngine(int limit) {
		this(Runtime.getRuntime().availableProcessors()*2, limit);
	}

	public TaskEngine(int threads, int limit) {
		this.threads = threads;
		this.limit = limit;
	}
	
	public synchronized void start() throws Exception {
		if(!this.isStarted) {
			this.isStarted = true;
			this.pool = Executors.newFixedThreadPool(this.threads);
			this.taskQueueLocker = new TaskQueueLocker(this.limit);
			this.taskHandler = new TaskHandler(taskQueueLocker);
			for(int i = 0; i < this.threads; i++) {
				this.pool.execute(this.taskHandler);
			}
		}
	}
	
	public synchronized void shutdown() {
		if(this.isStarted) {
			this.taskQueueLocker.exit();
			this.pool.shutdown();
			this.taskQueueLocker = null;
			this.taskHandler = null;
			this.pool = null;
			this.isStarted = false;
		}
	}
	
	public synchronized void post(Task task) {
		this.taskQueueLocker.add(task);
	}
	
	public synchronized int taskCount() {
		return this.taskQueueLocker.size();
	}
}
