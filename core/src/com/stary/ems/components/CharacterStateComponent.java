/**
 * @author billzhu
 */
package com.stary.ems.components;

import com.badlogic.ashley.core.Component;

public class CharacterStateComponent implements Component {
	public String name="";
	public short hp=999;
	public short mp=999;
	public boolean right=true;
	public Action action=Action.idle;
	public Action lastAction=Action.idle;
	public float jumpForce;
	
	public int jumpTime=0;//记录跳跃次数，实现2级跳跃
	
	public long state=0;//记录角色状态
	
	public static long idle=0;//空闲
	
	public static long walk=1<<0;// l/r buttom hold on 移动键＝〉walk，walk状态直至松开键
	public static long sidestep=1<<4;//移动连击2次＝〉划步，walk保留直到放开按键，
	
	public static long atk=1<<1;//攻击键1秒内松开，普攻。在攻击状态下X秒内
	public static long jump=1<<2;//跳跃键，
	public static long block=1<<3;//挡格，按着攻击键1秒不放进入挡格状态
	
	public static void main(String[] args) {
		System.out.println("idle "+idle);
		System.out.println("walk "+walk);
		System.out.println("atk "+atk);
		System.out.println("jump "+jump);
		
		long state=walk|atk;
		System.out.println(state^atk);
	}
}
