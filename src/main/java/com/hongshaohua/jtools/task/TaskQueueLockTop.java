package com.hongshaohua.jtools.task;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by shaoh on 2017/5/11.
 */
public class TaskQueueLockTop extends TaskQueueLockExit {

    private final Object topLockObj = new Object();
    private final Lock topLock = new ReentrantLock();
    private final Condition topCondition = topLock.newCondition();

    public TaskQueueLockTop() {
        super();
    }

    @Override
    public Task top() {
        Task task = null;
        synchronized (topLockObj) {
            try {
                this.topLock.lock();
                while(this.isEmpty()) {
                    this.topCondition.await();
                    if(this.isExit()) {
                        return null;
                    }
                }
                if(this.isExit()) {
                    return null;
                }
                task = super.top();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                this.topLock.unlock();
            }
        }
        return task;
    }

    private void topLockSignal() {
        try {
            this.topLock.lock();
            this.topCondition.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.topLock.unlock();
        }
    }

    public void add(Task task) {
        if(task == null) {
            return;
        }
        super.add(task);
        this.topLockSignal();
    }

    @Override
    public void exit() {
        super.exit();
        this.topLockSignal();
    }
}
