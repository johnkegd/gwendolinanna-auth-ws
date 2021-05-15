package com.gwendolinanna.ws.auth.app.ui.model.response;

/**
 * @author Johnkegd
 */
public class OperationStatusModel {
    private String operationResult;
    private String operationName;
    private String operationData;

    public String getOperationResult() {
        return operationResult;
    }

    public void setOperationResult(String operationResult) {
        this.operationResult = operationResult;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public String getOperationData() {
        return operationData;
    }

    public void setOperationData(String operationData) {
        this.operationData = operationData;
    }
}
