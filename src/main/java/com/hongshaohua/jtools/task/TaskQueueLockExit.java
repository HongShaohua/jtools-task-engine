package com.hongshaohua.jtools.task;

/**
 * Created by shaoh on 2017/5/11.
 */
public class TaskQueueLockExit {

    private TaskQueue taskQueue;

    private final Object exitLockObj = new Object();
    private boolean exit = false;

    public TaskQueueLockExit() {
        this.taskQueue = new TaskQueue();
    }

    public Task top() {
        return this.taskQueue.top();
    }

    public void add(Task task) {
        if(task == null) {
            return;
        }
        this.taskQueue.add(task);
    }

    public void exit() {
        synchronized (exitLockObj) {
            this.exit = true;
        }
    }

    public boolean isExit() {
        boolean exit = false;
        synchronized (exitLockObj) {
            exit = this.exit;
        }
        return exit;
    }

    public int size() {
        return this.taskQueue.size();
    }

    public boolean isEmpty() {
        return this.taskQueue.isEmpty();
    }
}
