package com.mygdx.gamepb6.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.gamepb6.net.packets.Packet02Move;
import com.mygdx.gamepb6.entities.Player;

/**
 * PlayerHandleInput gestisce gli input da tastiera del giocatore e ne associa gli impulsi applicati a Player 
 * per permettegli il movimento attraverso la mappa di gioco. Inoltre gestisce anche gli input provenienti dalle Casse
 * che attivano le skill.
 */
public class PlayerHandleInput {
	private Body body;
	private int posX;
	private int posY;
	private boolean enableSkill;
	private Player player;
	
	/**
	 * @param player	Player a cui voglio assegnare la gestione degli input
	 */
	public PlayerHandleInput(Player player) {
		this.player = player;
		this.body = player.b2body;
		this.posX=0;
		this.posY=0;
		this.enableSkill=false;
		update();
	}
	

	public boolean up() {
		return Gdx.input.isKeyPressed(Input.Keys.UP);
	}
	
	public boolean down() {
		return  Gdx.input.isKeyPressed(Input.Keys.DOWN);
	}
	
	public boolean left() {
		return Gdx.input.isKeyPressed(Input.Keys.LEFT);
	}
	
	public boolean right() {
		return Gdx.input.isKeyPressed(Input.Keys.RIGHT);
	}
	
	public boolean still() {
		return (!Gdx.input.isKeyPressed(Input.Keys.ANY_KEY));
	}
	public boolean skillKey() {
		return Gdx.input.isKeyJustPressed(Input.Keys.SPACE);
	}
	
	/**
	 * Questo metodo viene chiamato ogni volta che si esegue il render del gioco e controlla la presenza di input che 
	 * può gestire.
	 */
	public void update() {
		if(up()) {
			this.body.setLinearVelocity(0f, 0.5f);
			setPos(0, 1);
		}
		
		if(right()) {
			this.body.setLinearVelocity(0.5f, 0f);
			setPos(1, 0);
		}
        
		if(left()) {
			this.body.setLinearVelocity(-0.5f, 0f);
			setPos(-1, 0);
		}
        
		if(down()) {
			this.body.setLinearVelocity(0f, -0.5f);
			setPos(0, -1);
		}
       
        if(still()) {
        	this.body.setLinearVelocity(0f, 0f);
        	setPos(0, 0);
        }
        
        if(skillKey() && enableSkill == true) {
        	player.skills();
        }
	}
	
	
	
	public void setPos(int posX, int posY) {
		setPosX(posX);
		setPosY(posY);
	}
	
	
	public int getPosX() {
		return posX;
	}

	
	public void setPosX(int posX) {
		this.posX = posX;
	}
	
	public int getPosY() {
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}
	
	public boolean isEnableSkill() {
		return enableSkill;
	}

	public void setEnableSkill(boolean enableSkill) {
		this.enableSkill = enableSkill;
	}
	

}
