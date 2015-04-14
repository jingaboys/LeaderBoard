package com.king.excercise.GameServer.Data;

import java.util.Date;

public class Session {
    private String userId;

    private String sessionId;
    private long creationTime;
    
    public Session(String name, String sessionId, long creationTime){
        this.userId = name;
        this.sessionId = sessionId;
        this.creationTime = creationTime;
    }

    public String getName() {
        return userId;
    }

    public void setName(String name) {
        this.userId = name;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }
    
    
    @Override
    public String toString() {
        return "Session [userId=" + userId + ", sessionId=" + sessionId
                + ", creationTime=" + new Date(creationTime) + "]";
    }

}
