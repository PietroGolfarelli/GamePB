package com.mygdx.gamepb6.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.gamepb6.player.Entity;

public class WorldContactListener implements ContactListener {
	
	private PlayScreen screen;
	private boolean cassa1 = true;
	private boolean cassa2 = true;
	private boolean cassa3 = true;
	private boolean cassa4 = true;
	private boolean cassa5 = true;
	
	public WorldContactListener(PlayScreen screen) {
		this.screen = screen;
	}

	@Override
	public void beginContact(Contact contact) {
		// TODO Auto-generated method stub
		Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if(fixA.getUserData()=="OggettiDuri" || fixB.getUserData()=="OggettiDuri"){
        	//System.out.println("Contact detected OggettiDuri");
        } 
        
        if(fixA.getUserData()=="Cassa1" && fixB.getUserData()=="Player" || 
        		fixA.getUserData()=="Player" && fixB.getUserData()=="Cassa1"){
        	if (cassa1 == true) {
        		System.out.println("Contact detected Cassa1 contro Player");
        		this.screen.player.setEnableSkill(true);
        	}
        } 
        
        if(fixA.getUserData()=="Cassa2" && fixB.getUserData()=="Player" || 
        		fixA.getUserData()=="Player" && fixB.getUserData()=="Cassa2"){
        	if (cassa2 == true) {
        		System.out.println("Contact detected Cassa2 contro Player");
        		this.screen.player.setEnableSkill(true);
        	}
        } 
        
        if(fixA.getUserData()=="Cassa3" && fixB.getUserData()=="Player" || 
        		fixA.getUserData()=="Player" && fixB.getUserData()=="Cassa3"){
        	if (cassa3 == true) {
        		System.out.println("Contact detected Cassa3 contro Player");
        		this.screen.player.setEnableSkill(true);
        	}
        } 
        
        if(fixA.getUserData()=="Cassa4" && fixB.getUserData()=="Player" || 
        		fixA.getUserData()=="Player" && fixB.getUserData()=="Cassa4"){
        	if (cassa4 == true) {
        		System.out.println("Contact detected Cassa4 contro Player");
        		this.screen.player.setEnableSkill(true);
        	}
        } 
        
        if(fixA.getUserData()=="Cassa5" && fixB.getUserData()=="Player" || 
        		fixA.getUserData()=="Player" && fixB.getUserData()=="Cassa5"){
        	if (cassa5 == true) {
        		System.out.println("Contact detected Cassa5 contro Player");
        		this.screen.player.setEnableSkill(true);
        	} 
        } 
        
        if(fixA.getUserData()=="Bullet" && fixB.getUserData()=="Player" || 
        		fixA.getUserData()=="Player" && fixB.getUserData()=="Bullet"){
        	this.screen.player.colpito();
        }
        
        //System.out.println("Contact detected a caso");
        
        
       
	}

	@Override
	public void endContact(Contact contact) {
		Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        
		if(fixA.getUserData()=="Cassa1" && fixB.getUserData()=="Player" || 
        		fixA.getUserData()=="Player" && fixB.getUserData()=="Cassa1"){
        	this.screen.player.setEnableSkill(false);
        	cassa1=false;
        }
		
		if(fixA.getUserData()=="Cassa2" && fixB.getUserData()=="Player" || 
        		fixA.getUserData()=="Player" && fixB.getUserData()=="Cassa2"){
        	
        	this.screen.player.setEnableSkill(false);
        	cassa2=false;
        }
		
		if(fixA.getUserData()=="Cassa3" && fixB.getUserData()=="Player" || 
        		fixA.getUserData()=="Player" && fixB.getUserData()=="Cassa3"){
        	
        	this.screen.player.setEnableSkill(false);
        	cassa3=false;
        }
		
		if(fixA.getUserData()=="Cassa4" && fixB.getUserData()=="Player" || 
        		fixA.getUserData()=="Player" && fixB.getUserData()=="Cassa4"){
        	this.screen.player.setEnableSkill(false);
        	cassa4=false;
        }
		
		if(fixA.getUserData()=="Cassa5" && fixB.getUserData()=="Player" || 
        		fixA.getUserData()=="Player" && fixB.getUserData()=="Cassa5"){
        	this.screen.player.setEnableSkill(false);
        	cassa5=false;
        }

	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
	
	}
}
