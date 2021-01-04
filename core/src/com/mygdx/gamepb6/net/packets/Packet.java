package com.mygdx.gamepb6.net.packets;

import com.mygdx.gamepb6.net.GameClient;
import com.mygdx.gamepb6.net.GameServer;

/**
 * 
 * Questa classe astratta rappresenta lo scheletro dei Pacchetti che vengono utilizzati per la comunicazione tra
 * GameClient e GameServer.
 */

public abstract class Packet {

    public static enum PacketTypes {
        INVALID(-1), LOGIN(00), DISCONNECT(01), MOVE(02), BULLET(03), LIFESKILL(04), SERVERANSWER(05);

        private int packetId;

        private PacketTypes(int packetId) {
            this.packetId = packetId;
        }

        public int getId() {
            return packetId;
        }
    }

    public byte packetId;

    public Packet(int packetId) {
        this.packetId = (byte) packetId;
    }
    
    /**
     * Permette al client di inviare i dati del pacchetto.
     * @param client	GameClient
     */
    public abstract void writeData(GameClient client);
    
    /**
     * permette al server di inviare i dari del pacchetto.
     * @param server	GameServer
     */
    public abstract void writeData(GameServer server);

    public String readData(byte[] data) {
        String message = new String(data).trim();
        return message.substring(2);
    }

    public abstract byte[] getData();
    
    /**
     * Permette di identificare il tipo di pacchetto dall'id passato per input
     * @param packetId	tag per riconoscere il tipo di pacchetto
     * @return			PacketTypes del pacchetto
     */
    public static PacketTypes lookupPacket(String packetId) {
        try {
            return lookupPacket(Integer.parseInt(packetId));
        } catch (NumberFormatException e) {
            return PacketTypes.INVALID;
        }
    }
    
    public static PacketTypes lookupPacket(int id) {
        for (PacketTypes p : PacketTypes.values()) {
            if (p.getId() == id) {
                return p;
            }
        }
        return PacketTypes.INVALID;
    }
}
