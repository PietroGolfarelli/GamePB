package com.mygdx.gamepb6.net.packets;

import com.mygdx.gamepb6.net.GameClient;
import com.mygdx.gamepb6.net.GameServer;

public class Packet03Bullet extends Packet {
	private String username;
    private int dirx, diry;
    
    
    public Packet03Bullet(byte[] data) {
        super(03);
        String[] dataArray = readData(data).split(",");
        this.username = dataArray[0];
        this.dirx = Integer.parseInt(dataArray[1]);
        this.diry = Integer.parseInt(dataArray[2]);
    }
    
   
    public Packet03Bullet(String username, int dirx, int diry) {
        super(03);
        this.username = username;
        this.dirx = dirx;
        this.diry = diry;
        
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
        return ("03" + this.username + "," + this.dirx + "," + this.diry).getBytes();
    }

    public String getUsername() {
        return username;
    }


	public int getDirx() {
		return dirx;
	}


	public void setDirx(int dirx) {
		this.dirx = dirx;
	}


	public int getDiry() {
		return diry;
	}


	public void setDiry(int diry) {
		this.diry = diry;
	}

   

	
}
