package com.mygdx.gamepb6.graphics;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * 
 * Animation è una classe che permette la gestione delle animazioni del personaggio a seconda degli eventi che si verificano.
 * Vengono dichiarati i campi inerenti alle animazioni del personaggio e allo corrispettivo stato
 * 
 * @author Zanni Davide 
 * @author Golfarelli Pietro
 */
public class AnimationsPB {
	public enum State { STANDING, LEFT, RIGHT, DEAD }
	public State currentState;
	private Animation animation;


	private TextureAtlas atlas;
	private TextureRegion playerDead;
	private TextureRegion playerStand;
	private Animation playerRunLeft;
	private Animation playerRunRight;
	private Animation playerRunUp;
	private Animation playerRunDown;
	private Array<TextureRegion> frames = new Array<TextureRegion>();;

	/**
	 * Costruttore che definisce le risorse del sistema utili per la creazione dell'oggetto. 
	 * Viene definito l'ogggetto dell'immagine del player.
	 */
	public AnimationsPB() {
		this.atlas = new TextureAtlas("Character.atlas");
		this.playerStand = playerStand(atlas);
		this.playerDead = playerDead(atlas);
		this.playerRunLeft = playerRunLeft (atlas);
		this.playerRunRight = playerRunRight (atlas);
		this.playerRunUp = playerRunUp (atlas);
		this.playerRunDown = playerRunDown (atlas);
	}



	/**
	 * Metodo che definisce l'array contenente i frame relativi alla corsa verso sinistra. 
	 */
	private Animation playerRunLeft(TextureAtlas atlas) {
		for(int i = 0; i < 3; i++)
			frames.add(new TextureRegion(atlas.findRegions("Left").get(i), i * 0, 0, 16, 16));
		animation = new Animation(0.2f, frames);
		frames.clear();
		return animation;
	}

	/**
	 * Metodo che definisce l'array contenente i frame relativi alla corsa verso destra.
	 */
	private Animation playerRunRight(TextureAtlas atlas) {
		for(int i = 0; i < 3; i++)
			frames.add(new TextureRegion(atlas.findRegions("Right").get(i), i * 0, 0, 16, 16));
		animation = new Animation(0.2f, frames);
		frames.clear();
		return animation;
	}

	/**
	 * Metodo che definisce l'array contenente i frame relativi alla corsa verso l'alto
	 */
	private Animation playerRunUp(TextureAtlas atlas) {
		for(int i = 0; i < 3; i++)
			frames.add(new TextureRegion(atlas.findRegions("Up").get(i), i * 0, 0, 16, 16));
		animation = new Animation(0.2f, frames);
		frames.clear();
		return animation;
	}

	/**
	 * Metodo che definisce l'array contenente i frame relativi alla corsa verso il basso 
	 */
	private Animation playerRunDown(TextureAtlas atlas) {
		for(int i = 0; i < 3; i++)
			frames.add(new TextureRegion(atlas.findRegions("Down").get(i), i * 0, 0, 16, 16));
		animation = new Animation(0.2f, frames);
		frames.clear();
		return animation;
	}

	/**
	 * Metodo che definisce l'oggetto quando il player rimane fermo
	 */
	public TextureRegion playerStand(TextureAtlas atlas){
		return new TextureRegion(atlas.findRegion("Standings"), 0, 0, 16, 16);
	}

	/**
	 * Metodo che definisce l'oggetto quando il player muore
	 */
	public TextureRegion playerDead(TextureAtlas atlas){
		return new TextureRegion(atlas.findRegion("Standings"), 0, 0, 16, 16);
	}


	public TextureRegion getPlayerStand(){
		return playerStand;
	}

	public TextureRegion getPlayerDead(){
		return playerDead;
	}

	public TextureRegion getAnimationLeft(float stateTimer) {
		return (TextureRegion) playerRunLeft.getKeyFrame(stateTimer, true);
	}
	public TextureRegion getAnimationRight(float stateTimer) {
		return (TextureRegion) playerRunRight.getKeyFrame(stateTimer, true);
	}
	public TextureRegion getAnimationUp(float stateTimer) {
		return (TextureRegion) playerRunUp.getKeyFrame(stateTimer, true);
	}
	public TextureRegion getAnimationDown(float stateTimer) {
		return (TextureRegion) playerRunDown.getKeyFrame(stateTimer, true);
	}






}
