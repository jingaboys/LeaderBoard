package com.king.excercise.GameServer.Data;

import java.util.SortedSet;
import java.util.concurrent.ConcurrentSkipListSet;
/**
 * Represents a set of user scores per level.
 * @author rrenjith
 *
 */
public class UserScore {


    private String userId;
    private SortedSet<Integer> scores;
    
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public UserScore(String userId) {
        this.userId = userId;
        scores = new ConcurrentSkipListSet<Integer>();
    }
    
    public int getTopScore() {
        if(scores == null || scores.isEmpty()){
            return -1;
        }
        return scores.last();
    }
    
    public boolean addScore(int score){
        scores.add(score);
        
        return true;
    }
    @Override
    public String toString() {
        return "UserScore [userId=" + userId + ", scores=" + scores + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((userId == null) ? 0 : userId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UserScore other = (UserScore) obj;
        if (userId == null) {
            if (other.userId != null)
                return false;
        } else if (!userId.equals(other.userId))
            return false;
        return true;
    }
    
}