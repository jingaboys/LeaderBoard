package com.king.excercise.GameServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import com.king.excercise.GameServer.Data.UserScore;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class GameServerHTTPHandler implements HttpHandler {
    public static final String GET = "GET";
    public static final String POST = "POST";
    
    private final String encoding;
    private final GameServerSession session;
    private final LeaderBoard leaderBoard;
    public GameServerHTTPHandler(GameServerSession session, LeaderBoard leaderBoard, String encoding) {
        this.session = session;
        this.leaderBoard = leaderBoard;
        this.encoding = encoding;
    }
    
    public void handle(HttpExchange ex) {
        OutputStream os = ex.getResponseBody();
        try {
                String path = ex.getRequestURI().getPath();
                Map<String, String> parameters = GameServerUtil.splitPath(path, encoding);

                if(ex.getRequestMethod().equals(GET) &&parameters.get("type") != null && parameters.get("type").equalsIgnoreCase("login")) {
                    String response = handleLoginRequest(parameters);
                    ex.sendResponseHeaders(200, response.length());
                    
                    
                    os.write(response.getBytes());
                    os.close(); 
                }
                else if(ex.getRequestMethod().equals(GET) && parameters.get("type") != null && parameters.get("type").equalsIgnoreCase("highscorelist")) {
                    String response = handleHighScoreRequest(parameters);
                    ex.sendResponseHeaders(200, response.length());
                    os.write(response.getBytes());
                    os.close(); 
                }
                else if(ex.getRequestMethod().equals(POST) && parameters.get("type") != null && parameters.get("type").equalsIgnoreCase("score")) {
                    parameters.putAll(GameServerUtil.splitQuery(ex.getRequestURI().getQuery(), encoding));
                    
                    
                    BufferedReader br = new BufferedReader(new InputStreamReader(ex.getRequestBody(),encoding));
                    StringBuilder body = new StringBuilder();
                    int b;
                    while ((b = br.read()) != -1) {
                        body.append((char) b);
                    }
                    br.close();
                    
                    String response = handlePostUserScore(parameters, body.toString());
                    ex.sendResponseHeaders(200, response.length());
                    os.write(response.getBytes());
                    os.close(); 
                }
                else{
                    String response = "Unknown input";
                    ex.sendResponseHeaders(500, response.length());
                    os.write(response.getBytes());
                    os.close(); 
                    
                }

        } catch (Exception e) {
            e.printStackTrace();
            String response = "Internal server error: " + e.getMessage();
            try {
                ex.sendResponseHeaders(500, response.length());
                os.write(response.getBytes());
                os.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
             
        }

    }

    public String handlePostUserScore(Map<String, String> parameters, String body) throws NumberFormatException{
        String levelId = parameters.get("levelid");
        String score = body;
        String sessionKey = parameters.get("sessionKey");
        String userId = session.validateAndGetUserId(sessionKey);
        if(userId != null) {
            leaderBoard.postNewScore(userId, Integer.parseInt(levelId), Integer.parseInt(score));
            return "success";
        }
        return "invalid request";
    }

    public String handleHighScoreRequest(Map<String, String> parameters) throws NumberFormatException{
        int level = Integer.parseInt(parameters.get("levelid"));
        List<UserScore> userScores = leaderBoard.getTopScores(level);
        
        return (GameServerUtil.convertToCSV(userScores));
    }

    

    public String handleLoginRequest(Map<String, String> parameters) {
        return session.createNewSession(parameters.get("userid"));
    }
    
    
    
    
    
    

}
