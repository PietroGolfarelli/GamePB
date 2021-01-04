package com.mygdx.gamepb6.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.gamepb6.MainGame;

/**
 * 
 * GraphicsPB è una classe che permette la gestione dell'interfaccia e di tutte le componenti che lo compongono. 
 * Implementa la classe Java Disposable, classe per l'utilizzo delle risorse dello schermo.
 * Vengono dichiarate tutte le componenti utili alla creazione dell'interfaccia e tutte le variabili utili all corretto funzionamneto 
 * delle componenti
 *  
 * @author Zanni Davide 
 * @author Golfarelli Pietro
 * 
 */
public class GraphicsPB implements Disposable {

	public Stage stage;
	private Viewport viewport;
	private boolean timeUp; 
	private int life;
	private int nBullets;
	private String messaggio;
	private Label timeLabel; 
	private Label userLabel;
	private Label lifeLabel;
	private Label bulletsLabel;
	private Label nbulletsLabel;
	private Label messaggioLabel;


	/**
	 * Costruttore che setta i valori e crea gli oggetti relativi al tavolo dell'interfaccia, destinato a contenere le label 
	 * e alla camera di gioco.    
	 * Vengono creati tutti i Label con le rispettive caratteristiche di messaggio, tipo di font e colore.
	 */
	public GraphicsPB(SpriteBatch sb, String username){
		this.life = 100;
		this.nBullets = 30;
		this.messaggio = "Benvenuto in GamePB6";

		viewport = new FitViewport(MainGame.V_WIDTH, MainGame.V_HEIGHT, new OrthographicCamera());

		stage = new Stage(viewport, sb);
		Table table = new Table();

		table.top();

		table.setFillParent(true);

		lifeLabel = new Label(String.format("%03d", life), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		messaggioLabel =new Label(messaggio, new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		timeLabel = new Label("LIFE", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		nbulletsLabel = new Label(String.format("%03d", nBullets), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		bulletsLabel = new Label("BULLETS", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		userLabel = new Label(username, new Label.LabelStyle(new BitmapFont(), Color.WHITE));

		table.add(userLabel).expandX().padTop(10);
		table.add(bulletsLabel).expandX().padTop(10);
		table.add(timeLabel).expandX().padTop(10);

		table.row();
		table.add(messaggioLabel).expandX();
		table.add(nbulletsLabel).expandX();
		table.add(lifeLabel).expandX();

		stage.addActor(table);
	}


	/**
	 * Metodo che aggiorna costantemente i valori mostrati dalle label life e nbullets 
	 */
	public void update(float dt){	
		lifeLabel.setText(String.format("%03d", life));
		nbulletsLabel.setText(String.format("%03d", nBullets));
		messaggioLabel.setText(messaggio);

	}

	public void setMessaggio(String messaggio){
		this.messaggio = messaggio;
		messaggioLabel.setText(messaggio);
	}


	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}

	public int getnBullets() {
		return nBullets;
	}


	public void setnBullets(int nBullets) {
		this.nBullets = nBullets;
	}


	@Override
	/*
	 *  libera le risorse video native usate dalla frame e dai suoi componenti
	 */
	public void dispose() {
		stage.dispose();
	}

	public boolean isTimeUp() {
		return timeUp; 
	}
}
