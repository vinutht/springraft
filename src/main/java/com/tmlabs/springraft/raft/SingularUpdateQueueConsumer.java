package com.tmlabs.springraft.raft;

import com.tmlabs.springraft.entities.RequestResponse;
import com.tmlabs.springraft.exception.LogAppenderQueueFullException;

import java.util.concurrent.CompletableFuture;

public interface SingularUpdateQueueConsumer {

    public CompletableFuture<Message<RequestResponse>> accept(Message<RequestResponse> message) throws LogAppenderQueueFullException;
}
