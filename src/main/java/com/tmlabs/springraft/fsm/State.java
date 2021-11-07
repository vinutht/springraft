package com.tmlabs.springraft.fsm;


public class State {

    private String name;
    private String title;
    private String nextState;
    private Command command;
    private String fsmLink;

    //Is the transition to this state asynchronous?
    private boolean async = false;

    //The running activity this state belongs to
    private String activityId;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNextState() {
        return nextState;
    }

    public void setNextState(String nextState) {
        this.nextState = nextState;
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public String getFsmLink() {
        return fsmLink;
    }

    public void setFsmLink(String fsmLink) {
        this.fsmLink = fsmLink;
    }

    public boolean isAsync() {
        return async;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }
}
