package com.king.excercise.GameServer;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.king.excercise.GameServer.Data.Session;

/**
 * A session store implementation for gameserver
 * @author rrenjith
 *
 */

public class GameServerSession {
    
    public static final long SESSION_VALIDITY_IN_MILLISECS = 10*60*1000; //10 minutes  
    
    //Map of sessionId to Session Object, stores the current valid sessions for the server
    private Map<String, Session> sessionStore = new ConcurrentHashMap<>();
    
    //used to generate random sessionId
    private SecureRandom random = new SecureRandom();
    
    private IClock clock;
    
    public GameServerSession(IClock clock){
        this.clock = clock;
    }
    
    
    /**
     * Generates a new sessionId and assigns it to the userId. 
     * **Any pervious sessionIds associated with this user is ignored and is expected to expire within next 10 minutes**
     * @param userId
     * @return a new sessionId valid from Now until next 10 minutes
     */
    public String createNewSession(String userId){
        String sessionId = getRandomSessionId();
        long creationTime = clock.now();
        Session session = new Session(userId, sessionId, creationTime);
        sessionStore.put(sessionId, session);
        
        return sessionId;
    }
    
    /**
     * Validates a sessionId and if valid, returns the corresponding userId
     * @param sessionId
     * @return userId for the session if valid, null otherwise
     */
    public String validateAndGetUserId(String sessionId) {
        Session session = sessionStore.get(sessionId);
        if(session != null && isValidSession(session)){
            return session.getName();
        }
        
        return null;
    }
    
    /**
     * Checks if a session is valid at the current time or not.
     * @param session
     * @return true/false
     */
    public boolean isValidSession(Session session) {
        long currentTime = clock.now();
        long startTime = session.getCreationTime();
        
        return ((currentTime-startTime) < SESSION_VALIDITY_IN_MILLISECS);
    }
    
    /**
     * Generates and returns a random sessionId. 
     * This works by choosing 130 bits from a cryptographically secure random bit generator, and encoding them in base-32. 128 bits is considered to be cryptographically strong, but each digit in a base 32 number can encode 5 bits, so 128 is rounded up to the next multiple of 5. 
     * @return sessionId
     */
    String getRandomSessionId() {
        return new BigInteger(130, random).toString(32);
    }
    
    /**
     * A helper method to remove expired sessions from sessionStore. This method should be called periodically to prevent sessionStore from growing in size.
     */
    public void removeInvalidSessions() {
        for(Map.Entry<String, Session> entry : sessionStore.entrySet()) {
            Session session = entry.getValue();
            if(!isValidSession(session)){
                System.out.println("Removing expired session: " + session);
                sessionStore.remove(entry.getKey());
            }
        }
    }

}
