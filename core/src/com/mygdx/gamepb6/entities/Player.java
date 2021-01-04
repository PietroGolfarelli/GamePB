package com.mygdx.gamepb6.entities;

import com.badlogic.gdx.Screen;
import com.mygdx.gamepb6.bullet.Gun;
import com.mygdx.gamepb6.graphics.GraphicsPB;
import com.mygdx.gamepb6.input.PlayerHandleInput;
import com.mygdx.gamepb6.net.packets.Packet01Disconnect;
import com.mygdx.gamepb6.net.packets.Packet02Move;
import com.mygdx.gamepb6.net.packets.Packet04LifeSkill;
import com.mygdx.gamepb6.screens.GameOver;
import com.mygdx.gamepb6.screens.PlayScreen;
import com.mygdx.gamepb6.skills.PlayerSkills;

/**
 * Player è l'oggetto controllato dal giocatore. La definizione del suo body e delle sue animazioni è
 * gestita dalla classe Entity che questa classe estende. Player inizializza PlayerHandleInput in modo da potersi
 * muovere, Gun in modo da poter sparare i Bullet e PlayerSkills per attivare eventuali abilità.
 */
public class Player extends Entity {

	public PlayerHandleInput input;
	private String username;
	private PlayerSkills skills;
	private static String type = "Player";
	
	/**
	 * @param screen	schermo che contiene la mappa di gioco
	 * @param username	nome scelto dal giocatore
	 * @param spawnX	coordianata x di creazione comunicata dal GameServer
	 * @param spawnY	coordianata y di creazione comunicata dal GameServer
	 */
	public Player(PlayScreen screen, String username, float spawnX, float spawnY) {
		super(screen, spawnX, spawnY, type);
		this.input = new PlayerHandleInput(this);
		this.gun= new Gun(getHud());
		this.skills= new PlayerSkills(this);
		this.username = username;
	}
	
	/**
	 * Richiama la classe PlayerHandleInput in modo da poter muovere il giocatore e invia il pacchetto Packet02Move
	 * al GameServer in modo da segnalare il movimento
	 * @param dt tempo dettato dal render del gioco
	 */
	public void handleInput(float dt){
			input.update();
			sendPacket02Move();
	}
	
	public GraphicsPB getHud() {
		return screen.getHud();
	}

	
	public void sendPacket04LifeSkill(int code) {
    	Packet04LifeSkill packetLS = new Packet04LifeSkill(this.getUsername(), code);
    	this.screen.game.socketClient.sendData(packetLS.getData());
    }
    
    public String getUsername() {
		return this.username;
	}
    
	public void sendPacket02Move() {
    	Packet02Move packet = new Packet02Move( this.username, input.getPosX(), input.getPosY(), 
    			getbX(this), getbY(this));
        this.screen.game.socketClient.sendData(packet.getData());
    }
    
	/**
	 * In caso il Player venga colpito da un Bullet allora decremento la sua vita di 10 e lo comunico al Server
	 */
    public void colpito() {
    	life = life - 10;
    	screen.getHud().setLife(life);
    	sendPacket04LifeSkill(life);
    	checkLife();
    }
    
    /**
     * Controllo se la vita del Player è a zero. In quel caso pongo il Player nello stato Dead ed è GameOver.
     */
    private void checkLife() {
		if (getLife() == 0) {
			this.currentState = State.DEAD;
			screen.game.died();
		}
		
	}

	public void skills() {
		skills.getSkill();
	}
	

}
