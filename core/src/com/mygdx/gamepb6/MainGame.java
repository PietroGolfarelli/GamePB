package com.mygdx.gamepb6;

import javax.swing.JOptionPane;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.gamepb6.net.Connessione;
import com.mygdx.gamepb6.net.GameClient;
import com.mygdx.gamepb6.net.GameServer;
import com.mygdx.gamepb6.net.packets.Packet00Login;
import com.mygdx.gamepb6.net.packets.Packet01Disconnect;
import com.mygdx.gamepb6.screens.PlayScreen;


public class MainGame extends Game implements Runnable {
	//Virtual Screen size and Box2D Scale(Pixels Per Meter)
	public static final int V_WIDTH = 400;
	public static final int V_HEIGHT = 400;
	public static final float PPM = 200;
	
	private static final String NAME = "gamepb5";
	public static MainGame game;

    private Thread thread;
    
    public SpriteBatch batch;
    public PlayScreen playscreen;
    
    public Connessione conn;
    public GameClient socketClient;
    public GameServer socketServer;
    
    public boolean isApplet = false;
    boolean running;
    boolean isServerRunning;
	private String username;
    
	
    @Override
    public void create() {
    	game=this;
    	batch = new SpriteBatch();
    	this.username=JOptionPane.showInputDialog(this, "Please enter a username");
    	playscreen= new PlayScreen(this, username);
    	
        running = true;
        
        thread = new Thread(this, NAME + "_main");
        thread.start();
        
        //serverStart();
        clientStart();
    	setScreen(playscreen);
    	sendLogin();
	}
    
      
    public void serverStart() {
    	System.out.println("Accendo il server...");
        if (!isApplet) {
            if (socketServer == null) {
                socketServer = new GameServer(this);
                socketServer.start();
            }            
        }
    }
    
    
    public void clientStart() {
    	socketClient = new GameClient(this, "localhost");
        socketClient.start();
    }
    
    
    public void sendLogin() {
    	Packet00Login loginPacket = new Packet00Login(playscreen.player.getUsername(), this.playscreen.player.getX(), 
				this.playscreen.player.getY());
		if (socketServer != null) {
			loginPacket.writeData(socketClient);
		}
		else {
			System.out.println("ERRORE NEL LOGIN, SERVER SPENTO");
			loginPacket.writeData(socketClient);
		}
    }

    
    @Override
    public void render() {
    	  super.render();
    }
  
    @Override
    public void dispose() {	
        super.dispose();
		batch.dispose(); 
		Packet01Disconnect packet = new Packet01Disconnect(this.playscreen.player.getUsername());
        packet.writeData(this.socketClient);
    }

	public void run() {
		render();
	}
}