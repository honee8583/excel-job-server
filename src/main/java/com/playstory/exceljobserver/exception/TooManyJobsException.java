package com.playstory.exceljobserver.exception;

public class TooManyJobsException extends RuntimeException {

    public TooManyJobsException() {
        super("대기 중인 작업이 많습니다. 잠시 후 다시 시도해 주세요.");
    }
}
