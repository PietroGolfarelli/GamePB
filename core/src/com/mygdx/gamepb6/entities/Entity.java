package com.mygdx.gamepb6.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.gamepb6.MainGame;
import com.mygdx.gamepb6.bullet.Gun;
import com.mygdx.gamepb6.graphics.AnimationsPB;
import com.mygdx.gamepb6.screens.PlayScreen;
import com.mygdx.gamepb6.skills.Skills;

/**
 * Entity � la classe astratta che pone la base per il nostro Player e i nostri Nemici.
 * Principalmente crea il body grazie a cui posso avere collissioni e posso muovermi all'interno del gioco.
 */
public abstract class Entity extends Sprite {
	public enum State { STANDING, RUNNING, DEAD }
	public PlayScreen screen;
	protected World world;
	public int life;
	public Gun gun;
	public AnimationsPB animations;
	public Body b2body;
	public State currentState;
	public State previousState;
	private float stateTimer;
	private boolean playerIsDead;
	private float spawnX;
	private float spawnY;
	public String type;
	
	
	public Entity(PlayScreen screen, float spawnX, float spawnY, String type){
		this.screen = screen;
		this.world = screen.getWorld();  	
		this.life = 100;
		this.animations = new AnimationsPB();
		this.spawnX = spawnX;
		this.spawnY = spawnY;
		this.type = type;
		playerIsDead = false;
		defineplayer();

		currentState = State.STANDING;
		previousState = State.STANDING;

		setBounds(0, 0, 16 / MainGame.PPM, 16 / MainGame.PPM);
		setRegion(animations.getPlayerStand());
	}

	
	/**
	 * Aggiorno la posizione di Entity in base agli impulsi applicati sul body.
	 * Aggiorno anche l'animazione in base al tipo di movimento che l'Entity sta svolgendo.
	 * @param dt	tempo dettato dal render del gioco
	 */
	public void update(float dt){ 
		setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
		setRegion(getFrame(dt));
	}
	
	/**
	 * Creo il body che rappresenter� Entity e ne permettera la collissione con i vari oggetti della mappa.
	 * Ne definisco le dimensioni e la posizione dove viene creato (spawn ricevuto dal GameServer)
	 */
	public void defineplayer(){
		BodyDef bdef = new BodyDef();
		bdef.position.set(spawnX, spawnY);

		bdef.type = BodyDef.BodyType.DynamicBody;
		bdef.gravityScale= 0.0f;
		b2body = world.createBody(bdef);

		FixtureDef fdef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(6 / MainGame.PPM);
		fdef.shape = shape;
		b2body.createFixture(fdef).setUserData(type);

		EdgeShape head = new EdgeShape();
		head.set(new Vector2(-2 / MainGame.PPM, 6 / MainGame.PPM), new Vector2(2 / MainGame.PPM, 6 / MainGame.PPM));
		fdef.shape = head;
		fdef.isSensor = true;

		b2body.createFixture(fdef).setUserData(this);

	}
	
	/**
	 * Ricavo il frame dalla classe Animazione che � necessario per aggiornare e visualizzare 
	 * in maniera corretta Entity.
	 * @param dt	tempo dettato dal render del gioco
	 * @return		frame adatto allo stato in cui si trova Entity
	 */
	public TextureRegion getFrame(float dt){
		currentState = getState();
		TextureRegion region = null;
		switch(currentState){
		case DEAD:
			region = animations.getPlayerDead();
			break;
		case RUNNING:
			if((b2body.getLinearVelocity().x < 0) )
				region = animations.getAnimationLeft(stateTimer);

			else if((b2body.getLinearVelocity().x > 0))
				region = animations.getAnimationRight(stateTimer);

			else if((b2body.getLinearVelocity().y > 0))
				region = animations.getAnimationUp(stateTimer);

			else if((b2body.getLinearVelocity().y < 0))
				region = animations.getAnimationDown(stateTimer);

			else
				region = animations.getPlayerStand();
			break;
		case STANDING:
		default:
			region = animations.getPlayerStand();
			break;
		}

		stateTimer = currentState == previousState ? stateTimer + dt : 0;
		previousState = currentState;
		return region;
	}
	
	/**
	 * Se Entity ha finito la vita ritorner� lo stato Dead.
	 * Se Entity si sta muovendo ritorner� lo stato Running, altrimenti se Entity � fermo ritorner� lo stato Standing.
	 * @return	Stato in cui si trova l'Entity
	 */
	public State getState(){
		if(isDead())
			return State.DEAD;
		else if(b2body.getLinearVelocity().x != 0 || b2body.getLinearVelocity().y != 0)
			return State.RUNNING;
		else
			return State.STANDING;
	}
    
    public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}
	
	public boolean isDead(){
        return playerIsDead;
    }
	
	public float getStateTimer(){
		return stateTimer;
	}


	public float getbX(Entity e) {
		return e.b2body.getPosition().x;
	}

	public float getbY(Entity e) {
		return e.b2body.getPosition().y;
	}
	
	public State getCurrentState() {
		return this.currentState;
	}

	public void draw(Batch batch){
        super.draw(batch);
    }
	
	public float getSpawnX() {
		return spawnX;
	}

	public void setSpawnX(float spawnX) {
		this.spawnX = spawnX;
	}

	public float getSpawnY() {
		return spawnY;
	}

	public void setSpawnY(float spawnY) {
		this.spawnY = spawnY;
	}

}
