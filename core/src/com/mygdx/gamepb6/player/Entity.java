package com.mygdx.gamepb6.player;

import java.net.InetAddress;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
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
import com.mygdx.gamepb6.graphics.GraphicsPB;
import com.mygdx.gamepb6.input.HandleInput;
import com.mygdx.gamepb6.net.packets.Packet02Move;
import com.mygdx.gamepb6.net.packets.Packet03Bullet;
import com.mygdx.gamepb6.net.packets.Packet04LifeSkill;
import com.mygdx.gamepb6.player.Entity.State;
import com.mygdx.gamepb6.screens.PlayScreen;
import com.mygdx.gamepb6.skills.Skills;
import com.mygdx.gamepb6.graphics.AnimationsPB;


public class Entity extends Sprite {
	public enum State { STANDING, RUNNING, DEAD }
    public State currentState;
    public State previousState;
    private boolean playerIsDead;
    
    public InetAddress ipAddress;
    public int port;
    public String username;
    
    public Packet02Move packet;
    private float stateTimer;
    public World world;
    public Body b2body;
    
    public int posX;
    public int posY;
    public int dir;
    
    public int modifier = 8;
    public int xOffset = posX - modifier / 2;
    public int yOffset = posY - modifier / 2 - 4;
    
    public int life;
    
	public PlayScreen screen;
	
	public BitmapFont font1;
	public FreeTypeFontGenerator generator;
	public FreeTypeFontParameter parameter;
	private int dirx;
	private int diry;
	private Packet03Bullet packetB;
	
	private Random rnd;
	private Packet04LifeSkill packetLS;
	public Gun gun;
	public AnimationsPB animations;
	public HandleInput input;
	public Skills skills;
    
	
	public Entity(PlayScreen screen){
        this.screen = screen;
        this.world = screen.getWorld();
    	this.dirx=5;
    	this.diry=5;
    	
    	this.life = 100;
    	
    	this.gun= new Gun(this.screen.getHud());
    	this.animations = new AnimationsPB();
    	
    	
    	this.xOffset = posX - modifier / 2;
    	this.yOffset = posY - modifier / 2 - 4;
    	
        defineplayer();
        this.input = new HandleInput(this);
        this.skills= new Skills(this);
        
        currentState = State.STANDING;
        previousState = State.STANDING;
        
        setBounds(0, 0, 16 / MainGame.PPM, 16 / MainGame.PPM);
       
        setRegion(animations.getPlayerStand());
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
    
    
    public void handleInput(float dt){
    	if(this.currentState != State.DEAD) {
    		input.update();
    		sendPacket02Move();
    	}
    }
    
    

    public String getUsername() {
		return username;
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
            playerIsDead = true;
            Filter filter = new Filter();
          
            for (Fixture fixture : b2body.getFixtureList()) {
                fixture.setFilterData(filter);
            }
            b2body.applyLinearImpulse(new Vector2(0, 4f), b2body.getWorldCenter(), true);
        }
    }

    public boolean isDead(){
        return playerIsDead;
    }

    public void defineplayer(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(32 / MainGame.PPM, 32 / MainGame.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.gravityScale= 0.0f;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / MainGame.PPM);
        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData("Player");

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / MainGame.PPM, 6 / MainGame.PPM), new Vector2(2 / MainGame.PPM, 6 / MainGame.PPM));
        fdef.shape = head;
        fdef.isSensor = true;
        
        b2body.createFixture(fdef).setUserData(this);
        
    }
    
	
	public GraphicsPB getHud() {
		return this.screen.getHud();
	}
    
	
	public void skills() {
		skills.getSkill();
	}
    
    public void sendPacket04LifeSkill(int code) {
    	packetLS = new Packet04LifeSkill(this.getUsername(), code);
    	this.screen.game.socketClient.sendData(packetLS.getData());
    }
    
    public void sendPacket02Move() {
    	Packet02Move packet = new Packet02Move( this.username, input.getPosX(), input.getPosY(), 
    			getbX(this), getbY(this));
        this.screen.game.socketClient.sendData(packet.getData());
    }


	public void moreLife() {
    	this.life = this.life + 50;
    	this.screen.getHud().setLife(this.life);
    }
    
    public void colpito() {
    	this.life = this.life - 10;
    	this.screen.getHud().setLife(this.life);
    	sendPacket04LifeSkill(this.life);
    }
    
    public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}

	public void draw(Batch batch){
        super.draw(batch);
        
    }
    
}

