package com.tmlabs.springraft;

public class Constants {



    public static final String START_STATE = "START";
    public static final String DONE_STATE = "DONE";
    public static final String ERROR_STATE = "ERROR";

    public static final String ROLE_LEADER = "LEADER";
    public static final String ROLE_CANDIDATE = "CANDIDATE";
    public static final String ROLE_FOLLOWER = "FOLLOWER";

    public static final String INTERNAL_SERVER_ERROR_MESSAGE = "Internal Server Error";

    public static final String NOT_A_LEADER_ERROR_MSG = "Not a leader. Operation not permitted";

    public enum STATUS {
        YET_TO_START, BEGIN, PROCESSING, SUCCESS, FAILURE, PARTIAL_FAILURE
    }

    /*public enum ROLE {
        LEADER, CANDIDATE, FOLLOWER;

        public static final String ROLE_LEADER = "LEADER";
        public static final String ROLE_CANDIDATE = "CANDIDATE";
        public static final String ROLE_FOLLOWER = "FOLLOWER";

        public static ROLE role(String role) {
            if(ROLE_LEADER.equals(role)) {
                return LEADER;
            }
            if(ROLE_CANDIDATE.equals(role)) {
                return CANDIDATE;
            }
            if(ROLE_FOLLOWER.equals(role)) {
                return FOLLOWER;
            }
            return null;
        }
    }*/
}
