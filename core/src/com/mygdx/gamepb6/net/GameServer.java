package com.mygdx.gamepb6.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import com.mygdx.gamepb6.MainGame;
import com.mygdx.gamepb6.net.packets.Packet;
import com.mygdx.gamepb6.net.packets.Packet00Login;
import com.mygdx.gamepb6.net.packets.Packet01Disconnect;
import com.mygdx.gamepb6.net.packets.Packet02Move;
import com.mygdx.gamepb6.net.packets.Packet03Bullet;
import com.mygdx.gamepb6.net.packets.Packet04LifeSkill;
import com.mygdx.gamepb6.net.packets.Packet.PacketTypes;

public class GameServer extends Thread {

    private DatagramSocket socket;
    @SuppressWarnings("unused")
	private MainGame game;
    private List<Connessione> playerConnessi = new ArrayList<Connessione>();
    public boolean running=false;

    public GameServer(MainGame game) {
        this.game = game;
        setRunning(true);
        try {
            this.socket = new DatagramSocket(1331);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
    
    public boolean isRunning() {
		return running;
	}

    
	public void setRunning(boolean running) {
		this.running = running;
	}

	
	public void run() {
        while (true) {
            byte[] data = new byte[1024];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
        }
    }

    private void parsePacket(byte[] data, InetAddress address, int port) {
        String message = new String(data).trim();
        PacketTypes type = Packet.lookupPacket(message.substring(0, 2));
        Packet packet = null;
        switch (type) {
        default:
        case INVALID:
            break;
        case LOGIN:
            packet = new Packet00Login(data);
            System.out.println("[" + address.getHostAddress() + ":" + port + "] "
                    + ((Packet00Login) packet).getUsername() + " has connected...");
            
            Connessione conn = new Connessione(((Packet00Login) packet).getUsername(), address, port);
            this.addConnection((Packet00Login) packet, conn);
            break;
        case DISCONNECT:
            packet = new Packet01Disconnect(data);
            System.out.println("[" + address.getHostAddress() + ":" + port + "] "
                    + ((Packet01Disconnect) packet).getUsername() + " has left...");
            this.removeConnection((Packet01Disconnect) packet);
            break;
        case MOVE:
            packet = new Packet02Move(data);
            this.handleMove(((Packet02Move) packet));
            break;
        case BULLET:
        	packet = new Packet03Bullet(data);
            this.handleBullet(((Packet03Bullet) packet));
            break;
        case LIFESKILL:
        	System.out.println("pacchetto life arriva nel server");
        	packet = new Packet04LifeSkill(data);
            this.handleLifeSkill(((Packet04LifeSkill) packet));
           
        }
    }


	public void addConnection(Packet00Login packet, Connessione conn) {
        boolean alreadyConnected=false;
        for (Connessione p : this.playerConnessi) {
        	if (packet.getUsername().equalsIgnoreCase(p.getUsername())) {
                if (p.ipAddress == null) {
                    p.ipAddress = conn.ipAddress;
                }
                if (p.port == -1) {
                    p.port = conn.port;
                }
                alreadyConnected = true;
            } 
            else {
                sendData(packet.getData(), p.ipAddress, p.port);
                packet = new Packet00Login(p.getUsername(), p.getX(), p.getY());        
                sendData(packet.getData(), conn.ipAddress, conn.port);                
            }
        }
        if (!alreadyConnected) {
            this.playerConnessi.add(conn);
        }
    }


    public void removeConnection(Packet01Disconnect packet) {
    	 if (getEntityMP(packet.getUsername()) != null) {
             int index = getEntityMPIndex(packet.getUsername());
             Connessione conn = this.playerConnessi.get(index);
             
             sendToAllButSender(packet, conn);
             playerConnessi.remove(conn);
          }
    }
    
    private void handleLifeSkill(Packet04LifeSkill packet) {
    	if (getEntityMP(packet.getUsername()) != null) {
         	int index = getEntityMPIndex(packet.getUsername());
            Connessione conn = this.playerConnessi.get(index);
            sendToAllButSender(packet, conn);
            System.out.println("pacchetto life spedito dal server");
         }
	}
    
    
    private void handleBullet(Packet03Bullet packet) {
    	 if (getEntityMP(packet.getUsername()) != null) {
         	int index = getEntityMPIndex(packet.getUsername());
            Connessione conn = this.playerConnessi.get(index);
            sendToAllButSender(packet, conn);
         }
	}

    
    private void handleMove(Packet02Move packet) {
        if (getEntityMP(packet.getUsername()) != null) {
        	int index = getEntityMPIndex(packet.getUsername());
            Connessione conn = this.playerConnessi.get(index);
            updateConnessione(packet, conn);
            sendToAllButSender(packet, conn);
        }
    }
    
    public void sendData(byte[] data, InetAddress ipAddress, int port) {
    	DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
        try {
        	this.socket.send(packet);
        } 
        catch (IOException e) {
        	e.printStackTrace();
        }
    }
    
    public Connessione getEntityMP(String username) {
        for (Connessione player : this.playerConnessi) {
            if (player.getUsername().equals(username)) {
                return player;
            }
        }
        return null;
    }
    
    public int getEntityMPIndex(String username) {
        int index = 0;
        for (Connessione player : this.playerConnessi) {
            if (player.getUsername().equals(username)) {
                break;
            }
            index++;
        }
        return index;
    }
    
    private void updateConnessione(Packet02Move packet, Connessione conn) {
        conn.setX(packet.getPosX());
        conn.setY(packet.getPosY());
    }
    
    private void sendToAllButSender(Packet packet, Connessione conn) {
    	for (Connessione p : playerConnessi) {
        	if (p.ipAddress != conn.ipAddress)
        		sendData(packet.getData(), p.ipAddress, p.port);
        }
    }
    
    public void sendDataToAllClients(byte[] data) {
        for (Connessione p : playerConnessi) {
            sendData(data, p.ipAddress, p.port);
        }
    }
    
    

    
    
}
