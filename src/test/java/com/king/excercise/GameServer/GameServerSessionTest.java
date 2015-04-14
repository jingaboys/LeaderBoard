package com.king.excercise.GameServer;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.king.excercise.GameServer.Data.Session;

public class GameServerSessionTest {
    IClock clock = Mockito.mock(IClock.class);
    private GameServerSession instance = new GameServerSession(clock);
    
    @Before
    public void setUpBeforeClass() throws Exception {
        Mockito.when(clock.now()).thenReturn(System.currentTimeMillis());
    }
    
    @Test
    public void TestgetRandomSessionId(){
        for(int i = 0 ; i < 1000 ; i ++){
            String ID1 = instance.getRandomSessionId();
            String ID2 = instance.getRandomSessionId();
            //System.out.println("Generated IDs are " + ID1 + " " + ID2);
            assertNotSame(ID1, ID2);
        }
    }
    
    @Test
    public void isInvalidSessionShouldReturnTrueforValidSession(){
        Session session = new Session("test", "id1", System.currentTimeMillis());
        assertTrue(instance.isValidSession(session));
        
        session = new Session("test", "id1", System.currentTimeMillis()-8*60*1000);
        assertTrue(instance.isValidSession(session));
    }
    
    @Test
    public void isInvalidSessionShouldReturnFalseforExpiredSession(){
        Session session = new Session("test", "id1", System.currentTimeMillis()-10*60*1000);
        assertFalse(instance.isValidSession(session));
        
        session = new Session("test", "id1", System.currentTimeMillis()-15*60*1000);
        assertFalse(instance.isValidSession(session));
    }
    
    @Test
    public void testCreateNewSession() {
        assertNotNull(instance.createNewSession("user1"));
    }
    
    @Test
    public void testValidateAndGetValidUserId() {
        String sessionId = instance.createNewSession("user2");
        assertNotNull(instance.validateAndGetUserId(sessionId));
    }
    
    @Test
    public void testValidateAndGetInValidUserId() {
        assertNull(instance.validateAndGetUserId("junkID1"));
        String sessionId = instance.createNewSession("user3");
        Mockito.when(clock.now()).thenReturn(System.currentTimeMillis()+15*60*1000);
        assertNull(instance.validateAndGetUserId(sessionId));
    }
    
    @Test
    public void testRemoveInvalidSessions() {
        String sessionId = instance.createNewSession("user3");
        assertNotNull(instance.validateAndGetUserId(sessionId));
        Mockito.when(clock.now()).thenReturn(System.currentTimeMillis()+15*60*1000);
        instance.removeInvalidSessions();
        assertNull(instance.validateAndGetUserId(sessionId));
    }

}
