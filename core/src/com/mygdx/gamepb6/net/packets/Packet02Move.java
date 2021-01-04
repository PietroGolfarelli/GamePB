package com.mygdx.gamepb6.net.packets;


import com.mygdx.gamepb6.net.GameClient;
import com.mygdx.gamepb6.net.GameServer;

/**
* Packet02Move viene utilizzato per indicare un un movimento di un giocatore. 
* Implementa tutti i metodi gia descritti nella classe astratta Packet.
* @see Packet
*/
public class Packet02Move extends Packet {

    private String username;
    private int x, y;
    private float posX, posY;
    
    public Packet02Move(byte[] data) {
        super(02);
        String[] dataArray = readData(data).split(",");
        this.username = dataArray[0];
        this.x = Integer.parseInt(dataArray[1]);
        this.y = Integer.parseInt(dataArray[2]);
        this.posX = Float.parseFloat(dataArray[3]);
        this.posY = Float.parseFloat(dataArray[4]);        
    }
    
   
    public Packet02Move(String username, int x, int y, float posX, float posY) {
        super(02);
        this.username = username;
        this.x = x;
        this.y = y;
        this.posX = posX;
        this.posY = posY;
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
        return ("02" + this.username + "," + this.x + "," + this.y + "," + this.posX + "," + this.posY).getBytes();
    }

    public String getUsername() {
        return username;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

	public float getPosX() {
		return posX;
	}

	public void setPosX(float posX) {
		this.posX = posX;
	}

	public float getPosY() {
		return posY;
	}

	public void setPosY(float posY) {
		this.posY = posY;
	}
}
