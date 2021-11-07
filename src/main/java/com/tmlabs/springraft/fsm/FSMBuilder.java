package com.tmlabs.springraft.fsm;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FSMBuilder {

    private static Logger logger = LoggerFactory.getLogger(FSMBuilder.class);
    private List<StateMachine> stateMachines;

    public List<StateMachine> getStateMachines() {
        return stateMachines;
    }

    public void setStateMachines(List<StateMachine> stateMachines) {
        this.stateMachines = stateMachines;
    }

    private Map<String, StateMachine> fsmMap() {
        Map<String, StateMachine> fsmMap = new HashMap<>();

        for(int i=0; i<stateMachines.size(); i++) {
            StateMachine stateMachine = stateMachines.get(i);
            fsmMap.put(stateMachine.getName(), stateMachine);
        }

        return fsmMap;
    }


    public static Map<String, StateMachine> initializeFSM(String fsmFile) throws StateManagerException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        //Resource resource = new ClassPathResource("fsms/client_execute_command_request.yaml");
        InputStream fsmStream = FSMBuilder.class.getClassLoader().getResourceAsStream(fsmFile);

        try {
            FSMBuilder stateMachineBuilder = mapper.readValue(fsmStream, FSMBuilder.class);

            if(logger.isInfoEnabled()) {
                logger.info(String.format("Successfully parsed %s", fsmFile));
            }

            return stateMachineBuilder.fsmMap();

        } catch (IOException e) {
            if(logger.isErrorEnabled()) {
                logger.error(e.getMessage());
            }
            throw new StateManagerException(
                    e,
                    StateManagerException.ERROR_CODE.FSM_PARSE_FAILURE,
                    String.format("The FSM file name %s", fsmFile)
            );
        }
    }
}
