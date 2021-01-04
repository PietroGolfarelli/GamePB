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


/**
 * 
 * PlayScreen contiene e inizializza la mappa di gioco. All'interno di questa classe si gestisce la creazione del 
 * Player e dei vari Nemici. Sono quindi gestiti eventuali errori nella posizione del nemici, i contatti tra i bullet e 
 * le varie Entity e tutto quello che riguarda la visione e la gestione della mappa di gioco.
 *
 */
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
	//	private List<Packet00Login> nemicidarifare = new ArrayList<Packet00Login>();

	/**
	 * Il costruttore di PlayScreen è solitamente chiamato da MainGame nella fase di caricameno del gioco
	 * @param game			istanza di MainGame che contiene le caratteristiche fondamentali
	 * @param p_username	username scelto dall'utente all'avvio del gioco
	 */
	public PlayScreen(MainGame game, String p_username){

		atlas = new TextureAtlas("Character.atlas");
		atlasB = new TextureAtlas("textures.atlas");
		this.game = game;
		this.username = p_username;
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


		gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
		world = new World(new Vector2(0.0f, 0.0f), true);

		b2dr = new Box2DDebugRenderer();
		creator = new B2WorldCreator(this);

		aw = false;
		world.setContactListener(new WorldContactListener(this));

		player = null;
		nuovonemico = false;
	}

	/**
	 * Una volta che il GameServer ha risposto al client questo metodo conferma la connessione tra Client e Server e
	 * definisce le coordinate nelle quale il Player deve essere creato ricevute dal server stesso attraverso un 
	 * pacchetto Packet05ServerAnswer.
	 * @param spawnX	coordinata x definita dal Server
	 * @param spawnY	coordianta y definita dal Server 
	 */
	public void connesso(int spawnX, int spawnY) {
		aw=true;
		this.spawnX = spawnX;
		this.spawnY = spawnY;
	}

	
	/**
	 * Si occupa di creare il Player sulla mappa di gioco.
	 */
	public void createPlayer() {
		player = new Player(this, this.username, this.spawnX, this.spawnY);
		aw=false;
	}

	/**
	 * Si occupa di creare il Nemico sulla mappa di gioco utilizzando una lista che contiene i pacchetti login ricevuti dal
	 * client. Questi corrispondo ai nuovi Client connessi al server.
	 */
	public void createNemico() {
		for (Packet00Login packet : packetlogin) {
			nemico = new Nemico(game.playscreen, packet.getUsername(), packet.getX(), packet.getY());
			addNemico(nemico);
		}
		packetlogin.clear();
		nuovonemico=false;
	}

	

	/**
	 * Inserisce il nuovo Nemico nella lista dei nemici che contiene la mappa di gioco.
	 * @param nemico 	Nemico appena creato all'interno del gioco
	 */
	public void addNemico (Nemico nemico) {
		this.listaNemici.add(nemico); 
	}

	/**
	 * Quando si colleg aun nuovo CLient al Server questo metodo riceve il pacchetto Packet00Login con le informazioni
	 * necessarie per creare il Nemico sulla mappa, questo viene salvato in una lista che verrà poi utilizzata per creare
	 * i nuovi nemici.
	 * @param packet Packet00Login del nuovo client connesso
	 */
	public void nuovoNemico(Packet00Login packet) {
		nuovonemico = true;
		packetlogin.add(packet);
	}
	
	/**
	 * Aggiungo il bullet alla lista dei bullet presenti sulla mappa in modo da poter aggiornare la loro posizione ad 
	 * ogni ciclo render.
	 * @param bullet 
	 */
	public void addBullet (Bullet bullet) {
		bullets.add(bullet);
	}
	
	
	
	
	/**
	 * Controlla la posizione del nemico, se questa differisce di 0.1 rispetto alla posizione in cui dovrebbe essere
	 * (che conosco grazie al pacchetto Packet02Move) allora devo ridefinire il Nemico (ne modifico lo stato interno). 
	 * @param n		Nemico da controllare
	 * @param x		posizione x in cui dovrebbe essere
	 * @param y		posizione y in cui dovrebbe essere
	 */
	public void checkPosition(Nemico n, float x, float y) {
		if (Math.abs(n.getbX(n) - x) > 0.1 || Math.abs(n.getbY(n) - y) > 0.1 ) {
			n.setVabene(false);
			n.setSpawnX(x);
			n.setSpawnY(y);
		} 
	}

	/**
	 * Gestisce il movimento il controllo del Nemico
	 * @param username		username del Nemico da muovere
	 * @param posX			direzione x in cui muoverlo
	 * @param posY			direzione y in cui muoverlo
	 * @param x				posizione x in cui dovrebbe essere
	 * @param y				posizione y in cui dovrebbe essere
	 */
	public synchronized void movePlayers(String username, int posX, int posY, float x, float y) {
		Nemico n = getNemico(username);
		if (n != null) {
			n.handleInput(posX, posY);
			checkPosition(n, x, y);
		}
	}


	public TextureAtlas getAtlas(){
		return atlas;
	}

	/**
	 * Aggiorna la posizione del Player, dei Nemici, dei Bullets e delle Camera.
	 * @param dt	tempo dettato dal render
	 */
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

		world.step(1 / 60f, 6, 2);

		if(player != null) {
			if (player.getCurrentState() != State.DEAD) {
				gamecam.position.x = player.b2body.getPosition().x;
				gamecam.position.y = player.b2body.getPosition().y;
			}
		}

		int wl = 200;  
		int hl = 200;  

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

		gamecam.update();

		renderer.setView(gamecam);
	}  	

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

	/**
	 * Permette a Player di sparare in una certa direzione.
	 * @param dirx	direzione x dello sparo (-1, 1)
	 * @param diry 	direzione y dello sparo (-1, 1)
	 */
	public  void shoot(int dirx, int diry) {
		Bullet b = this.player.gun.shootBullet(this, this.player.getbX(player) , this.player.getbY(player), dirx, diry);
		this.addBullet(b);
		this.player.gun.removeOneBullet();
		this.player.gun.updateHud();

		packetB = new Packet03Bullet(player.getUsername(), dirx, diry);
		this.game.socketClient.sendData(packetB.getData());
	}
	
	/**
	 * Permette al Nemico a cui è collegatto quello username di sparare in una certa direzione.
	 * @param username		username del Nemico che si vuol far spare 
	 * @param dirx			direzione x dello sparo (-1, 1)
	 * @param diry			direzione y dello sparo (-1, 1)
	 */
	public synchronized void firePlayers(String username, int dirx, int diry) {
		Nemico n = getNemico(username);
		if (n != null) {
			shootNemico(n, dirx, diry);
		}
	}
	
	public  void shootNemico(Nemico nemico, int dirx, int diry) {
		Bullet b = nemico.gun.shootBullet(this, nemico.getbX(nemico) , nemico.getbY(nemico), dirx, diry);
		this.addBullet(b);
	}
	
	/**
	 * Attiva in un Nemico una certa skill.
	 * @param username	username del Nemico a cui si vuole assegnare una skill
	 * @param code		tipo di skill che si vuole attribuire
	 */
	public synchronized void nemicoLifeSkill(String username, int code) {
		Nemico n = getNemico(username);
		if (n != null) {
			if (code >= 0)
				n.setLife(code);
			else 
				n.skills.skillType(code);
		}
	}
	
	/**
	 * Rimuove un Nemico a cui è assegnato un certo username.
	 * @param username		username del Nemico che si vuole eliminare.
	 */
	public void removeEntityMP(String username) {
		nemico = getNemico(username);
		world.destroyBody(nemico.b2body);
		removeNemico(nemico);
	}
	
	/**
	 * Metodo che rimuove un bullet dalla lista dei bullets in gioco.
	 * @param bulletR	Bullet che voglio rimuovere
	 */
	public void removeBullet(Bullet bulletR) {
		for (Bullet b : bullets) {
			if (b==bulletR) {
				bullets.remove(b);
				break;
			}
		}
	}
	
	/**
	 * Rimuove un nemico dalla lista dei nemici presenti sulla mappa.
	 */
	public void removeNemico (Nemico nemico) {
		for (Nemico n : listaNemici) {
			if (n.username == nemico.username) {
				listaNemici.remove(n);
				break;
			}
		}
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

	public int getSpawnX() {
		return this.spawnX;
	}

	public int getSpawnY() {
		return this.spawnY;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	/**
	 * libera le risorse video native usate dalla frame e dai suoi componenti
	 */
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
