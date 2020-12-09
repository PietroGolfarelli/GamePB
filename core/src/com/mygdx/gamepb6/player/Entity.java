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
import com.mygdx.gamepb6.net.packets.Packet02Move;
import com.mygdx.gamepb6.net.packets.Packet03Bullet;
import com.mygdx.gamepb6.net.packets.Packet04LifeSkill;
import com.mygdx.gamepb6.player.Entity.State;
import com.mygdx.gamepb6.screens.PlayScreen;


public class Entity extends Sprite {
	public enum State { STANDING, RUNNING, DEAD }
    public State currentState;
    public State previousState;
    private boolean playerIsDead;
    
    public InetAddress ipAddress;
    public int port;
    public String username;
    
    public Packet02Move packet;
    
    private TextureRegion playerDead;
    
	private TextureRegion playerStand;
	@SuppressWarnings("rawtypes")
	private Animation playerRunLeft;
    @SuppressWarnings("rawtypes")
	private Animation playerRunRight;
    @SuppressWarnings("rawtypes")
	private Animation playerRunUp;
    @SuppressWarnings("rawtypes")
	private Animation playerRunDown;
    
    
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
	
	public boolean enableSkill;
	private Random rnd;
	private Packet04LifeSkill packetLS;
	public Gun gun;
    
	
	public Entity(PlayScreen screen){
        this.screen = screen;
        this.world = screen.getWorld();
        this.posX=0;
    	this.posY=0;
    	this.dirx=5;
    	this.diry=5;
    	
    	this.life = 100;
    	this.enableSkill = false;
    	this.gun= new Gun(this.screen.getHud());
    	
    	this.xOffset = posX - modifier / 2;
    	this.yOffset = posY - modifier / 2 - 4;
    	
    	rnd = new Random();
        
        currentState = State.STANDING;
        previousState = State.STANDING;
        defineplayer();
        setBounds(0, 0, 16 / MainGame.PPM, 16 / MainGame.PPM);
        
        //set initial values for players location, width and height. And initial frame as playerStand.
        
        /*ANIMATIONS*/
        Array<TextureRegion> frames = new Array<TextureRegion>();
        
        
    	for(int i = 0; i < 3; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegions("Left").get(i), i * 0, 0, 16, 16));
        playerRunLeft = new Animation(0.2f, frames);
        frames.clear();
        
        for(int i = 0; i < 3; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegions("Right").get(i), 0, 0, 16, 16));
        playerRunRight = new Animation(0.1f, frames);
        frames.clear();
        
        for(int i = 0; i < 3; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegions("Up").get(i), 0, 0, 16, 16));
        playerRunUp = new Animation(0.1f, frames);
        frames.clear();
        
        for(int i = 0; i < 3; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegions("Down").get(i), 0, 0, 16, 16));
        playerRunDown = new Animation(0.1f, frames);
        frames.clear();
        
        //create texture region for player standing
        playerStand = new TextureRegion(screen.getAtlas().findRegion("Standings"), 0, 0, 16, 16);
        //create dead player texture region
        playerDead = new TextureRegion(screen.getAtlas().findRegion("Standings"), 96, 0, 16, 16);
        
        setRegion(playerStand);
    }
	
	 public void update(float dt){ 
	        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
	        //setRegion(getFrame());
	        setRegion(getFrame(dt));
	    }
	    
    public TextureRegion getFrame(float dt){
        currentState = getState();
        TextureRegion region = null;
      
        switch(currentState){
            case DEAD:
                region = playerDead;
                break;
            case RUNNING:
            	if((b2body.getLinearVelocity().x < 0) ){
                    //region.flip(true, false);
                    region = (TextureRegion) playerRunLeft.getKeyFrame(stateTimer, true);          
                }
            	else if((b2body.getLinearVelocity().x > 0)){
                    //region.flip(true, false);
                    region = (TextureRegion) playerRunRight.getKeyFrame(stateTimer, true);                  
                }
            	else if((b2body.getLinearVelocity().y > 0)){
                    //region.flip(true, false);
                    region = (TextureRegion) playerRunUp.getKeyFrame(stateTimer, true);     
                }
            	else if((b2body.getLinearVelocity().y < 0)){
                    //region.flip(true, false);
                    region = (TextureRegion) playerRunDown.getKeyFrame(stateTimer, true);     
                }
            	else {
            		region = (TextureRegion) playerStand;
            	}
                break;
            case STANDING:
            default:
                region = playerStand;
                break;
        }

        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        //return our final adjusted frame
        return region;
    }
    
    
    public void handleInput(float dt){
    	if(this.currentState != State.DEAD) {
    		
	        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
	        	this.b2body.setLinearVelocity(0f, 0.5f);
	        	posX=0;
	        	posY=1;
	        }
	        	
	        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) { 
	        	this.b2body.setLinearVelocity(0.5f, 0f);
	        	posX=1;
	        	posY=0;
	        }
	        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) { 
	            this.b2body.setLinearVelocity(-0.5f, 0f);
	            posX=-1;
	        	posY=0;
	            
	        }
	        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) { 
	            this.b2body.setLinearVelocity(0f, -0.5f);
	            posX=0;
	        	posY=-1;
	        }
	        
	        if(!Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {
            	this.b2body.setLinearVelocity(0f, 0f);
            	posX=0;
	        	posY=0;
            }
	        
	        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && enableSkill == true) {
            	skills(randomNumber());
            }
	        

            
	        packet = new Packet02Move( this.username, posX, posY, this.b2body.getPosition().x, this.b2body.getPosition().y);
	        this.screen.game.socketClient.sendData(packet.getData());	        	
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
            //filter.maskBits = MainGame.NOTHING_BIT;

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
        //fdef.filter.categoryBits = MainGame.player_HEAD_BIT;
        fdef.shape = head;
        fdef.isSensor = true;
        
        b2body.createFixture(fdef).setUserData(this);
        
    }
    
    public boolean isEnableSkill() {
		return enableSkill;
	}

	public void setEnableSkill(boolean enableSkill) {
		this.enableSkill = enableSkill;
	}
	
	public int randomNumber() {
		int max = 5;
		int min = 1;
		
		int r=(int) ((Math.random() * (max - min)) + min);
		return -r;
	}
	
	
	public GraphicsPB getHud() {
		return this.screen.getHud();
	}
    
    public void skills(int skillType) {
    	if (skillType == -1) {
    		moreLife();
    		getHud().setMessaggio("LIFEPOINTS AUMENTATI");
    		gun.updateHud();
    	}
    	
    	if (skillType == -2) {
    		gun.shootBigger();
    		getHud().setMessaggio("BIG-BULLET ACTIVATED");
    	}
    	
    	if (skillType == -3) {
    		gun.setNumeroBullets(gun.getNumeroBullets() + 30);
    		getHud().setMessaggio("RICARICA BULLET EFFETTUATA");
    		gun.updateHud();
    	}
    	
    	if (skillType == -4) {
    		gun.shootFaster();
    		getHud().setMessaggio("SPEED-BULLET ACTIVATED");
    	}
    	
    	setEnableSkill(false);
    	sendPacket04LifeSkill(skillType);
    }
    
    
    public void sendPacket04LifeSkill(int code) {
    	packetLS = new Packet04LifeSkill(this.getUsername(), code);
    	this.screen.game.socketClient.sendData(packetLS.getData());
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
    
    
    
    
    public void draw(Batch batch){
        super.draw(batch);
        
    }
    
}

