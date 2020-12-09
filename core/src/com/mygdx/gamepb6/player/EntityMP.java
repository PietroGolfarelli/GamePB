package com.mygdx.gamepb6.player;

import java.net.InetAddress;

import com.mygdx.gamepb6.screens.PlayScreen;

public class EntityMP extends Entity {
	public InetAddress ipAddress;
    public int port;
    public String username;
    
    public EntityMP(PlayScreen screen, String username, InetAddress ipAddress, int port) {
        super(screen);
        super.ipAddress = ipAddress;
        super.port = port;
        super.username = username;
    }
    

    public void input(float dt) {
        super.handleInput(dt);
    }
   
    
    public String getUsername() {
        return super.username;
    }
    
    public void setMoving(boolean isMoving) {
    	
    }
    public void setMovingDir(int dir) {
    	
    }
    public void setNumSteps(int num_step) {
    	
    }

}
