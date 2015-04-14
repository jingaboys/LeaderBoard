package com.king.excercise.GameServer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import com.king.excercise.GameServer.Data.UserScore;
import com.sun.net.httpserver.HttpExchange;

public class GameServerHTTPHandlerTest {
    GameServerSession session = Mockito.mock(GameServerSession.class);
    LeaderBoard leaderBoard = Mockito.mock(LeaderBoard.class);
    private GameServerHTTPHandler handler = new GameServerHTTPHandler(session, leaderBoard, "utf-8");
    
    private ByteArrayOutputStream out = new ByteArrayOutputStream();
    
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Test(expected=NumberFormatException.class)
    public void TesthandleHighScoreRequestShouldThrowNumberFormatException() {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("levelid", "invalid");
        handler.handleHighScoreRequest(parameters);
        assertTrue(false);
    }
    @Test()
    public void TesthandleHighScoreRequestShouldReturnValidCSV() {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("levelid", "23");
        List<UserScore> result = new ArrayList<>();
        UserScore obj = new UserScore("user1");
        obj.addScore(100);
        obj.addScore(200);
        result.add(obj);
        obj = new UserScore("user2");
        obj.addScore(300);
        obj.addScore(400);
        result.add(obj);
        
        Mockito.when(leaderBoard.getTopScores(Mockito.anyInt())).thenReturn(result);
        String value = handler.handleHighScoreRequest(parameters);
        assertEquals("user1=200,user2=400,", value);
    }
    @Test()
    public void TesthandlePostUserScoreShouldReturnSuccess() {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("levelid", "23");
        parameters.put("sessionKey", "ihiuhihad");
        Mockito.when(session.validateAndGetUserId(Mockito.anyString())).thenReturn("user1");
        String value = handler.handlePostUserScore(parameters, "2233");
        assertEquals("success", value);
    }
    @Test(expected=NumberFormatException.class)
    public void TesthandlePostUserScoreWithInvalidLevelIdShouldThrowException() {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("levelid", "junk");
        parameters.put("sessionKey", "ihiuhihad");
        Mockito.when(session.validateAndGetUserId(Mockito.anyString())).thenReturn("user1");
        String value = handler.handlePostUserScore(parameters, "2233");
        assertTrue(false);
    }
    @Test(expected=NumberFormatException.class)
    public void TesthandlePostUserScoreWithInvalidScoreShouldThrowException() {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("levelid", "111");
        parameters.put("sessionKey", "ihiuhihad");
        Mockito.when(session.validateAndGetUserId(Mockito.anyString())).thenReturn("user1");
        String value = handler.handlePostUserScore(parameters, "ddss");
        assertTrue(false);
    }
    
    @Test
    public void TesthandlePostUserScoreWithInvalidSessionKeyShouldReturnInvalidRequest() {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("levelid", "23");
        parameters.put("sessionKey", "ihiuhihad");
        Mockito.when(session.validateAndGetUserId(Mockito.anyString())).thenReturn(null);
        String value = handler.handlePostUserScore(parameters, "2233");
        assertEquals("invalid request", value);
    }
    
    
    @Test
    public void TestHandleforLogin() throws URISyntaxException, IOException{
        HttpExchange ex = Mockito.mock(HttpExchange.class);
        URI u = new URI("/user1/login");
        Mockito.when(ex.getRequestURI()).thenReturn(u);
        Mockito.when(ex.getResponseBody()).thenReturn(out);
        Mockito.doNothing().when(ex).sendResponseHeaders(Mockito.anyInt(), Mockito.anyLong());
        Mockito.when(ex.getRequestMethod()).thenReturn("GET");
        Mockito.when(session.createNewSession(Mockito.anyString())).thenReturn("sdfdsfsd");
        
        handler.handle(ex);
        assertEquals("sdfdsfsd", out.toString());
        System.out.println(out.toString());
    }
    
    @Test
    public void TestHandleforHighScore() throws URISyntaxException, IOException{
        HttpExchange ex = Mockito.mock(HttpExchange.class);
        URI u = new URI("/1/highscorelist");
        Mockito.when(ex.getRequestURI()).thenReturn(u);
        Mockito.when(ex.getResponseBody()).thenReturn(out);
        Mockito.doNothing().when(ex).sendResponseHeaders(Mockito.anyInt(), Mockito.anyLong());
        Mockito.when(ex.getRequestMethod()).thenReturn("GET");
        
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("levelid", "23");
        List<UserScore> result = new ArrayList<>();
        UserScore obj = new UserScore("user1");
        obj.addScore(100);
        obj.addScore(200);
        result.add(obj);
        obj = new UserScore("user2");
        obj.addScore(300);
        obj.addScore(400);
        result.add(obj);
        
        Mockito.when(leaderBoard.getTopScores(Mockito.anyInt())).thenReturn(result);
        
        handler.handle(ex);
        assertEquals("user1=200,user2=400,", out.toString());
        System.out.println(out.toString());
    }
    
    @Test
    public void TestHandleforPostScore() throws URISyntaxException, IOException{
        HttpExchange ex = Mockito.mock(HttpExchange.class);
        URI u = new URI("/2/score?sessionKey=iuhgiudfhgdisuf");
        Mockito.when(ex.getRequestURI()).thenReturn(u);
        Mockito.when(ex.getResponseBody()).thenReturn(out);
        Mockito.doNothing().when(ex).sendResponseHeaders(Mockito.anyInt(), Mockito.anyLong());
        Mockito.when(ex.getRequestMethod()).thenReturn("POST");
        InputStream in = new ByteArrayInputStream("1122".getBytes(StandardCharsets.UTF_8));
        Mockito.when(ex.getRequestBody()).thenReturn(in);
        Mockito.when(session.validateAndGetUserId(Mockito.anyString())).thenReturn("user1");
        Mockito.when(leaderBoard.postNewScore(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(true);
        
        handler.handle(ex);
        assertEquals("success", out.toString());
        System.out.println(out.toString());
    }
}
