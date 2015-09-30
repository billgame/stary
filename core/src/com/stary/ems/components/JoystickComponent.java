/**
 * @author billzhu
 */
package com.stary.ems.components;

import com.badlogic.ashley.core.Component;

public class JoystickComponent implements Component{

	
	public final static long key_left=1<<1;
	public final static long key_right=1<<2;
	public final static long key_up=1<<3;
	public final static long key_down=1<<4;
	public final static long key_O=1<<5;
	public final static long key_X=1<<6;
	public final static long key_jump=1<<7;
	
	public long keys=0;
	
	public boolean leftKey=false;
	public boolean rightKey=false;
	public boolean upKey=false;
	public boolean downKey=false;
	public boolean OKey=false;
	public boolean XKey=false;
	public boolean jumpKey=false;
	public float atkDuration;
	
	public boolean justPressedLeft=false;
	public boolean justPressedRight=false;
	public boolean justPressedUp=false;
	public boolean justPressedDown=false;
	public boolean justPressedO=false;
	public boolean justPressedX=false;
	public boolean justPressedJump=false;
	
	
	public void reset(){
		justPressedLeft=false;
		justPressedRight=false;
		justPressedUp=false;
		justPressedDown=false;
		justPressedO=false;
		justPressedX=false;
		justPressedJump=false;
	}
}
