package com.stary.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
/**
 * a Odin-like
 * side-scrolling action game
 * 
 * 
 * @author billzhu
 *
 */
public class BasicScreen extends InputAdapter implements Screen {
	public Game game;
//	public InputMultiplexer inputs = new InputMultiplexer();
	public OrthographicCamera cameraMap;
	public BasicScreen(Game game) {
		super();
		this.game = game; 
		cameraMap=new OrthographicCamera();
//		Gdx.input.setInputProcessor(inputs);
	}
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
