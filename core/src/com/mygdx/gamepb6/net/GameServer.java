package com.mygdx.gamepb6.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.ApplicationListener;
import com.mygdx.gamepb6.MainGame;
import com.mygdx.gamepb6.net.packets.Packet;
import com.mygdx.gamepb6.net.packets.Packet00Login;
import com.mygdx.gamepb6.net.packets.Packet01Disconnect;
import com.mygdx.gamepb6.net.packets.Packet02Move;
import com.mygdx.gamepb6.net.packets.Packet03Bullet;
import com.mygdx.gamepb6.net.packets.Packet04LifeSkill;
import com.mygdx.gamepb6.net.packets.Packet05ServerAnswer;
import com.mygdx.gamepb6.net.packets.Packet.PacketTypes;


/**
 * La classe GameServer rappresenta il server del gioco : la sua funzionalità è quella di 
 * permettere ai vari client (giocatori) di connettersi da remoto e giocare insieme. 
 * Di conseguenza molti dei metodi contenuti all'interno della classe mirano alla gestione 
 * dei pacchetti inviati dai client attraverso una connessione socket. 
 * Questi pacchetti vengono riconosciuti, smistati e infine gestiti dagli specifici metodi.
 * 
 */
public class GameServer extends Thread implements Runnable, ApplicationListener {
	private DatagramSocket socket;
	@SuppressWarnings("unused")
	private MainGame game;
	private List<Connessione> playerConnessi = new ArrayList<Connessione>();
	public boolean running=false;
	private Thread thread;
	private List<String> listUsername = new ArrayList<String>();
	private String username;
	private int spawnX[] = {1, 1, 6, 6};
	private int spawnY[] = {1, 3, 3, 1};
	private int countgiocatori = 0;


	public GameServer() {
		setRunning(true);
		try {
			this.socket = new DatagramSocket(1331);
		} catch (SocketException e) {
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
	 * Questo metodo ha l'obbiettivo di smistare i pacchetti che vengono ricevuti dal GameServer
	 * in modo da poterli indirizzare al metodo che potrà gestirli correttamente.
	 * 
	 * @param data		byte di dati del pachetto ricevuto
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
			contaPlayers();
			handleLogin((Packet00Login)packet, address, port);
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

	public void contaPlayers() {
		countgiocatori++;
		if (countgiocatori > 4) {
			countgiocatori = 1;
		}
	}

	public void addConnection(Packet00Login packet, Connessione conn) {
		sendToAllButSender(packet, conn);
		for (Connessione p : playerConnessi) {
			packet = new Packet00Login(p.getUsername(), p.getX(), p.getY());
			sendData(packet.getData(), conn.ipAddress, conn.port);  
		}
		playerConnessi.add(conn);
	}


	public void removeConnection(Packet01Disconnect packet) {
		if (getEntityMP(packet.getUsername()) != null) {
			int index = getEntityMPIndex(packet.getUsername());
			Connessione conn = this.playerConnessi.get(index);

			sendToAllButSender(packet, conn);
			playerConnessi.remove(conn);
		}
	}

	public void sendToSender(Packet05ServerAnswer packet, Connessione conn) {
		for (Connessione p : playerConnessi) {
			if (p.ipAddress == conn.ipAddress)
				sendData(packet.getData(), p.ipAddress, p.port);
			break;
		}
	}


	/**
	 *  Questo metodo ha il compito di gestire il login dei client quindi l'accesso dei nuovi giocatori.
	 *  Questa classe svolge diversi compiti:
	 * 	- istanzia un oggetto Connessione corrispondente al Client, in modo da poterlo riconoscere e gestire in futuro.
	 * 	- manda un pacchetto di risposta al Client con posizione di spawn del giocatore
	 * 	- comunica a tutti gli altri giocatori connessi (se presenti) la presenza di un nuovo giocatore
	 * 		
	 * @param packet	pacchetto di login arrivato al server
	 * @param address	IPaddress del Client
	 * @param port		porta del Client
	 */
	public void handleLogin(Packet00Login packet, InetAddress address, int port) {
		String usernameCorretto = controlUsername(packet.getUsername());
		packet.setUsername(usernameCorretto);
		System.out.println("[" + address.getHostAddress() + ":" + port + "] "
				+ usernameCorretto + " has connected...");
		Connessione conn = new Connessione(packet.getUsername(), address, port);
		packet.setX(getSpawnX());
		packet.setY(getSpawnY());
		conn.setX(getSpawnX());
		conn.setY(getSpawnY());
		addConnection((Packet00Login) packet, conn);
		Packet05ServerAnswer packet05 = new Packet05ServerAnswer(getSpawnX(),getSpawnY(), packet.getUsername());
		sendData(packet05.getData(), address, port);
	}

	/**
	 * Questo metodo ha l'obbiettivo di gestire i pacchetti Packet04LifeSkill.
	 * Il compito di questo metodo é quello di inoltrare a tutti i client connessi tranne al mittente il 
	 * pacchetto Packet04LifeSkill
	 * @see Packet04LifeSkill 
	 * @param packet 	Packet04LifeSkill
	 */
	private void handleLifeSkill(Packet04LifeSkill packet) {
		if (getEntityMP(packet.getUsername()) != null) {
			int index = getEntityMPIndex(packet.getUsername());
			Connessione conn = this.playerConnessi.get(index);
			sendToAllButSender(packet, conn);
		}
	}
	
	/**
	 * Questo metodo ha l'obbiettivo di gestire i pacchetti Packet03Bullet.
	 * Il compito di questo metodo é quello di inoltrare a tutti i client connessi tranne al mittente il 
	 * pacchetto Packet03Bullet
	 * @param packet	Packet03Bullet
	 */
	void handleBullet(Packet03Bullet packet) {
		if (getEntityMP(packet.getUsername()) != null) {
			int index = getEntityMPIndex(packet.getUsername());
			Connessione conn = this.playerConnessi.get(index);
			sendToAllButSender(packet, conn);
		}
	}

	/**
	 * Questo metodo ha l'obbiettivo di gestire i pacchetti Packet02Move.
	 * Il compito di questo metodo é quello di inoltrare a tutti i client connessi tranne al mittente il 
	 * pacchetto Packet02Move e di aggiornare la Connessione del Client che si sta muovendo
	 * @see updateConnessione
	 * @param packet	Packet02Move 
	 */
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

	/**
	 * Questo metodo aggiorna le Connessioni utilizzando i pacchetti Packet02Move in questo modo
	 * si avrà un posizione sempre aggiornata per lo spawn dei giocatori già presenti nel gioco alla connessione 
	 * di un nuovo client
	 * @param packet	Packet02Move
	 * @param conn		Connessione riferita al Client che sta cambiando posizione
	 */
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
	
	/**
	 * Questo metodo controlla e in caso corregge gli username in modo che non si creino duplicati e non ci siano problemi
	 * nel riconoscere il client corretto.
	 * @param username		username con cui il client vuole connettersi al gioco
	 * @return				stringa con lo username non duplicato
	 */
	public String controlUsername(String username){
		String newusername;
		boolean univoco = true;

		for (Connessione c : playerConnessi) {
			if (c.getUsername().equals(username)) {
				univoco = false;
				break;
			}
		}
		if (univoco == false)
			newusername = newUsername(username);
		else 
			newusername = username;
		return newusername;
	}
	
	/**
	 * Modifica la stringa in input e controlla che non sia uno username già esistente.
	 * @param username	stringa in input da modificare
	 * @return			username modificato
	 */
	public String newUsername(String username) {
		String newusername;
		newusername = username+"1";
		newusername = controlUsername(newusername);
		return newusername;
	}
	
	public void addUsername(String username) {
		listUsername.add(username);
	}

	public String getUsername() {
		return username;
	}

	

	public int getSpawnX() {
		return spawnX[countgiocatori - 1];
	}


	public int getSpawnY() {
		return spawnY[countgiocatori - 1];
	}

	public boolean isRunning() {
		return running;
	}


	public void setRunning(boolean running) {
		this.running = running;
	}

	@Override
	public void create() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void render() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}



}