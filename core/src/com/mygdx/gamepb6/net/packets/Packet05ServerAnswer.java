package com.mygdx.gamepb6.net.packets;

import com.mygdx.gamepb6.net.GameClient;
import com.mygdx.gamepb6.net.GameServer;

public class Packet05ServerAnswer extends Packet {
	private int spawnX;
    private int spawnY;    
    
    public Packet05ServerAnswer(byte[] data) {
        super(05);
        String[] dataArray = readData(data).split(",");
        this.spawnX = Integer.parseInt(dataArray[0]);
        this.spawnY = Integer.parseInt(dataArray[1]);
    }
    
   
    public Packet05ServerAnswer(int spawnX, int spawnY) {
        super(05);
        this.spawnX = spawnX;
        this.spawnY = spawnY;
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
        return ("05" + this.spawnX + "," + this.spawnY).getBytes();
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