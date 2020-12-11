package com.mygdx.gamepb6.skills;

import com.mygdx.gamepb6.entities.Entity;

public class Skills {
	
	private Entity entity;

	public Skills(Entity entity) {
		this.entity = entity;
	}
	
	public void skillType(int skillType) {
		if (skillType == -1) {
    		moreLife();
    		/*entity.screen.getHud().setLife(entity.getLife());
    		entity.getHud().setMessaggio("LIFEPOINTS AUMENTATI");
    		entity.gun.updateHud();*/
    	}
    	
    	if (skillType == -2) {
    		shootBigger();
    		//entity.getHud().setMessaggio("BIG-BULLET ACTIVATED");
    	}
    	
    	if (skillType == -3) {
    		ricarica();
    		/*entity.getHud().setMessaggio("RICARICA BULLET EFFETTUATA");
    		entity.gun.updateHud();*/
    	}
    	
    	if (skillType == -4) {
    		shootFaster();
    		//entity.getHud().setMessaggio("SPEED-BULLET ACTIVATED");
    	}
    	
    	//entity.input.setEnableSkill(false);
    	//entity.sendPacket04LifeSkill(skillType);
	}
	
	public void shootFaster() {
		entity.gun.shootFaster();
	}
	
	public void ricarica() {
		entity.gun.setNumeroBullets(entity.gun.getNumeroBullets() + 30);
	}
	
	
	public void shootBigger() {
		entity.gun.shootBigger();
	}
	
	public void moreLife() {
		entity.setLife(entity.getLife() + 50 );
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
