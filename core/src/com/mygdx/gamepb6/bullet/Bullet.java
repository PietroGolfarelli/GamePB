package com.mygdx.gamepb6.bullet;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.gamepb6.screens.PlayScreen;

/**
 * Bullet è una classe astratta che permette la gestione dei proiettili  durante la fase di scontro tra i giocatori.
 * Partendo dalla classe astratta vengono successivamente implementate sottoclassi, le quali ci permettono di creare proiettili 
 * con caratteristiche e funzionalità differenti.
 * La classe eredita la già predefinita classe Java Sprite.
 * La classe Sprite permette di rendere fisica l'immagine caricata come texture, contenendo informazioni relative alla geometria,colore e posizione 
 * dell'oggetto. 
 * 
 * Vengono dichiarati campi che permettono di gestire e di aggiornare costantemente la posizione del proiettile come (x,y,dirx,diry) 
 * Altri campi come screen che consentono di raffigurare il proiettile sulla mappa di gioco oppure img e atlasB contenenti la texture dell'oggetto
 * 
 * @author Zanni Davide 
 * @author Golfarelli Pietro
 *
 */
public abstract class Bullet extends Sprite{

	public PlayScreen screen;
	public World world;
	public Body b2body;
	public TextureAtlas atlasB;
	public TextureRegion img;

	public int dirx, diry;
	public float x,y;	

	public boolean vivo;

	/**
	 * Costruttore che definisce le risorse del sistema utili per la creazione dell'oggetto. 
	 * Vengono inoltre definite le istruzioni di posizione che deve assumere il proiettile una volta definite le direzioni lungo gli assi (x,y).
	 * 
	 */
	public Bullet (PlayScreen screen, float x, float y, int dirx, int diry) {

		this.screen = screen;
		this.world = screen.getWorld();
		this.dirx=dirx;
		this.diry=diry;
		if (dirx == 0 && diry == 1)
			y=y+0.2f;
		if (dirx == 0 && diry == -1)
			y=y-0.2f;
		if (dirx == -1 && diry == 0)
			x=x-0.2f;
		if (dirx == 1 && diry == 0)
			x=x+0.2f;

		this.x=x;
		this.y=y;

	}


	/**
	 * Metodo astratto utilizzato per la distruzzione del corpo del proiettile
	 */
	public abstract void distruggi();

	/**
	 *  Metodo astratto utilizzato per il costante aggiornamento della posizione del proiettile e del timer relativo alla durata della vita del proiettile.
	 *  Una volta che il timer è azzerato viene chiamato il metodo distruggi.
	 *  
	 *  @param bullet oggetto su cui agisce il metodo
	 */
	public abstract void update(float deltaTime, Bullet bullet);

	/**
	 * Metodo che richiama dalla classe padre il metodo draw per disegnare il proiettile
	 */
	public void draw(Batch batch) {
		super.draw(batch);
	}

	/**
	 * Metodo astratto che definisce il corpo del proiettile, dà al corpo una propria forma fisica,gravità nella mappa e rende il corpo sensibile 
	 * alle collisioni.
	 * In più gestisce le direzioni e la velocità che assume il proiettile
	 */
	public abstract void defineBullet();

}