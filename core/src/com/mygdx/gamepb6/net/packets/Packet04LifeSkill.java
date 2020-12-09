package com.mygdx.gamepb6.net.packets;

import com.mygdx.gamepb6.net.GameClient;
import com.mygdx.gamepb6.net.GameServer;

public class Packet04LifeSkill extends Packet {
	private String username;
    private int code;    
    
    public Packet04LifeSkill(byte[] data) {
        super(04);
        String[] dataArray = readData(data).split(",");
        this.username = dataArray[0];
        this.code = Integer.parseInt(dataArray[1]);
    }
    
   
    public Packet04LifeSkill(String username, int code) {
        super(04);
        this.username = username;
        this.code = code;
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
        return ("04" + this.username + "," + this.code).getBytes();
    }

    public String getUsername() {
        return username;
    }


	public int getCode() {
		return code;
	}


	public void setCode(int code) {
		this.code = code;
	}




	
   

	
}
