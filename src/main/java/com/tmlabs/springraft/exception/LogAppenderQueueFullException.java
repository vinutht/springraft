package com.tmlabs.springraft.exception;

public class LogAppenderQueueFullException extends RaftException {

    private static final String WORK_QUEUE_FULL_ERROR_MSG = "Server busy. The log appender queue is full. Please try after sometime.";
    public LogAppenderQueueFullException(String message) {
        super(message);
    }

    public LogAppenderQueueFullException() {
        super(WORK_QUEUE_FULL_ERROR_MSG);
    }
}
