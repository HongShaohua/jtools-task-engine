package com.hongshaohua.jtools.task;

import java.util.*;

class TaskQueue {

	private static final int LIST_TOP = 0;
	
	private Map<Integer, List<Task>> tasksMap;
	private int size;
	
	public TaskQueue() {
		this.tasksMap = new TreeMap<Integer, List<Task>>();
		this.size = 0;
	}
	
	public synchronized void add(Task task) {
		if(task == null) {
			return;
		}
		List<Task> tasks = this.tasksMap.get(task.level());
		if(tasks == null) {
			tasks = new LinkedList<Task>();
			this.tasksMap.put(task.level(), tasks);
		}
		tasks.add(task);
		this.size++;
	}
	
	public synchronized Task top() {
		Iterator<List<Task>> it = this.tasksMap.values().iterator();
		while(it.hasNext()) {
			List<Task> tasks = it.next();
			if(!tasks.isEmpty()) {
				this.size--;
				return tasks.remove(LIST_TOP);
			}
		}
		return null;
	}
	
	public synchronized boolean isEmpty() {
		return size() == 0 ? true : false;
	}
	
	public synchronized void clear() {
		this.tasksMap.clear();
		this.size = 0;
	}
	
	public synchronized void clear(int level) {
		this.tasksMap.remove(level);
		this.size = this.mapSize();
	}

	private synchronized int mapSize() {
		int size = 0;
		Iterator<List<Task>> it = this.tasksMap.values().iterator();
		while(it.hasNext()) {
			List<Task> tasks = it.next();
			if(tasks != null) {
				size += tasks.size();
			}
		}
		return size;
	}
	
	public synchronized int size() {
		return this.size;
	}
}
