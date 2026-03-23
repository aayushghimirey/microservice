package com.sts.utils;


public interface AppConstants {

    interface Logs {
        String REQUEST_RECEIVED = "Request received for path: {}";
        String REQUEST_PROCESSED = "Request processed for path: {}";
    }


    interface Errors {
        String INVALID_TOKEN = "Invalid token provided";
        String TOKEN_EXPIRED = "Token has expired";
        String UNAUTHORIZED_ACCESS = "Unauthorized access attempt";
        String FILTER_ERROR = "Error processing JWT token: {}";
    }
}
