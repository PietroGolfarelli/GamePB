package com.mygdx.gamepb6.net;

import java.io.IOException;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.mygdx.gamepb6.MainGame;
import com.mygdx.gamepb6.net.packets.Packet;
import com.mygdx.gamepb6.net.packets.Packet00Login;
import com.mygdx.gamepb6.net.packets.Packet01Disconnect;
import com.mygdx.gamepb6.net.packets.Packet02Move;
import com.mygdx.gamepb6.net.packets.Packet03Bullet;
import com.mygdx.gamepb6.net.packets.Packet04LifeSkill;
import com.mygdx.gamepb6.net.packets.Packet05ServerAnswer;
import com.mygdx.gamepb6.net.packets.Packet.PacketTypes;
import com.mygdx.gamepb6.player.Nemico;


public class GameClient extends Thread {

    private InetAddress ipAddress;
    private DatagramSocket socket;
    private MainGame game;

    
    public GameClient(MainGame game, String ipAddress) {
        this.game = game;
        try {
            this.socket = new DatagramSocket();
            this.ipAddress = InetAddress.getByName(ipAddress);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
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
            handleLogin((Packet00Login) packet, address, port);
            break;
        case DISCONNECT:
            packet = new Packet01Disconnect(data);
            System.out.println("[" + address.getHostAddress() + ":" + port + "] "
                    + ((Packet01Disconnect) packet).getUsername() + " has left the world...");
            game.playscreen.removeEntityMP(((Packet01Disconnect) packet).getUsername());
            break;
        case MOVE:
            packet = new Packet02Move(data);
            handleMove((Packet02Move) packet);
            break;
        case BULLET:
        	packet = new Packet03Bullet(data);
            handleBullet(((Packet03Bullet) packet));
            break;
        case LIFESKILL:
        	packet = new Packet04LifeSkill(data);
            this.handleLifeSkill(((Packet04LifeSkill) packet));
            break;
        case SERVERANSWER:
        	packet = new Packet05ServerAnswer(data);
            handleServerAnswer(((Packet05ServerAnswer) packet));
        }
    }

    
	private void handleServerAnswer(Packet05ServerAnswer packet) {
		game.setAnswer(true);
		System.out.println("è arrivata la risposa dal server");
		game.playscreen.connesso(packet.getSpawnX(), packet.getSpawnY());
		//game.loadGame();
		//game.loadGame(true);
	}


	public void sendData(byte[] data) {
    	DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, 1331);
    	try {
    		socket.send(packet);
            } 
    	catch (IOException e) {
            	e.printStackTrace();
          }
    }
    
    
    private void handleLogin(Packet00Login packet, InetAddress address, int port) {
        System.out.println("[" + address.getHostAddress() + ":" + port + "] " 
        		+ packet.getUsername() + " has joined the game...");
    	
        Nemico nemico = new Nemico(this.game.playscreen, packet.getUsername(), 
        		address , port, packet.getX(), packet.getY());
        
        this.game.playscreen.addNemico(nemico);
    }

    
    private void handleMove(Packet02Move packet) {
        game.playscreen.movePlayers(packet.getUsername(), packet.getX(), 
        		packet.getY(), packet.getPosX(), packet.getPosY());
    }
    
    private void handleBullet(Packet03Bullet packet) {
    	game.playscreen.firePlayers(packet.getUsername(), packet.getDirx(), packet.getDiry());
		
	}
    
    private void handleLifeSkill(Packet04LifeSkill packet) {
    	System.out.println("pacchetto life arriva in handlelife del client " );
    	game.playscreen.nemicoLifeSkill(packet.getUsername(), packet.getCode());
		
	}
    
}
