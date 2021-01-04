package com.mygdx.gamepb6.bullet;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.mygdx.gamepb6.MainGame;
import com.mygdx.gamepb6.screens.PlayScreen;

/**
 * Sottoclasse della classe Bullet che definisce il proiettile piu veloce rispetto al proiettile standard e il suo comportamento.
 * Vengono dichiarati campi relativi all'immagine del proiettile e alla durata della vita del proiettile.
 *
 *
 * @author Golfarelli Pietro
 * @author Zanni Davide
 */
public class SpeedBullet extends Bullet{	

	private int lifetime = 60;
	private TextureRegion img;

	public boolean vivo;

	/**
	 * Costruttore che definisce le risorse del sistema utili per la creazione dell'oggetto. 
	 * Viene definita l'immagine e la propria grandezza e viene chiamato il metodo per la definizione del corpo del proiettile.
	 */
	public SpeedBullet(PlayScreen screen, float x, float y, int dirx, int diry) {
		super(screen, x, y, dirx, diry);
		this.vivo = true;
		defineBullet();
		setBounds(0, 0, 16 / MainGame.PPM, 16 / MainGame.PPM);
		img = new TextureRegion(super.screen.atlasB.findRegions("slime/left").get(0), 0, 4, 16, 16);
		setRegion(img);
	}



	public void distruggi() {
		world.destroyBody(this.b2body);
		this.vivo=false;
	}


	@Override
	public void update(float deltaTime,Bullet bullet) {
		if(this.vivo==true) {
			setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
			setRegion(img);
			lifetime--;
			if(lifetime == 0) {
				distruggi();
			}
		}		
	}

	@Override
	public void draw(Batch batch) {
		if(this.vivo == true)
			super.draw(batch);
	}

	@Override
	/**
	 * Nella gestione della direzione e dalla velocita viene incrementato il valore di quest ultimo per rendere il proiettile più veloce
	 */
	public void defineBullet() {
		BodyDef bdef = new BodyDef();
		bdef.position.set(x,y);
		bdef.type = BodyDef.BodyType.DynamicBody;
		bdef.gravityScale= 0.0f;
		b2body = world.createBody(bdef);

		FixtureDef fdef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(3 / MainGame.PPM);
		shape.setPosition(new Vector2(0,0));
		fdef.shape = shape;
		fdef.restitution = 1;
		fdef.friction = 0;
		b2body.setBullet(true);
		b2body.createFixture(fdef).setUserData("Bullet");
		shape.dispose();

		if (dirx==0 && diry==1) { 
			b2body.setLinearVelocity(0f, 2f);

		}
		if (dirx==1 && diry==0) { 
			b2body.setLinearVelocity(2f, 0f);

		}
		if (dirx==-1 && diry==0) { 
			b2body.setLinearVelocity(-2f, 0f);
		}

		if (dirx==0 && diry==-1) { 
			b2body.setLinearVelocity(0f, -2f);
		}

	}

}