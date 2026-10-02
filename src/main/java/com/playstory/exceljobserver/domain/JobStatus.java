package com.playstory.exceljobserver.domain;

public enum JobStatus {
    PENDING, PROCESSING, DONE, FAILED;

    public String value() {
        return name().toLowerCase();
    }
}
