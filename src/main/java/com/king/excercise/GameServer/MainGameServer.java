package com.king.excercise.GameServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.sun.net.httpserver.*;

/**
 * MainGameServer
 *
 */
public class MainGameServer 
{
    private static final int DEFAULT_PORT = 8083;
    private static final int HIGH_SCORE_SIZE = 15;
    private final HttpServer server;
    private final HttpHandler handler;
    private final int port;
    private final GameServerSession session;
    private final LeaderBoard leaderBoard;



    public MainGameServer(int port) throws IOException{
        this.port = port;
        this.session = new GameServerSession(new SystemClock());
        this.leaderBoard = new LeaderBoard(HIGH_SCORE_SIZE);

        //Schedule the session store cleanup to run every 1 minute
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(new Runnable()
        {
            @Override
            public void run() {
                session.removeInvalidSessions();
            }
        }, 
        0, 1, TimeUnit.MINUTES);

        this.handler = new GameServerHTTPHandler(session, leaderBoard, "utf-8");

        InetSocketAddress addr = new InetSocketAddress(port); 
        server = HttpServer.create( addr, 0 );
        server.createContext("/", this.handler);
        server.setExecutor(Executors.newCachedThreadPool());
    }

    public void start(){
        server.start();
    }

    public void stop() {
        server.stop(port);
    }

    public static void main( String[] args )
    {
        try {
            MainGameServer objServer = new MainGameServer(DEFAULT_PORT);
            objServer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
