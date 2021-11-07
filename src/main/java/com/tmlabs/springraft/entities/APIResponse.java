package com.tmlabs.springraft.entities;

import java.util.Date;
import java.util.List;


public class APIResponse {

    private final Meta metadata;
    private List<Object> data;
    private int count;

    public enum STATUS {
        SUCCESS, FAILURE;
    }

    public APIResponse() {
        metadata = new Meta();
        metadata.setTimestamp((new Date()).toString());
    }

    public Meta getMetadata() {
        return metadata;
    }

    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
        if(data != null) {
            count = data.size();
        }
    }

    public void setServerBusy(boolean serverBusy) {
        metadata.serverBusy = serverBusy;
    }


    public void setTraceId(String traceId) {
        metadata.setTraceId(traceId);
    }


    public void setStatus(STATUS status) {
        metadata.setStatus(status);
    }


    public void setTitle(String title) {
        metadata.setTitle(title);
    }


    public void setDetails(String details) {
        metadata.setDetails(details);
    }


    public int getCount() {
        return count;
    }

    public void setTimestamp(String timestamp) {
        metadata.setTimestamp(timestamp);
    }

    public void setReason(String reason) {
        metadata.setReason(reason);
    }

    public void setExecutionTime(long executionTime) {
        metadata.setExecutionTime(executionTime);
    }

    public static class Meta {
        private String traceId;
        private STATUS status = STATUS.SUCCESS;
        private String title;
        private String reason;
        private String details;
        private String timestamp;
        private long executionTime;
        private boolean serverBusy = false;

        public String getTraceId() {
            return traceId;
        }

        public void setTraceId(String traceId) {
            this.traceId = traceId;
        }

        public STATUS getStatus() {
            return status;
        }

        public void setStatus(STATUS status) {
            this.status = status;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDetails() {
            return details;
        }

        public void setDetails(String details) {
            this.details = details;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public long getExecutionTime() {
            return executionTime;
        }

        public void setExecutionTime(long executionTime) {
            this.executionTime = executionTime;
        }

        public boolean isServerBusy() {
            return serverBusy;
        }

        public void setServerBusy(boolean serverBusy) {
            this.serverBusy = serverBusy;
        }
    }
}
