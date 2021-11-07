package com.tmlabs.springraft.bootstrap;

import com.tmlabs.springraft.SystemContext;
import com.tmlabs.springraft.services.ElectionTermService;
import com.tmlabs.springraft.services.LogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class RaftBootstrap implements Bootstrap {

    @Autowired
    private Environment env;

    @Autowired
    LogService logService;

    @Autowired
    ElectionTermService electionTermService;

    @Override
    public void setup() throws Exception {
        SystemContext systemContext = SystemContext.getInstance();

        systemContext.setLogService(logService);

        systemContext.setNodeName(env.getProperty("raft.node.name"));

        String peersStr = env.getProperty("raft.peers");
        String[] peers = peersStr.split(",");
        systemContext.setPeers(List.of(peers));

        systemContext.setRole(env.getProperty("raft.role"));

        systemContext.initializeFSM("fsms/client_execute_command_request.yaml");

        electionTermService.init();

        systemContext.initRaftServer();

        log.info("Raft Node started!!");
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            setup();
        }
        catch (Exception e) {
            log.error("Failed to start Raft Node >> ", e);
            System.exit(-1);
        }
    }
}
