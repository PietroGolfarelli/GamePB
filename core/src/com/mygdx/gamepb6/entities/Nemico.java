package com.mygdx.gamepb6.entities;

import com.mygdx.gamepb6.bullet.Gun;
import com.mygdx.gamepb6.input.NemicoHandleInput;
import com.mygdx.gamepb6.screens.PlayScreen;
import com.mygdx.gamepb6.skills.Skills;


/**
 * Nemico è l'oggetto controllato dal giocatore avversario. La definizione del suo body e delle sue animazioni è
 * gestita dalla classe Entity che questa classe estende. Nemico inizializza NemicoHandleInput in modo da potersi
 * muovere, Gun in modo da poter sparare i Bullet e NemicoSkills per attivare eventuali abilità.
 */
public class Nemico extends Entity{
	
	public String username;
	@SuppressWarnings("unused")
	private float startX;
	@SuppressWarnings("unused")
	private float startY;
	private NemicoHandleInput input;
	public Skills skills;
	private boolean vabene;
	private static String type = "Nemico";
	
	/**
	 * @param screen	schermo che contiene la mappa di gioco
	 * @param username	nome scelto dal giocatore avversario
	 * @param startX	coordianata x di creazione comunicata dal GameServer
	 * @param startY	coordianata y di creazione comunicata dal GameServer
	 */
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
	
	/**
	 * Questo metodo è necessario in modo da poter rimediare ad una posizione 
	 * sbagliata del Nemico distruggendolo e ridefinirlo nella posizione corretta.
	 */
    public void redefine(){
        super.world.destroyBody(b2body);
        defineplayer();
        this.setVabene(true);
    }


}
