package com.tmlabs.springraft.raft;

import com.tmlabs.springraft.SystemContext;
import com.tmlabs.springraft.entities.RequestResponse;
import com.tmlabs.springraft.exception.LogAppenderQueueFullException;

import java.util.concurrent.CompletableFuture;

public class LogAppendRequestConsumer implements SingularUpdateQueueConsumer {

    private final SingularUpdateQueue<Message<RequestResponse>, Message<RequestResponse>> logEntriesQueue;
    private final SystemContext systemContext;

    public LogAppendRequestConsumer() {
        systemContext = SystemContext.getInstance();
        logEntriesQueue = new SingularUpdateQueue<>((message) -> {
            //Write the message to the database
            RequestResponse requestResponse = message.getRequestResponse();
            requestResponse.getRequest();
            return message;
        });
        startHandling();
    }

    private void startHandling() {this.logEntriesQueue.start();}

    @Override
    public CompletableFuture<Message<RequestResponse>> accept(Message<RequestResponse> message)
            throws LogAppenderQueueFullException {

        /*CompletableFuture<Message<RequestResponse>> future = logEntriesQueue.submit(message);
        future.whenComplete((responseMessage, error) -> {
            sendResponse(responseMessage);
        });*/
        return logEntriesQueue.submit(message);
    }
}
