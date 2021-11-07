package com.tmlabs.springraft.raft;

public class LeaderNode extends Node {
    @Override
    public boolean isLeader() {
        return true;
    }
}
