package com.mygdx.gamepb6.skills;

import com.mygdx.gamepb6.player.Entity;

public class Skills {
	
	private Entity player;

	public Skills(Entity player) {
		this.player = player;
	}
	
	public void skillType(int skillType) {
		if (skillType == -1) {
    		moreLife();
    		player.screen.getHud().setLife(player.getLife());
    		player.getHud().setMessaggio("LIFEPOINTS AUMENTATI");
    		player.gun.updateHud();
    	}
    	
    	if (skillType == -2) {
    		shootBigger();
    		player.getHud().setMessaggio("BIG-BULLET ACTIVATED");
    	}
    	
    	if (skillType == -3) {
    		ricarica();
    		player.getHud().setMessaggio("RICARICA BULLET EFFETTUATA");
    		player.gun.updateHud();
    	}
    	
    	if (skillType == -4) {
    		shootFaster();
    		player.getHud().setMessaggio("SPEED-BULLET ACTIVATED");
    	}
    	
    	player.input.setEnableSkill(false);
    	player.sendPacket04LifeSkill(skillType);
	}
	
	public void shootFaster() {
		player.gun.shootFaster();
	}
	
	public void ricarica() {
		player.gun.setNumeroBullets(player.gun.getNumeroBullets() + 30);
	}
	
	
	public void shootBigger() {
		player.gun.shootBigger();
	}
	
	public void moreLife() {
		player.setLife(player.getLife() + 50 );
    }
	
	public void getSkill() {
		skillType(randomNumber());
	}
	
	public int randomNumber() {
		int max = 5;
		int min = 1;
		
		int r=(int) ((Math.random() * (max - min)) + min);
		return -r;
	}
	
}
