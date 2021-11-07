package com.tmlabs.springraft.services;

import com.tmlabs.springraft.Constants;
import com.tmlabs.springraft.SystemContext;
import com.tmlabs.springraft.entities.ClientCommandResponse;
import com.tmlabs.springraft.entities.RequestResponse;
import com.tmlabs.springraft.fsm.Command;
import com.tmlabs.springraft.fsm.CommandProcessor;
import com.tmlabs.springraft.fsm.State;
import com.tmlabs.springraft.fsm.StateManagerException;
import com.tmlabs.springraft.raft.RaftServer;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

import static com.tmlabs.springraft.fsm.StateManagerException.ERROR_CODE.COMMAND_PROCESSING_FAILED;
import static com.tmlabs.springraft.fsm.StateManagerException.ERROR_CODE.UNHANDLED_CASE;

@Slf4j
public class ClientExecuteCommandRequestProcessor implements CommandProcessor {

    private final SystemContext systemContext;

    public ClientExecuteCommandRequestProcessor() {
        systemContext = SystemContext.getInstance();
    }

    @Override
    public RequestResponse process(Command command, State state, RequestResponse payload) throws StateManagerException {


        log.info(String.format("Received command %s on state %s", command.getName(), state.getName()));


        Objects.requireNonNull(payload, "Payload is mandatory and cannot be null or empty");

        if(state.getName().equals("START")
                && command.getName().equals("INIT_CLIENT_EXECUTE_COMMAND_REQUEST")) {
            return processInitCliExecComReq(payload);
        }

        if(state.getName().equals("LEADER_CHECK")
                && command.getName().equals("CHECK_LEADER")) {
            return processCheckLeader(payload);
        }

        if(state.getName().equals("LOG_LOCAL_APPEND")
                && command.getName().equals("APPEND_LOG_LOCAL")) {
            return processAppendLogLocal(payload);
        }

        throw new StateManagerException(
                null,
                UNHANDLED_CASE,
                ""
        );
    }


    private RequestResponse processInitCliExecComReq(RequestResponse payload) throws StateManagerException {

        log.info("Inside processInitCliExecComReq >>>>>>>>>> ");

        return payload;
    }


    private RequestResponse processCheckLeader(RequestResponse payload) throws StateManagerException {

        log.info("Inside processCheckLeader >>>>>>>>>> ");

        if(!systemContext.raftServer().isLeader()) {
            ClientCommandResponse clientCommandResponse = new ClientCommandResponse();
            clientCommandResponse.setStatus(Constants.STATUS.FAILURE.name());
            clientCommandResponse.setMsg(Constants.NOT_A_LEADER_ERROR_MSG);
            payload.setResponse(clientCommandResponse);
        }
        return payload;
    }


    private RequestResponse processAppendLogLocal(RequestResponse payload) throws StateManagerException {

        log.info("Inside processAppendLogLocal >>>>>>>>>> ");

        return payload;
    }





}
