package com.mygdx.gamepb6.screens;

import java.util.ArrayList;

import java.util.List;

import org.ietf.jgss.GSSName;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.gamepb6.MainGame;
import com.mygdx.gamepb6.graphics.GraphicsPB;
import com.mygdx.gamepb6.net.packets.Packet00Login;
import com.mygdx.gamepb6.net.packets.Packet02Move;
import com.mygdx.gamepb6.net.packets.Packet03Bullet;
import com.mygdx.gamepb6.bullet.Bullet;
import com.mygdx.gamepb6.entities.Entity.State;
import com.mygdx.gamepb6.entities.Nemico;
import com.mygdx.gamepb6.entities.Player;



public class PlayScreen implements Screen{
    public MainGame game;
    
    private TextureAtlas atlas;
    public static boolean alreadyDestroyed = false;
    
    private GraphicsPB hud;
    
    private OrthographicCamera gamecam;
    private Viewport gamePort;
    
    private TmxMapLoader maploader;
    private TiledMap map;
    private int mapPixelWidth; 
    private int mapPixelHeight;
    private OrthogonalTiledMapRenderer renderer;
    
    private World world;
    private Box2DDebugRenderer b2dr;
    
	@SuppressWarnings("unused")
	private B2WorldCreator creator;
	
	public Player player;

    //private List<EntityMP> nemici = new ArrayList<EntityMP>();
	private List<Nemico> listaNemici = new ArrayList<Nemico>();
	public Nemico nemico;
	
	public ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	
	private Packet03Bullet packetB;

	public TextureAtlas atlasB;

	public String username;

	private boolean aw;
	private int spawnX, spawnY;

	private boolean nuovonemico;
	private List<Packet00Login> packetlogin = new ArrayList<Packet00Login>();
	private List<Packet00Login> nemicidarifare = new ArrayList<Packet00Login>();
	
	

    public PlayScreen(MainGame game, String p_username){

        atlas = new TextureAtlas("Character.atlas");
        atlasB = new TextureAtlas("textures.atlas");
        this.game = game;
        this.username = p_username;
        //create cam used to follow mario through cam world
        gamecam = new OrthographicCamera();
        
        
        gamePort = new FitViewport(MainGame.V_WIDTH / MainGame.PPM, MainGame.V_HEIGHT / MainGame.PPM, gamecam);
        gamecam.setToOrtho(false, gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2);
        gamecam.update();

        maploader = new TmxMapLoader();
        map = maploader.load("map4.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1  / MainGame.PPM);
        
        hud = new GraphicsPB(game.batch,  p_username);
        
        MapProperties prop = map.getProperties();
        
        int mapWidth = prop.get("width", Integer.class);
        int mapHeight = prop.get("height", Integer.class);
        int tilePixelWidth = prop.get("tilewidth", Integer.class);
        int tilePixelHeight = prop.get("tileheight", Integer.class);

        mapPixelWidth = mapWidth * tilePixelWidth;
        mapPixelHeight = mapHeight * tilePixelHeight;
              
        
        //initially set our gamcam to be centered correctly at the start of of map
        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
        world = new World(new Vector2(0.0f, 0.0f), true);
        
        //allows for debug lines of our box2d world.
        b2dr = new Box2DDebugRenderer();
        creator = new B2WorldCreator(this);
        
        //this.player = new EntityMP(this, p_username, null, -1);
        aw = false;
        world.setContactListener(new WorldContactListener(this));
        
        player = null;
        nuovonemico = false;
    }
    
    
    public void connesso(int spawnX, int spawnY) {
    	System.out.println("sono in create player");
    	//player = new EntityMP(this, this.username, null, -1);
    	aw=true;
    	this.spawnX = spawnX;
    	this.spawnY = spawnY;
    }
    
    public int getSpawnX() {
    	return this.spawnX;
    }
    
    public int getSpawnY() {
    	return this.spawnY;
    }
    
    public void createPlayer() {
    	player = new Player(this, this.username, this.spawnX, this.spawnY);
		aw=false;
    }
    
    
    public void createNemico() {
    	for (Packet00Login packet : packetlogin) {
    		nemico = new Nemico(game.playscreen, packet.getUsername(), packet.getX(), packet.getY());
    		addNemico(nemico);
    	}
    	packetlogin.clear();
		nuovonemico=false;
    }
    
    
    public void setUsername(String username) {
		this.username = username;
	}


	public void addNemico (Nemico nemico) {
    	this.listaNemici.add(nemico); 
    }
    
    public void nuovoNemico(Packet00Login packet) {
		nuovonemico = true;
		packetlogin.add(packet);
	}
    
    public void addBullet (Bullet bullet) {
    	bullets.add(bullet);
    }
    
    public synchronized List<Nemico> getEntities() {
        return this.listaNemici;
    }
    
    private int getPlayerMPIndex(String username) {
        int index = 0;
        for (Nemico e : getEntities()) {
        	if (e.getUsername().equals(username)) {
                break;
            }
            index++;
            
        }
        return index;
    }
    
    public Nemico getNemico(String username) {
        for (Nemico e : getEntities()) {
        	if (e.getUsername().equals(username)) {
        		nemico=e;
                break;
            }           
        }
        return nemico;
    }
    
    public String getUsername() {
    	return this.username;
    }
    
    public void checkPosition(Nemico n, float x, float y) {
    	if (Math.abs(n.getbX(n) - x) > 0.1 || Math.abs(n.getbY(n) - y) > 0.1 ) {
    		//removeEntityMP(n.username);
    		n.setVabene(false);
    		n.setSpawnX(x);
    		n.setSpawnY(y);
    		//Packet00Login packet = new Packet00Login(nem.username, x ,y);
    		//nemicidarifare.add(packet);
    	} 
    }
    
    
    public synchronized void movePlayers(String username, int posX, int posY, float x, float y) {
    	//System.out.println(usernameS + " " + this.username);
    	Nemico n = getNemico(username);
    	if (n != null) {
	    	n.handleInput(posX, posY);
	    	//if (posX == 0 && posY == 0)
	    	checkPosition(n, x, y);
    	}
    }
       
    
    public TextureAtlas getAtlas(){
        return atlas;
    }
    
    
    public void update(float dt){
    	if(nuovonemico == true) {
    		createNemico();
    	}
    	
    	if(aw == true) {
    		createPlayer();
    	}
    	
    	if(player != null) {
	    	player.handleInput(dt);
	    	player.update(dt);
	    }
    	hud.update(dt);
    	
    	for (Nemico e : getEntities()) {
    		if (e.isVabene() == false)
    			e.redefine();
	     	e.update(dt);
	     }
		 
		for (Bullet b : bullets) {
			b.update(dt, b);
		}
		 
        //takes 1 step in the physics simulation(60 times per second)
        world.step(1 / 60f, 6, 2);
        
        //attach our gamecam to our players.x coordinate
        if(player != null) {
	        if (player.getCurrentState() != State.DEAD) {
	            gamecam.position.x = player.b2body.getPosition().x;
	            gamecam.position.y = player.b2body.getPosition().y;
	        }
        }

        int wl = 200;  //400
        int hl = 200;  //320
        
        if(gamecam.position.x<wl/ MainGame.PPM){
            gamecam.position.x=wl/ MainGame.PPM;
        }else if(gamecam.position.x>(mapPixelWidth-wl)/ MainGame.PPM){
            gamecam.position.x=(mapPixelWidth-wl)/ MainGame.PPM;
        }

        if(gamecam.position.y<hl/ MainGame.PPM){
            gamecam.position.y=hl/ MainGame.PPM;
        } else if(gamecam.position.y>(mapPixelHeight-hl)/ MainGame.PPM){
            gamecam.position.y=(mapPixelHeight-hl)/ MainGame.PPM;
        }
        	 
        //update our gamecam with correct coordinates after changes
        gamecam.update();
        
        //tell our renderer to draw only what our camera can see in our game world.
        renderer.setView(gamecam);
    }  	
   

	/*private void rigeneraNemico(Nemico e) {
		removeEntityMP(e.username);
		//Packet00Login packetUsato;
		for (Packet00Login packet : nemicidarifare) {
			if ( packet.getUsername().equals(e.getUsername()) ) {
				nemico = new Nemico(game.playscreen, packet.getUsername(), packet.getX(), packet.getY());
				addNemico(nemico);
				//packetUsato = packet;
				nemicidarifare.remove(packet);
				break;
			}
    	}
	}*/


	@Override
    public void render(float delta) {
        update(delta);
    
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render();

        b2dr.render(world, gamecam.combined);

        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();	
        if(this.player != null) {
        if (!this.player.gun.zeroBullets()) {
	        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
	    		shoot(0, 1);
			}
	        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
	    		shoot(-1, 0);
			}
	        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
	    		shoot(0, -1);
			}
	        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
	    		shoot(1, 0);
			}
        }
        else {
        	getHud().setMessaggio("HAI FINITO I BULLETS");
        }
        }
        
        for (Nemico n : getEntities()) {
        	n.draw(game.batch);
        }
        
        
        for (Bullet b : bullets) {
        	b.draw(game.batch);
        }
        if(this.player != null) {
        	this.player.draw(game.batch);
        }
        game.batch.end();
        
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }
    
    
    @Override
    public void resize(int width, int height) {
        gamePort.update(width,height);
    }
    
    
    public  void shoot(int dirx, int diry) {
    	Bullet b = this.player.gun.shootBullet(this, this.player.getbX(player) , this.player.getbY(player), dirx, diry);
    	this.addBullet(b);
		this.player.gun.removeOneBullet();
		this.player.gun.updateHud();
		
		packetB = new Packet03Bullet(player.getUsername(), dirx, diry);
        this.game.socketClient.sendData(packetB.getData());
    }
    
    public  void shootNemico(Nemico nemico, int dirx, int diry) {
    	Bullet b = nemico.gun.shootBullet(this, nemico.getbX(nemico) , nemico.getbY(nemico), dirx, diry);
    	this.addBullet(b);
    }
    
    public synchronized void firePlayers(String username, int dirx, int diry) {
    	Nemico n = getNemico(username);
    	if (n != null) {
	    	shootNemico(n, dirx, diry);
    	}
    }
    
    public synchronized void nemicoLifeSkill(String username, int code) {
    	Nemico n = getNemico(username);
    	if (n != null) {
    		if (code >= 0)
    			n.setLife(code);
    		else 
				n.skills.skillType(code);
    	}
    }
    
    public void removeEntityMP(String username) {
    	nemico = getNemico(username);
    	world.destroyBody(nemico.b2body);
    	removeNemico(nemico);
    }
    
    public void removeBullet(Bullet bulletR) {
    	for (Bullet b : bullets) {
    		if (b==bulletR) {
    			bullets.remove(b);
    			break;
    		}
    	}
    }
    
    public void removeNemico (Nemico nemico) {
    	for (Nemico n : listaNemici) {
    		if (n.username == nemico.username) {
    			listaNemici.remove(n);
    			break;
    		}
    	}
    }
    
    public TiledMap getMap(){
        return map;
    }
    
    
    public World getWorld(){
        return world;
    }
    
    public GraphicsPB getHud() {
		return hud;
	}

	public void setHud(GraphicsPB hud) {
		this.hud = hud;
	}
	

  
    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
        
    }


	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}


}
