package com.tmlabs.springraft.raft;

public class CandidateNode extends Node {
    @Override
    public boolean isLeader() {
        return false;
    }
}
