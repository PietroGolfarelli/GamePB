package com.mygdx.gamepb6.player;

import java.net.InetAddress;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.gamepb6.MainGame;
import com.mygdx.gamepb6.net.packets.Packet02Move;
import com.mygdx.gamepb6.screens.PlayScreen;

public class Nemico extends Sprite {
	public enum State { STANDING, RUNNING, DEAD }
	public State currentState;
	public State previousState;
	private boolean marioIsDead;

	public Packet02Move packet;

	private TextureRegion marioDead;

	private TextureRegion marioStand;
	@SuppressWarnings("rawtypes")
	private Animation marioRunLeft;
	@SuppressWarnings("rawtypes")
	private Animation marioRunRight;
	@SuppressWarnings("rawtypes")
	private Animation marioRunUp;
	@SuppressWarnings("rawtypes")
	private Animation marioRunDown;


	private float stateTimer;
	public World world;
	public Body b2body;

	public int x;
	public int y;
	public int dir;

	public PlayScreen screen;

	public InetAddress ipAddress;
	public int port;
	public String username;
	public int posizioneX;
	public int posizioneY;
	public float startX;
	public float startY;
	public int life;
	public Gun gun;

	
	public Nemico(PlayScreen screen, String username, InetAddress ipAddress, int port, float startX, float startY) {
		this.ipAddress = ipAddress;
		this.port = port;
		this.username = username;
		this.screen = screen;
		this.world = screen.getWorld();
		this.startX= startX;
		this.startY= startY;
		this.life = 100;
		this.gun= new Gun();


		currentState = State.STANDING;
		previousState = State.STANDING;

		defineMario();
		//set initial values for marios location, width and height. And initial frame as marioStand.
		setBounds(super.getX(), super.getY(), 16 / MainGame.PPM, 16 / MainGame.PPM);

		/*ANIMATIONS*/
		Array<TextureRegion> frames = new Array<TextureRegion>();
		for(int i = 0; i < 3; i++)
			frames.add(new TextureRegion(screen.getAtlas().findRegions("Left").get(i), i * 0, 0, 16, 16));
		marioRunLeft = new Animation(0.2f, frames);
		frames.clear();

		for(int i = 0; i < 3; i++)
			frames.add(new TextureRegion(screen.getAtlas().findRegions("Right").get(i), 0, 0, 16, 16));
		marioRunRight = new Animation(0.1f, frames);
		frames.clear();

		for(int i = 0; i < 3; i++)
			frames.add(new TextureRegion(screen.getAtlas().findRegions("Up").get(i), 0, 0, 16, 16));
		marioRunUp = new Animation(0.1f, frames);
		frames.clear();

		for(int i = 0; i < 3; i++)
			frames.add(new TextureRegion(screen.getAtlas().findRegions("Down").get(i), 0, 0, 16, 16));
		marioRunDown = new Animation(0.1f, frames);
		frames.clear();

		//create texture region for mario standing
		marioStand = new TextureRegion(screen.getAtlas().findRegion("Standings"), 0, 0, 16, 16);
		//create dead mario texture region
		marioDead = new TextureRegion(screen.getAtlas().findRegion("Standings"), 96, 0, 16, 16);

		setRegion(marioStand);

	}

	

	public void update(float dt){ 
		setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
		setRegion(getFrame(dt));
	}

	public TextureRegion getFrame(float dt){
		currentState = getState();
		TextureRegion region = null;

		switch(currentState){
		case DEAD:
			region = marioDead;
			break;
		case RUNNING:
			if((b2body.getLinearVelocity().x < 0) ){
				//region.flip(true, false);
				region = (TextureRegion) marioRunLeft.getKeyFrame(stateTimer, true);          
			}
			else if((b2body.getLinearVelocity().x > 0)){
				//region.flip(true, false);
				region = (TextureRegion) marioRunRight.getKeyFrame(stateTimer, true);                  
			}
			else if((b2body.getLinearVelocity().y > 0)){
				//region.flip(true, false);
				region = (TextureRegion) marioRunUp.getKeyFrame(stateTimer, true);     
			}
			else if((b2body.getLinearVelocity().y < 0)){
				//region.flip(true, false);
				region = (TextureRegion) marioRunDown.getKeyFrame(stateTimer, true);     
			}
			else {
				region = (TextureRegion) marioStand;
			}
			break;
		case STANDING:
		default:
			region = marioStand;
			break;
		}

		stateTimer = currentState == previousState ? stateTimer + dt : 0;
		previousState = currentState;
		//return our final adjusted frame
		return region;
	}

	public float getBOXx() {
		return (float) this.b2body.getPosition().x;
	}

	public float getBOXy() {
		return (float) this.b2body.getPosition().y;
	}


	public void checkX(float x) {
		if (this.getBOXx() != x) {
			if (this.getBOXx() < x) {
				this.b2body.setLinearVelocity(0.5f, 0f);

			}
			else {
				this.b2body.setLinearVelocity(-0.5f, 0f);
			}	        		
		}
	}

	public void checkY(float y) {
		if (this.getBOXy() != y) {
			if (this.getBOXy() < y) {
				this.b2body.setLinearVelocity(0f, 0.5f);
			}
			else {
				this.b2body.setLinearVelocity(0f, -0.5f);
			}	        		
		}
	}

	
	public void handleInput(int posizioneX, int posizioneY, float x, float y){

		if(this.currentState != State.DEAD ){

			if (posizioneX==0 && posizioneY==1) { 
				this.b2body.setLinearVelocity(0f, 0.5f);

			}
			if (posizioneX==1 && posizioneY==0) { 
				this.b2body.setLinearVelocity(0.5f, 0f);

			}
			if (posizioneX==-1 && posizioneY==0) { 
				this.b2body.setLinearVelocity(-0.5f, 0f);

			}
			if (posizioneX==0 && posizioneY==-1) { 
				this.b2body.setLinearVelocity(0f, -0.5f);

			}
			if(posizioneX==0 && posizioneY==0) {
				this.b2body.setLinearVelocity(0f, 0f);

			}
		}
	}
	
	public float getbX(Nemico nemico) {
    	return nemico.b2body.getPosition().x;
    }
    
    public float getbY(Nemico nemico) {
    	return nemico.b2body.getPosition().y;
    }

	public float getStateTimer(){
		return stateTimer;
	}

	public State getState(){
		if(isDead())
			return State.DEAD;
		else if(b2body.getLinearVelocity().x != 0 || b2body.getLinearVelocity().y != 0)
			return State.RUNNING;
		else
			return State.STANDING;
	}

	public void die() {
		if (!isDead()) {
			marioIsDead = true;
			Filter filter = new Filter();
			//filter.maskBits = MainGame.NOTHING_BIT;

			for (Fixture fixture : b2body.getFixtureList()) {
				fixture.setFilterData(filter);
			}
			b2body.applyLinearImpulse(new Vector2(0, 4f), b2body.getWorldCenter(), true);
		}
	}

	public boolean isDead(){
		return marioIsDead;
	}

	public void defineMario(){
		BodyDef bdef = new BodyDef();
		if (startX != 0 && startY!= 0)
			bdef.position.set(startX /*/ MainGame.PPM*/, startY /*/ MainGame.PPM*/);
		else
			bdef.position.set(32 / MainGame.PPM, 32 / MainGame.PPM);
		bdef.type = BodyDef.BodyType.DynamicBody;
		bdef.gravityScale= 0.0f;
		b2body = world.createBody(bdef);

		FixtureDef fdef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(6 / MainGame.PPM);
		fdef.shape = shape;
		b2body.createFixture(fdef).setUserData(this);

		EdgeShape head = new EdgeShape();
		head.set(new Vector2(-2 / MainGame.PPM, 6 / MainGame.PPM), new Vector2(2 / MainGame.PPM, 6 / MainGame.PPM));
		//fdef.filter.categoryBits = MainGame.MARIO_HEAD_BIT;
		fdef.shape = head;
		fdef.isSensor = true;

		b2body.createFixture(fdef).setUserData(this);
	}


	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setLife(int life) {
		this.life = life;
		if (this.life == 0) {
			System.out.println(this.username + " è morto");
		}
	}
	
	public void moreLife() {
    	this.life = this.life + 50;
    }
	
	public void setSkills(int skillType) {
		if (skillType == -1) {
			moreLife();
		}

		if (skillType == -2) {
			gun.shootBigger();

		}

		if (skillType == -3) {
			gun.setNumeroBullets(gun.getNumeroBullets() + 30);
		}

		if (skillType == -4) {
			gun.shootFaster();
		}

	}
	
	

	public void draw(Batch batch){
		super.draw(batch);
	}	
}
