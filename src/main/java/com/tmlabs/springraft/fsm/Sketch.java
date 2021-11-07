package com.tmlabs.springraft.fsm;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.tmlabs.springraft.Constants;
import com.tmlabs.springraft.SystemContext;
import com.tmlabs.springraft.entities.RequestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;


public class Sketch {

    private static final Logger logger = LoggerFactory.getLogger(Sketch.class);

    private Node head;
    private Node headless;
    private final String name;
    private final String title;

    private final CommandProcessor commandProcessor;

    private final SystemContext systemContext;
    private ObjectMapper om = new ObjectMapper();

    //private Map<String, ActivitiesAPIBean.FSMTransitionAPIBean> stateToTransitionsMap;

    public Sketch(String sketchName, String title, CommandProcessor commandProcessor) {
        name = sketchName;
        this.title = title;
        systemContext = SystemContext.getInstance();
        this.commandProcessor = commandProcessor;
    }

    public void init(Map<String, State> nameToStateMap) {

        State startState = nameToStateMap.get(Constants.START_STATE);
        Node startNode = new Node();
        startNode.setState(startState);
        headless = startNode;
        headless.setStatus(Constants.STATUS.BEGIN);

        recurseInit(nameToStateMap, startNode, startState);
    }



    public Sketch load() throws StateManagerException {
        heads(Constants.STATUS.BEGIN);
        return this;
    }

    public Object start(Object payload) throws StateManagerException {

        RequestResponse requestResponse = new RequestResponse();
        requestResponse.setRequest(payload);

        if(head == null) {
            throw new StateManagerException(
                    null,
                    StateManagerException.ERROR_CODE.HEAD_NOT_INITIALIZED,
                    "Sketch is not materialized. Please run statemachine.materialize() and then call sketch.load() before calling start()"
            );
        }

        try {
            next(head, requestResponse);
        } catch (StateManagerException e) {
            Object response = requestResponse.getResponse();
            if(response == null) {
                throw e;
            }

        }
        return requestResponse.getResponse();
    }

    public Object restart(Object payload) throws StateManagerException {

        if(head == null) {
            throw new StateManagerException(
                    null,
                    StateManagerException.ERROR_CODE.HEAD_NOT_INITIALIZED,
                    "Sketch is not materialized. Please run statemachine.materialize() and then call sketch.load() before calling start()"
            );
        }

        RequestResponse requestResponse = new RequestResponse();
        requestResponse.setRequest(payload);



        try {
            next(head, requestResponse);
        } catch (StateManagerException e) {
            Object responseObject = requestResponse.getResponse();
            if(responseObject != null) {
                return responseObject;
            }
            throw e;
        }
        return requestResponse.getResponse();
    }

    private RequestResponse next(Node node, RequestResponse requestResponse) throws StateManagerException {

        if(node == null) {
            //reached the end?
            done();
            return requestResponse;
        }

        //If the node is async then wait for if and restart the FSM
        if(!node.processCommand
                && node.getState().isAsync()) {
            node.asyncProcessing();
            return requestResponse;
        }

        State state = node.getState();
        //state.setActivityId(currentActivity.getId());
        Command command = state.getCommand();

        node.setStartTime(System.currentTimeMillis());
        node.setPayload(requestResponse);

        try {
            RequestResponse returnedReqRes = commandProcessor.process(command, state, requestResponse);
            node.success();

            Object response = returnedReqRes.getResponse();
            if(response != null) {
                return returnedReqRes;
            }
            return next(node.next, returnedReqRes);

        }
        catch (StateManagerException e) {
            throw e;
        }
        catch (Exception e) {
            node.setEndTime(System.currentTimeMillis());
            node.setStatus(Constants.STATUS.FAILURE);

            String reason = String.format("Failed to process the command %s on state %s for FSM %s due to ->>>>>>>>- %s", command.getName(), state.getName(), name, e.getMessage());
            node.error(reason);
            error(reason);
            throw new StateManagerException(
                    e,
                    StateManagerException.ERROR_CODE.COMMAND_PROCESSING_FAILED,
                    reason
            );
        }

    }

    private void done() throws StateManagerException {

    }

    private void error(String errorMsg) throws StateManagerException {

    }

    //Setting the head
    private void heads(Constants.STATUS nodeStatusToStartFrom) throws StateManagerException {
        if(headless == null) {
            throw new StateManagerException(
                    null,
                    StateManagerException.ERROR_CODE.HEAD_NOT_INITIALIZED,
                    "Sketch is not initialized. Please run sketch.init()"
            );
        }

        Node node = headless;
        while(node != null) {
            if(node.getStatus() == nodeStatusToStartFrom) {
                head = node;
                break;
            }
            node = node.getNext();
        }

        if(head == null) {
            throw new StateManagerException(
                    null,
                    StateManagerException.ERROR_CODE.HEAD_NOT_INITIALIZED,
                    "Sketch is not initialized. Please run sketch.init()"
            );
        }
        head.setProcessCommand(true);
    }







    private String getCurrentTimeAsStr() {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        return  format.format(new Date(System.currentTimeMillis()));
    }



    private void recurseInit(Map<String, State> nameToStateMap, Node node, State state) {

        if(state.getNextState().equals(Constants.DONE_STATE)) {
            return;
        }

        State nextState = nameToStateMap.get(state.getNextState());
        Node nextNode = new Node();
        nextNode.setState(nextState);

        node.setNext(nextNode);

        recurseInit(nameToStateMap, nextNode, nextState);
    }

    public class Node {

        private State state;
        private boolean processCommand = true;
        private Node next;

        private long startTime;
        private long endTime;
        private String message;

        private Object payload;

        private Constants.STATUS status = Constants.STATUS.YET_TO_START;

        public void setState(State state) {
            if(state.isAsync()) {
                processCommand = false;
            }
            this.state = state;
        }

        public void success() throws StateManagerException {
            endTime = System.currentTimeMillis();
            status = Constants.STATUS.SUCCESS;
            message = "Completed Successfully";
        }

        public void asyncProcessing() throws StateManagerException {
            status = Constants.STATUS.PROCESSING;
            message = "Processing ...";
        }

        public void error(String errorMsg) throws StateManagerException {
            endTime = System.currentTimeMillis();
            status = Constants.STATUS.FAILURE;
            message = errorMsg;
        }



        private String getTimeAsStr(long time) {

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            return  format.format(new Date(time));
        }

        public State getState() {
            return state;
        }

        public boolean isProcessCommand() {
            return processCommand;
        }

        public void setProcessCommand(boolean processCommand) {
            this.processCommand = processCommand;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public long getEndTime() {
            return endTime;
        }

        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Object getPayload() {
            return payload;
        }

        public void setPayload(Object payload) {
            this.payload = payload;
        }

        public Constants.STATUS getStatus() {
            return status;
        }

        public void setStatus(Constants.STATUS status) {
            this.status = status;
        }
    }


}
