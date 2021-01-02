package com.mygdx.gamepb6.bullet;

import com.mygdx.gamepb6.graphics.GraphicsPB;
import com.mygdx.gamepb6.screens.PlayScreen;

public class Gun {
	private int numeroBullets;
	private boolean zeroBullets;
	private GraphicsPB hud;
	private boolean normalBullet, speedBullet, superBullet;
	private Bullet bullet;
	
	
	public Gun(GraphicsPB hud) {
		this.numeroBullets = 30;
		this.zeroBullets = false;
		this.hud = hud;
		this.normalBullet = true;
		this.speedBullet = false;
		this.superBullet = false;
	}
	
	public Gun() {
		this.numeroBullets = 30;
		this.zeroBullets = false;
		this.normalBullet = true;
		this.speedBullet = false;
		this.superBullet = false;
	}
	
	public Bullet shootBullet(PlayScreen screen, float x, float y, int dirx, int diry) {
		if (superBullet == true)
			bullet = new SuperBullet(screen, x, y , dirx, diry);
		else if (speedBullet == true) 
			bullet = new SpeedBullet(screen, x, y , dirx, diry);
		else if (normalBullet == true)
			bullet = new NormalBullet(screen, x, y , dirx, diry);
		return bullet;
	}
	
	public boolean zeroBullets() {
		if (getNumeroBullets() == 0) {
			this.zeroBullets = true;
		}
		return zeroBullets;
	}
	
	public void updateHud() {
		hud.setnBullets(getNumeroBullets());
	}
	
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

	public void shootBigger() {
		setNormalBullet(false);
		setSuperBullet(true);
		setSpeedBullet(false);
	}
	
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
