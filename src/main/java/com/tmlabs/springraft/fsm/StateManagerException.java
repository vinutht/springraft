package com.tmlabs.springraft.fsm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StateManagerException extends Exception {

    private static final Logger logger = LoggerFactory.getLogger(StateManagerException.class);

    private final ERROR_CODE errorCode;
    private final String additionalDetails;
    private final Throwable originalException;

    private static final String STR_FORMAT = "%s >>> %s";

    public enum ERROR_CODE {

        EMPTY("Empty error code."),
        COMMAND_PROCESSING_FAILED("Failed to process the command."),
        UNHANDLED_CASE("Reached the case which is unhandled or not thought of."),
        HEAD_NOT_INITIALIZED("Head not initialized."),
        CLASS_LOAD_FAILED("Failed to load the class"),
        OBJECT_INSTANTIATION_FAILED("Failed to instantiate class"),
        FSM_PARSE_FAILURE("Failed to parse the FSM yaml file."),
        INVALID_COMMAND("Invalid command. Cannot process.");

        private String errorMsg;

        private ERROR_CODE(String errorMsg) {
            this.errorMsg = errorMsg;
        }

        @Override
        public String toString() {
            return errorMsg;
        }
    }


    public StateManagerException(Throwable exception, ERROR_CODE errorCode, String additionalDetails) {
        super(exception);
        this.errorCode = errorCode;
        if(additionalDetails == null || additionalDetails.isEmpty()) {
            additionalDetails = "Details not available";
        }
        this.additionalDetails = additionalDetails;
        this.originalException = exception;
    }

    @Override
    public String getMessage() {
        String msg = String.format("Additional Information: %s", additionalDetails);
        if(originalException != null) {
            msg = String.format("Reason: %s >> Additional Information: %s", originalException.getMessage(), additionalDetails);
        }
        return String.format(STR_FORMAT, errorCode, msg);
    }


    public ERROR_CODE getErrorCode() {
        return errorCode;
    }

    public void logMsg() {
        if(logger.isErrorEnabled()) {
            if(originalException != null) {
                logger.error(String.format(STR_FORMAT, errorCode, additionalDetails), originalException);
            }
            else {
                logger.error(String.format(STR_FORMAT, errorCode, additionalDetails));
            }
        }
    }
}
