package com.playstory.exceljobserver.job;

public enum JobStatus {
    PENDING, PROCESSING, DONE, FAILED;

    public String value() {
        return name().toLowerCase();
    }
}
