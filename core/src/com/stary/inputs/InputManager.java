/**
 * @author billzhu
 */
package com.stary.inputs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class InputManager {

	private InputMultiplexer manager, previousManager;
	

	private Screen screen;
	public Stage pxStage;
	public Stage phyStage;
	public CharacterControler cc=new CharacterControler();
	public InputManager(Screen screen,Stage pxStage,Stage phyStage) {
		this.pxStage=pxStage;
		this.phyStage=phyStage;
		setDefault();
		Gdx.input.setInputProcessor(manager);
	}
	public void setEnabled(boolean enabled) {
		if (enabled) {
			Gdx.input.setInputProcessor(manager);
		}
		else Gdx.input.setInputProcessor(null);
	}
//	public void previousManager(){
//		InputMultiplexer tmp=manager;
//		manager=previousManager;
//		previousManager=tmp;
////		setEnabled(true);
//	}
	public void setDefault() {
		manager = new InputMultiplexer(pxStage,phyStage,cc/*,basic,drag,select,cameraMoveTo*/);
		previousManager=manager;
		setEnabled(true);
	}
//	
//	public void appendInputSystems(InputProcessor... processors) {
//		previousManager.setProcessors(manager.getProcessors());
//		for (int i = 0; i < processors.length; i++) manager.addProcessor(processors[i]);
//	}
//
//	public void setInputSystems(InputProcessor... processors) {
//		previousManager.setProcessors(manager.getProcessors());
//		manager = new InputMultiplexer(processors);
//		Gdx.input.setInputProcessor(manager);
//	}
//	
//	public void prependInputSystems(InputProcessor... processors) {
//		previousManager.setProcessors(manager.getProcessors());
//		InputMultiplexer newMultiplexer = new InputMultiplexer();
//		
//		for (int i = 0; i < processors.length; i++) {
//			newMultiplexer.addProcessor(processors[i]);
//		}
//		
//		for (InputProcessor p : manager.getProcessors()) {
//			newMultiplexer.addProcessor(p);
//		}
//		
//		manager = newMultiplexer;
//		Gdx.input.setInputProcessor(manager);
//	}
//	
//	public void removeInputSystems(InputProcessor... processors) {
//		for (int i = 0; i < processors.length; i++) {
//			manager.removeProcessor(processors[i]);
//		}
//	}
}
