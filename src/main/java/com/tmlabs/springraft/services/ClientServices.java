package com.tmlabs.springraft.services;

import com.tmlabs.springraft.Constants;
import com.tmlabs.springraft.SystemContext;
import com.tmlabs.springraft.entities.ClientCommandRequest;
import com.tmlabs.springraft.entities.ClientCommandResponse;
import com.tmlabs.springraft.fsm.StateManagerException;
import org.springframework.stereotype.Service;

@Service
public class ClientServices {

    private final SystemContext systemContext;

    public ClientServices() {
        systemContext = SystemContext.getInstance();
    }

    public ClientCommandResponse executeCommand(ClientCommandRequest clientCommandRequest) throws StateManagerException {

        return (ClientCommandResponse)systemContext.getFSM("CLIENT_EXECUTE_COMMAND_REQUEST")
                .materialize()
                .load()
                .start(clientCommandRequest);

    }
}
