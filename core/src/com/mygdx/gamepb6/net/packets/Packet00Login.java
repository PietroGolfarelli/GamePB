package com.mygdx.gamepb6.net.packets;


import com.mygdx.gamepb6.net.GameClient;
import com.mygdx.gamepb6.net.GameServer;

/**
 * Packet00Login contiene tutti i dati che permettono la connessione di un giocatore. 
 * Implementa tutti i metodi gia descritti nella classe astratta Packet.
 * @see Packet
 */
public class Packet00Login extends Packet {

    private String username;
    float x;
	private float y;

    public Packet00Login(byte[] data) {
        super(00);
        String[] dataArray = readData(data).split(",");
        this.username = dataArray[0];
        this.x = Float.parseFloat(dataArray[1]);
        this.y = Float.parseFloat(dataArray[2]);
    }

	public Packet00Login(String username, float f, float g) {
        super(00);
        this.username = username;
        this.x = f;
        this.y = g;
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
        return ("00" + this.username + "," + getX() + "," + getY()).getBytes();
    }
    

	public void setUsername(String username) {
		this.username = username;
	}

    public String getUsername() {
        return username;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
    
    public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

}
