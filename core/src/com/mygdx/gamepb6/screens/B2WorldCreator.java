package com.mygdx.gamepb6.screens;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.gamepb6.MainGame;

/**
 * 
 * 
 * B2WorldCreator è una classe che permette la definizioni delle immagini definite come dure in oggetti con contorni, utili in altre classi
 * per definire le collisioni.
 * Esamina ogni oggetto all'interno della mappa e ne costruisce dei bordi seguendo figure geometriche già note come rettangoli,cerchi o ellissi 
 * 
 * @author Zanni Davide 
 * @author Golfarelli Pietro 
 *
 */
public class B2WorldCreator {
	
	/**
	 * Costruttore che definisce le risorse del sistema utili per la creazione dell'oggetto. 
	 * Riconosce oggetti definiti come duri o come casse e esaminando le caratteristiche setta intorno a ognuno dei contorni. 
	 * Vengono confrontati con le forme geometriche del rettangolo,cerchio e ellisse.
	 */
    public B2WorldCreator(PlayScreen screen){
        World world = screen.getWorld();
        TiledMap map = screen.getMap();
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        CircleShape cshape= new CircleShape();
        
       
        FixtureDef fdef = new FixtureDef();
        Body body;

        for(MapObject object : map.getLayers().get("OggettiDuri").getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / MainGame.PPM, (rect.getY() + rect.getHeight() / 2) / MainGame.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / MainGame.PPM, rect.getHeight() / 2 / MainGame.PPM);
            fdef.shape = shape;
            body.createFixture(fdef).setUserData("OggettiDuri");
        }
        
        for(MapObject object : map.getLayers().get("OggettiDuri").getObjects().getByType(CircleMapObject.class)){
            //Rectangle rect = ((RectangleMapObject) object).getRectangle();
        	Circle circle = ((CircleMapObject) object).getCircle();
        	
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((circle.x + circle.radius) / MainGame.PPM, (circle.y + circle.radius) / MainGame.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(circle.radius / MainGame.PPM, circle.radius / MainGame.PPM);
            fdef.shape = shape;
            body.createFixture(fdef).setUserData("OggettiDuri");
        }
        
        for(MapObject object : map.getLayers().get("OggettiDuri").getObjects().getByType(EllipseMapObject.class)){
            //Rectangle rect = ((RectangleMapObject) object).getRectangle();
        	Ellipse ellips = ((EllipseMapObject) object).getEllipse();
        	
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((ellips.x + ellips.width/2 ) / MainGame.PPM, (ellips.y + ellips.height/2) / MainGame.PPM);

            body = world.createBody(bdef);
            
            cshape.setRadius(ellips.width/(2*MainGame.PPM));
            fdef.shape = cshape;
            body.createFixture(fdef).setUserData("OggettiDuri");
        }
        
        for(MapObject object : map.getLayers().get("Cassa1").getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / MainGame.PPM, (rect.getY() + rect.getHeight() / 2) / MainGame.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / MainGame.PPM, rect.getHeight() / 2 / MainGame.PPM);
            fdef.shape = shape;
            body.createFixture(fdef).setUserData("Cassa1");
        }
        
        for(MapObject object : map.getLayers().get("Cassa2").getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / MainGame.PPM, (rect.getY() + rect.getHeight() / 2) / MainGame.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / MainGame.PPM, rect.getHeight() / 2 / MainGame.PPM);
            fdef.shape = shape;
            body.createFixture(fdef).setUserData("Cassa2");
        }
        
        for(MapObject object : map.getLayers().get("Cassa3").getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / MainGame.PPM, (rect.getY() + rect.getHeight() / 2) / MainGame.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / MainGame.PPM, rect.getHeight() / 2 / MainGame.PPM);
            fdef.shape = shape;
            body.createFixture(fdef).setUserData("Cassa3");
        }
        
        for(MapObject object : map.getLayers().get("Cassa4").getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / MainGame.PPM, (rect.getY() + rect.getHeight() / 2) / MainGame.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / MainGame.PPM, rect.getHeight() / 2 / MainGame.PPM);
            fdef.shape = shape;
            body.createFixture(fdef).setUserData("Cassa4");
        }
        
        for(MapObject object : map.getLayers().get("Cassa5").getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / MainGame.PPM, (rect.getY() + rect.getHeight() / 2) / MainGame.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / MainGame.PPM, rect.getHeight() / 2 / MainGame.PPM);
            fdef.shape = shape;
            body.createFixture(fdef).setUserData("Cassa5");
        }
                
    }
}

