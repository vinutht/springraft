package com.tmlabs.springraft.fsm;

import com.tmlabs.springraft.entities.RequestResponse;

public interface CommandProcessor {

    public RequestResponse process(Command command, State state, RequestResponse payload) throws StateManagerException;
}
