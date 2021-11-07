package com.tmlabs.springraft;

import com.tmlabs.springraft.fsm.FSMBuilder;
import com.tmlabs.springraft.fsm.StateMachine;
import com.tmlabs.springraft.fsm.StateManagerException;
import com.tmlabs.springraft.raft.*;
import com.tmlabs.springraft.services.LogService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SystemContext {

    private static SystemContext instance;

    private String role;
    private List<String> peers;
    private String nodeName;

    private Map<String, StateMachine> fsmMap = new HashMap();

    private RaftServer raftServer;
    private LogService logService;

    private SystemContext() {
        if (instance != null) {
            throw new RuntimeException("Use getInstance() method to get the singleton instance of this class.");
        }
    }

    public static synchronized SystemContext getInstance() {
        if(instance == null) {
            instance = new SystemContext();
        }
        return instance;
    }

    public static void setInstance(SystemContext instance) {
        SystemContext.instance = instance;
    }

    public String getRole() {
        return role;
    }

    public Node getNode() {
        if(Constants.ROLE_LEADER.equals(role)) {
            return new LeaderNode();
        }

        if(Constants.ROLE_FOLLOWER.equals(role)) {
            return new FollowerNode();
        }

        if(Constants.ROLE_CANDIDATE.equals(role)) {
            return new CandidateNode();
        }

        return null;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<String> getPeers() {
        return peers;
    }

    public void setPeers(List<String> peers) {
        this.peers = peers;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public void initializeFSM(String fsmFile) throws StateManagerException {
        fsmMap = FSMBuilder.initializeFSM(fsmFile);
    }

    public StateMachine getFSM(String fsmName) {
        return fsmMap.get(fsmName);
    }

    public void initRaftServer() {
        if(raftServer == null) {
            raftServer = new RaftServer();
        }
    }

    public RaftServer raftServer() {
        return raftServer;
    }

    public LogService getLogService() {
        return logService;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }
}
