package com.mygdx.gamepb6.net;

import java.net.InetAddress;

import com.mygdx.gamepb6.MainGame;

public class Connessione {
	public InetAddress ipAddress;
    int port;
    String username;
    float x;
    float y;
    MainGame game;
   
	public  Connessione(String username, InetAddress ipAddress, int port) {
    	this.username = username;
    	this.ipAddress = ipAddress;
        this.port = port;
        this.x= -1;
        this.y= -1;
	}

	public InetAddress getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(InetAddress ipAddress) {
		this.ipAddress = ipAddress;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	 public float getX() {
		return x ;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

}
