package com.mygdx.gamepb6.net;

import java.net.InetAddress;
import com.mygdx.gamepb6.MainGame;


/**
 * Questa classe contiene tutti i dati di un Client che si connette al GameServer, è anche utilizzata per mantenere
 * aggiornata la posizione del giocatore controllata del Client che rappresenta.
 */
public class Connessione {
	public InetAddress ipAddress;
    int port;
    String username;
    float x;
    float y;
    MainGame game;
   /**
    * @see GameServer
    * @param username	username non duplicato (vedi GameServer) con cui il Client gioca
    * @param ipAddress	IPaddress con cui si collega il Client a GameServer
    * @param port		porta con cui si collega il Client a GameServer
    */
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
