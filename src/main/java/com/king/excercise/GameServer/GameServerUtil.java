package com.king.excercise.GameServer;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.king.excercise.GameServer.Data.UserScore;

public class GameServerUtil {

    public static Map<String, String> splitQuery(String query, String encoding) throws UnsupportedEncodingException {
        Map<String, String> query_pairs = new HashMap<>();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            query_pairs.put(URLDecoder.decode(pair.substring(0, idx), encoding), URLDecoder.decode(pair.substring(idx + 1), encoding));
        }
        return query_pairs;
    }

    public static Map<String, String> splitPath(String path, String encoding) {
        Map<String, String> path_parameter = new HashMap<>();
        String[] tokens = path.split("/");
        if(tokens.length > 2) {
            if(tokens[2].equals("score") || tokens[2].equals("highscorelist")) {
                path_parameter.put("levelid", tokens[1]);
                path_parameter.put("type",tokens[2]);
            } else if(tokens[2].equals("login")) {
                path_parameter.put("userid", tokens[1]);
                path_parameter.put("type",tokens[2]);
            }
            else {
                path_parameter.put("request","not supported");
            }
        }
        else {
            path_parameter.put("request","not supported");
        }
        return path_parameter;
    }
    
    public static String convertToCSV(List<UserScore> userScores) {
        StringBuffer buffer = new StringBuffer("");
        for(UserScore objUserScore : userScores) {
            buffer.append(objUserScore.getUserId()).append("=").append(objUserScore.getTopScore()).append(",");
        }
        return buffer.toString();
    }

    /*public static String getSessionKey(URI requestURI, String encoding) {
        String query = requestURI.getQuery();
        Map<String, String> queryPairs = 
        return null;
    }*/
}