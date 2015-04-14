package com.king.excercise.GameServer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

import com.king.excercise.GameServer.Data.UserScore;
import com.king.excercise.GameServer.Data.UserScoreComparator;

/**
 * A class to implement thread safe leaderboard.
 * @author rrenjith
 *
 */
public class LeaderBoard {
    //defines the size of highscorerlist to maintain per level
    private final int highScoreSize;
    //Map to store all scores of users on a per level basis. Key is levelId and Value is a map of userID to UserScore object.
    private Map<Integer, Map<String, UserScore>> globalLeaderBoard = new ConcurrentHashMap<>();
    
    //Map to store a per level highscorer set. Key is levelId and value is a set top n high scorer UserScore objects.
    private Map<Integer, ConcurrentSkipListSet<UserScore>> highScorers = new ConcurrentHashMap<>();//new ConcurrentSkipListSet<UserScore>(new UserScoreComparator());
    
    public LeaderBoard(int size){
        highScoreSize = size;
    }
    
    /**
     * Posts a new score for given a user and level
     * @param userId
     * @param level
     * @param score
     * @return
     */
    public boolean postNewScore(String userId, int level, int score) {
        globalLeaderBoard.putIfAbsent(level, new ConcurrentHashMap<String, UserScore>());
        Map<String, UserScore> userScoresForLevel = globalLeaderBoard.get(level);
        
        userScoresForLevel.putIfAbsent(userId, new UserScore(userId));
        UserScore objUserScore = userScoresForLevel.get(userId);
        
        
        RemoveFromHighScorers(level, objUserScore);
        objUserScore.addScore(score);
        checkAndAddToHighScorers(level, objUserScore);
        
        return true;
    }
    /**
     * Removes the UserScore object from highscorers for that level 
     * @param level
     * @param objUserScore
     */
    public void RemoveFromHighScorers(int level, UserScore objUserScore) {
        NavigableSet<UserScore> highScorerForLevel = highScorers.get(level);
        if(highScorerForLevel != null) {
            //Remove the object
            highScorerForLevel.remove(objUserScore);
        }
        
    }

    /**
     * If needed, adds the UserScore object to the highscorer list for the given level and trims the last element is the highscorer list count goes above highScoreSize. 
     * @param level
     * @param objUserScore
     */
    public void checkAndAddToHighScorers(int level, UserScore objUserScore) {
        highScorers.putIfAbsent(level, new ConcurrentSkipListSet<UserScore>(new UserScoreComparator()));
        NavigableSet<UserScore> highScorerForLevel = highScorers.get(level);

        //Remove and add the same object to restore the sort order
        highScorerForLevel.remove(objUserScore); //should not be present, but for safety check again and remove if needed
        highScorerForLevel.add(objUserScore);

        if(highScorerForLevel.size() > highScoreSize){
            //double check locking. Need to synchronize here since another thread could be modifying the highScorerForLevel set 
            synchronized (highScorerForLevel) {
                //Trim the end element if needed according to the size
                if(highScorerForLevel.size() > highScoreSize) {
                    highScorerForLevel.remove(highScorerForLevel.last());
                }
            }
        }
    }

    /**
     * Returns the top n highscorers for the given level
     * @param level
     * @return
     */
    public List<UserScore> getTopScores(int level){
        NavigableSet<UserScore> highScorerForLevel = highScorers.get(level);
        if(highScorerForLevel != null) {
            return new ArrayList<UserScore>(highScorerForLevel); 
        }
        
        return new ArrayList<UserScore>();
    }
}