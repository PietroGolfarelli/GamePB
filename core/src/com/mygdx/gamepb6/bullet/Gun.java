package com.mygdx.gamepb6.bullet;

import com.mygdx.gamepb6.graphics.GraphicsPB;
import com.mygdx.gamepb6.screens.PlayScreen;

/**
 * Gun è una classe che permette la gestione di tutti i proiettili definiti nella classe astratta Bullet.
 * Vengono definite tutte le caratteristiche come il numero di proiettili che un giocatore può sparare oppure quando utilizzare classi speciali 
 * come SpeedBullet,SuperBullet 
 * Dichiarati campi utilizzati per la caratterizzazione del bullet 
 * 
 * @author Zanni Davide
 * @author Golfarelli Pietro
 */
public class Gun {
	private int numeroBullets;
	private boolean zeroBullets;
	private GraphicsPB hud;
	private boolean normalBullet, speedBullet, superBullet;
	private Bullet bullet;


	/*
	 * Costruttore polimorfa che definisce le risorse del sistema, sfruttando anche l'elemento di grafica utile  
	 */
	public Gun(GraphicsPB hud) {
		this.numeroBullets = 30;
		this.zeroBullets = false;
		this.hud = hud;
		this.normalBullet = true;
		this.speedBullet = false;
		this.superBullet = false;
	}

	/*
	 * Costruttore polimorfa che definisce le risorse del sistema
	 */
	public Gun() {
		this.numeroBullets = 30;
		this.zeroBullets = false;
		this.normalBullet = true;
		this.speedBullet = false;
		this.superBullet = false;
	}

	/*
	 * Metodo che definisce quando utilizzare i diversi tipi di proiettili, creandone l'oggetto con i rispettivi parametri di posizione
	 */
	public Bullet shootBullet(PlayScreen screen, float x, float y, int dirx, int diry) {
		if (superBullet == true)
			bullet = new SuperBullet(screen, x, y , dirx, diry);
		else if (speedBullet == true) 
			bullet = new SpeedBullet(screen, x, y , dirx, diry);
		else if (normalBullet == true)
			bullet = new NormalBullet(screen, x, y , dirx, diry);
		return bullet;
	}

	/**
	 * Metodo utile quando terminano il numero di proiettili disponibili, rendendo temporaneamente indisponibile l'opzione di sparare
	 */
	public boolean zeroBullets() {
		if (getNumeroBullets() == 0) {
			this.zeroBullets = true;
		}
		return zeroBullets;
	}

	/**
	 * Metodo che aggiorna il numero di proiettili visibili sullo schermo
	 */
	public void updateHud() {
		hud.setnBullets(getNumeroBullets());
	}

	/**
	 * Metodod che decrementa il numero di proiettili disponibili
	 */
	public void removeOneBullet() {
		setNumeroBullets(getNumeroBullets()-1);

	}

	public int getNumeroBullets() {
		return numeroBullets;
	}

	public void setNumeroBullets(int numeroBullets) {
		this.numeroBullets = numeroBullets;

	}

	public boolean isSpeedBullet() {
		return speedBullet;
	}

	public void setSpeedBullet(boolean speedBullet) {
		this.speedBullet = speedBullet;
	}

	/**
	 * Metodo che setta il proiettile di tipo Super
	 */
	public void shootBigger() {
		setNormalBullet(false);
		setSuperBullet(true);
		setSpeedBullet(false);
	}

	/**
	 * Metodo che setta il proiettile di tipo Speed
	 */
	public void shootFaster() {
		setNormalBullet(false);
		setSuperBullet(false);
		setSpeedBullet(true);
	}

	public boolean isNormalBullet() {
		return normalBullet;
	}

	public void setNormalBullet(boolean normalBullet) {
		this.normalBullet = normalBullet;
	}

	public boolean isSuperBullet() {
		return superBullet;
	}

	public void setSuperBullet(boolean superBullet) {
		this.superBullet = superBullet;
	}





}
