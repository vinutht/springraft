package com.tmlabs.springraft.raft;

import com.tmlabs.springraft.exception.LogAppenderQueueFullException;

import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class SingularUpdateQueue<Req, Res> extends Thread {

    private ArrayBlockingQueue<RequestWrapper<Req, Res>> workQueue
            = new ArrayBlockingQueue<RequestWrapper<Req, Res>>(100);

    private final Function<Req, Res> handler;
    private volatile boolean isRunning = false;

    public SingularUpdateQueue(Function<Req, Res> handler) {
        this.handler = handler;
    }

    public void run() {
        isRunning = true;
        while(isRunning) {
            Optional<RequestWrapper<Req, Res>> item = take();
            item.ifPresent(requestWrapper -> {
                try {
                    Res response = handler.apply(requestWrapper.getRequest());
                    requestWrapper.complete(response);

                } catch (Exception e) {
                    requestWrapper.completeExceptionally(e);
                }
            });
        }
    }

    private Optional<RequestWrapper<Req, Res>> take() {
        try {
            return Optional.ofNullable(workQueue.poll(300, TimeUnit.MILLISECONDS));

        } catch (InterruptedException e) {
            return Optional.empty();
        }
    }


    public CompletableFuture<Res> submit(Req req) throws LogAppenderQueueFullException {
        RequestWrapper requestWrapper = new RequestWrapper(req);
        boolean insertSuccess = workQueue.offer(requestWrapper);
        if(!insertSuccess) {
            throw new LogAppenderQueueFullException();
        }
        return requestWrapper.getFuture();
    }

    public void shutdown() {
        this.isRunning = false;
    }

    class RequestWrapper<Req, Res> {

        private final CompletableFuture<Res> future;
        private final Req request;

        public RequestWrapper(Req request) {
            this.request = request;
            this.future = new CompletableFuture<Res>();
        }

        public CompletableFuture<Res> getFuture() {
            return future;
        }
        public Req getRequest() {return request;}

        public void complete(Res response) {
            future.complete(response);
        }
        public void completeExceptionally(Exception e) {
            future.completeExceptionally(e);
        }

    }
}
