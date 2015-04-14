Assumptions: 
-------------------------
-Expired sessions are only removed by a cleanup thread which is scheduled to run every 1 minute.

-If the same user requests for a new session, the old session is not destroyed. The old session will eventually expire.

-All the scores are stored for a given user and level. The assumption is to support a future usecase of fetching all user scores for a level. 
If this is not needed, we can remove the globalLeaderBoard map in LeaderBoard.java

-The UserScore object is removed and added back to highscorers list to maintain sort order.

-I couldn't use separate httphandlers for URLs since they were of the format /<userid>/login, /<levelid>/score, /<levelid>/highscorelist

-I assume the code would have been cleaner to handle login/<userid>, score/<levelid> etc

How to run: 
-------------------------------
mvn clean install
cd target
java -jar GameServer-0.0.1-SNAPSHOT-jar-with-dependencies.jar


TODOs/ future enhancements:
-------------------------------
- Add a logging framework instead of sysouts

- Implement dynamic session handling, i.e. expire the previous session if an existing user requests for a new session

- Better parsing of path and parameters (perhaps use Filter) 
  