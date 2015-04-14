package com.king.excercise.GameServer;

public class SystemClock implements IClock {

    @Override
    public long now() {
       return System.currentTimeMillis();
    }

}
