package com.mygdx.game.desktop;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.gamepb6.MainGame;
import com.mygdx.gamepb6.net.GameServer;

public class ServerLauncher {
	public static GameServer socketServer;
	
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		serverStart();
		new LwjglApplication(socketServer, config);
		//serverStart();
	}
	
	
	 public static void serverStart() {
		 System.out.println("Accendo il server...");
	        if (socketServer == null) {
	        	socketServer = new GameServer();
	            socketServer.start();
	        }	
	    }

}
