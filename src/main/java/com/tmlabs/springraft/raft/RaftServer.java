package com.tmlabs.springraft.raft;

import com.tmlabs.springraft.SystemContext;

public class RaftServer {

    private final SystemContext systemContext;

    private final Node node;

    public RaftServer() {
        systemContext = SystemContext.getInstance();
        node = systemContext.getNode();
    }

    public boolean isLeader() {
        return node.isLeader();
    }
}
