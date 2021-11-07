package com.tmlabs.springraft.apis;

import com.tmlabs.springraft.Constants;
import com.tmlabs.springraft.SystemContext;
import com.tmlabs.springraft.entities.APIResponse;
import com.tmlabs.springraft.entities.ClientCommandRequest;
import com.tmlabs.springraft.entities.ClientCommandResponse;
import com.tmlabs.springraft.fsm.StateManagerException;
import com.tmlabs.springraft.services.ClientServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/raft/client/v1")
public class ClientController {

    @Autowired
    ClientServices clientServices;

    private final SystemContext systemContext;

    public ClientController() {
        systemContext = SystemContext.getInstance();
    }

    @PostMapping(
            value = "/execute-command",
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    public ResponseEntity<APIResponse> executeClientCommand(
            @RequestBody ClientCommandRequest clientCommandRequest
    ) {
        long startTime = System.currentTimeMillis();
        if(log.isInfoEnabled()) {
            log.info("ClientController.executeClientCommand");
        }
        APIResponse response = new APIResponse();
        response.setTitle("Execute Command");
        List data = new ArrayList<>();
        response.setData(data);
        try {

            ClientCommandResponse clientCommandResponse = clientServices.executeCommand(clientCommandRequest);
            if(clientCommandResponse != null) {
                data.add(clientCommandResponse);
            }

        } catch (StateManagerException e) {
            log.error("Failed to execute client command", e);
            response.setStatus(APIResponse.STATUS.FAILURE);
            response.setReason(Constants.INTERNAL_SERVER_ERROR_MESSAGE);
            response.setDetails(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        }

        long totalTime = System.currentTimeMillis() - startTime;
        response.setExecutionTime(totalTime);
        if(log.isDebugEnabled()) {
            log.debug(String.format("Time taken in millis -> ClientController.executeClientCommand: %s", totalTime));
        }

        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
