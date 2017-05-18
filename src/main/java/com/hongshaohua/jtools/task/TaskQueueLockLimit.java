package com.hongshaohua.jtools.task;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by shaoh on 2017/5/11.
 */
public class TaskQueueLockLimit extends TaskQueueLockTop {

    private int limit;

    private final Object addLockObj = new Object();
    private final Lock addLock = new ReentrantLock();
    private final Condition addCondition = addLock.newCondition();

    public TaskQueueLockLimit(int limit) {
        super();
        this.limit = limit;
    }

    public int limit() {
        return limit;
    }

    @Override
    public void add(Task task) {
        if(task == null) {
            return;
        }
        synchronized (addLockObj) {
            try {
                this.addLock.lock();
                while(this.size() >= this.limit) {
                    System.out.println("addCondition await " + Thread.currentThread().getId());
                    this.addCondition.await();
                    System.out.println("addCondition await " + Thread.currentThread().getId() + " finished");
                    if(this.isExit()) {
                        return;
                    }
                }
                if(this.isExit()) {
                    return;
                }
                super.add(task);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                this.addLock.unlock();
            }
        }
    }

    private void addLockSignal() {
        try {
            this.addLock.lock();
            this.addCondition.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.addLock.unlock();
        }
    }

    @Override
    public Task top() {
        Task task = super.top();
        this.addLockSignal();
        return task;
    }

    @Override
    public void exit() {
        super.exit();
        this.addLockSignal();
    }
}
