package com.mygdx.gamepb6.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.gamepb6.entities.Nemico;

/**
 * NemicoHandleInput gestisce gli input del del giocatore avversario che vengono ricevuti grazie a Packet02Move e ne associa gli impulsi applicati a Nemico 
 * per permettegli il movimento attraverso la mappa di gioco. Inoltre gestisce anche gli input provenienti dai pacchetti Packet04LifeSkill
 * che attivano le skill.
 */
public class NemicoHandleInput {

	private Body body;
	private int posX;
	private int posY;
	private boolean enableSkill;
	private Nemico nemico;
	
	/**
	 * @param nemico	Nemico a cui voglio assegnare la gestione degli input
	 */
	public NemicoHandleInput(Nemico nemico) {
		this.nemico = nemico;
		this.body = nemico.b2body;
		this.posX=0;
		this.posY=0;
		this.enableSkill=false;
	}
	

	public boolean up() {
		if (posX==0 && posY==1)
			return true;
		else
			return false;
	}
	
	public boolean down() {
		if (posX==0 && posY==-1)
			return true;
		else
			return false;
	}
	
	public boolean left() {
		if (posX==-1 && posY==0)
			return true;
		else
			return false;
	}
	
	public boolean right() {
		if (posX==1 && posY==0)
			return true;
		else
			return false;
	}
	
	public boolean still() {
		if (posX==0 && posY==0)
			return true;
		else
			return false;
	}
	
	
	/**
	 * Applica l'impulso adatto grazie a posX e posY che indicano la direzione in cui deve essere applicato
	 * @param posX	coordinata X contenuta nel pacchetto Packet02Move 
	 * @param posY	coordinata Y contenuta nel pacchetto Packet02Move 
	 */
	public void update(int posX, int posY) {
		setPos(posX, posY);
		if(up()) {
			this.body.setLinearVelocity(0f, 0.5f);
		}
		
		if(right()) {
			this.body.setLinearVelocity(0.5f, 0f);
		}
        
		if(left()) {
			this.body.setLinearVelocity(-0.5f, 0f);
		}
        
		if(down()) {
			this.body.setLinearVelocity(0f, -0.5f);
		}
       
        if(still()) {
        	this.body.setLinearVelocity(0f, 0f);
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
