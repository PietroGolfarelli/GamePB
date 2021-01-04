package com.mygdx.gamepb6.skills;

import com.mygdx.gamepb6.entities.Entity;

/**
 * Skills contiene la lista di abilità che possono essere attivate da un Entity. Queste vanno infatti a modificare vita e 
 * bullets delle Entity. Le Skill possono essere attivate solo premendo la barra spaziatrice in prossimita di una cassa che
 * si trova sulla mappa di gioco. Una cassa può essere utilizzata una sola volta da ogni giocatore.
 */
public class Skills {
	
	private Entity entity;
	private Integer num = 0;

	public Skills(Entity entity) {
		this.entity = entity;
	}
	
	/**
	 * attraverso ad un numero negativo passato come parametro viene attivata una certa skill rispetto ad un'altra.
	 * @param skillType		numero int negativo che indica la skill da attivare
	 */
	public void skillType(int skillType) {
		if (skillType == -1) {
    		moreLife();
    	}
    	
    	if (skillType == -2) {
    		shootBigger();
    	}
    	
    	if (skillType == -3) {
    		ricarica();
    	}
    	
    	if (skillType == -4) {
    		shootFaster();
    	}
    	
	}
	
	/**
	 * Incrementa la velocità dei bullets.
	 */
	public void shootFaster() {
		entity.gun.shootFaster();
	}
	
	/**
	 * Incrementa il numero di bullets di 30.
	 */
	public void ricarica() {
		entity.gun.setNumeroBullets(entity.gun.getNumeroBullets() + 30);
	}
	
	/**
	 * Incrementa la dimensione dei bullets.
	 */
	public void shootBigger() {
		entity.gun.shootBigger();
	}
	
	/**
	 * Incrementa il valore della vita di 50.
	 */
	public void moreLife() {
		entity.setLife(entity.getLife() + 50 );
    }
	
	
	public void getSkill() {
		//skillType(randomNumber());
		skillType(nextNumber());
	}
	
	public int nextNumber() {
		num = num-1;
		if (num < -4)
			num = randomNumber();
		return num;
	}
		
	public int randomNumber() {
		int max = 5;
		int min = 1;
		int r=(int) ((Math.random() * (max - min)) + min);
		return -r;
	}
	
}
