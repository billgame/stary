/**
 * @author billzhu
 */
package com.stary.inputs;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.utils.ArrayMap;
import com.stary.data.GameData;
import com.stary.ems.components.Action;
import com.stary.ems.components.CharacterStateComponent;
import com.stary.ems.components.JoystickComponent;
import com.stary.ems.components.CharacterStateComponent.State;
import com.stary.ems.components.SkeletonBox2dComponent;
import com.stary.utils.Box2dUtil;

public class CharacterControler  implements InputProcessor{
	public static boolean isLeft,isRight;
	public static boolean isUp,isDown;
	public static boolean isO/*attack,OK*/,isX/*cancel*/;
	public static boolean isJump;
	public final static long left=1<<1;
	public final static long right=1<<2;
	public final static long up=1<<3;
	public final static long down=1<<4;
	public final static long O=1<<5;
	public final static long X=1<<6;
	public final static long jump=1<<7;
	public static long keyed=0;

	ArrayMap<Long, Long> keys=new ArrayMap<Long, Long>(10);
//	public boolean idleState=false;//空闲
//	public boolean walkState=false;//0000 0001  移动键＝〉walk，walk状态直至松开键
//	public boolean upState=false;//抬头
//	public boolean downState=false;//下蹲
//	public boolean atkState=false;//攻击键1秒内松开，普攻。在攻击状态下X秒内
////	public boolean walkJumpState=false;//跳跃键，
//	public boolean jumpState=false;//跳跃键，
//	public boolean blockState=false;//挡格，按着攻击键1秒不放进入挡格状态
	@Override
	public boolean keyDown(int keycode) {
		Entity player=GameData.ashley.getEntity(GameData.instance.currentCharacter);
		if (player==null) return false;
//		SkeletonBox2dComponent sb=player.getComponent(SkeletonBox2dComponent.class);
//		CharacterStateComponent state=player.getComponent(CharacterStateComponent.class);
		JoystickComponent joystick=player.getComponent(JoystickComponent.class);
		switch (keycode) {
		case Keys.LEFT: //left
			joystick.leftKey=true;
			joystick.justPressedLeft=true;
			break;
		case Keys.RIGHT://right
			joystick.rightKey=true;
			joystick.justPressedRight=true;
			break;
		case Keys.UP://up
			joystick.upKey=true;
			joystick.justPressedUp=true;
			break;
		case Keys.DOWN://down //下蹲
			joystick.downKey=true;
			joystick.justPressedDown=true;
			break;
		case Keys.NUMPAD_0://jump
			joystick.jumpKey=true;
			joystick.justPressedJump=true;
			break;
		case Keys.NUMPAD_1://attack
			joystick.OKey=true;//block状态由System判断。
			joystick.justPressedO=true;
			break;
		case Keys.NUMPAD_2://cancel
			joystick.XKey=true;
			joystick.justPressedX=true;
			break;

		default:
			break;
		}		 
		return false; 
	}

	@Override
	public boolean keyUp(int keycode) {	
		Entity player=GameData.ashley.getEntity(GameData.instance.currentCharacter);
		if (player==null) return false;
		SkeletonBox2dComponent sb=player.getComponent(SkeletonBox2dComponent.class);
		CharacterStateComponent state=player.getComponent(CharacterStateComponent.class);
		JoystickComponent joystick=player.getComponent(JoystickComponent.class);
		switch (keycode) {
		case Keys.LEFT: //left
			joystick.leftKey=false;
			break;
		case Keys.RIGHT://right
			joystick.rightKey=false;
			break;
		case Keys.UP://up
			joystick.upKey=false;
			break;
		case Keys.DOWN://down
			joystick.downKey=false;
			break;
		case Keys.NUMPAD_0://jump
			joystick.jumpKey=false;
			break;
		case Keys.NUMPAD_1://attack
			joystick.OKey=false;
			break;
		case Keys.NUMPAD_2://cancel
			joystick.XKey=false;
			break;

		default:
			break;
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
