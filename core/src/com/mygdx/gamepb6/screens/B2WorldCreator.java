package com.mygdx.gamepb6.screens;


import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;
import com.mygdx.gamepb6.MainGame;

public class B2WorldCreator {
    
    public B2WorldCreator(PlayScreen screen){
        World world = screen.getWorld();
        TiledMap map = screen.getMap();
        //create body and fixture variables
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        CircleShape cshape= new CircleShape();
        
       
        FixtureDef fdef = new FixtureDef();
        Body body;

        //create ground bodies/fixtures
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

            //shape.setAsBox(ellips.width/2 / MainGame.PPM, ellips.height/2 / MainGame.PPM);
            
            //shape.setRadius(ellips.width/MainGame.PPM);
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
        
        
        /*
        for(MapObject object : map.getLayers().get("OggettiDuri").getObjects().getByType(PolylineMapObject.class)){
        	Polyline polylineObject = ((PolylineMapObject) object).getPolyline();
        	
            float[] vertices = polylineObject.getTransformedVertices();
            Vector2[] worldVertices = new Vector2[vertices.length / 2];

            for (int i = 0; i < vertices.length / 2; ++i) {
                worldVertices[i] = new Vector2();
                worldVertices[i].x = vertices[i * 2] / MainGame.PPM;
                worldVertices[i].y = vertices[i * 2 + 1] / MainGame.PPM;
            }

            ChainShape chain = new ChainShape(); 
            chain.createChain(worldVertices);
           
            bdef.type = BodyType.StaticBody;
            body = world.createBody(bdef);
            fdef.shape=chain;
            body.createFixture(fdef).setUserData("OggettoDuro");
            //body.setUserData(body);
           
        }
        
        for(MapObject object : map.getLayers().get("Cassa").getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / MainGame.PPM, (rect.getY() + rect.getHeight() / 2) / MainGame.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / MainGame.PPM, rect.getHeight() / 2 / MainGame.PPM);
            fdef.shape = shape;
            body.createFixture(fdef).setUserData("Cassa");
            
        }
*/
        /*create pipe bodies/fixtures
        for(MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / MainGame.PPM, (rect.getY() + rect.getHeight() / 2) / MainGame.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / MainGame.PPM, rect.getHeight() / 2 / MainGame.PPM);
            fdef.shape = shape;
            fdef.filter.categoryBits = MainGame.OBJECT_BIT;
            body.createFixture(fdef);
        }
        */
                
    }
}

