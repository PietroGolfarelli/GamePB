package com.mygdx.gamepb6.entities;

import com.mygdx.gamepb6.bullet.Gun;
import com.mygdx.gamepb6.input.NemicoHandleInput;
import com.mygdx.gamepb6.screens.PlayScreen;
import com.mygdx.gamepb6.skills.Skills;

public class Nemico extends Entity{

	public String username;
	private float startX;
	private float startY;
	private NemicoHandleInput input;
	public Skills skills;
	private boolean vabene;
	private static String type = "Nemico";

	public Nemico(PlayScreen screen, String username, float startX, float startY) {
		super(screen, startX, startY, type);
		this.username = username;
		this.startX= startX;
		this.startY= startY;
		this.gun= new Gun();
		this.input = new NemicoHandleInput(this);
		this.skills= new Skills(this);
		this.vabene = true;
		// TODO Auto-generated constructor stub
	}
	
	

	public void handleInput(int posX, int posY){
		input.update(posX, posY);
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public boolean isVabene() {
		return vabene;
	}

	public void setVabene(boolean vabene) {
		this.vabene = vabene;
	}
	
    public void redefine(){
        super.world.destroyBody(b2body);
        defineplayer();
        this.setVabene(true);
    }


}
