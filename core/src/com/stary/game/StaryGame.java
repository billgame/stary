package com.stary.game;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.stary.screens.GameScreen;
/**
 * a Odin-like
 * side-scrolling action game
 * 
 * 
 * @author billzhu
 *
 */
public class StaryGame extends Game {
	Engine world;
	SpriteBatch batch;
//	Texture img;
	
	@Override
	public void create () {
		setScreen(new GameScreen(this));
		world=new Engine();
		batch = new SpriteBatch();
//		img = new Texture("badlogic.jpg");
	}

//	@Override
//	public void render () {
//		Gdx.gl.glClearColor(1, 0, 0, 1);
//		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//		batch.begin();
//		batch.draw(img, 0, 0);
//		batch.end();
//	}
	
}
