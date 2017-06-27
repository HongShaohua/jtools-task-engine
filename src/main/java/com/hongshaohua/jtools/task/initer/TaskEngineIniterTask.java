package com.hongshaohua.jtools.task.initer;

import com.hongshaohua.jtools.common.initer.IniterTaskTemplate;
import com.hongshaohua.jtools.task.TaskEngine;

/**
 * Created by Aska on 2017/6/22.
 */
public abstract class TaskEngineIniterTask extends IniterTaskTemplate<TaskEngine> {

    private int threads = 0;
    private int limit = 0;

    public TaskEngineIniterTask() {
    }

    public TaskEngineIniterTask(int limit) {
        this.limit = limit;
    }

    public TaskEngineIniterTask(int threads, int limit) {
        this.threads = threads;
        this.limit = limit;
    }

    @Override
    public void init() throws Exception {
        if(this.threads > 0 && this.limit > 0) {
            this.finish(new TaskEngine(this.threads, this.limit));
        } else if(this.limit > 0) {
            this.finish(new TaskEngine(this.limit));
        } else {
            this.finish(new TaskEngine());
        }
    }
}
