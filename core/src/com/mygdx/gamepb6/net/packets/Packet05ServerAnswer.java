package com.mygdx.gamepb6.net.packets;

import com.mygdx.gamepb6.net.GameClient;
import com.mygdx.gamepb6.net.GameServer;

/**
 * Packet05ServerAnswer viene usato dal GameServer per confermare al Client la connessione e 
 * gli comunica le coordinate di spawn del suo giocatore. 
 * Implementa tutti i metodi gia descritti nella classe astratta Packet.
 * @see Packet
 */
public class Packet05ServerAnswer extends Packet {
	private int spawnX;
    private int spawnY;
	private String username;    
    
    public Packet05ServerAnswer(byte[] data) {
        super(05);
        String[] dataArray = readData(data).split(",");
        this.spawnX = Integer.parseInt(dataArray[0]);
        this.spawnY = Integer.parseInt(dataArray[1]);
        this.username = dataArray[2];
    }
    
   
    public Packet05ServerAnswer(int spawnX, int spawnY, String username) {
        super(05);
        this.spawnX = spawnX;
        this.spawnY = spawnY;
        this.username = username;
    }

    @Override
    public void writeData(GameClient client) {
        client.sendData(getData());
    }

    @Override
    public void writeData(GameServer server) {
        server.sendDataToAllClients(getData());
    }
    
  
    @Override
    public byte[] getData() {
        return ("05" + this.spawnX + "," + this.spawnY+ "," + this.username).getBytes();
    }


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public int getSpawnX() {
		return spawnX;
	}


	public void setSpawnX(int spawnX) {
		this.spawnX = spawnX;
	}


	public int getSpawnY() {
		return spawnY;
	}


	public void setSpawnY(int spawnY) {
		this.spawnY = spawnY;
	}
    
    
}