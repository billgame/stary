/**
 * @author billzhu
 */
package com.stary.inputs;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.stary.data.GameData;
import com.stary.ems.components.Action;
import com.stary.ems.components.SkeletonBox2dComponent;
import com.stary.utils.Box2dUtil;

public class CharacterControler  implements InputProcessor{
	
	@Override
	public boolean keyDown(int keycode) {
		Entity player=GameData.ashley.getEntity(GameData.instance.currentCharacter);
		if (player==null) return false;
		SkeletonBox2dComponent sbc=player.getComponent(SkeletonBox2dComponent.class);
//		System.out.println(GameData.instance.phyStage.getCamera().position);
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			sbc.right=false;
			sbc.lastAction=sbc.action;
			sbc.action=Action.walk;
			sbc.skeleton.setFlipX(true);
			sbc.animationState.setAnimation(0, "walk", true);
			Box2dUtil.updateBody(sbc.skeleton);
			System.out.println("left");
		}else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			sbc.right=true;
			sbc.lastAction=sbc.action;
			sbc.action=Action.walk;
			sbc.skeleton.setFlipX(false);
			sbc.animationState.setAnimation(0, "walk", true);
			Box2dUtil.updateBody(sbc.skeleton);
			System.out.println("right");
		}else if(Gdx.input.isKeyPressed(Keys.SPACE)){
			System.out.println("space");
			sbc.action=Action.jump;
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {		
		Entity player=GameData.ashley.getEntity(GameData.instance.currentCharacter);
		if (player==null) return false;
		SkeletonBox2dComponent sbc=player.getComponent(SkeletonBox2dComponent.class);
		if (Keys.LEFT==keycode) {
			if (Gdx.input.isKeyPressed(Keys.RIGHT))  {
				System.out.println("r"+1);
				sbc.right=true;
				sbc.lastAction=sbc.action;
				sbc.action=Action.walk;
				sbc.skeleton.setFlipX(false);
				sbc.animationState.setAnimation(0, "walk", true);
				Box2dUtil.updateBody(sbc.skeleton);
				return false;
			}
			sbc.lastAction=sbc.action;
			sbc.action=Action.idle;
			sbc.animationState.setAnimation(0, "idle", true);
		}else if (Keys.RIGHT==keycode) {
			if (Gdx.input.isKeyPressed(Keys.LEFT)) {
				System.out.println("l"+1);
				sbc.right=false;
				sbc.lastAction=sbc.action;
				sbc.action=Action.walk;
				sbc.skeleton.setFlipX(true);
				sbc.animationState.setAnimation(0, "walk", true);
				Box2dUtil.updateBody(sbc.skeleton);
				return false;
			}
			sbc.lastAction=sbc.action;
			sbc.action=Action.idle;
			sbc.animationState.setAnimation(0, "idle", true);
		}
		
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
