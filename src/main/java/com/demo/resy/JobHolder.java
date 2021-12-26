package com.demo.resy;

public final class JobHolder {

    private final static JobHolder INSTANCE = new JobHolder();
    private Job job;

    private JobHolder() {}

    public static JobHolder getInstance() {
        return INSTANCE;
    }

    public Job getJob() {
        return this.job;
    }

    public void setJob(Job j) {
        this.job = j;
    }
}
