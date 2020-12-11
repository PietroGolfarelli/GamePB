package com.mygdx.gamepb6.entities;

import com.mygdx.gamepb6.bullet.Gun;
import com.mygdx.gamepb6.graphics.GraphicsPB;
import com.mygdx.gamepb6.input.PlayerHandleInput;
import com.mygdx.gamepb6.net.packets.Packet02Move;
import com.mygdx.gamepb6.net.packets.Packet04LifeSkill;
import com.mygdx.gamepb6.screens.PlayScreen;
import com.mygdx.gamepb6.skills.PlayerSkills;

public class Player extends Entity {

	public PlayerHandleInput input;
	private String username;
	private PlayerSkills skills;
	private static String type = "Player";

	public Player(PlayScreen screen, String username, float spawnX, float spawnY) {
		super(screen, spawnX, spawnY, type);
		this.input = new PlayerHandleInput(this);
		this.gun= new Gun(getHud());
		this.skills= new PlayerSkills(this);
		this.username = username;
	}
	
	
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
    
    public void colpito() {
    	life = life - 10;
    	screen.getHud().setLife(life);
    	sendPacket04LifeSkill(life);
    }
    
    public void skills() {
		skills.getSkill();
	}
}
