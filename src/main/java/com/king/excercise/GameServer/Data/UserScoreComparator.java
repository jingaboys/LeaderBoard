package com.king.excercise.GameServer.Data;

import java.util.Comparator;

public class UserScoreComparator implements Comparator<UserScore> {

    @Override
    public int compare(UserScore user1, UserScore user2) {
        if(user1.equals(user2))
            return 0;
        int nameCompare = user1.getUserId().compareTo(user2.getUserId());
        int scoreCompare = user2.getTopScore() - user1.getTopScore();
        
        if(nameCompare == 0){
            return 0;
        }
        if(scoreCompare != 0){
            return scoreCompare;
        }
        else{
            return nameCompare;
        }
        
    }

}
