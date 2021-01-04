package com.mygdx.gamepb6.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.gamepb6.MainGame;

/**
 * GameOver è una classe utilizzata per la fine del game quando uno dei due player perde la partita, nel quale compare un messaggio centrale 
 * di sconfitta.
 * Vengono dichiarati i campi per la gestione dello schermo
 *
 * @author Zanni Davide
 * @author Golfarelli Pietro
 *
 */
public class GameOver implements Screen {
	
	private Viewport viewport;
	private Stage stage;
	private Game game;

	/**
	 * Costruttore che definisce le risorse del sistema utili per la creazione dell'oggetto.
	 *  Costruttore che setta i valori e crea gli oggetti relativi e alla camera di gioco.
	 *      
	 */
	public GameOver(Game game, PlayScreen screen ){

		this.game = game;

		viewport = new FitViewport(MainGame.V_WIDTH, MainGame.V_HEIGHT, new OrthographicCamera());
		stage = new Stage(viewport, screen.game.batch);

		Table table = new Table();
		table.center();
		table.setFillParent(true);

		Label gameOverLabel = new Label("GAME OVER", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		table.add(gameOverLabel).expandX();
		table.row();

		stage.addActor(table);
	}

	@Override
	public void show() {

	}

	@Override
	/*
	 * Metodo che costantemente aggiorna lo schermo rendendolo nero e disegnando il messaggio contenuto nello stage 
	 */
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	/*
	 *  libera le risorse video native usate dalla frame e dai suoi componenti
	 */
	public void dispose() {
		stage.dispose();
	}
}
