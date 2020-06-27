package ru.osipov.deploy.models;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class ErrorResponse implements Serializable {
    private int status;
    private String error;
    private String message;
    private Date timestamp;

    public ErrorResponse() {
        timestamp = new Date();
    }

    public ErrorResponse(int status, String error) {
        this.status = status;
        this.error = error;
        this.timestamp = new Date();
    }

    public ErrorResponse(int status, String error, String message) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.timestamp = new Date();
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErrorResponse that = (ErrorResponse) o;
        return status == that.status &&
                Objects.equals(error, that.error) &&
                Objects.equals(message, that.message) &&
                Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, error, message, timestamp);
    }

    @Override
    public String toString() {
         return com.google.common.base.MoreObjects.toStringHelper(this)
                .add("status",status)
                 .add("error",error)
                 .add("message",message)
                 .add("timestamp",timestamp).toString();

    }
}
