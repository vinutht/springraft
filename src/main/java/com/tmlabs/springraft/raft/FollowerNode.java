package com.tmlabs.springraft.raft;

public class FollowerNode extends Node {
    @Override
    public boolean isLeader() {
        return false;
    }
}
