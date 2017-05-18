package com.hongshaohua.jtools.task;

public abstract class Task {

	private int level;
	
	public Task(int level) {
		this.level = level;
	}
	
	final int level() {
		return this.level;
	}

	public abstract void execute() throws Exception;
	
}
