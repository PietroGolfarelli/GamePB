package com.mygdx.gamepb6.net;

import java.io.IOException;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.mygdx.gamepb6.graphics.AnimationsPB;
import com.mygdx.gamepb6.MainGame;
import com.mygdx.gamepb6.net.packets.Packet;
import com.mygdx.gamepb6.net.packets.Packet00Login;
import com.mygdx.gamepb6.net.packets.Packet01Disconnect;
import com.mygdx.gamepb6.net.packets.Packet02Move;
import com.mygdx.gamepb6.net.packets.Packet03Bullet;
import com.mygdx.gamepb6.net.packets.Packet04LifeSkill;
import com.mygdx.gamepb6.net.packets.Packet05ServerAnswer;
import com.mygdx.gamepb6.net.packets.Packet.PacketTypes;
import com.mygdx.gamepb6.entities.Nemico;

/**
 * La classe GameClient rappresenta il Client del giocatore : la sua funzionalità è quella di 
 * permettere al giocatore di connettersi al GameServer e giocare insieme ad altri giocatori. 
 * Molti dei metodi contenuti all'interno della classe mirano alla gestione 
 * dei pacchetti che vengono ricevuti dal server attraverso una connessione socket. 
 * Questi pacchetti vengono riconosciuti, smistati e infine gestiti dagli specifici metodi.
 */
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

    /**
     * Questo metodo ha l'obbiettivo di smistare i pacchetti che vengono ricevuti dal GameClient
	 * in modo da poterli indirizzare al metodo che potrà gestirli correttamente.
     * @param data		byte che rappresentano i dati
     * @param address	IPaddress con cui il Client comunica con il server
	 * @param port		porta con cui il Client comunica con il server
     */
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
            handleLifeSkill(((Packet04LifeSkill) packet));
            break;
        case SERVERANSWER:
        	packet = new Packet05ServerAnswer(data);
            handleServerAnswer(((Packet05ServerAnswer) packet));
        }
    }

    /**
     * Questo metodo gestisce la risposta ricevuta dal server che indica la corretta connessione con
     * il GameServer. Solo una volta che la connessione è corretta viene avviato il gioco.
     * @param packet	Packet05ServerAnswer
     */
	private void handleServerAnswer(Packet05ServerAnswer packet) {
		game.setAnswer(true);
		System.out.println("è arrivata la risposa dal server");
		game.playscreen.setUsername(packet.getUsername());
		game.playscreen.connesso(packet.getSpawnX(), packet.getSpawnY());
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
    
    /**
     * Questo metodo gestisce il login di un nuovo giocatore al GameServer. Viene quindi creato all'interno del gioco
     * il Nemico che riceverà i comandi da remoto.
     * @param packet	Packet00Login del nuovo giocatore connesso 
     * @param address	IPaddress nuovo Client
	 * @param port		porta del nuovo Client
     */
    private void handleLogin(Packet00Login packet, InetAddress address, int port) {
    	 System.out.println("[" + address.getHostAddress() + ":" + port + "] " 
        		+ packet.getUsername() + " has joined the game...");
        game.playscreen.nuovoNemico(packet);
    }

    /**
     * Questo metodo gestisce il movimento controllato da remoto di un Nemico all'interno del gioco.
     * @param packet	Packet02Move del giocatore avversario
     */
    private void handleMove(Packet02Move packet) {
    	game.playscreen.movePlayers(packet.getUsername(), packet.getX(), 
    			packet.getY(), packet.getPosX(), packet.getPosY());

    }

    /**
     * Questo metodo gestisce lo sparare controllato da remoto di un Nemico all'interno del gioco. 
     * @param packet	Packet03Bullet 
     */
    private void handleBullet(Packet03Bullet packet) {
    	game.playscreen.firePlayers(packet.getUsername(), packet.getDirx(), packet.getDiry());
	}
    
    /**
     * Questo metodo gestisce Packet04LifeSkill assegnati ad un Nemico. Indica la perdita di vita del nemico
     * o l'attivazione di una skill.
     * @param packet	Packet04LifeSkill
     */
    private void handleLifeSkill(Packet04LifeSkill packet) {
    	game.playscreen.nemicoLifeSkill(packet.getUsername(), packet.getCode());
		
	}
    
}
