package com.mygdx.gamepb6;

import javax.swing.JOptionPane;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.gamepb6.net.Connessione;
import com.mygdx.gamepb6.net.GameClient;
import com.mygdx.gamepb6.net.GameServer;
import com.mygdx.gamepb6.net.packets.Packet00Login;
import com.mygdx.gamepb6.net.packets.Packet01Disconnect;
import com.mygdx.gamepb6.screens.GameOver;
import com.mygdx.gamepb6.screens.PlayScreen;

/**
 * MainGame è il punto di inizio del gioco. Permette di inserire l'IP del server a cui ci si vuole collegare,
 * lo username con cui ci si vuole identificare e fa partire la procedura di handshake con il GameServer.
 * Inizializza tutte le caratteristiche generali del gioco (es. grandezza della finestra), il GameClient e gli screen 
 * che utilizzeremo per giocare.
 * 
 */
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
    private Screen gameOver;
    
    public Connessione conn;
    public GameClient socketClient;
    
    public boolean isApplet = false;
    boolean running;
    boolean isServerRunning;
	private String username;
	private boolean answer;
	private String serverIP;
    
	/**
	 * Crea tutti gli oggetti necessari per connettersi al GameServer e caricare il gioco.
	 */
    @Override
    public void create() {
    	game=this;
    	batch = new SpriteBatch();
    	running = true;
    	
        thread = new Thread(this, NAME + "_main");
        thread.start();
        this.serverIP=JOptionPane.showInputDialog("IP del server", "Please enter IP del server");
        this.username=JOptionPane.showInputDialog("Username", "Please enter a username");
        playscreen= new PlayScreen(game, this.username);
        gameOver = new GameOver(game, playscreen);
        answer = false;
        loadGame();
        clientStart();
    	sendLogin();
	}
    
    /**
     * Imposto come schermo la mappa di gioco creata nel metodo create()
     */
    public void loadGame() {
    	setScreen(playscreen);	
    }
    
    
    public void clientStart() {
    	socketClient = new GameClient(this, this.serverIP);
        socketClient.start();
    }
    
    
    public void sendLogin() {
    	Packet00Login loginPacket = new Packet00Login(this.username, 1, 1);
		loginPacket.writeData(socketClient);		
    }
    
    public void setAnswer(boolean answer) {
    	this.answer = answer;
    }
    
    public boolean getAnswer() {
    	return this.answer;
    }
    
    /**
     * In caso il giocatore termini la vita viene visualizzato lo schermo gameOver
     */
    public void died() {
    	disconnetti();
		setScreen(gameOver);
    }
    
    @Override
    public void render() {
    	  super.render();
    }
    
    public void disconnetti () {
    	Packet01Disconnect packet = new Packet01Disconnect(playscreen.getUsername());
        packet.writeData(this.socketClient);
        running = false;
    }
  
    /**
     * Alla chiusura della finestra dealloco gli oggetti instanziati in precedenza  disconnetto il client al GameServer
     */
    @Override
    public void dispose() {	
        super.dispose();
		batch.dispose(); 
		if (running)
			disconnetti();
    }

	public void run() {
		render();
	}
}