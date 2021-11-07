package com.tmlabs.springraft.raft;

import lombok.Data;

@Data
public class Message<T> {
    private T requestResponse;
}
