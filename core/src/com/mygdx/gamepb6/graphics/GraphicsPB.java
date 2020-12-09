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

public class GraphicsPB implements Disposable {
	//Scene2D.ui Stage and its own Viewport for HUD
    public Stage stage;
    private Viewport viewport;

    //Mario score/time Tracking Variables
    private Integer worldTimer;
    private boolean timeUp; // true when the world timer reaches 0
    private float timeCount;
    private static Integer score;
    private int life;
    private int nBullets;
    private String messaggio;

   
	//Scene2D widgets
    private Label countdownLabel;
    private Label timeLabel;
    
   
    private Label marioLabel;
    
    private Label lifeLabel;
    private Label bulletsLabel;
    private Label nbulletsLabel;
    private Label messaggioLabel;
    public static Label userLabel;
    

    public GraphicsPB(SpriteBatch sb, String username){
        //define our tracking variables
        worldTimer = 300;
        timeCount = 0;
        score = 0;
        this.life = 100;
        this.nBullets = 30;
        this.messaggio = "Benvenuto in GamePB6";
        
        userLabel = new Label(username, new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        //setup the HUD viewport using a new camera seperate from our gamecam
        //define our stage using that viewport and our games spritebatch
        viewport = new FitViewport(MainGame.V_WIDTH, MainGame.V_HEIGHT, new OrthographicCamera());
        //viewport = new ScalingViewport(Scaling.fill,MainGame.V_WIDTH / MainGame.PPM, MainGame.V_HEIGHT / MainGame.PPM, new OrthographicCamera());
        
        stage = new Stage(viewport, sb);

        //define a table used to organize our hud's labels
        Table table = new Table();
        //Top-Align table
        table.top();
        //make the table fill the entire stage
        table.setFillParent(true);

        //define our labels using the String, and a Label style consisting of a font and color
        //countdownLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        lifeLabel = new Label(String.format("%03d", life), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        messaggioLabel =new Label(messaggio, new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        //timeLabel = new Label("TIME", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timeLabel = new Label("LIFE", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        nbulletsLabel = new Label(String.format("%03d", nBullets), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        bulletsLabel = new Label("BULLETS", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        marioLabel = new Label(username, new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        

        //add our labels to our table, padding the top, and giving them all equal width with expandX
        table.add(marioLabel).expandX().padTop(10);
        table.add(bulletsLabel).expandX().padTop(10);
        table.add(timeLabel).expandX().padTop(10);
        //add a second row to our table
        table.row();
        table.add(messaggioLabel).expandX();
        table.add(nbulletsLabel).expandX();
        table.add(lifeLabel).expandX();
       
        
        //add our table to the stage
        stage.addActor(table);
       

    }


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
    public void dispose() { stage.dispose(); }

    public boolean isTimeUp() { return timeUp; }
}
