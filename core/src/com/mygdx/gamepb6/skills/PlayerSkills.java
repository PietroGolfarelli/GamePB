package com.mygdx.gamepb6.skills;

import com.mygdx.gamepb6.entities.Player;

/**
 * 
 * PlayerSkills permette ad un Player di utilizzare delle skill se queste vengono attivate cliccando la barra spaziatrice
 * in prossimità delle Casse sulla mappa di gioco. Estende Skill e il suo comportamento è in gran parte simile alla suddetta
 * classe, la differenza sostanziale sta nell'aggiornamento dell'elemento di grafica Hud che può avvenire solo da parte 
 * di un Player.
 *
 */
public class PlayerSkills extends Skills{

	private Player player;
	
	/**
	 * 
	 * @param player 	Player a cui voglio assegnare questa gestione dellle skill
	 */
	public PlayerSkills(Player player) {
		super(player);
		this.player = player;
	}
	
	
	@Override
	public void skillType(int skillType) {
		if (skillType == -1) {
    		moreLife();
    		player.getHud().setLife(player.getLife());
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

}
