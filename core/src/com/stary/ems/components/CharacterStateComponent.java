/**
 * @author billzhu
 */
package com.stary.ems.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.stary.ems.components.states.CharacterState;
import com.stary.ems.components.states.CharacterStateMachine;

public class CharacterStateComponent implements Component {
	public String name="";
	public short hp=999;
	public short mp=999;
//	public boolean right=true;
	public Action action=Action.idle;
	public Action lastAction=Action.idle;
	public float jumpX=0.5f;
	public float jumpVelocity=5;
	public Vector2 velocity=new Vector2();
	public int jumpTime=0;//记录跳跃次数，实现2级跳跃
	public int atkNum=1;//
//	public float atkPressedIntrval=0.35f;
	
//	public long state=0;//记录角色状态
	public State state=State.idle;
	
	public CharacterState cState=CharacterState.AtkState;
	public CharacterStateMachine stateMachine;
	public enum State {
		idle,walk,up,down,sidetep,atk,jump,block
	}
	
//	public static long idleState=0;//空闲
//	public static long walkState=1<<0;//0000 0001  移动键＝〉walk，walk状态直至松开键
//	public static long upState=1<<5;//抬头
//	public static long downState=1<<6;//下蹲
//	public static long sidestepState=1<<4;//移动连击2次＝〉划步，walk保留直到放开按键，
//	public static long atkState=1<<1;//攻击键1秒内松开，普攻。在攻击状态下X秒内
//	public static long jumpState=1<<2;//跳跃键，
//	public static long blockState=1<<3;//挡格，按着攻击键1秒不放进入挡格状态

	//move into Joystick class
//	public boolean leftKey=false;
//	public boolean rightKey=false;
//	public boolean upKey=false;
//	public boolean downKey=false;
//	public boolean OKey=false;
//	public boolean XKey=false;
//	public boolean jumpKey=false;
	public float OpressedDuration;
	public float OpressedTimes;

//	public final static long leftKey=1<<1;
//	public final static long rightKey=1<<2;
//	public final static long upKey=1<<3;
//	public final static long downKey=1<<4;
//	public final static long OKey=1<<5;
//	public final static long XKey=1<<6;
//	public final static long jumpKey=1<<7;
	public static long keys=0;
	public CharacterStateComponent(Entity entity){
		stateMachine=new CharacterStateMachine(entity);
		stateMachine.setInitialState(CharacterState.IdleState);
	}
	public static void main(String[] args) {
//		System.out.println("idle "+idleState);
//		System.out.println("walk "+walkState);
//		System.out.println("atk "+atkState);
//		System.out.println("jump "+jumpState);
//		
//		long state=walkState|atkState|jumpState;
//		if ((state&jumpState)!=0) {//如果有jump
//			state^=jumpState;//去除jump
//		}
//		System.out.println(state&jumpState);//&按位与，  ^异域   |按位或
	}
	
	public void walk(){
	}
}
