package com.mygdx.gamepb6.net.packets;

import com.mygdx.gamepb6.net.GameClient;
import com.mygdx.gamepb6.net.GameServer;

/**
* Packet01Disconnect viene utilizzato  per indicare ed effetture la disconnessione di un Client. 
* Implementa tutti i metodi gia descritti nella classe astratta Packet.
* @see Packet
*/
public class Packet01Disconnect extends Packet {

    private String username;

    public Packet01Disconnect(byte[] data) {
        super(01);
        this.username = readData(data);
    }

    public Packet01Disconnect(String username) {
        super(01);
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
        return ("01" + this.username).getBytes();
    }

    public String getUsername() {
        return username;
    }

}
