package com.tmlabs.springraft.entities;

import lombok.Data;

@Data
public class ClientCommandRequest {
    private String command;
    private String uuid;
}
