package com.king.excercise.GameServer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.king.excercise.GameServer.Data.UserScore;

public class LeaderBoardTest {

    private LeaderBoard leaderBoard; 
    @Before
    public void setUpBeforeClass() throws Exception {
        leaderBoard = new LeaderBoard(3);
    }

    @Test
    public void testPostNewScoreAndFetchValidHighScores() {
        assertTrue(leaderBoard.postNewScore("tom", 1, 300));
        assertTrue(leaderBoard.postNewScore("dick", 1, 200));
        assertTrue(leaderBoard.postNewScore("tom", 1, 500));
        assertTrue(leaderBoard.postNewScore("harry", 1, 400));
        assertTrue(leaderBoard.postNewScore("harry", 1, 20));
        assertTrue(leaderBoard.postNewScore("bob", 1, 20000));
        assertTrue(leaderBoard.postNewScore("alice", 1, 500));
        
        List<UserScore> result = leaderBoard.getTopScores(1);
        System.out.println("Top scorers are ");
        for(UserScore element : result){
            System.out.print(" userid=" + element.getUserId());
            System.out.print(" topScore=" + element.getTopScore());
            
            System.out.println("");
        }
        assertEquals(3, result.size());
        assertEquals("bob", result.get(0).getUserId());
        assertEquals(20000, result.get(0).getTopScore());
        
        assertEquals("alice", result.get(1).getUserId());
        assertEquals(500, result.get(1).getTopScore());
        assertEquals("tom", result.get(2).getUserId());
        assertEquals(500, result.get(2).getTopScore());
    }
    
    @Test
    public void testPostNewScoreAndFetchValidHighScores2() {
        assertTrue(leaderBoard.postNewScore("bob", 1, 500));
        assertTrue(leaderBoard.postNewScore("tom", 1, 500));
        assertTrue(leaderBoard.postNewScore("alice", 1, 300));
        assertTrue(leaderBoard.postNewScore("harry", 1, 800));
        assertTrue(leaderBoard.postNewScore("bob", 1, 700));
        
        List<UserScore> result = leaderBoard.getTopScores(1);
        System.out.println("Top scorers are ");
        for(UserScore element : result){
            System.out.print(" userid=" + element.getUserId());
            System.out.print(" topScore=" + element.getTopScore());
            
            System.out.println("");
        }
        assertEquals(3, result.size());
        assertEquals("harry", result.get(0).getUserId());
        assertEquals(800, result.get(0).getTopScore());
        
        assertEquals("bob", result.get(1).getUserId());
        assertEquals(700, result.get(1).getTopScore());
        assertEquals("tom", result.get(2).getUserId());
        assertEquals(500, result.get(2).getTopScore());
    }
    @Test
    public void testPostNewScoreAndFetchValidHighScores3() {
        LeaderBoard leaderBoard = new LeaderBoard(15);
        assertTrue(leaderBoard.postNewScore("user1", 1, 500));
        assertTrue(leaderBoard.postNewScore("user2", 1, 500));
        assertTrue(leaderBoard.postNewScore("user3", 1, 500));
        assertTrue(leaderBoard.postNewScore("user4", 1, 500));
        assertTrue(leaderBoard.postNewScore("user4", 1, 600));
        assertTrue(leaderBoard.postNewScore("user5", 1, 60));
        assertTrue(leaderBoard.postNewScore("user5", 1, 6000));
        
        List<UserScore> result = leaderBoard.getTopScores(1);
        assertEquals(5, result.size());
    }
    
    @Test
    public void testPostNewScoreAndFetchValidHighScoresForMultipleLevels() {
        assertTrue(leaderBoard.postNewScore("tom", 1, 300));
        assertTrue(leaderBoard.postNewScore("dick", 1, 200));
        assertTrue(leaderBoard.postNewScore("harry", 1, 400));
        assertTrue(leaderBoard.postNewScore("bob", 1, 20000));
        assertTrue(leaderBoard.postNewScore("tom", 2, 500));
        assertTrue(leaderBoard.postNewScore("harry", 2, 20));
        assertTrue(leaderBoard.postNewScore("alice", 3, 3));
        
        List<UserScore> result = leaderBoard.getTopScores(1);
        assertEquals(3, result.size());
        assertEquals("bob", result.get(0).getUserId());
        assertEquals(20000, result.get(0).getTopScore());
        assertEquals("harry", result.get(1).getUserId());
        assertEquals(400, result.get(1).getTopScore());
        assertEquals("tom", result.get(2).getUserId());
        assertEquals(300, result.get(2).getTopScore());
        
        result = leaderBoard.getTopScores(2);
        assertEquals(2, result.size());
        assertEquals("tom", result.get(0).getUserId());
        assertEquals(500, result.get(0).getTopScore());
        assertEquals("harry", result.get(1).getUserId());
        assertEquals(20, result.get(1).getTopScore());
        
        result = leaderBoard.getTopScores(3);
        assertEquals(1, result.size());
        assertEquals("alice", result.get(0).getUserId());
        assertEquals(3, result.get(0).getTopScore());
    }
    
    @Test
    public void PostNewScoresAndVerifyOnlyOneScoreReturnedPerUser() {
        assertTrue(leaderBoard.postNewScore("tom", 1, 300));
        assertTrue(leaderBoard.postNewScore("dick", 1, 200));
        assertTrue(leaderBoard.postNewScore("tom", 1, 500));
        assertTrue(leaderBoard.postNewScore("harry", 1, 400));
        
        List<UserScore> result = leaderBoard.getTopScores(1);
        
        assertEquals(3, result.size());
        assertEquals("tom", result.get(0).getUserId());
        assertEquals(500, result.get(0).getTopScore());
        assertEquals("harry", result.get(1).getUserId());
        assertEquals(400, result.get(1).getTopScore());
        assertEquals("dick", result.get(2).getUserId());
        assertEquals(200, result.get(2).getTopScore());
    }
    
    @Test
    public void testPostNewScoreAndFetchInvalidLevelHighScores() {
        assertTrue(leaderBoard.postNewScore("user2", 1, 200));
        assertTrue(leaderBoard.postNewScore("user1", 1, 300));
        assertTrue(leaderBoard.postNewScore("user3", 1, 400));
        assertTrue(leaderBoard.postNewScore("user1", 1, 500));
        assertTrue(leaderBoard.postNewScore("user3", 1, 20));
        
        List<UserScore> result = leaderBoard.getTopScores(2);
        assertEquals(0, result.size());
    }

}
