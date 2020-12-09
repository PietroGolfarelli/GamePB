package com.mygdx.gamepb6.bullet;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.gamepb6.screens.PlayScreen;

public abstract class Bullet extends Sprite{

	public PlayScreen screen;
	public World world;
	public Body b2body;
	public TextureAtlas atlasB;
	public TextureRegion img;

	public int dirx, diry;
	public float x,y;	

	public boolean vivo;

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

	public abstract void distruggi();

	public abstract void update(float deltaTime, Bullet bullet);

	public void draw(Batch batch) {
		super.draw(batch);
	}
 
	public abstract void defineBullet();

}